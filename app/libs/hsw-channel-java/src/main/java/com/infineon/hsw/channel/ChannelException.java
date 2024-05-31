// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.channel;

/**
 * Exception class for channel related exceptions.
 */
public class ChannelException extends Exception {
    /**
     * Calls the super constructor with the given message.
     *
     * @param message message for exception
     */
    public ChannelException(final String message) {
        super(message);
    }

    /**
     * Calls the super constructor with the exception stack
     *
     * @param message message for exception.
     * @param e       exception stack.
     */
    public ChannelException(final String message, final Exception e) {
        super(message, e);
    }
}
