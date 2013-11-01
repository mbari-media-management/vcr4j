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
package org.mbari.vcr.ui;

import org.mbari.vcr.IVCRState;

/**
 * <p>Rewind button for thte VCR UI</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 140 $
 */
public class VCRRewindButton extends VCRButton {

    /**
     *
     */
    private static final long serialVersionUID = -6450670235135628905L;

    /** Constructor */
    public VCRRewindButton() {
        super();
        setOnIcon("/images/vcr/rewind_r.png");
        setOffIcon("/images/vcr/rewind.png");
        setToolTipText("Rewind");
    }

    /**
     * This class should be registered to a VCRState object as an observer.
     * @param observed A VCRState Object
     * @param stateChange A message about the change of state.
     */
    public void update(Object observed, Object stateChange) {
        IVCRState s = (IVCRState) observed;

        if (s.isRewinding()) {
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
    protected void vcrAction() {
        if (vcr != null) {
            IVCRState m = vcr.getVcrState();

            if (m != null) {
                vcr.rewind();
            }
        }
    }
}
