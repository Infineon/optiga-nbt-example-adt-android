// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.utils.Utils;
import java.util.Arrays;

/**
 * Container class for APDU responses. The response consists of response data
 * (optional) and
 * status word (mandatory).
 */
public class ApduResponse {
    /** Status word indicating successful operation */
    public static final int SW_NO_ERROR = 0x9000;

    /** Error message for invalid card */
    public static final String
            INVALID_CARD_ERROR = "Card in terminal is no valid debug target";

    /**
     * Status word indicating File/DF is deactivated but might be selected on
     * card
     */
    public static final int SW_SELECT_NO_ERROR = 0x6283;

    /** Status word indicating condition of use not satisfied */
    public static final int SW_CONDITIONS_NOT_SATISFIED = 0x6985;

    /** byte array containing response data and status word */
    private byte[] abResponse;

    /** Command execution time */
    private long lExecTime;

    /**
     * Build a response from a byte data stream.
     *
     * @param response byte data stream containing card response.
     * @param execTime command execution time in nanoseconds
     * @throws ApduException if response is null.
     */
    public ApduResponse(byte[] response, long execTime) throws ApduException {
        if (response == null) {
            throw new ApduException("No response");
        }

        abResponse = response.clone();
        lExecTime = execTime;

        // check if valid response
        if (abResponse.length < 2) {
            // build dummy response
            abResponse = new byte[2];
        }
    }

    /**
     * Build a response from a byte data stream.
     *
     * @param response byte data stream containing card response.
     * @param execTime command execution time in nanoseconds
     * @throws ApduException if conversion of response object into byte array
     *         fails.
     */
    public ApduResponse(String response, long execTime) throws ApduException {
        this(ApduUtils.toBytes(response), execTime);
    }

    /**
     * Append new response to existing response. This method is useful if the
     * response data
     * has to be concatenated from the content of multiple APDU responses (GET
     * RESPONSE).
     * The status word of the existing response will be replaced by the status
     * word of the new response. The new execution time will be added to the
     * existing execution time to form an overall response time. The method
     * returns a reference to 'this' to allow simple concatenation of
     * operations.
     *
     * @param response new response to be appended to existing response.
     * @param execTime execution time in nanoseconds for the new response
     *         fragment
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws ApduException in case response fragment cannot be converted into
     *         a byte array.
     */
    public ApduResponse appendResponse(ApduResponse response, long execTime)
            throws ApduException {
        return appendResponse(ApduUtils.toBytes(response), execTime);
    }

    /**
     * Append new response to existing response. This method is useful if the
     * response data
     * has to be concatenated from the content of multiple APDU responses (GET
     * RESPONSE).
     * The status word of the existing response will be replaced by the status
     * word of the new response. The new execution time will be added to the
     * existing execution time to form an overall response time. The method
     * returns a reference to 'this' to allow simple concatenation of
     * operations.
     *
     * @param response new response to be appended to existing response.
     * @param execTime execution time in nanoseconds for the new response
     *         fragment
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws ApduException in case response fragment cannot be converted into
     *         a byte array.
     */
    public ApduResponse appendResponse(byte[] response, long execTime)
            throws ApduException {
        // build byte array from new response and determine byte length
        byte[] newResponse = response;
        int length = newResponse.length;

        // add execution time
        lExecTime += execTime;

        if (length >= 2) {
            // append response data and overwrite status word of existing
            // response
            abResponse = Arrays.copyOf(abResponse,
                                       abResponse.length + length - 2);
            System.arraycopy(newResponse, 0, abResponse,
                             abResponse.length - length, length);
        }

        return this;
    }

    /**
     * Check if status word is SW_NO_ERROR (9000).
     *
     * @return reference to this. This allows to concatenate checks and other
     *         operations in one source code line.
     * @throws ApduException if received status word is not 9000.
     */
    public ApduResponse checkStatus() throws ApduException {
        return checkSW(SW_NO_ERROR);
    }

    /**
     * Check if status word is SW_NO_ERROR (9000).
     *
     * @return reference to this. This allows to concatenate checks and other
     *         operations in one source code line.
     * @throws ApduException if received status word is not 9000.
     */
    public ApduResponse checkOK() throws ApduException {
        return checkSW(SW_NO_ERROR);
    }

    /**
     * Check if status word is equal to the presented value.
     *
     * @param statusWord expected status word. Note that only the lower 16 bits
     *         are evaluated.
     * @return reference to 'this'. This allows to concatenate checks and other
     *         operations in one source code line.
     * @throws ApduException if received status word is not expected.
     */
    public ApduResponse checkSW(int statusWord) throws ApduException {
        if ((statusWord & 0xFFFF) != getSW()) {
            throw new ApduException(
                    String.format("Unexpected status word %02X", getSW()));
        }

        return this;
    }

    /**
     * Check if response data length matches to the presented value.
     *
     * @param length expected length of response data.
     * @return reference to 'this'. This allows to concatenate checks and other
     *         operations in one source code line.
     * @throws ApduException if received data length does not match to expected
     *                       value.
     */
    public ApduResponse checkDataLength(int length) throws ApduException {
        if (length != abResponse.length - 2) {
            throw new ApduException(
                    String.format("Unexpected response data length %d",
                                  abResponse.length - 2));
        }

        return this;
    }

    /**
     * Returns the status word contained in the card response.
     *
     * @return status word as integer (always positive value).
     */
    public int getSW() {
        return ApduUtils.getShort(abResponse, abResponse.length - 2);
    }

    /**
     * Get response data array.
     *
     * @return array containing the response data.
     */
    public byte[] getData() {
        return Arrays.copyOf(abResponse, abResponse.length - 2);
    }

    /**
     * Get length of response data.
     *
     * @return length of response data.
     */
    public int getDataLength() {
        return abResponse.length - 2;
    }

    /**
     * Return byte array representation of response data.
     *
     * @return byte array containing response and status word.
     */
    public byte[] toBytes() {
        return abResponse.clone();
    }

    @Override
    public String toString() {
        return Utils.toHexString(abResponse);
    }

    /**
     * Return command execution time in nano seconds. Depending on
     * the underlying reader hardware the execution time may contain overhead
     * times like transmission etc.
     *
     * @return Command execution time.
     */
    public long getExecutionTime() {
        return lExecTime;
    }
}
