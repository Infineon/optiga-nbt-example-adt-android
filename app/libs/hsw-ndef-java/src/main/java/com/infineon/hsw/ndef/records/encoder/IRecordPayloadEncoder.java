// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;

/**
 * Interface to encode the payload of the NDEF record.
 */
public interface IRecordPayloadEncoder {
    /**
     * Error message if the payload type is not supported.
     */
    static final String ERR_UNSUPPORTED_TYPE = "Unsupported payload type";
    /**
     * Encodes the record payload.
     *
     * @param abstractRecord Library-known NDEF record
     * @return NDEF record payload byte array
     * @throws NdefException If unable to encode the record payload
     */
    byte[] encode(AbstractRecord abstractRecord) throws NdefException;
}
