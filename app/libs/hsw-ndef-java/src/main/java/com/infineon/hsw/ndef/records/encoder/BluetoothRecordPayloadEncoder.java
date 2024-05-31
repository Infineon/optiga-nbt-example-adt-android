// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.model.EirData;
import com.infineon.hsw.ndef.records.rtd.BluetoothRecord;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Encodes the Bluetooth record type to payload byte array.
 */
public class BluetoothRecordPayloadEncoder implements IRecordPayloadEncoder {
    /**
     * Defines length of the Bluetooth address.
     */
    private static final int BLUETOOTH_DEVICE_ADDRESS_LENGTH = 6;

    /**
     * Defines size of the OOB data length size.
     */
    private static final int OOB_LENGTH_FIELD_SIZE = 2;

    /**
     * Error message if input stream of the Bluetooth device address is not 6
     * bytes.
     */
    protected static final String ERR_MESSAGE_DEVICE_ADDRESS =
            "Bluetooth device address must be 6 bytes";

    /**
     * BluetoothRecordPayloadEncoder constructor.
     */
    public BluetoothRecordPayloadEncoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Encodes the Bluetooth record data structure into the record payload
     * byte array.
     *
     * @param mimeRecord MimeRecord BluetoothRecord
     * @return Bluetooth record payload byte array
     * @throws NdefException If unable to encode the record payload
     */
    @Override
    public byte[] encode(@NotNull final AbstractRecord mimeRecord)
            throws NdefException {
        if (!(mimeRecord instanceof BluetoothRecord)) {
            throw new NdefException(ERR_UNSUPPORTED_TYPE);
        }

        final BluetoothRecord bluetoothRecord = (BluetoothRecord) mimeRecord;
        validateBluetoothPayload(bluetoothRecord);

        byte[] payload = bluetoothRecord.getAddress();
        if (bluetoothRecord.getName() != null) {
            payload = Utils.concat(payload,
                                   bluetoothRecord.getName().toBytes());
        }
        if (bluetoothRecord.getDeviceClass() != null) {
            payload = Utils.concat(payload,
                                   bluetoothRecord.getDeviceClass().toBytes());
        }
        if (bluetoothRecord.getServiceClassUUIDs() != null) {
            payload = Utils.concat(payload,
                                   bluetoothRecord.getServiceClassUUIDs()
                                           .toBytes());
        }
        if (bluetoothRecord.getSimplePairingHash() != null) {
            payload = Utils.concat(payload,
                                   bluetoothRecord.getSimplePairingHash()
                                           .toBytes());
        }
        if (bluetoothRecord.getSimplePairingRandomizer() != null) {
            payload = Utils.concat(payload,
                                   bluetoothRecord.getSimplePairingRandomizer()
                                           .toBytes());
        }

        for (EirData data : bluetoothRecord.getOtherEIRList()) {
            if (data != null) {
                payload = Utils.concat(payload, data.toBytes());
            }
        }
        byte[] b = new byte[OOB_LENGTH_FIELD_SIZE];
        Utils.setIntLittleEndian(payload.length + OOB_LENGTH_FIELD_SIZE, b, 0,
                                 OOB_LENGTH_FIELD_SIZE);
        payload = Utils.concat(b, payload);

        return payload;
    }

    /**
     * Validate the input BluetoothRecord.
     *
     * @param bluetoothRecord Bluetooth record to be validated
     * @throws NdefException If unable to decode the record payload
     */
    private static void validateBluetoothPayload(
            @NotNull final BluetoothRecord bluetoothRecord)
            throws NdefException {
        if (bluetoothRecord.getAddress() == null ||
            bluetoothRecord.getAddress().length !=
                    BLUETOOTH_DEVICE_ADDRESS_LENGTH) {
            throw new NdefException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
    }
}
