/*
 * @(#)StatusMonitor.java   2009.02.24 at 09:44:55 PST
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



package org.mbari.vcr.timer;

import org.mbari.util.IObserver;
import org.mbari.vcr.IVCR;
import org.mbari.vcr.IVCRState;
import org.mbari.vcr.VCRAdapter;

/**
 *
 * @author brian
 */
public class StatusMonitor extends Monitor {

    public static final String MONITOR_NAME = "StatusMonitor";
    private IObserver stateObserver = new IObserver() {

        public void update(final Object obj, final Object changeCode) {
            IVCRState vcrState = (IVCRState) obj;

            if (vcrState.isConnected()) {
                start();
            }
            else {
                stop();
            }
        }
    };

    /**
     * Constructs ...
     */
    public StatusMonitor() {
        this(new VCRAdapter());
        setIntervalMin(250);
        setInterval(1000);
    }

    /**
     * Constructs ...
     *
     * @param vcr
     */
    public StatusMonitor(final IVCR vcr) {
        super(ReadStateTimerTask.class, MONITOR_NAME, vcr);
    }

    @Override
    public synchronized void setVcr(final IVCR vcr) {
        IVCR oldVcr = getVcr();

        if (oldVcr != null) {
            oldVcr.getVcrState().removeObserver(stateObserver);
        }

        vcr.getVcrState().addObserver(stateObserver);
        super.setVcr(vcr);
    }
}
