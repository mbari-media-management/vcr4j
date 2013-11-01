/*
 * @(#)VCRGotoButton.java   2009.02.24 at 09:44:53 PST
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
package org.mbari.vcr.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import org.mbari.vcr.IVCR;
import org.mbari.vcr.IVCRState;

/**
 * <p>Goto button used for the VCR UI</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 332 $
 */
public class VCRGotoButton extends VCRButton {

    /**
     *
     */
    private static final long serialVersionUID = 1070830147836296796L;

    final VCRGotoFrame f = new VCRGotoFrame();

    private boolean firstShowing = true;

    /** Constructor */
    public VCRGotoButton() {
        super();
        setOnIcon("/images/vcr/goto_r.png");
        setOffIcon("/images/vcr/goto.png");
        setToolTipText("Go to a timecode");
        f.setResizable(false);
        f.pack();
    }

    /**
     * Sets the IVCR object to register to and pass commands to. This method overrides
     * <i>setVcr</i> from VCRBUtton in order to register the <i>IVCR</i> with
     * the timecode frame that pops up when this button is pushed.
     * @param vcr The VCR to send commands to.
     */
    public void setVcr(IVCR vcr) {
        super.setVcr(vcr);
        f.setVcr(vcr);
    }

    /**
     * This class should be registered to a VCRState object as an observer.
     * @param observed A VCRState Object
     * @param stateChange A message about the change of state.
     */
    public void update(Object observed, Object stateChange) {
        IVCRState s = (IVCRState) observed;

        if (s.isCueingUp()) {
            setIcon(onIcon);
        }
        else {
            setIcon(offIcon);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    void vcrAction() {
        if (vcr != null) {
            IVCRState m = vcr.getVcrState();

            if (m != null) {

                // Set the frame on screen if it hasn't been shown before.
                // Subsequent showings should be where ever the user placed it.
                if (firstShowing) {

                    // Make sure the goto panel is on screen
                    int thisY = getY();                 // This buttons y height
                    Dimension fD = f.getSize();         // The VCRGotoFrames size
                    int fY = fD.height;                 // The VCRGotoFrames height
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int sY = screenSize.height;         // The height of the screen
                    Point p = getLocationOnScreen();    // The location of the Top-left corner fo the bottum

                    if (thisY + fY + p.getY() > sY) {
                        int newY = ((int) p.getY()) - fY;
                        int newX = (int) p.getX();

                        f.setLocation(newX, newY);
                    }
                    else {
                        f.setLocation(getLocationOnScreen());
                    }

                    firstShowing = false;
                }

                f.setVisible(true);
            }
        }
    }
}
