/*
 * @(#)VCRRewindButton.java   2009.02.24 at 09:44:53 PST
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



/* Generated by Together */
package org.mbari.vcr4j.ui.swing;

import javafx.beans.property.ObjectProperty;
import org.mbari.vcr4j.VideoController;
import org.mbari.vcr4j.VideoError;
import org.mbari.vcr4j.VideoState;

import javax.swing.*;
import java.util.Optional;

/**
 * <p>Rewind button for thte VCR UI</p>
 *
 * @author Brian Schlining $
 */
public class VCRRewindButton extends VCRButton {



    public VCRRewindButton(ObjectProperty<VideoController<? extends VideoState, ? extends VideoError>> videoController) {
        super(videoController);
        setOnIcon("/images/vcr/rewind_r.png");
        setOffIcon("/images/vcr/rewind.png");
        setToolTipText("Rewind");
    }

    @Override
    void onNext(VideoState videoState) {
        Icon icon = videoState.isRewinding() ? onIcon : offIcon;
        setIcon(icon);
    }


    protected void doAction() {
        Optional.ofNullable(videoControllerProperty().get())
                .ifPresent(VideoController::rewind);
    }
}
