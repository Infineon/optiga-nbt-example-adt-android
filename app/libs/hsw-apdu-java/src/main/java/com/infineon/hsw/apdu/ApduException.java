// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

/**
 * Exception class for APDU (and communication) related exceptions.
 */
public class ApduException extends Exception {
    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 379523773824198452L;

    /**
     * Calls the super constructor with the given message and adds a message
     * prefix.
     *
     * @param message message for exception
     */
    public ApduException(String message) {
        super(message);
    }

    /**
     * Calls the super constructor with the exception stack, the given message
     * and adds a message prefix.
     *
     * @param message message for exception
     * @param e       exception stack
     */
    public ApduException(String message, Exception e) {
        super(message, e);
    }
}
