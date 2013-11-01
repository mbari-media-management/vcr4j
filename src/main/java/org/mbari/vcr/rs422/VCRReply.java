/*
 * @(#)VCRReply.java   2013.01.18 at 12:16:00 PST
 *
 * Copyright 2009 MBARI
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/* Generated by Together */
package org.mbari.vcr.rs422;

import java.util.Arrays;
import org.mbari.util.NumberUtilities;
import org.mbari.vcr.IVCRError;
import org.mbari.vcr.IVCRState;
import org.mbari.vcr.IVCRTimecode;
import org.mbari.vcr.IVCRUserbits;
import org.mbari.vcr.VCRException;
import org.mbari.vcr.VCRReplyAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Use to store replys from a sony VCR. The proper method for using this class is:</p>
 *
 * <pre>
 * vcrReply.update(command, cmd, data, checksum);
 * </pre>
 *
 * <p>It will check the checksum value and set the appropriate VCRStatus flags and
 * reply errors. If the checksum is bad it will hrow a SonyVCRException</p>
 *
 * <p>A reply consists of 3 parts. The first part is the cmd block, which consists
 * of 2 bytes. This block contains both the type of reply the VCR is sending and
 * the number of additional blocks of data that will follow. This may be followed
 * by a data block which can contain some anxillary information such as
 * a problem with the response (if the reply is a Nack), timecode information or
 * the VCR status. Finally the VCR finished its reply with a checksum byte. After
 * setting the parts of the reply, using setCmd, setData and setChecksum, a call
 * to update caues the VCRReply to update he approriate state objects such as
 * VCRTimecode, VCRState, and VCRError.</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 1.4 $
 */
public class VCRReply extends VCRReplyAdapter {

    public static final byte[] ACK = { 0x10, 0x01 };

    public static final byte[] NACK = { 0x11, 0x12 };

    /** Corrected LTC time data */
    public static final byte[] OTHER_STATUS_REPLY = { 0x71, 0x20 };

    /** <!-- Field description --> */
    public static final byte[] STATUS_REPLY = { 0x73, 0x20 };
    private static final Logger log = LoggerFactory.getLogger(VCRReply.class);

    /**
         * The checksum of the command submitted to the VCR
         */
    private byte[] commandChecksum = new byte[1];
    private final VCRState vcrStatus = new VCRState();
    private final VCRTimecode vcrTimecode = new VCRTimecode();
    private final VCRError vcrError = new VCRError();
    private final VCRUserbits vcrUserbits = new VCRUserbits();
    private byte[] checksum;
    private byte[] cmd;

    /**
         * The command submited to the VCR
         */
    private byte[] command;
    private byte[] data;

    /**
     * The last byte in a command block is the checksum, i.e. the lower eight
     * bits of the sum of the other bytes in the command block
     * @param command The byte array to to calculate a checksum
     * @return The checksum value of the command
     */
    public static final byte calculateChecksum(byte[] command) {
        int temp = 0;

        for (int i = 0; i < command.length; i++) {
            temp += command[i];
        }

        return ((byte) temp);
    }

    /**
         * @return  The checksum value stored in the VCRReply
         * @uml.property  name="checksum"
         */
    public byte[] getChecksum() {
        return checksum;
    }

    /**
         * @return  The command block of the reply
         * @uml.property  name="cmd"
         */
    public byte[] getCmd() {
        return cmd;
    }

    /**
         * @return  The Command that was sent to the VCR
         * @uml.property  name="command"
         */
    public byte[] getCommand() {
        return this.command;
    }

    /**
         * @return  The checksum of the command that was sent to the VCR.
         * @uml.property  name="commandChecksum"
         */
    public byte[] getCommandChecksum() {
        return this.commandChecksum;
    }

    /**
         * @return  Get the data block of the VCR reply.
         * @uml.property  name="data"
         */
    public byte[] getData() {
        return data;
    }

    /**
     * @return The error object associated with this reply
     */
    @Override
    public IVCRError getVcrError() {
        return vcrError;
    }

    /**
     * @return The state object associated with this reply
     */
    @Override
    public IVCRState getVcrState() {
        return vcrStatus;
    }

    /**
     * @return The timecode object associated with this reply
     */
    @Override
    public IVCRTimecode getVcrTimecode() {
        return vcrTimecode;
    }

    /**
     *
     * @return
     */
    @Override
    public IVCRUserbits getVcrUserbits() {
        return vcrUserbits;
    }

    /**
     * Checks to see if the reply is an ack (acknowledgement)
     * @return True if the reply is ACK
     */
    @Override
    public boolean isAck() {
        return (Arrays.equals(cmd, ACK));
    }

    /**
     * Checks the checksum in the reply with the calculated checksum. Sets
     * the appropriate error status in the vCRStatus object.
     *
     * @return
     */
    private boolean isChecksumOK() {
        boolean OK;

        if (cmd == null) {
            OK = false;
        }
        else {

            // Put the Command block recieved from the VCR into a single byte array
            // so that we can calculate the
            byte[] cmdBlock;

            if ((data == null) || (data.length == 0)) {
                cmdBlock = new byte[cmd.length];
            }
            else {
                cmdBlock = new byte[cmd.length + data.length];
            }

            for (int i = 0; i < cmd.length; i++) {
                cmdBlock[i] = cmd[i];
            }

            if ((data != null) && (data.length > 0)) {
                for (int i = 2; i < cmdBlock.length; i++) {
                    cmdBlock[i] = data[i - 2];
                }
            }

            // Compare the checksums
            byte checksum2 = calculateChecksum(cmdBlock);

            if (checksum[0] != checksum2) {
                if (log.isDebugEnabled()) {
                    log.debug("Invalid checksum. [Expected = " +
                              NumberUtilities.toHexString(new byte[] { checksum2 }) + "] [recieved = " +
                              NumberUtilities.toHexString(checksum) + "]");
                }

                vcrError.setError(VCRError.CHECKSUM_ERROR);
                OK = false;
            }
            else {
                vcrError.setError(VCRError.OK);
                OK = true;
            }
        }

        return OK;
    }

    /**
     * Checks to see if the reply is a nack (i.e error)
     * @return True if the reply is NACK
     */
    @Override
    public boolean isNack() {
        return (Arrays.equals(cmd, NACK));
    }

    /**
     * Checks to see if the reply is a reply to a Status sense (also called GET_STATUS
     * @return True if he reply contains a status update
     */
    @Override
    public boolean isStatusReply() {
        return (Arrays.equals(cmd, STATUS_REPLY));
    }

    /**
     * Checks to see if he reply is a response to a timecode command
     * @return True if the reply is for a timecode
     */
    @Override
    public boolean isTimecodeReply() {
        return VCRTimecode.isTimecodeReply(cmd);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isUserbitsReply() {
        return VCRUserbits.isUserbitsReply(cmd);
    }

    /**
     * Update the ste objects associated with this VCRReply
     * @param command The command that was sent to the VCR
     * @param cmd The command portion of the VCR's reply
     * @param data The data portion of the VCR's reply
     * @param checksum The checksum of the VCR's reply
     *
     * @throws VCRException Thrown if the checksum supplied in the reply
     * does not match the checksum calculated from the cmd and data blocks.
     */
    public synchronized void update(byte[] command, byte[] cmd, byte[] data, byte[] checksum) throws VCRException {
        this.command = command;
        this.commandChecksum[0] = calculateChecksum(command);
        this.cmd = cmd;
        this.data = data;
        this.checksum = checksum;

        // Make sure the checksum matches corectly. The VCRError for bad checksums
        // is set in the isCheckSum OK method.
        if (!isChecksumOK()) {
            String nullS = "";
            final String commandS = (command == null) ? nullS : NumberUtilities.toHexString(command);
            final String cmdS = (cmd == null) ? nullS : NumberUtilities.toHexString(cmd);
            final String dataS = (data == null) ? nullS : NumberUtilities.toHexString(data);
            final String checksumS = (checksum == null) ? nullS : NumberUtilities.toHexString(checksum);

            throw new VCRException("Invalid checksum. [command sent = " + commandS + "] [command recieved = " + cmdS +
                                   "] [data = " + dataS + "] [checksum = " + checksumS + "]");
        }

        if (isAck()) {
            vcrError.setError(IVCRError.OK);
        }
        else if (isTimecodeReply()) {

            // Handle updating the status and/or timecode as appropriate
            // System.out.println("VCRReply.update() <= setting time code");
            vcrTimecode.setTimecodeBytes(data);
        }
        else if (isUserbitsReply()) {
            String bs = "";

            for (int i = 0; i < cmd.length; i++) {
                bs = bs + cmd[i] + " ";
            }

            for (int i = 0; i < data.length; i++) {
                bs = bs + data[i] + " ";
            }

            for (int i = 0; i < checksum.length; i++) {
                bs = bs + checksum[i] + " ";
            }

            // System.out.println("VCRReply.update(" + bs + ")");
            vcrUserbits.setUserbits(data);
        }
        else if (isStatusReply()) {
            vcrStatus.setStatus(NumberUtilities.toLong(data));
        }
        else if (isNack()) {

            // If NACK the Reply should set the VCRError bits only
            int nackData = (int) data[0];

            vcrError.setError(nackData);
        }

        notifyObservers();
    }
}
