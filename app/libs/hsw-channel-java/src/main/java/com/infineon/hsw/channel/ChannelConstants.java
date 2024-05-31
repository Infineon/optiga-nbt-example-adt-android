// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.channel;

/**
 *
 * Class to define the channel related constants.
 *
 */
public final class ChannelConstants {
    /**
     * String constant for connection mode
     */
    public static final String CONNECTION_MODE = "Connection Mode";
    /**
     * String constant for protocol
     */
    public static final String PROTOCOL = "Protocol";
    /**
     * String constant for PCSC terminals
     */
    public static final String PCSC_CHANNEL_TYPE_NAME = "PCSC Terminals";
    /**
     * String constant for shared connection mode
     */
    public static final String SHARED = "Shared";
    /**
     * String constant for exclusive connection mode
     */
    public static final String EXCLUSIVE = "Exclusive";
    /**
     * String constant for direct connection mode
     */
    public static final String DIRECT = "direct";
    /**
     * String constant for automatic connection mode
     */
    public static final String AUTO = "Auto";
    /**
     * String constant for asterisk symbol
     */
    public static final String STAR = "*";
    /**
     * String constant for T=0 protocol
     */
    public static final String T0 = "T=0";
    /**
     * String constant for T=1 protocol
     */
    public static final String T1 = "T=1";

    /**
     * String constant for disposition action
     */
    public static final String
            DISPOSITION_ACTION_LABEL = "Disposition on disconnect";

    /**
     * Default disposition action
     */
    public static final String DISPOSITION_ACTION =
            DISPOSITION_TYPES.SCARD_UNPOWER_CARD.getValue();

    /**
     * Private constructors for class.
     */
    private ChannelConstants() {
    }
}
