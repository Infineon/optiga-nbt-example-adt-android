// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.rtd.ErrorRecord;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Decodes the payload byte array of error record type.
 */
public class ErrorRecordPayloadDecoder implements IRecordPayloadDecoder {
    /**
     * Defines the minimum length of the error record payload data.
     */
    private static final int MIN_LENGTH = 1;

    /**
     * Defines the error reason field index.
     */
    private static final int ERROR_REASON_FIELD_INDEX = 0;

    /**
     * Defines the error data field start index.
     */
    private static final int ERROR_DATA_FIELD_START_INDEX = 1;

    /**
     * ErrorRecordPayloadDecoder constructor.
     */
    public ErrorRecordPayloadDecoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Decodes the error record payload byte array into the record data
     * structure.
     *
     * @param payload ErrorRecord payload byte array
     * @return Abstract record data structure
     * @throws NdefException Invalid payload
     */
    @Override
    public AbstractRecord decode(@NotNull byte[] payload) throws NdefException {
        try {
            validate(payload, MIN_LENGTH);
            final int ERROR_DATA_FIELD_LENGTH = payload.length - 1;
            return new ErrorRecord(
                    payload[ERROR_REASON_FIELD_INDEX],
                    Utils.extractBytes(payload, ERROR_DATA_FIELD_START_INDEX,
                                       ERROR_DATA_FIELD_LENGTH));
        } catch (RuntimeException e) {
            throw new NdefException(ERR_MESSAGE_INVALID_PAYLOAD, e);
        }
    }
}
