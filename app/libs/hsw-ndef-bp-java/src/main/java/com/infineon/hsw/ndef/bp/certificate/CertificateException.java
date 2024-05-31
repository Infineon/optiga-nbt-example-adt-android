// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.bp.certificate;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Exception class for certificate encode and decode related exceptions.
 */
public class CertificateException extends NdefException {
    private static final long serialVersionUID = 44562773824198452L;

    /**
     * Constructs an exception with the given exception message.
     *
     * @param message Message for the exception.
     */
    public CertificateException(@NotNull String message) {
        super(message);
    }

    /**
     * Constructs an exception with the exception message and exception stack.
     *
     * @param message   Message for the exception.
     * @param exception Exception stack
     */
    public CertificateException(@NotNull String message,
                                @NotNull Exception exception) {
        super(message, exception);
    }
}
