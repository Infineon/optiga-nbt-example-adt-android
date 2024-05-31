// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Enumeration representing the Bluetooth LE address types.
 */
public enum BluetoothLeDeviceAddressType {
    /**
     * LSB = 1, random device address.
     */
    RANDOM((byte) 0x01),
    /**
     * LSB = 0, public device address.
     */
    PUBLIC((byte) 0x00);

    /**
     * Private variable to store the value.
     */
    private final byte value;

    /**
     * Private enumeration constructor.
     *
     * @param typeValue LE address type as then random or public device address
     *                  value.
     */
    BluetoothLeDeviceAddressType(@NotNull final byte typeValue) {
        this.value = typeValue;
    }

    /**
     * Gets the LE address type as random or public device address value.
     *
     * @return Returns the LE address type as random or public device address
     *     value.
     */
    public byte getValue() {
        return value;
    }

    /**
     * Gets the LE address type enumeration with respect to the input value.
     *
     * @param value Bluetooth LE address value
     * @return Returns the LE address type enumeration.
     */
    public static BluetoothLeDeviceAddressType getBluetoothLeDeviceAddressType(
            @NotNull final byte value) {
        for (BluetoothLeDeviceAddressType type :
             BluetoothLeDeviceAddressType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
