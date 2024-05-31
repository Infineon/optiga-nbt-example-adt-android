// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.bp.encoder;

import com.infineon.hsw.ndef.bp.BrandProtectionRecord;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.encoder.IRecordPayloadEncoder;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Encodes the payload bytes of the brand protection record.
 */
public class BrandProtectionRecordPayloadEncoder
        implements IRecordPayloadEncoder {
    /**
     * Error message if the abstract record is not an instance of the brand
     * protection record.
     */
    public static final String ERR_MESSAGE_UNKNOWN_RECORD =
            "Abstract record should be instance of brand protection record";
    /**
     * Error message if the payload is invalid.
     */
    private static final String
            ERR_MESSAGE_PAYLOAD_NULL = "Payload should not be null";

    /**
     * Constructor of the brand protection record payload encoder.
     */
    public BrandProtectionRecordPayloadEncoder() {
        /* Default constructor to be left empty. */
    }

    /**
     * Encodes the brand protection record data structure into the record
     * payload bytes.
     *
     * @param brandProtectionRecord Brand protection record containing the
     *     certificate.
     * @return Encoded record payload as bytes.
     * @throws NdefException If unable to encode the payload X.509 v3
     *         certificate bytes.
     */
    @Override
    public byte[] encode(@NotNull AbstractRecord brandProtectionRecord)
            throws NdefException {
        if (brandProtectionRecord instanceof BrandProtectionRecord) {
            BrandProtectionRecord bpRecord = (BrandProtectionRecord)
                    brandProtectionRecord;
            if (bpRecord.getPayload() == null) {
                throw new NdefException(ERR_MESSAGE_PAYLOAD_NULL);
            }
            return bpRecord.getPayload();
        } else {
            throw new NdefException(ERR_MESSAGE_UNKNOWN_RECORD);
        }
    }
}
