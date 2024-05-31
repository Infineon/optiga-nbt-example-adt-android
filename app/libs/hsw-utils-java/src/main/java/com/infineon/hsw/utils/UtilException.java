// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.utils;

/**
 * Exception class for Utility related exceptions.
 */
public class UtilException extends Exception {
    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 379528577894198452L;

    /**
     * Calls the super constructor with the given message.
     *
     * @param message message for exception
     */
    public UtilException(String message) {
        super(message);
    }

    /**
     * Calls the super constructor with the given message and the exception
     * stack.
     *
     * @param message message for exception
     * @param e       exception stack
     */
    public UtilException(String message, Exception e) {
        super(message, e);
    }
}
