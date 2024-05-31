// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.RecordEncoder;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Encodes the NDEF (NFC Data Exchange Format) message. Returns a encoder
 * function that encodes NDEF record objects into an NDEF message octet
 * sequence.
 */
public final class NdefMessageEncoder {
    /**
     * Error if NdefMessage or records in message is not present
     */
    private static final String
            ERR_MESSAGE_NULL = "Messages that are to be encoded is null";

    /**
     * Private instance of NDEF message encoder to encode the NDEF message
     */
    private static NdefMessageEncoder encoder = new NdefMessageEncoder();

    /**
     * Private constructor to restrict object creation.
     */
    private NdefMessageEncoder() {
    }

    /**
     * Returns the instance of NDEF message encoder.
     *
     * @return Returns the singleton instance of NDEF message encoder.
     */
    public static synchronized NdefMessageEncoder getInstance() {
        if (encoder == null) {
            encoder = new NdefMessageEncoder();
        }
        return encoder;
    }

    /**
     * Encodes the NDEF message into bytes.
     *
     * @param ndefMessage NDEF message to be encoded.
     * @return Encoded NDEF message as bytes.
     * @throws NdefException Throws an NDEF exception if unable to encode the
     *         NDEF message bytes.
     */
    public byte[] encode(@NotNull IfxNdefMessage ndefMessage)
            throws NdefException {
        if (ndefMessage == null) {
            throw new NdefException(ERR_MESSAGE_NULL);
        }
        return encode(
                ndefMessage.getNdefRecords().toArray(new AbstractRecord[0]));
    }

    /**
     * Encodes the collection of NDEF records into bytes.
     *
     * @param ndefRecords Collection of NDEF records to be encoded.
     * @return Encoded NDEF message as bytes.
     * @throws NdefException Throws an NDEF exception if unable to encode the
     *         NDEF message bytes.
     */
    public byte[] encode(AbstractRecord... ndefRecords) throws NdefException {
        if (ndefRecords == null || ndefRecords.length == 0) {
            return new byte[] { (byte) 0xD0, 0x00, 0x00 };
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte header = (byte) NdefConstants.MB;
        for (int i = 0; i < ndefRecords.length; i++) {
            AbstractRecord tempRecord = ndefRecords[i];
            if (i == ndefRecords.length - 1) {
                header |= NdefConstants.ME;
            }
            byte[] encodedRecord = RecordEncoder.getInstance().encode(
                    tempRecord);
            encodedRecord[0] |= header;
            header = 0;
            try {
                stream.write(encodedRecord);
            } catch (IOException e) {
                throw new NdefException(e.getMessage(), e);
            }
        }
        return stream.toByteArray();
    }

    /**
     * Encodes the list of abstract records into bytes.
     *
     * @param records Collection of abstract records.
     * @param outputStream    Byte array output stream to write the NDEF
     *         message.
     * @throws NdefException Throws an NDEF exception if unable to encode the
     *         NDEF message bytes.
     */
    public void encode(List<AbstractRecord> records,
                       @NotNull ByteArrayOutputStream outputStream)
            throws NdefException {
        byte[] bytes = encode(records.toArray(new AbstractRecord[0]));
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new NdefException(e.getMessage(), e);
        }
    }
}
