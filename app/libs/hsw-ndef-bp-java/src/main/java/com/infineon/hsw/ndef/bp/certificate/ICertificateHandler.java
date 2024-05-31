// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.bp.certificate;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Interface for certificate encoder and decoder.
 */
public interface ICertificateHandler {
    /**
     * Parses the certificate bytes and returns the decoded certificate object.
     *
     * @param certificateBytes Encoded certificate as bytes.
     * @return Decoded certificate object.
     * @throws CertificateException If parsing certificate fails
     */
    Object decode(@NotNull byte[] certificateBytes) throws CertificateException;

    /**
     * Encodes the certificate into bytes.
     *
     * @param certificate Certificate to be encoded.
     * @return Encoded certificate as bytes.
     * @throws CertificateException If encoding the certificate fails
     */
    byte[] encode(@NotNull Object certificate) throws CertificateException;
}
