// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Enumeration representing the common data types.
 */
public enum DataTypes {
    /**
     * Bluetooth flags
     */
    FLAGS((byte) 0x01),
    /**
     * Incomplete list of 16-­bit service class UUIDs
     */
    INCOMPLETE_SERVICE_CLASS_UUID_16_BIT((byte) 0x02),
    /**
     * Complete list of 16-­bit service class UUIDs
     */
    COMPLETE_SERVICE_CLASS_UUID_16_BIT((byte) 0x03),
    /**
     * Incomplete list of 32-­bit service class UUIDs
     */
    INCOMPLETE_SERVICE_CLASS_UUID_32_BIT((byte) 0x04),
    /**
     * Complete list of 32-­bit service class UUIDs
     */
    COMPLETE_SERVICE_CLASS_UUID_32_BIT((byte) 0x05),
    /**
     * Incomplete list of 128-­bit service class UUIDs
     */
    INCOMPLETE_SERVICE_CLASS_UUID_128_BIT((byte) 0x06),
    /**
     * Complete list of 128-­bit service class UUIDs
     */
    COMPLETE_SERVICE_CLASS_UUID_128_BIT((byte) 0x07),
    /**
     * Shortened local name
     */
    SHORTENED_LOCAL_NAME((byte) 0x08),
    /**
     * Complete local name
     */
    COMPLETE_LOCAL_NAME((byte) 0x09),
    /**
     * Tx power level
     */
    TX_POWER_LEVEL((byte) 0x0A),
    /**
     * Class of device
     */
    DEVICE_CLASS((byte) 0x0D),
    /**
     * Simple pairing hash C-­192
     */
    SIMPLE_PAIRING_HASH_C_192((byte) 0x0E),
    /**
     * Simple pairing randomizer R-­192
     */
    SIMPLE_PAIRING_RANDOMIZER_R_192((byte) 0x0F),
    /**
     * Security manager TK value
     */
    SECURITY_MANAGER_TK_VALUE((byte) 0x10),
    /**
     * Security manager out of band flags
     */
    SECURITY_MANAGER_OOB_FLAGS((byte) 0x11),
    /**
     * Slave connection interval range
     */
    SLAVE_CONNECTION_INTERVAL_RANGE((byte) 0x11),
    /**
     * List of 16­-bit service solicitation UUIDs
     */
    SERVICE_SOLICITATION_UUIDS_16_BIT((byte) 0x14),
    /**
     * List of 128­-bit service solicitation UUIDs
     */
    SERVICE_SOLICITATION_UUIDS_128_BIT((byte) 0x15),
    /**
     * Service data ­16-­bit UUID
     */
    SERVICE_DATA_UUIDS_16_BIT((byte) 0x16),
    /**
     * Public target address
     */
    PUBLIC_TARGET_ADDRESS((byte) 0x17),
    /**
     * Random target address
     */
    RANDOM_TARGET_ADDRESS((byte) 0x18),
    /**
     * Appearance
     */
    APPEARANCE((byte) 0x19),
    /**
     * Advertising interval
     */
    ADVERTISING_INTERVAL((byte) 0x1A),
    /**
     * LE bluetooth device address
     */
    LE_BLUETOOTH_DEVICE_ADDRESS((byte) 0x1B),
    /**
     * LE bluetooth Role
     */
    LE_BLUETOOTH_ROLE((byte) 0x1C),
    /**
     * Simple pairing hash C-­256
     */
    SIMPLE_PAIRING_HASH_C_256((byte) 0x1D),
    /**
     * Simple pairing randomizer R-256
     */
    SIMPLE_PAIRING_RANDOMIZER_R_256((byte) 0x1E),
    /**
     * List of 32­-bit service solicitation UUIDs
     */
    SERVICE_SOLICITATION_UUIDS_32_BIT((byte) 0x1F),
    /**
     * Service data ­32-­bit UUID
     */
    SERVICE_DATA_UUIDS_32_BIT((byte) 0x20),
    /**
     * Service data ­128-­bit UUID
     */
    SERVICE_DATA_UUIDS_128_BIT((byte) 0x21),
    /**
     * LE secure connections confirmation Value
     */
    LE_BLUETOOTH_SECURE_CONNECTIONS_CONFIRMATION_VALUE((byte) 0x22),
    /**
     * LE secure connections random value
     */
    LE_BLUETOOTH_SECURE_CONNECTIONS_RANDOM_VALUE((byte) 0x23),
    /**
     * URI
     */
    URI((byte) 0x24),
    /**
     * Indoor positioning
     */
    INDOOR_POSITIONING((byte) 0x25),
    /**
     * Transport discovery data
     */
    TRANSPORT_DISCOVERY_DATA((byte) 0x26),
    /**
     * LE supported features
     */
    LE_BLUETOOTH_SUPPORTED_FEATURES((byte) 0x27),
    /**
     * Channel map update indication
     */
    CHANNEL_MAP_UPDATE_INDICATION((byte) 0x28),
    /**
     * PB-­ADV: A provisioning bearer used to provision a device over the
     * Bluetooth advertising channels.
     */
    PB_ADV((byte) 0x29),
    /**
     * Mesh message
     */
    MESH_MESSAGE((byte) 0x2A),
    /**
     * Mesh beacon
     */
    MESH_BEACON((byte) 0x2B),
    /**
     * Big info
     */
    BIG_INFO((byte) 0x2C),
    /**
     * Broadcast code
     */
    BROADCAST_CODE((byte) 0x2D),
    /**
     * Resolvable set identifier
     */
    RESOLVABLE_SET_IDENTIFIER((byte) 0x2E),
    /**
     * Advertising interval ­long
     */
    ADVERTISING_INTERVAL_LONG((byte) 0x2F),
    /**
     * Broadcast name
     */
    BROADCAST_NAME((byte) 0x30),
    /**
     * 3D information data
     */
    INFORMATION_DATA_3D((byte) 0x3D),
    /**
     * Manufacturer specific data
     */
    MANUFACTURER_SPECIFIC_DATA((byte) 0xFF);
    /**
     * Private variable to store the common data type value.
     */
    private final byte value;
    /**
     * Private enumeration constructor
     *
     * @param adValue Common data type value
     */
    DataTypes(@NotNull final byte adValue) {
        this.value = adValue;
    }

    /**
     * Get the common data type value.
     *
     * @return Returns the common data type value.
     */
    public byte getValue() {
        return value;
    }
}
