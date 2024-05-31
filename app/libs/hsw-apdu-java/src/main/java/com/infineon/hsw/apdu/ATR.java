// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.utils.Utils;

/**
 * Container class for the card answer to reset.
 */
public class ATR {
    /** Byte array containing the ATR */
    private final byte[] abATR;

    /**
     * Constructor.
     *
     * @param atr byte array containing ATR.
     */
    public ATR(byte[] atr) {
        abATR = atr.clone();
    }

    /**
     * Returns the byte array representation of the ATR.
     *
     * @return byte array containing the card answer to reset.
     */
    public byte[] toBytes() {
        return abATR != null ? abATR.clone() : null;
    }

    /**
     * Returns the ATR as a hex string.
     *
     * @return ATR as hex string.
     */
    public String toString() {
        return Utils.toHexString(abATR);
    }
}
