// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.bp.decoder;

import com.infineon.hsw.ndef.bp.BrandProtectionRecord;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.decoder.IRecordPayloadDecoder;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Decodes the payload of the brand protection record.
 */
public class BrandProtectionRecordPayloadDecoder
        implements IRecordPayloadDecoder {
    /**
     * Constructor of the brand protection record payload decoder.
     */
    public BrandProtectionRecordPayloadDecoder() {
        /* Default constructor to be left empty. */
    }

    /**
     * Decodes the brand protection record payload bytes into the record
     * data structure.
     *
     * @param x509V3CertificateBytes Payload of the brand protection record
     *     containing the X.509 v3 certificate.
     * @return Brand protection record data structure.
     * @throws NdefException If unable to decode the payload X.509 v3
     *         certificate
     *     bytes.
     */
    @Override
    public AbstractRecord decode(@NotNull byte[] x509V3CertificateBytes)
            throws NdefException {
        return new BrandProtectionRecord(x509V3CertificateBytes);
    }
}
