// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.model.RecordType;

/**
 * Interface to encode the library known NDEF record types.
 */
public interface IRecordEncoder {
    /**
     * Checks if the library supports encoding the specified record type.
     *
     * @param recordType NDEF record type
     * @return True if the record can be encoded by the library
     */
    boolean canEncode(RecordType recordType);

    /**
     * Encodes the NDEF record.
     *
     * @param abstractRecord NDEF record
     * @return Encoded NDEF record bytes
     * @throws NdefException If unable to encode the NDEF record
     */
    byte[] encode(AbstractRecord abstractRecord) throws NdefException;
}
