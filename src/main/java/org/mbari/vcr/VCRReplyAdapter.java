/*
 * @(#)VCRReplyAdapter.java   2009.02.24 at 09:44:50 PST
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



package org.mbari.vcr;

import org.mbari.util.IObserver;
import org.mbari.util.ObservableSupport;
import org.mbari.vcr.rs422.VCRUserbits;

/**
 *
 * @author brian
 */
public class VCRReplyAdapter implements IVCRReply {

    /**
         * @uml.property  name="os"
         * @uml.associationEnd  multiplicity="(1 1)"
         */
    private ObservableSupport os = new ObservableSupport();

    /**
         * @uml.property  name="vcrError"
         * @uml.associationEnd
         */
    protected IVCRError vcrError;

    /**
         * @uml.property  name="vcrState"
         * @uml.associationEnd
         */
    protected IVCRState vcrState;

    /**
         * @uml.property  name="vcrTimecode"
         * @uml.associationEnd
         */
    protected IVCRTimecode vcrTimecode;

    /**
         * @uml.property  name="vcrUserbits"
         * @uml.associationEnd
         */
    protected IVCRUserbits vcrUserbits;

    /** Creates a new instance of VCRReplyAdapter */
    public VCRReplyAdapter() {}

    public void addObserver(IObserver observer) {
        os.add(observer);
    }

    /**
         * @return  the vcrError
         * @uml.property  name="vcrError"
         */
    public IVCRError getVcrError() {
        if (vcrError == null) {
            vcrError = new VCRErrorAdapter();
        }

        return vcrError;
    }

    /**
         * @return  the vcrState
         * @uml.property  name="vcrState"
         */
    public IVCRState getVcrState() {
        if (vcrState == null) {
            vcrState = new VCRStateAdapter();
        }

        return vcrState;
    }

    /**
         * @return  the vcrTimecode
         * @uml.property  name="vcrTimecode"
         */
    public IVCRTimecode getVcrTimecode() {
        if (vcrTimecode == null) {
            vcrTimecode = new VCRTimecodeAdapter();
        }

        return vcrTimecode;
    }

    /**
         * @return  the vcrUserbits
         * @uml.property  name="vcrUserbits"
         */
    public IVCRUserbits getVcrUserbits() {
        if (vcrUserbits == null) {
            vcrUserbits = new VCRUserbits();
        }

        return vcrUserbits;
    }

    public boolean isAck() {
        return false;
    }

    public boolean isNack() {
        return false;
    }

    public boolean isStatusReply() {
        return false;
    }

    public boolean isTimecodeReply() {
        return false;
    }

    public boolean isUserbitsReply() {
        return false;
    }

    /** Notifies all the observers of a chage of state */
    protected void notifyObservers() {
        os.notify(this, null);
    }

    public void removeAllObservers() {
        os.clear();
    }

    public void removeObserver(IObserver observer) {
        os.remove(observer);
    }
}
