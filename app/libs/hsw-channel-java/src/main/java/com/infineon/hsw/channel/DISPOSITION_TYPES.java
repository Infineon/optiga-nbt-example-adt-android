// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.channel;

/**
 * DISPOSITION_TYPES enum defines action to take on the card in the connected
 * reader on close.
 */
public enum DISPOSITION_TYPES {
    // cspell:ignore SCARD, UNPOWER
    /**
     *  Does not perform any reset.
     */
    SCARD_LEAVE_CARD(0, "SCARD_LEAVE_CARD"),
    /**
     * Reset the card (warm reset).
     */
    SCARD_RESET_CARD(1, "SCARD_RESET_CARD"),
    /**
     * Power down the card (cold reset).
     */
    SCARD_UNPOWER_CARD(2, "SCARD_UNPOWER_CARD");

    /**
     * Position of the disposition value (ID)
     */
    private final int index;

    /**
     * Disposition action value
     */
    private final String value;

    /**
     * Create the DISPOSITION TYPES with parameters.
     *
     * @param index position of the disposition value (ID)
     * @param value Disposition action value
     */
    DISPOSITION_TYPES(int index, String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * provides the disposition action value with respect to index (ID)
     *
     * @param index position of the disposition value (ID)
     * @return Disposition action value
     */
    public static String getValue(int index) {
        for (DISPOSITION_TYPES type : DISPOSITION_TYPES.values()) {
            if (type.index == index) {
                return type.value;
            }
        }
        return SCARD_RESET_CARD.getValue();
    }

    /**
     * Provides the disposition action value index (ID) with respect to
     * disposition value
     *
     * @param value  Disposition action value
     * @return Position of the disposition value (ID)
     */
    public static int getIndex(String value) {
        for (DISPOSITION_TYPES type : DISPOSITION_TYPES.values()) {
            if (type.value.equals(value)) {
                return type.index;
            }
        }
        return SCARD_RESET_CARD.getIndex();
    }

    /**
     * Getter for index position of the disposition value (ID)
     * @return Position of the disposition value (ID)
     */
    public int getIndex() {
        return index;
    }

    /**
     * Getter for disposition action value
     * @return Disposition action
     */
    public String getValue() {
        return value;
    }
}
