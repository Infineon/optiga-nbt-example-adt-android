// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.bp.certificate;

import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

/**
 * Certificate handler implementation for handling X509 certificates.
 */
public class X509CertificateHandler implements ICertificateHandler {
    /** Format of the X.509 certificate. */
    public static final String FORMAT_X509 = "X.509";

    /**
     * Error message if the payload is invalid.
     */
    private static final String
            ERR_MESSAGE_PAYLOAD_NULL = "Payload should not be null";

    /**
     * Error message if the certificate is not an X.509 certificate.
     */
    private static final String ERR_MESSAGE_UNKNOWN_CERTIFICATE =
            "Certificate is not an X.509 certificate";

    /* default */ Logger logger = Logger.getLogger(
            X509CertificateHandler.class.getName());

    /**
     * Constructor of X.509 CertificateHandler class.
     */
    public X509CertificateHandler() {
        /* Default constructor to be left empty. */
    }

    /**
     * Gets the certificate format supported by this handler.
     *
     * @return Format
     */
    private static String getFormat() {
        return FORMAT_X509;
    }

    /**
     * Decodes the X.509 certificate bytes into X.509 certificate object.
     *
     * @param certificateBytes X.509 certificate as bytes.
     * @return  Decoded X.509 certificate.
     * @throws CertificateException If certificate decoding fails.
     */
    public X509Certificate decode(@NotNull byte[] certificateBytes)
            throws CertificateException {
        if (certificateBytes == null) {
            throw new CertificateException(ERR_MESSAGE_PAYLOAD_NULL);
        }
        try {
            return (X509Certificate) CertificateFactory.getInstance(getFormat())
                    .generateCertificate(
                            new ByteArrayInputStream(certificateBytes));
        } catch (java.security.cert.CertificateException exception) {
            throw new CertificateException(exception.getMessage(), exception);
        }
    }

    /**
     * Encodes the X.509 certificate into bytes.
     *
     * @param certificate X.509 certificate to be encoded.
     * @return Encoded X.509 certificate.
     * @throws CertificateException If certificate encoding fails.
     */
    public byte[] encode(@NotNull Object certificate)
            throws CertificateException {
        try {
            if (!(certificate instanceof X509Certificate)) {
                throw new CertificateException(ERR_MESSAGE_UNKNOWN_CERTIFICATE);
            }
            return ((X509Certificate) certificate).getEncoded();
        } catch (CertificateEncodingException exception) {
            throw new CertificateException(exception.getMessage(), exception);
        }
    }
}
