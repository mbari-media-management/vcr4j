/*
 * @(#)VCRStopButton.java   2009.02.24 at 09:44:53 PST
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

import javafx.beans.property.ObjectProperty;
import org.mbari.vcr4j.VideoController;
import org.mbari.vcr4j.VideoError;
import org.mbari.vcr4j.VideoState;

import javax.swing.*;
import java.util.Optional;

/**
 * <p>The stop button for use with the VCR UI.</p>
 *
 * @author Brian Schlining
 */
public class VCRStopButton extends VCRButton {


    /** Constructor 
     * @param videoController THe videoController to use
    */
    public VCRStopButton(ObjectProperty<VideoController<? extends VideoState, ? extends VideoError>> videoController) {
        super(videoController);
        setOnIcon("/images/vcr/stop_r.png");
        setOffIcon("/images/vcr/stop.png");
        setToolTipText("Stop");
    }

    @Override
    void onNext(VideoState videoState) {
        Icon icon = videoState.isStopped() ? onIcon : offIcon;
        setIcon(icon);
    }


    protected void doAction() {
        Optional.ofNullable(videoControllerProperty().get())
                .ifPresent(VideoController::stop);
    }
}
