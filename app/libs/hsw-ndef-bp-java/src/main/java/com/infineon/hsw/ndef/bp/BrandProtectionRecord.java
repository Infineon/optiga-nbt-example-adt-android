// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.bp;

import com.infineon.hsw.ndef.bp.certificate.CertificateException;
import com.infineon.hsw.ndef.bp.certificate.ICertificateHandler;
import com.infineon.hsw.ndef.bp.certificate.X509CertificateHandler;
import com.infineon.hsw.ndef.records.rtd.AbstractExternalTypeRecord;
import com.infineon.hsw.utils.annotation.NotNull;
import java.security.cert.Certificate;

/**
 * Stores the brand protection record, including an X.509 v3 certificate for
 * offline authentication use cases.
 */
public class BrandProtectionRecord extends AbstractExternalTypeRecord {
    /**
     * Error message if ICertificateHandler is not set.
     */
    private static final String
            ERR_MESSAGE_UNKNOWN_HANDLER = "Certificate handler is not provided";

    /**
     * Error message if the payload is invalid.
     */
    private static final String
            ERR_MESSAGE_PAYLOAD_NULL = "Payload should not be null";

    /**
     * CertificateHandler for encoding and decoding a specific type of
     * certificate.
     */
    private ICertificateHandler certificateHandler;

    /**
     * Defines the brand protection record type definition (RTD).
     */
    public static final String
            BRAND_PROTECTION_RTD_TYPE = "infineon.com:nfc-bridge-tag.x509";

    /**
     * Constructor to create the brand protection record.
     *
     * @param certificate Content of the X.509 v3 certificate as bytes
     */
    public BrandProtectionRecord(@NotNull byte[] certificate) {
        super(BRAND_PROTECTION_RTD_TYPE);
        this.payload = certificate;
        setCertificateHandler(new X509CertificateHandler());
    }

    /**
     * Constructor to create a brand protection record.
     */
    public BrandProtectionRecord() {
        super(BRAND_PROTECTION_RTD_TYPE);
        setCertificateHandler(new X509CertificateHandler());
    }

    /**
     * Gets the decoded payload with the help of ICertificateHandler and provide
     * a certificate object.
     *
     * @return Certificate object.
     * @throws CertificateException If ICertificateHandler is unable to decode
     *         the certificate.
     */
    public java.security.cert.Certificate getCertificate()
            throws CertificateException {
        if (certificateHandler == null) {
            throw new CertificateException(ERR_MESSAGE_UNKNOWN_HANDLER);
        }
        if (payload == null) {
            throw new CertificateException(ERR_MESSAGE_PAYLOAD_NULL);
        }
        return (Certificate) certificateHandler.decode(payload);
    }

    /**
     * Sets the certificate object as a payload of the record.
     *
     * @param certificate Certificate object.
     * @throws CertificateException If ICertificateHandler is unable to encode
     *         the certificate.
     */
    public void setCertificate(
            @NotNull java.security.cert.Certificate certificate)
            throws CertificateException {
        if (certificateHandler == null) {
            throw new CertificateException(ERR_MESSAGE_UNKNOWN_HANDLER);
        }
        this.payload = certificateHandler.encode(certificate);
    }

    /**
     * Returns the record payload bytes.
     *
     * @return Returns the record payload bytes.
     */
    @Override
    public byte[] getPayload() {
        if (payload == null) {
            return new byte[0];
        }
        return payload.clone();
    }

    /**
     * Sets an encoded certificate as record payload.
     *
     * @param payload Encoded certificate.
     */
    @Override
    public void setPayload(@NotNull byte[] payload) {
        this.payload = payload;
    }

    /**
     * Sets the certificate handler.
     *
     * @param certificateHandler Certificate handler to be used for encoding and
     *     decoding.
     */
    private void setCertificateHandler(
            @NotNull ICertificateHandler certificateHandler) {
        this.certificateHandler = certificateHandler;
    }
}
