// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Interface for the NDEF record payload decoder.
 */
public interface IRecordPayloadDecoder {
    /**
     * Error message if the payload is invalid.
     */
    String ERR_MESSAGE_INVALID_LENGTH = "Invalid length of payload bytes";

    /**
     * Error message if unable to decode the payload.
     */
    String ERR_MESSAGE_INVALID_PAYLOAD = "Unable to decode payload bytes";

    /**
     * Decodes the NDEF record payload byte array into the record data
     * structure.
     *
     * @param payload NDEF record payload byte array
     * @return Abstract record data structure
     * @throws NdefException Invalid payload
     */
    AbstractRecord decode(@NotNull byte[] payload) throws NdefException;

    /**
     * Default function to validate the input payload.
     *
     * @param payload           NDEF record payload byte array
     * @param minExpectedLength Minimum expected payload length
     * @throws NdefException Invalid payload length
     */
    default void validate(@NotNull byte[] payload,
                          @NotNull int minExpectedLength) throws NdefException {
        if (payload == null || payload.length < minExpectedLength) {
            throw new NdefException(ERR_MESSAGE_INVALID_LENGTH);
        }
    }
}