// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.model.AdData;
import com.infineon.hsw.ndef.records.model.DataTypes;
import com.infineon.hsw.ndef.records.rtd.BluetoothLeRecord;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Decodes the payload byte array of Bluetooth low energy (BLE) record type.
 */
public class BluetoothLeRecordPayloadDecoder implements IRecordPayloadDecoder {
    /**
     * Defines the minimum length of BLE record payload data.
     */
    private static final int MIN_LENGTH = 10;

    /**
     * BluetoothLeRecordPayloadDecoder constructor.
     */
    public BluetoothLeRecordPayloadDecoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Decodes the BLE record payload byte array into the BLE record data
     * structure.
     *
     * @param payload Input stream of the BLE record payload bytes
     * @return Abstract record data structure of the BLE record
     */
    @Override
    public AbstractRecord decode(@NotNull final byte[] payload)
            throws NdefException {
        try {
            validate(payload, MIN_LENGTH);
            ByteArrayInputStream payloadInputStream = new ByteArrayInputStream(
                    payload);
            return parseOOBData(payloadInputStream);
        } catch (RuntimeException e) {
            throw new NdefException(ERR_MESSAGE_INVALID_PAYLOAD, e);
        }
    }

    /**
     * Decodes security manager out-of-band (OOB) pairing data.
     *
     * @param payloadInputStream Input stream of the BLE record payload bytes
     * @return Abstract record data structure of the BLE record
     * @throws NdefException In case of errors in reading the input stream
     */
    private static BluetoothLeRecord parseOOBData(
            @NotNull final ByteArrayInputStream payloadInputStream)
            throws NdefException {
        BluetoothLeRecord bleRecord = new BluetoothLeRecord();
        while (payloadInputStream.available() > 0) {
            int length = payloadInputStream.read();
            AdData advertisingResponse;
            try {
                advertisingResponse = new AdData(
                        Utils.getBytesFromStream(length, payloadInputStream));
            } catch (UtilException e) {
                throw new NdefException(e.getMessage(), e);
            }
            byte type = advertisingResponse.getType();
            if (type == DataTypes.COMPLETE_LOCAL_NAME.getValue()) {
                bleRecord.setName(DataTypes.COMPLETE_LOCAL_NAME,
                                  new String(advertisingResponse.getData(),
                                             StandardCharsets.UTF_8));
            } else if (type == DataTypes.SHORTENED_LOCAL_NAME.getValue()) {
                bleRecord.setName(DataTypes.SHORTENED_LOCAL_NAME,
                                  new String(advertisingResponse.getData(),
                                             StandardCharsets.UTF_8));
            } else if (type == DataTypes.FLAGS.getValue()) {
                bleRecord.setFlags(advertisingResponse.getData());
            } else if (type == DataTypes.APPEARANCE.getValue()) {
                bleRecord.setAppearance(advertisingResponse.getDataShort());
            } else if (type == DataTypes.LE_BLUETOOTH_ROLE.getValue()) {
                bleRecord.setLeRole(advertisingResponse.getDataByte());
            } else if (type ==
                       DataTypes.LE_BLUETOOTH_DEVICE_ADDRESS.getValue()) {
                bleRecord.setAddress(advertisingResponse.getData());
            } else if (type == DataTypes.SECURITY_MANAGER_TK_VALUE.getValue()) {
                bleRecord.setSecurityManagerTKValue(
                        advertisingResponse.getData());
            } else {
                bleRecord.addOptionalAD(advertisingResponse);
            }
        }
        return bleRecord;
    }
}
