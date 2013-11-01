/*
 * @(#)VCRShuttleButton.java   2009.02.24 at 09:44:53 PST
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



package org.mbari.vcr.ui;

/**
 * <p>The shuttle button for use with the VCR UI. This class can be subclassed
 * by components that need to associate with a slider component. For example
 * The Fast-forward and rewidn controls may implements shuttle functionality.
 * In which case they need to associate with a slider to acquire the shuttle speed.</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 332 $
 */
public abstract class VCRShuttleButton extends VCRButton {

    /**
         * @uml.property  name="slider"
         * @uml.associationEnd
         */
    javax.swing.JSlider slider;

    /**
         * <p><!-- Method description --></p>
         * @return
         * @uml.property  name="slider"
         */
    public javax.swing.JSlider getSlider() {
        return slider;
    }

    /**
         * <p><!-- Method description --></p>
         * @param  slider
         * @uml.property  name="slider"
         */
    public void setSlider(javax.swing.JSlider slider) {
        this.slider = slider;
    }
}
