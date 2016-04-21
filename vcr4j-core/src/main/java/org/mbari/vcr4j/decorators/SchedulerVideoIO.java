package org.mbari.vcr4j.decorators;

import org.mbari.vcr4j.VideoCommand;
import org.mbari.vcr4j.VideoError;
import org.mbari.vcr4j.VideoIO;
import org.mbari.vcr4j.VideoIndex;
import org.mbari.vcr4j.VideoState;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Decorator that moves the IO off of the currently executing thread. All commands are sent from a single
 * independent thread (not managed by any scheduler or executor). All other observables have their state
 * dealt with on the Executor or Scheduler that you provide.
 *
 * @author Brian Schlining
 * @since 2016-02-11T14:38:00
 */
public class SchedulerVideoIO<S extends VideoState, E extends VideoError> implements VideoIO<S, E>, Decorator {

    private final VideoIO io;

    private final Observable<E> errorObservable;
    private final Observable<S> stateObservable;
    private final Observable<VideoIndex> indexObservable;
    private final CommandQueue commandQueue = new CommandQueue();
    private final Subject<VideoCommand, VideoCommand> commandSubject;
    private final Scheduler scheduler;
    private final Subscriber<VideoCommand> commandSubscriber;

    public SchedulerVideoIO(VideoIO<S, E> io, Executor executor) {
        this(io, Schedulers.from(executor));
    }

    public SchedulerVideoIO(VideoIO<S, E> io, Scheduler scheduler) {
        this.io = io;
        this.scheduler = scheduler;
        errorObservable = io.getErrorObservable().observeOn(scheduler);
        stateObservable = io.getStateObservable().observeOn(scheduler);
        indexObservable = io.getIndexObservable().observeOn(scheduler);


        commandSubscriber = new Subscriber<VideoCommand>() {
            @Override
            public void onCompleted() {
                io.getCommandSubject().onCompleted();
            }

            @Override
            public void onError(Throwable throwable) {
                io.getCommandSubject().onError(throwable);
            }

            @Override
            public void onNext(VideoCommand videoCommand) {
                commandQueue.send(videoCommand);
            }
        };

        commandSubject = new SerializedSubject<>(PublishSubject.create());
        commandSubject.subscribe(commandSubscriber);
    }

    @Override
    public void unsubscribe() {
        commandSubscriber.unsubscribe();
        errorObservable.unsubscribeOn(scheduler);
        stateObservable.unsubscribeOn(scheduler);
        indexObservable.unsubscribeOn(scheduler);
        commandQueue.kill();
    }

    @Override
    public void send(VideoCommand videoCommand) {
        commandSubject.onNext(videoCommand);
    }

    @Override
    public Subject<VideoCommand, VideoCommand> getCommandSubject() {
        return commandSubject;
    }

    @Override
    public String getConnectionID() {
        return io.getConnectionID();
    }

    @Override
    public void close() {
        io.close();
    }

    @Override
    public Observable<E> getErrorObservable() {
        return errorObservable;
    }

    @Override
    public Observable<S> getStateObservable() {
        return stateObservable;
    }

    @Override
    public Observable<VideoIndex> getIndexObservable() {
        return indexObservable;
    }

    /**
     * This manages the commands to be sent in a separate thread.
     */
    private class CommandQueue {
        final BlockingQueue<VideoCommand> pendingQueue = new LinkedBlockingQueue<VideoCommand>();
        final Thread thread; // All IO will be done on this thread
        AtomicBoolean isRunning = new AtomicBoolean(true);
        final Runnable runnable = () -> {
            while(isRunning.get()) {
                VideoCommand videoCommand = null;
                try {
                    videoCommand = pendingQueue.poll(3600L, TimeUnit.SECONDS);
                }
                catch (InterruptedException e) {
                    // TODO ?
                }
                if (videoCommand != null) {
                    io.getCommandSubject().onNext(videoCommand);
                }
            }
        };

        void kill() {
            isRunning.set(false);
        }

        void send(VideoCommand videoCommand) {
            pendingQueue.offer(videoCommand);
        }

        public CommandQueue() {
            thread = new Thread(runnable, SchedulerVideoIO.this.getClass().getSimpleName());
            thread.setDaemon(true);
            thread.start();
        }
    }
}