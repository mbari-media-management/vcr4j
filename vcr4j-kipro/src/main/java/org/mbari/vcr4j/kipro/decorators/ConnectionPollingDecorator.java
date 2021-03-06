package org.mbari.vcr4j.kipro.decorators;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.mbari.vcr4j.VideoIO;
import org.mbari.vcr4j.VideoState;
import org.mbari.vcr4j.decorators.Decorator;
import org.mbari.vcr4j.kipro.QuadError;
import org.mbari.vcr4j.kipro.commands.QuadVideoCommands;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Based off the JavaScript library on the KiPro. This decorator polls the connection and
 * reconnects if needed.
 * @author Brian Schlining
 * @since 2016-04-21T10:22:00
 */
public class ConnectionPollingDecorator implements Decorator {

    private AtomicBoolean isConnected = new AtomicBoolean(false);

    private Disposable disposable;

    protected final Observer<QuadError> errorSubscriber = new Observer<QuadError>() {
        @Override
        public void onComplete() {}

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onNext(QuadError quadError) {
            isConnected.set(!quadError.hasConnectionError());
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            ConnectionPollingDecorator.this.disposable = disposable;
        }
    };


    public ConnectionPollingDecorator(VideoIO<? extends VideoState, QuadError> io) {

        io.getErrorObservable().subscribe(errorSubscriber);

        // -- When not connected poll for new connection at 5 second interfals
        Observable.interval(5, TimeUnit.SECONDS).subscribe(x -> {
                    //System.out.println("Poll 1");
                    if (!isConnected.get()) {
                        io.getCommandSubject().onNext(QuadVideoCommands.CONNECT);
                    }
                });

        // -- When connected poll connection at 50 ms intervals
        Observable.interval(50, TimeUnit.MILLISECONDS).subscribe(x -> {
                    //System.out.println("Poll 2");
                    if (isConnected.get()) {
                        io.getCommandSubject().onNext(QuadVideoCommands.CONFIG_EVENT);
                    }
                });

    }

    @Override
    public void unsubscribe() {
        disposable.dispose();
    }
}
