// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.model.DataTypes;
import com.infineon.hsw.ndef.records.model.EirData;
import com.infineon.hsw.ndef.records.rtd.BluetoothRecord;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Decodes the payload byte array of the Bluetooth record type.
 */
public class BluetoothRecordPayloadDecoder implements IRecordPayloadDecoder {
    /**
     * Error message if input stream of the Bluetooth record payload bytes are
     * not valid.
     */
    protected static final String ERR_MESSAGE_PAYLOAD =
            "Input stream of Bluetooth record payload bytes are not valid";

    /**
     * Defines the minimum length of the Bluetooth record payload data.
     */
    private static final int MIN_LENGTH = 8;

    /**
     * Defines the length of the Bluetooth address.
     */
    private static final int BLUETOOTH_DEVICE_ADDRESS_LENGTH = 6;

    /**
     * Defines the length of the OOB data size filed.
     */
    private static final int OOB_DATA_LENGTH_FIELD_SIZE = 2;

    /**
     * BluetoothRecordPayloadDecoder constructor.
     */
    public BluetoothRecordPayloadDecoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Decodes the Bluetooth record payload byte array into the Bluetooth
     * record data structure.
     *
     * @param payload Input stream of the Bluetooth record payload bytes
     * @return Abstract record data structure of the Bluetooth record
     */
    @Override
    public AbstractRecord decode(@NotNull final byte[] payload)
            throws NdefException {
        try {
            validate(payload, MIN_LENGTH);
            ByteArrayInputStream payloadInputStream = new ByteArrayInputStream(
                    payload);
            if (payloadInputStream.skip(OOB_DATA_LENGTH_FIELD_SIZE) !=
                OOB_DATA_LENGTH_FIELD_SIZE) {
                throw new NdefException(ERR_MESSAGE_PAYLOAD);
            }
            byte[] deviceAddress = readDeviceAddress(payloadInputStream);
            return parseOOBOptionalData(payloadInputStream, deviceAddress);
        } catch (RuntimeException e) {
            throw new NdefException(ERR_MESSAGE_INVALID_PAYLOAD, e);
        }
    }

    /**
     * Decodes secure simple pairing OOB optional data.
     *
     * @param payloadInputStream Input stream of the Bluetooth record payload
     *     bytes
     * @param deviceAddress      Device address bytes
     * @return Abstract record data structure of the Bluetooth record
     * @throws NdefException In case of errors in reading the input stream
     */
    private static BluetoothRecord parseOOBOptionalData(
            @NotNull final ByteArrayInputStream payloadInputStream,
            @NotNull final byte[] deviceAddress) throws NdefException {
        BluetoothRecord bluetoothRecord;
        bluetoothRecord = new BluetoothRecord(deviceAddress);
        while (payloadInputStream.available() > 0) {
            int length = payloadInputStream.read();
            EirData optionalOOBData;
            try {
                optionalOOBData = new EirData(
                        Utils.getBytesFromStream(length, payloadInputStream));
            } catch (UtilException e) {
                throw new NdefException(e.getMessage(), e);
            }
            final byte type = optionalOOBData.getType();
            if (type == DataTypes.COMPLETE_LOCAL_NAME.getValue()) {
                bluetoothRecord.setName(DataTypes.COMPLETE_LOCAL_NAME,
                                        new String(optionalOOBData.getData(),
                                                   StandardCharsets.UTF_8));
            } else if (type == DataTypes.SHORTENED_LOCAL_NAME.getValue()) {
                bluetoothRecord.setName(DataTypes.SHORTENED_LOCAL_NAME,
                                        new String(optionalOOBData.getData(),
                                                   StandardCharsets.UTF_8));
            } else if (type == DataTypes.SIMPLE_PAIRING_HASH_C_192.getValue()) {
                bluetoothRecord.setSimplePairingHash(
                        DataTypes.SIMPLE_PAIRING_HASH_C_192,
                        optionalOOBData.getData());
            } else if (type == DataTypes.SIMPLE_PAIRING_HASH_C_256.getValue()) {
                bluetoothRecord.setSimplePairingHash(
                        DataTypes.SIMPLE_PAIRING_HASH_C_256,
                        optionalOOBData.getData());
            } else if (type ==
                       DataTypes.SIMPLE_PAIRING_RANDOMIZER_R_192.getValue()) {
                bluetoothRecord.setSimplePairingRandomizer(
                        DataTypes.SIMPLE_PAIRING_RANDOMIZER_R_192,
                        optionalOOBData.getData());
            } else if (type ==
                       DataTypes.SIMPLE_PAIRING_RANDOMIZER_R_256.getValue()) {
                bluetoothRecord.setSimplePairingRandomizer(
                        DataTypes.SIMPLE_PAIRING_RANDOMIZER_R_256,
                        optionalOOBData.getData());
            } else if (type == DataTypes.DEVICE_CLASS.getValue()) {
                bluetoothRecord.setDeviceClass(optionalOOBData.getData());
            } else if (type == DataTypes.INCOMPLETE_SERVICE_CLASS_UUID_16_BIT
                                       .getValue()) {
                bluetoothRecord.setServiceClassUUIDs(
                        DataTypes.INCOMPLETE_SERVICE_CLASS_UUID_16_BIT,
                        optionalOOBData.getData());
            } else if (type == DataTypes.COMPLETE_SERVICE_CLASS_UUID_16_BIT
                                       .getValue()) {
                bluetoothRecord.setServiceClassUUIDs(
                        DataTypes.COMPLETE_SERVICE_CLASS_UUID_16_BIT,
                        optionalOOBData.getData());
            } else if (type == DataTypes.INCOMPLETE_SERVICE_CLASS_UUID_32_BIT
                                       .getValue()) {
                bluetoothRecord.setServiceClassUUIDs(
                        DataTypes.INCOMPLETE_SERVICE_CLASS_UUID_32_BIT,
                        optionalOOBData.getData());
            } else if (type == DataTypes.COMPLETE_SERVICE_CLASS_UUID_32_BIT
                                       .getValue()) {
                bluetoothRecord.setServiceClassUUIDs(
                        DataTypes.COMPLETE_SERVICE_CLASS_UUID_32_BIT,
                        optionalOOBData.getData());
            } else if (type == DataTypes.INCOMPLETE_SERVICE_CLASS_UUID_128_BIT
                                       .getValue()) {
                bluetoothRecord.setServiceClassUUIDs(
                        DataTypes.INCOMPLETE_SERVICE_CLASS_UUID_128_BIT,
                        optionalOOBData.getData());
            } else if (type == DataTypes.COMPLETE_SERVICE_CLASS_UUID_128_BIT
                                       .getValue()) {
                bluetoothRecord.setServiceClassUUIDs(
                        DataTypes.COMPLETE_SERVICE_CLASS_UUID_128_BIT,
                        optionalOOBData.getData());
            } else {
                bluetoothRecord.addOtherEIResponseList(optionalOOBData);
            }
        }
        return bluetoothRecord;
    }

    /**
     * Decodes the device address.
     *
     * @param payloadInputStream Input stream of the Bluetooth record payload
     *     bytes
     * @return Device address bytes
     * @throws NdefException In case of errors in reading the input stream
     */
    private static byte[] readDeviceAddress(
            @NotNull final ByteArrayInputStream payloadInputStream)
            throws NdefException {
        try {
            return Utils.getBytesFromStream(BLUETOOTH_DEVICE_ADDRESS_LENGTH,
                                            payloadInputStream);
        } catch (UtilException e) {
            throw new NdefException(e.getMessage(), e);
        }
    }
}
