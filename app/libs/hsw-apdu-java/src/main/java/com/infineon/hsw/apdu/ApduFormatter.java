// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.utils.Utils;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Formatter class for formatting logged APDU objects (ATR, command, response)
 * into strings.
 */
public class ApduFormatter extends SimpleFormatter {
    /** Prepend APDU and ATR with descriptive markers ('--&gt; ' / '&lt;-- ') */
    public static final int OPT_ADD_APDU_MARKER = 0x00000001;

    /** Extract APDU header in separate line */
    public static final int OPT_SEPARATE_HEADER = 0x00000002;

    /** Extract status word in separate line */
    public static final int OPT_SEPARATE_SW12 = 0x00000004;

    /** Helper constant for an empty string */
    private static final String EMPTY_STRING = "";

    /** Constant string for a single blank character */
    private static final String BLANK = " ";

    /** Constant string for a new line character */
    private static final String NEW_LINE = "\n";

    /** Constant string for an empty marker prefix */
    private static final String NUL_MARKER = "     ";

    /** Constant string for an APDU command marker prefix */
    private static final String CMD_MARKER = " --> ";

    /** Constant string for an APDU response marker prefix */
    private static final String RES_MARKER = " <-- ";

    /** Constant string for an ATR marker prefix */
    private static final String ATR_MARKER = "ATR: ";

    /** Constant string for a status word marker prefix */
    private static final String SW_MARKER = " SW: ";

    /** Format string for APDU response status */
    private static final String
            STATUS_LINE = "%02X %02X  Data:%5d Bytes  Exec Time: %8.2f ms\n \n";

    /** Number of bytes per line in case of byte array parameters */
    private int iBytesPerLine = 16;

    /** General formatting options */
    private int iOptions = OPT_ADD_APDU_MARKER | OPT_SEPARATE_HEADER |
                           OPT_SEPARATE_SW12;

    /**
     * Default constructor.
     */
    public ApduFormatter() {
        super();
    }

    /**
     * Constructor that provides specific options and the number of data bytes
     * which will be printed in one line when byte array data has to be
     * formatted.
     *
     * @param options      bit flags of formatting options.
     * @param bytesPerLine number of bytes per line in case of byte data to be
     *                     formatted.
     */
    public ApduFormatter(int options, int bytesPerLine) {
        iBytesPerLine = bytesPerLine;
        iOptions = options;
    }

    /**
     * Check if an optional feature is enabled.
     *
     * @param option one of the OPT_xx constants is enabled. If a combination of
     *               features is
     *               passed, the method returns true if all of the features are
     *               enabled.
     * @return true if feature is enabled.
     */
    public boolean isOptionEnabled(int option) {
        return (iOptions & option) == option;
    }

    /**
     * Enable or disable one or more optional feature(s). This method can also
     * be used to query the current set of enabled features by passing zero as
     * <code>option</code> parameter.
     *
     * @param option  one (or combination) of the OPT_xxx constants
     * @param enabled if true the feature(s) are enabled, otherwise the features
     *                will be disabled
     * @return new combination of optional features.
     */
    public int enableOption(int option, boolean enabled) {
        if (enabled)
            iOptions |= option;
        else
            iOptions &= ~option;

        return iOptions;
    }

    /**
     * Set the number of data bytes that will be placed into one line when byte
     * array data is to be formatted.
     *
     * @param bytesPerLine number of bytes per line (1..Integer.MAX_VALUE).
     * @throws ApduException if bytesPerLine parameter is smaller or equal to
     *         zero.
     */
    public void setBytesPerLine(int bytesPerLine) throws ApduException {
        if (bytesPerLine >= 1)
            iBytesPerLine = bytesPerLine;
        else
            throw new ApduException(
                    "Illegal value for bytes per line (must be larger than 0)");
    }

    /**
     * Retrieve the number of data bytes that will be placed in on line when
     * byte array data is to be formatted.
     *
     * @return number of bytes per line.
     */
    public int getBytesPerLine() {
        return iBytesPerLine;
    }

    @Override
    public String format(LogRecord logRecord) {
        String strMessage = logRecord.getMessage();
        Object[] aoParams = logRecord.getParameters();

        // check if parameter is a byte array
        if ((aoParams != null) && (aoParams.length == 1)) {
            if (aoParams[0] instanceof byte[]) {
                strMessage = formatByteArray(strMessage, (byte[]) aoParams[0]);
            } else if (aoParams[0] instanceof ApduCommand) {
                strMessage = formatApduCommand(strMessage,
                                               (ApduCommand) aoParams[0]);
            } else if (aoParams[0] instanceof ApduResponse) {
                strMessage = formatApduResponse(strMessage,
                                                (ApduResponse) aoParams[0]);
            } else if (aoParams[0] instanceof ATR) {
                strMessage = formatATR(strMessage, (ATR) aoParams[0]);
            } else {
                // use standard formatting if various parameters defined
                strMessage = new MessageFormat(strMessage)
                                     .format(aoParams, new StringBuffer(), null)
                                     .toString();
            }
        } else if (aoParams != null) {
            // use standard formatting if various parameters defined
            strMessage = new MessageFormat(strMessage)
                                 .format(aoParams, new StringBuffer(), null)
                                 .toString();
        }

        if (!strMessage.endsWith(NEW_LINE))
            strMessage = strMessage + NEW_LINE;

        return strMessage;
    }

    /**
     * Helper method to format the content of an ATR object.
     *
     * @param strMessage optional comment to be printed in a separate line
     *         before
     *                   the ATR bytes.
     * @param atr        ATR object to be formatted.
     * @return String related to the message and ATR.
     */
    private String formatATR(String strMessage, ATR atr) {
        byte[] atrBytes = atr.toBytes();

        if (strMessage == null)
            strMessage = EMPTY_STRING;

        // create string builder
        StringBuilder buffer = new StringBuilder(strMessage.length() +
                                                 atrBytes.length * 3 +
                                                 (ATR_MARKER.length() + 1) * 2);

        // append message first
        buffer.append(strMessage);

        // append new line if string is not empty or does not end with new line
        if (!strMessage.isEmpty() && !strMessage.endsWith(NEW_LINE))
            buffer.append(NEW_LINE);

        String marker = EMPTY_STRING;
        if (isOptionEnabled(OPT_ADD_APDU_MARKER))
            marker = ATR_MARKER;

        buffer.append(formatByteArray(marker, atr.toBytes()));
        buffer.append(BLANK).append(NEW_LINE);

        return buffer.toString();
    }

    /**
     * Helper method to format the content of an APDU response.
     *
     * @param strMessage   optional message to be printed in a separate line
     *         before
     *                     the actual content of the APDU response.
     * @param apduResponse APDU response object to be formatted.
     * @return String related to the message and the APDU response.
     */
    public String formatApduResponse(String strMessage,
                                     ApduResponse apduResponse) {
        int iLength = apduResponse.getDataLength();
        String marker = isOptionEnabled(OPT_ADD_APDU_MARKER) ? RES_MARKER
                                                             : EMPTY_STRING;

        // force defined message
        if (strMessage == null)
            strMessage = EMPTY_STRING;

        // create string builder
        StringBuilder buffer = new StringBuilder(
                strMessage.length() + 7 + iLength * 3 +
                (marker.length() + 1) * (iLength / iBytesPerLine + 1));

        // append message first
        buffer.append(strMessage);

        // append new line if string is not empty or does not end with new line
        if (!strMessage.isEmpty() && !strMessage.endsWith(NEW_LINE))
            buffer.append(NEW_LINE);

        // check if special formatting
        if (isOptionEnabled(OPT_SEPARATE_SW12)) {
            // check if response data available
            if (iLength > 0)
                buffer.append(formatByteArray(marker, apduResponse.getData()));

            // check if special tags shall be added
            if (isOptionEnabled(OPT_ADD_APDU_MARKER))
                buffer.append(SW_MARKER);

            // print status word and additional info
            buffer.append(
                    String.format(STATUS_LINE,
                                  (byte) (apduResponse.getSW() >> 8),
                                  (byte) apduResponse.getSW(),
                            iLength,
                                  Float.valueOf(
                                          "" + apduResponse.getExecutionTime() /
                                                       1000000.0)));
        } else {
            // append complete response
            buffer.append(formatByteArray(marker, apduResponse.toBytes()));
        }

        return buffer.toString();
    }

    /**
     * Helper method to format an APDU command.
     *
     * @param strMessage  optional message printed in a separate line before the
     *                    APDU data
     * @param apduCommand APDU command object to be formatted.
     * @return String related to message and APDU command.
     */
    private String formatApduCommand(String strMessage,
                                     ApduCommand apduCommand) {
        byte[] apduBytes = apduCommand.toBytes();
        int iLength = apduBytes.length;
        String marker1 = EMPTY_STRING;
        String marker2 = EMPTY_STRING;

        // prepend marker if enabled
        if (isOptionEnabled(OPT_ADD_APDU_MARKER)) {
            marker1 = CMD_MARKER;
            marker2 = NUL_MARKER;
        }

        // force defined message
        if (strMessage == null)
            strMessage = EMPTY_STRING;

        // create string builder
        StringBuilder buffer = new StringBuilder(
                strMessage.length() + 1 + iLength * 3 +
                (marker1.length() + 1) * (iLength / iBytesPerLine + 1));

        // append message first
        buffer.append(strMessage);

        // append new line if string is not empty or does not end with new line
        if (!strMessage.isEmpty() && !strMessage.endsWith(NEW_LINE))
            buffer.append(NEW_LINE);

        if ((apduBytes.length <= 5) || !isOptionEnabled(OPT_SEPARATE_HEADER)) {
            // append command header or complete APDU
            buffer.append(formatByteArray(marker1, apduBytes));
        } else {
            // append command header
            buffer.append(
                    formatByteArray(marker1, Arrays.copyOf(apduBytes, 5)));
            // append command data
            buffer.append(
                    formatByteArray(marker2,
                                    Arrays.copyOfRange(apduBytes, 5,
                                                       apduBytes.length)));
        }

        return buffer.toString();
    }

    /**
     * Helper method to format byte array data.
     *
     * @param prefix prefix string which will be placed in front of the hex
     *         data.
     *               If the hex data is formatted in more than one line, the
     *               following lines will
     *               have a prefix of blanks of the same length as the original
     *               prefix.
     * @param data   byte array data to be formatted as hex string.
     * @return String related to the byte array data.
     */
    private String formatByteArray(String prefix, byte[] data) {
        int iOffset = 0, iLength = data.length;

        if (iLength <= iBytesPerLine) {
            // output single line
            prefix = prefix + Utils.toHexString(data, iOffset, iLength, BLANK) +
                     "\n";
        } else {
            // first line starts with comment
            char[] acComment = prefix.toCharArray();

            // create string builder
            StringBuilder buffer = new StringBuilder(
                    acComment.length + iLength * 3 + (iLength >> 3) + 1);

            do {
                // determine length for this round
                int iTempLen = (Math.min(iLength, iBytesPerLine));

                // output command and data
                buffer.append(acComment);
                buffer.append(
                        Utils.toHexString(data, iOffset, iTempLen, BLANK));
                buffer.append('\n');

                // next lines start with blanks
                Arrays.fill(acComment, ' ');

                // decrease length and increase offset
                iLength -= iTempLen;
                iOffset += iTempLen;
            } while (iLength > 0);

            prefix = buffer.toString();
        }
        return prefix;
    }
}