// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records;

import com.infineon.hsw.ndef.NdefManager;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.encoder.IRecordEncoder;
import com.infineon.hsw.ndef.records.encoder.IRecordPayloadEncoder;
import com.infineon.hsw.ndef.records.model.RecordType;
import com.infineon.hsw.ndef.records.rtd.IfxNdefRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayOutputStream;

/**
 * Encodes the records into NDEF records.
 */
public class RecordEncoder implements IRecordEncoder {
    /*
     * Singleton instance of record encoder.
     */
    private static RecordEncoder instance;

    /**
     * Maximum length for the short record is 255.
     */
    private static final int MAX_LENGTH_FOR_SHORT_RECORD = 255;

    private static final String
            ERR_INVALID_ID_LENGTH = "Expected record id length <= 255 bytes";

    private static final String
            ERR_UNSUPPORTED_RECORD_TYPE = "Unsupported record type";
    /**
     * NDEF record payload encoder.
     */
    private IRecordPayloadEncoder payloadEncoder;

    /**
     * Constructor for record encoder.
     */
    private RecordEncoder() { /* Default constructor to be left empty. */
    }

    /*
     * Creates a thread-safe singleton instance of record encoder.
     */
    public static synchronized RecordEncoder getInstance() {
        if (instance == null) {
            instance = new RecordEncoder();
        }
        return instance;
    }

    /**
     * Checks if the library supports encoding the specified record type.
     *
     * @param recordType NDEF record type.
     * @return Returns true, if record can be encoded by the library.
     */
    @Override
    public boolean canEncode(@NotNull RecordType recordType) {
        payloadEncoder = NdefManager.getInstance().getPayloadEncoder(
                recordType);
        return payloadEncoder != null;
    }

    /**
     * Encodes the NDEF record.
     *
     * @param abstractRecord NDEF record
     * @return Returns the encoded byte array of the NDEF record.
     * @throws NdefException Throws an NDEF exception if unable to encode the
     * 						 record.
     */
    @Override
    public byte[] encode(@NotNull AbstractRecord abstractRecord)
            throws NdefException {
        byte[] key = abstractRecord.getId();
        if (key != null && key.length > 255) {
            throw new NdefException(ERR_INVALID_ID_LENGTH);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] payload;
        // Check for the decoded payload.
        // check the available existing hashCode with new generated hash code
        // to confirm whether the fields are modified.
        // If fields are modified, generate the payload from the fields else
        // use the payload as it is.
        //
        if (abstractRecord.hashCode() == abstractRecord.getHashCode() ||
            abstractRecord instanceof IfxNdefRecord) {
            payload = abstractRecord.getPayload();
        } else {
            if (!canEncode(abstractRecord.getRecordType())) {
                throw new NdefException(ERR_UNSUPPORTED_RECORD_TYPE);
            }
            payload = payloadEncoder.encode(abstractRecord);
        }
        assembleRecordBytes(stream, abstractRecord, payload);
        return stream.toByteArray();
    }

    /**
     * Assembles the record bytes in the structure frame.
     *
     * @param outputStream Output stream in which the assembled byte is stored.
     * @param ndefRecord   NDEF record.
     * @param payload      Encoded payload byte array of the record.
     */
    private static void assembleRecordBytes(ByteArrayOutputStream outputStream,
                                            AbstractRecord ndefRecord,
                                            byte[] payload) {
        appendHeader(outputStream, ndefRecord, payload);
        outputStream.write(ndefRecord.getType().length);
        appendPayloadLength(outputStream, payload.length);

        if (ndefRecord.getId() != null) {
            appendIdLength(outputStream, ndefRecord.getId().length);
        }
        appendBytes(outputStream, ndefRecord.getType());

        if (ndefRecord.getId() != null) {
            appendBytes(outputStream, ndefRecord.getId());
        }
        appendBytes(outputStream, payload);
    }

    /**
     * Appends the header of the record.
     *
     * @param outputStream Output stream in which the assembled byte is stored.
     * @param ndefRecord   NDEF record.
     * @param payload      Encoded payload byte array of the record.
     */
    private static void appendHeader(ByteArrayOutputStream outputStream,
                                     AbstractRecord ndefRecord,
                                     byte[] payload) {
        byte header = 0;
        header = setShortRecord(header, payload);
        header = setIdLength(header, ndefRecord);
        header = setTypeNameFormat(header, ndefRecord);
        outputStream.write(header);
    }

    /**
     * Appends the input bytes to the output stream.
     *
     * @param outputStream Output stream in which the appended byte is stored.
     * @param bytes        To be appended.
     */
    private static void appendBytes(ByteArrayOutputStream outputStream,
                                    byte[] bytes) {
        outputStream.write(bytes, 0, bytes.length);
    }

    /**
     * Sets short record flag, if it is short.
     *
     * @param header  Header of the record.
     * @param payload Encoded payload byte array of the record.
     * @return Returns the updated header of the record.
     */
    private static byte setShortRecord(byte header, @NotNull byte[] payload) {
        if (payload.length <= MAX_LENGTH_FOR_SHORT_RECORD) {
            header |= NdefConstants.SR;
        }
        return header;
    }

    /**
     * Sets record ID flag, if record has record ID.
     *
     * @param header     Header of the record.
     * @param ndefRecord NDEF record.
     * @return Returns the updated header of the record.
     */
    private static byte setIdLength(byte header, AbstractRecord ndefRecord) {
        if (ndefRecord.getId() != null && ndefRecord.getId().length > 0) {
            header |= NdefConstants.IL;
        }
        return header;
    }

    /**
     * Sets the record TNF bits.
     *
     * @param header     Header of the record.
     * @param ndefRecord NDEF record.
     * @return Returns the updated header of the record.
     */
    private static byte setTypeNameFormat(byte header,
                                          AbstractRecord ndefRecord) {
        header |= ndefRecord.getTnf();
        return header;
    }
    /**
     * Appends the record payload length to the output stream.
     *
     * @param outputStream Output stream in which the appended byte is stored.
     * @param length       Record payload length.
     */
    private static void appendPayloadLength(ByteArrayOutputStream outputStream,
                                            int length) {
        if (length <= MAX_LENGTH_FOR_SHORT_RECORD) {
            outputStream.write(length);
        } else {
            byte[] payloadLengthArray = new byte[4];
            payloadLengthArray[0] = (byte) (length >>> 24);
            payloadLengthArray[1] = (byte) (length >>> 16);
            payloadLengthArray[2] = (byte) (length >>> 8);
            payloadLengthArray[3] = (byte) (length & 0xff);
            outputStream.write(payloadLengthArray, 0,
                               payloadLengthArray.length);
        }
    }

    /**
     * Appends the record ID length to the output stream.
     *
     * @param outputStream Output stream to append the record ID.
     * @param length       Record ID length.
     */
    private static void appendIdLength(ByteArrayOutputStream outputStream,
                                       int length) {
        if (length > 0) {
            outputStream.write(length);
        }
    }
}
