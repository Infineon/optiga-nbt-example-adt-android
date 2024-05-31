// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt.model;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Exception class for FAP policy encode and decode related exceptions.
 */
public class FileAccessPolicyException extends Exception {
    /**
     * Constructs an exception with the given exception message.
     *
     * @param message Message for the exception.
     */
    public FileAccessPolicyException(@NotNull final String message) {
        super(message);
    }

    /**
     * Constructs an exception with the exception message and exception stack.
     *
     * @param message   Message for the exception.
     * @param exception Exception stack
     */
    public FileAccessPolicyException(@NotNull final String message,
                                     @NotNull final Exception exception) {
        super(message, exception);
    }
}
