/*
 * @(#)TimeCodeField.java   2009.02.24 at 09:44:54 PST
 *
 * Copyright 2007 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.mbari.vcr4j.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import javafx.beans.property.ObjectProperty;
import org.mbari.vcr4j.VideoController;
import org.mbari.vcr4j.VideoError;
import org.mbari.vcr4j.VideoIndex;
import org.mbari.vcr4j.VideoState;
import org.mbari.vcr4j.time.Timecode;

/**
 * <p>A JTextField object that is prettied up for use in a VCR GUI and can be
 * registered to a VCRTimecode object. Once this object is registered, by using
 * IVCR.getVCRManager().addTimecodeObserver(thisTimeCodeField), then it will
 * automatically update and dsiplay the timecode whenever a call to
 * IVCR.getTimecode() occurs.</p>
 *
 * @author Brian Schlining
 */
public class TimeCodeField extends JTextField {

    private volatile Disposable disposable;

    private Dimension size = new Dimension(180, 40);

    //private ObjectProperty<VideoController<? extends VideoState, ? extends VideoError>> videoController;

    public TimeCodeField(ObjectProperty<VideoController<? extends VideoState, ? extends VideoError>> videoController) {
        super();
        initComponent();
        videoController.addListener((obs, oldVal, newVal) -> {
            if (oldVal != null && disposable != null) {
                disposable.dispose();
                disposable = null;
            }

            if (newVal != null) {

                // Only deal with indices that have timecodes. Otherwise the TextField
                // does a weird stutter between valid and invalid timecodes
                newVal.getIndexObservable()
                        .filter(vi -> vi.getTimecode().isPresent())
                        .subscribe(newSubscriber());
            }
        });
    }

    private void initComponent() {

        // time code display:
        setBackground(Color.black);
        setFont(new java.awt.Font("SansSerif", Font.BOLD, 26));
        setForeground(Color.red);
        setBorder(BorderFactory.createEtchedBorder());

        // setMaximumSize(new Dimension(150, 40));
        // setMinimumSize(new Dimension(150, 40));
        setPreferredSize(size);
        setRequestFocusEnabled(false);
        setCaretColor(Color.black);
        setDisabledTextColor(Color.black);
        setEditable(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setSelectedTextColor(Color.red);
        setSelectionColor(Color.black);
        setColumns(9);
        setAlignmentX(CENTER_ALIGNMENT);
    }

    private Observer<VideoIndex> newSubscriber() {
        return new Observer<VideoIndex>() {
            @Override
            public void onComplete() {}

            @Override
            public void onError(Throwable throwable) {}

            @Override
            public void onNext(VideoIndex videoIndex) {
                String txt = videoIndex.getTimecode()
                        .map(Timecode::toString)
                        .orElse(Timecode.EMPTY_TIMECODE_STRING);
                setText(txt);
            }

            @Override
            public void onSubscribe(Disposable disposable) {

            }
        };
    }


}
