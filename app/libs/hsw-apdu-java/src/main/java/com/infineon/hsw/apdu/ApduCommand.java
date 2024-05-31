// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.utils.Utils;
import java.util.Arrays;

/**
 * Container class to store APDU structured data packages. The APDU command
 * consists of header (mandatory) and command data (optional).
 */
public class ApduCommand {
    /** Constant for case 1 APDU command */
    public static final int APDU_CASE_1 = 1;
    /** Constant for case 2 APDU command */
    public static final int APDU_CASE_2 = 2;
    /** Constant for case 3 APDU command */
    public static final int APDU_CASE_3 = 3;
    /** Constant for case 4 APDU command */
    public static final int APDU_CASE_4 = 4;

    /** Array containing command header */
    private byte[] header;
    /** Array containing command data */
    private byte[] data;
    /** Value of expected response data length */
    private int le;

    private boolean forceExtended = false;

    /**
     * Builds an APDU from CLA, INS, P1, P2, optional command data and le byte.
     *
     * @param cla  class byte
     * @param ins  instruction byte
     * @param p1   parameter byte 1
     * @param p2   parameter byte 2
     * @param data command data
     * @param le   expected response data length
     * @throws ApduException if command data cannot be converted into a byte
     *         stream.
     */
    public ApduCommand(int cla, int ins, int p1, int p2, Object data, int le)
            throws ApduException {
        header = new byte[4];
        header[0] = (byte) cla;
        header[1] = (byte) ins;
        header[2] = (byte) p1;
        header[3] = (byte) p2;
        this.le = le;
        this.data = ApduUtils.toBytes(data);
    }

    /**
     * Build an APDU command from a byte stream representation.
     *
     * @param command object containing byte stream.
     * @throws ApduException if APDU cannot be build due to syntactical reasons.
     */
    public ApduCommand(String command) throws ApduException {
        this(ApduUtils.toBytes(command));
    }

    /**
     * Build an APDU command from a byte stream representation.
     *
     * @param command object containing byte stream.
     * @throws ApduException if APDU cannot be build due to syntactical reasons.
     */
    public ApduCommand(byte[] command) throws ApduException {
        byte[] abCommand = command;
        int iLength = abCommand.length;
        int iOffset = 4;
        int iLc = 0;

        // check if valid APDU
        if (iLength < 4) {
            throw new ApduException("APDU command shorter than 4 bytes");
        }

        // copy header
        header = Arrays.copyOf(abCommand, 4);

        if (iLength == 4) {
            // case 1
            le = 0;
        } else {
            // get short Lc or le
            int iValue = abCommand[iOffset] & 0xFF;
            iOffset += 1;

            if (iLength == 5) {
                // case 2 short
                le = ((iValue - 1) & 0xFF) + 1;
            } else if ((iValue != 0) || (iLength < 7)) {
                // case 3 or 4 short
                if (iLength == 5 + iValue) {
                    // case 3 short
                    iLc = iValue;
                    le = 0;
                } else if (iLength == 6 + iValue) {
                    // case 4 short
                    iLc = iValue;
                    le = ((abCommand[iOffset + iValue] - 1) & 0xFF) + 1;
                } else {
                    throw new ApduException(
                            "APDU has incorrect Lc byte or data length)");
                }
            } else {
                // get extended Lc or le
                iValue = ApduUtils.getShort(abCommand, iOffset);
                iOffset += 2;

                if (iLength == 7) {
                    // case 2 extended
                    le = ((iValue - 1) & 0xFFFF) + 1;
                } else if (iLength == 7 + iValue) {
                    // case 3 extended
                    iLc = iValue;
                    le = 0;
                } else if (iLength == 9 + iValue) {
                    // case 4 extended
                    iLc = iValue;
                    le = ((ApduUtils.getShort(abCommand, iOffset + iValue) -
                           1) &
                          0xFFFF) +
                         1;
                } else {
                    throw new ApduException(
                            "APDU has incorrect extended Lc or data length)");
                }
                forceExtended = true;
            }
        }

        // copy command data
        data = new byte[iLc];
        System.arraycopy(abCommand, iOffset, data, 0, iLc);
    }

    /**
     * Returns the case of the APDU command.
     *
     * @return APDU case constant.
     */
    public int getCase() {
        if ((data == null) || (data.length == 0)) {
            return (le == 0) ? APDU_CASE_1 : APDU_CASE_2;
        }

        return (le == 0) ? APDU_CASE_3 : APDU_CASE_4;
    }

    /**
     * Set logic channel bits in class byte (CLA). The method returns a
     * reference to 'this' to allow simple concatenation of operations.
     *
     * @param logChannel logical channel to be set in class byte.
     * @return same instance
     * @throws ApduException If there is APDU or communication related failures
     */
    public ApduCommand setLogChannel(int logChannel) throws ApduException {
        int cla = header[0];

        if ((logChannel < 0) || (logChannel >= 20))
            throw new ApduException(
                    String.format("Invalid logical channel number %d",
                                  logChannel));

        if (logChannel < 4) {
            switch ((byte) (cla & 0xF0)) {
            case (byte) 0x00:
            case (byte) 0x10:
            case (byte) 0x80:
            case (byte) 0x90:
            case (byte) 0xA0:
            case (byte) 0xB0:
                header[0] = (byte) ((cla & 0xFC) | logChannel);
                break;

            default:
                throw new ApduException(String.format(
                        "Cannot assign channel number %d to CLA %02X!",
                        logChannel, cla));
            }
        } else {
            cla |= 0x40;

            switch ((byte) (cla & 0xF0)) {
            case (byte) 0x40:
            case (byte) 0x50:
            case (byte) 0x60:
            case (byte) 0x70:
            case (byte) 0xC0:
            case (byte) 0xD0:
            case (byte) 0xE0:
            case (byte) 0xF0:
                header[0] = (byte) ((cla & 0xF0) | (logChannel - 4));
                break;

            default:
                throw new ApduException(String.format(
                        "Cannot assign channel number %d to CLA %02X!",
                        logChannel, cla));
            }
        }

        return this;
    }

    /**
     * Get logical channel signaled in class byte (CLA).
     *
     * @return logical channel bits set in class byte.
     */
    public int getLogChannel() {
        int cla = header[0];

        switch ((byte) (cla & 0xF0)) {
        case (byte) 0x00:
        case (byte) 0x01:
        case (byte) 0x80:
        case (byte) 0x90:
        case (byte) 0xA0:
        case (byte) 0xB0:
            return cla & 0x03;

        case (byte) 0x40:
        case (byte) 0x50:
        case (byte) 0x60:
        case (byte) 0x70:
        case (byte) 0xC0:
        case (byte) 0xD0:
        case (byte) 0xE0:
        case (byte) 0xF0:
            return (cla & 0x0F) + 4;
        default: {
            // do nothing
        }
        }

        return 0;
    }

    /**
     * Set class byte (CLA). The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param cla new class byte.
     * @return this
     */
    public ApduCommand setCLA(int cla) {
        header[0] = (byte) cla;
        return this;
    }

    /**
     * Get class byte (CLA).
     *
     * @return class byte.
     */
    public int getCLA() {
        return header[0];
    }

    /**
     * Set instruction byte (INS). The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param ins new class byte.
     * @return this
     */
    public ApduCommand setINS(int ins) {
        header[1] = (byte) ins;
        return this;
    }

    /**
     * Get instruction byte (INS).
     *
     * @return instruction byte.
     */
    public int getINS() {
        return header[1];
    }

    /**
     * Set parameter byte P1. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param p1 new P1 byte.
     * @return this
     */
    public ApduCommand setP1(int p1) {
        header[2] = (byte) p1;
        return this;
    }

    /**
     * Get parameter byte P1.
     *
     * @return P1 byte.
     */
    public int getP1() {
        return header[2];
    }

    /**
     * Set parameter byte P2. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param p2 new P2 byte.
     * @return this
     */
    public ApduCommand setP2(int p2) {
        header[3] = (byte) p2;
        return this;
    }

    /**
     * Get parameter byte P2.
     *
     * @return P2 byte.
     */
    public int getP2() {
        return header[3];
    }

    /**
     * Set command header of APDU. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param header new command data.
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws ApduException if conversion of data object into a byte array
     *         fails.
     */
    public ApduCommand setHeader(byte[] header) throws ApduException {
        this.header = Arrays.copyOf(header, 4);
        return this;
    }

    /**
     * Get command data of APDU.
     *
     * @return byte array containing the command data of APDU.
     */
    public byte[] getHeader() {
        return header != null ? header.clone() : null;
    }

    /**
     * Set command data of APDU. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param data new command data.
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws ApduException if conversion of data object into a byte array
     *         fails.
     */
    public ApduCommand setData(byte[] data) throws ApduException {
        this.data = data.clone();
        if (!checkExtendedApdu())
            forceExtended = false;
        return this;
    }

    /**
     * Get command data of APDU.
     *
     * @return byte array containing the command data of APDU.
     */
    public byte[] getData() {
        return data != null ? data.clone() : null;
    }

    /**
     * Set expected length (le). The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param expectedLength expected response data length in bytes.
     * @return reference to 'this' to allow simple concatenation of operations.
     */
    public ApduCommand setLe(int expectedLength) {
        le = expectedLength;
        if (!checkExtendedApdu())
            forceExtended = false;
        return this;
    }

    /**
     * Get expected response data length (le)
     *
     * @return expected response data length.
     */
    public int getLe() {
        return le;
    }

    /**
     * Get length of command data (Lc).
     *
     * @return length of command data in bytes.
     */
    public int getLc() {
        return data.length;
    }

    /**
     * Returns the length of the APDU command in bytes.
     *
     * @return length of the APDU command including the header, data and
     *         potential le byte.
     */
    public int getLength() {
        int iLength;
        int iLc = data.length;

        if (iLc == 0) {
            if (le == 0) {
                // case 1
                iLength = 4;
            } else if (le <= 256) {
                // case 2 short
                iLength = 5;
                if (forceExtended)
                    iLength = 7;
            } else {
                // case 2 extended
                iLength = 7;
            }
        } else {
            if ((iLc <= 255) && (le <= 256)) {
                // short case 3 or 4
                if (le == 0) {
                    // case 3 short
                    iLength = 5 + iLc;
                    if (forceExtended)
                        iLength = 7 + iLc;
                } else {
                    // case 4 short
                    iLength = 6 + iLc;
                    if (forceExtended)
                        iLength = 9 + iLc;
                }
            } else {
                if (le == 0) {
                    // case 3 extended
                    iLength = 7 + iLc;
                } else {
                    // case 4 extended
                    iLength = 9 + iLc;
                }
            }
        }

        return iLength;
    }

    /**
     * Returns a byte sequence representation of the APDU command.
     *
     * @return byte array containing the APDU command.
     */
    public byte[] toBytes() {
        byte[] abCommand = new byte[getLength()];
        int iOffset = 4;
        int iLc = data.length;

        // set first four header bytes
        System.arraycopy(header, 0, abCommand, 0, 4);

        // check if short APDU format
        if ((iLc <= 255) && (le <= 256) && !forceExtended) {
            // short APDU

            if (iLc > 0) {
                // set Lc byte and copy data
                abCommand[iOffset] = (byte) iLc;
                iOffset += 1;
                System.arraycopy(data, 0, abCommand, iOffset, iLc);
                iOffset += iLc;
            }

            if (le > 0) {
                // set le byte
                abCommand[iOffset] = (byte) le;
            }
        } else {
            // extended APDU
            abCommand[iOffset] = 0;
            iOffset += 1;
            if (iLc > 0) {
                // set extended Lc and copy data
                abCommand[iOffset] = (byte) (iLc >> 8);
                iOffset += 1;
                abCommand[iOffset] = (byte) iLc;
                iOffset += 1;
                System.arraycopy(data, 0, abCommand, iOffset, iLc);
                iOffset += iLc;
            }

            if (le > 0) {
                // set extended le
                abCommand[iOffset] = (byte) (le >> 8);
                iOffset += 1;
                abCommand[iOffset] = (byte) le;
            }
        }

        return abCommand;
    }

    /**
     * Allows changing Command APDU format to Extended or Short APDU format with
     * following conditions;
     * <br>
     * <b>Force-changing a Natural Extended APDU to Short APDU, will not take
     * effect. It still remains an Extended APDU.</b>
     * <br>
     * <b>Force-changing a Case-1 APDU to Extended APDU will not take effect. It
     * still remains a Short APDU.</b>
     *
     * @param isExtended TRUE for Extended form
     * @see ApduCommand#isExtendedFormat()
     */
    public void setExtendedFormat(boolean isExtended) {
        // For case-1: Cant allow to force as extended APDU
        if (getCase() == APDU_CASE_1)
            forceExtended = false;
        else {
            // If real extended APDU; cant allow to make it short APDU
            if (!checkExtendedApdu()) {
                forceExtended = isExtended;
            } else {
                forceExtended = true;
            }
        }
    }

    /**
     * Indicates whether this APDU is of Extended Format or not.
     *
     * @see ApduCommand#setExtendedFormat(boolean)
     * @return TRUE for Extended Format.
     */
    public boolean isExtendedFormat() {
        return forceExtended || checkExtendedApdu();
    }

    @Override
    public String toString() {
        return Utils.toHexString(toBytes());
    }

    /**
     * Append command data to current command.
     *
     * @param data byte stream of command data.
     * @return reference to 'this'. This allows concatenation of append
     *         operations within one source code line.
     * @throws ApduException if object cannot be converted in a byte stream.
     */
    public ApduCommand appendData(byte[] data) throws ApduException {
        byte[] abData = data;
        int iOldLength = this.data.length;
        int iNewLength = abData.length;

        // build new data array
        this.data = Arrays.copyOf(this.data, iOldLength + iNewLength);
        System.arraycopy(abData, 0, this.data, iOldLength, iNewLength);

        return this;
    }

    /**
     * Indicates whether this APDU is of Extended APDU or not.
     */
    private boolean checkExtendedApdu() {
        return (this.data.length > 255 || le > 256);
    }
}
