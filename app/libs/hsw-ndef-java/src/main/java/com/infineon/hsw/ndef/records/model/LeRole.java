// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Enumeration representing the LE role data type which defines the LE role
 * capabilities of the device.
 */
public enum LeRole {
    /**
     * Only peripheral role is supported.
     */
    PERIPHERAL((byte) 0x00),
    /**
     * Only central role is supported.
     */
    CENTRAL((byte) 0x01),
    /**
     * Peripheral and central roles are supported. Peripheral role is preferred
     * for connection establishment.
     */
    PERIPHERAL_CENTRAL((byte) 0x02),
    /**
     * Unknown role.
     */
    UNKNOWN((byte) 0x00),
    /**
     * Peripheral and central roles are supported. Central role is preferred for
     * connection establishment.
     */
    CENTRAL_PERIPHERAL((byte) 0x03);

    /**
     * Private variable to store the value.
     */
    private final byte value;

    /**
     * Constructor for private enumeration.
     *
     * @param roleValue LE role capabilities value
     */
    LeRole(final byte roleValue) {
        this.value = roleValue;
    }

    /**
     * Gets the LE role capabilities value.
     *
     * @return Returns the LE role capabilities value.
     */
    public byte getValue() {
        return value;
    }

    /**
     * Gets the LE role type enumeration with respect to input value.
     *
     * @param value Bluetooth LE role type value
     * @return Returns the LE role type enumeration.
     */
    public static LeRole getLeRole(@NotNull final byte value) {
        for (LeRole enumRole : LeRole.values()) {
            if (enumRole.getValue() == value) {
                return enumRole;
            }
        }
        return null;
    }
}
