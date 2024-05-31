// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.rtd.ErrorRecord;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Encodes the error record type to payload byte array.
 */
public class ErrorRecordPayloadEncoder implements IRecordPayloadEncoder {
    /**
     * Defines empty error data reference size.
     */
    private static final int EMPTY_ERROR_DATA_SIZE = 0;

    /**
     * Defines start error data reference size.
     */
    private static final int ERROR_DATA_BYTE_START_OFFSET = 0;

    /**
     * Defines the length.
     */
    private static final int DEF_LEN = 1;

    /**
     * Defines the error reason field index.
     */
    private static final int ERROR_REASON_FIELD_INDEX = 0;

    /**
     * Defines the error data field start index.
     */
    private static final int ERROR_DATA_FIELD_START_INDEX = 1;

    /**
     * ErrorRecordPayloadEncoder constructor.
     */
    public ErrorRecordPayloadEncoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Encodes the error record data structure into the record payload byte
     * array.
     *
     * @param wellKnownRecord WellKnownRecord ErrorRecord
     * @return Record payload byte array
     * @throws NdefException If unable to encode the record payload
     */
    @Override
    public byte[] encode(@NotNull AbstractRecord wellKnownRecord)
            throws NdefException {
        if (!(wellKnownRecord instanceof ErrorRecord)) {
            throw new NdefException(ERR_UNSUPPORTED_TYPE);
        }

        final ErrorRecord errorRecord = (ErrorRecord) wellKnownRecord;
        byte[] errorRecordPayloadAsBytes;
        if (errorRecord.getErrorData() == null ||
            errorRecord.getErrorData().length == EMPTY_ERROR_DATA_SIZE) {
            errorRecordPayloadAsBytes = new byte[DEF_LEN];
        } else {
            errorRecordPayloadAsBytes =
                    new byte[errorRecord.getErrorData().length + DEF_LEN];
            Utils.arrayCopy(errorRecord.getErrorData(),
                            ERROR_DATA_BYTE_START_OFFSET,
                            errorRecordPayloadAsBytes,
                            ERROR_DATA_FIELD_START_INDEX,
                            errorRecord.getErrorData().length);
        }
        errorRecordPayloadAsBytes[ERROR_REASON_FIELD_INDEX] =
                errorRecord.getErrorReason();
        return errorRecordPayloadAsBytes;
    }
}
