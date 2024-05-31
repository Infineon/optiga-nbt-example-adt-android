// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.model.AdData;
import com.infineon.hsw.ndef.records.rtd.BluetoothLeRecord;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Encodes the BLE record type to payload byte array.
 */
public class BluetoothLeRecordPayloadEncoder implements IRecordPayloadEncoder {
    /**
     * Defines length of the BLE address.
     */
    private static final int DEVICE_ADDRESS_LENGTH = 7;

    /**
     * Error message if input stream of Bluetooth LE device address is not 7
     * bytes.
     */
    protected static final String ERR_MESSAGE_DEVICE_ADDRESS =
            "Bluetooth device address must be 7 bytes";

    /**
     * Error message if input stream of Bluetooth LE device role is null or
     * undefined.
     */
    protected static final String ERR_MESSAGE_DEVICE_ROLE =
            "Bluetooth device role must not be null or undefined";

    /**
     * BluetoothLeRecordPayloadEncoder constructor.
     */
    public BluetoothLeRecordPayloadEncoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Encodes the BLE record data structure into the record payload byte
     * array.
     *
     * @param mimeRecord MimeRecord BluetoothLeRecord
     * @return BLE record payload byte array
     * @throws NdefException If unable to decode the record payload
     */
    @Override
    public byte[] encode(@NotNull final AbstractRecord mimeRecord)
            throws NdefException {
        if (!(mimeRecord instanceof BluetoothLeRecord)) {
            throw new NdefException(ERR_UNSUPPORTED_TYPE);
        }

        final BluetoothLeRecord bleRecord = (BluetoothLeRecord) mimeRecord;

        validateBLEPayload(bleRecord);

        byte[] payload = new byte[0];
        if (bleRecord.getAddress() != null) {
            payload = Utils.concat(payload, bleRecord.getAddress().toBytes());
        }
        if (bleRecord.getRole() != null) {
            payload = Utils.concat(payload, bleRecord.getRole().toBytes());
        }
        if (bleRecord.getSecurityManagerTKValue() != null) {
            payload = Utils.concat(payload,
                                   bleRecord.getSecurityManagerTKValue()
                                           .toBytes());
        }
        if (bleRecord.getAppearance() != null) {
            payload = Utils.concat(payload,
                                   bleRecord.getAppearance().toBytes());
        }
        if (bleRecord.getFlags() != null) {
            payload = Utils.concat(payload, bleRecord.getFlags().toBytes());
        }
        if (bleRecord.getName() != null) {
            payload = Utils.concat(payload, bleRecord.getName().toBytes());
        }
        for (AdData otherEIR : bleRecord.getOptionalADList()) {
            payload = Utils.concat(payload, otherEIR.toBytes());
        }
        return payload;
    }

    /**
     * Validates the input Bluetooth LE record.
     *
     * @param bleRecord Mime BluetoothLeRecord
     * @throws NdefException If unable to encode the record payload
     */
    private static void validateBLEPayload(
            @NotNull final BluetoothLeRecord bleRecord) throws NdefException {
        if (bleRecord.getAddress() == null ||
            bleRecord.getAddress().getData() == null ||
            bleRecord.getAddress().getData().length != DEVICE_ADDRESS_LENGTH) {
            throw new NdefException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
        if (bleRecord.getRole() == null ||
            bleRecord.getRole().getData() == null) {
            throw new NdefException(ERR_MESSAGE_DEVICE_ROLE);
        }
    }
}
