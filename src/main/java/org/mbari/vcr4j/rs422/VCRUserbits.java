/*
 * @(#)VCRUserbits.java   2009.02.24 at 09:44:55 PST
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



package org.mbari.vcr4j.rs422;

import java.util.Arrays;
import org.mbari.vcr4j.VCRUserbitsAdapter;

/**
 * <p>Class for monitoring the userbits of a sony VCR.</p>
 *
 * @author  : Brian Schlining
 */
public class VCRUserbits extends VCRUserbitsAdapter {

    public static final byte[] LTC_USERBITS = { 0x74, 0x05 };

    public static final byte[] VTC_USERBITS = { 0x74, 0x07 };

    /** Default constructor. */
    public VCRUserbits() {
        super();
    }

    /**
     * @param cmd
     *
     * @return
     */
    public static boolean isUserbitsReply(byte[] cmd) {
        return ((Arrays.equals(cmd, LTC_USERBITS)) || (Arrays.equals(cmd, VTC_USERBITS)));
    }
}
