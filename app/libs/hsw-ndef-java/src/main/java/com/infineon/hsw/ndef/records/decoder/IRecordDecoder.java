// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.InputStream;

/**
 * Interface to decode the NDEF well known record types.
 */
public interface IRecordDecoder {
    /**
     * Checks if the library supports decoding the specified record type.
     *
     * @param recordType NDEF record type
     * @return Returns true, if the record can be decoded by the library.
     */
    boolean canDecode(byte[] recordType);

    /**
     * Decodes the NDEF record.
     *
     * @param header Header of the NDEF record
     * @param recordStream NDEF record
     * @return Returns the decoded NDEF record.
     * @throws NdefException Throws an NDEF exception, if unable to decode the
     * 						 the record.
     */
    AbstractRecord decode(@NotNull int header, InputStream recordStream)
            throws NdefException;
}
