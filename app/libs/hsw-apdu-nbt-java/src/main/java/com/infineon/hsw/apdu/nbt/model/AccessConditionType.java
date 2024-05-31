// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt.model;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Enumeration defines the access condition of file access policy.
 */
public enum AccessConditionType {
    /**
     * No password verification is required, if the file is configured with
     * ALWAYS access condition.
     */
    ALWAYS((byte) 0x40),

    /**
     * Access is not allowed to the file, if file is configured with NEVER
     * access condition.
     */
    NEVER((byte) 0x00),

    /**
     * Access is allowed only after password verification, if the file is
     * configured with PASSWORD_PROTECTED access condition. This
     * configuration byte has to be appended with password ID (5 bits).
     */
    PASSWORD_PROTECTED((byte) 0x80);

    /*
     * Value stores the byte value of the access condition.
     */
    private final byte value;

    /*
     * Constructor for the enumeration
     */
    AccessConditionType(@NotNull byte value) {
        this.value = value;
    }

    /**
     * Getter for the access condition byte.
     *
     * @return Returns the byte value respective of access condition.
     */
    public byte getValue() {
        return value;
    }
}
