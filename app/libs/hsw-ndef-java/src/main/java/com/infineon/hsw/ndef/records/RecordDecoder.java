// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records;

import com.infineon.hsw.ndef.NdefManager;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.decoder.IRecordDecoder;
import com.infineon.hsw.ndef.records.decoder.IRecordPayloadDecoder;
import com.infineon.hsw.ndef.records.model.RecordType;
import com.infineon.hsw.ndef.records.rtd.IfxNdefRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes the NDEF record.
 */
public class RecordDecoder implements IRecordDecoder {
    /*
     * Singleton instance of record decoder.
     */
    private static RecordDecoder instance;

    /**
     * Payload length field length.
     */
    private static final int PAYLOAD_LENGTH_FIELD_LENGTH = 4;

    /**
     * NDEF record payload decoder.
     */
    private IRecordPayloadDecoder recordPayloadDecoder;

    /**
     * Constructor of record decoder.
     */
    private RecordDecoder() { /* Default constructor to be left empty. */
    }

    /*
     * Creates a thread-safe singleton instance of record decoder.
     */
    public static synchronized RecordDecoder getInstance() {
        if (instance == null) {
            instance = new RecordDecoder();
        }
        return instance;
    }

    /**
     * Checks if the library supports decoding the specified record type.
     *
     * @param recordType NDEF record type.
     * @return Returns true, if record can be decoded by the library.
     */
    @Override
    public boolean canDecode(@NotNull byte[] recordType) {
        recordPayloadDecoder = NdefManager.getInstance().getPayloadDecoder(
                new RecordType(recordType));
        return recordPayloadDecoder != null;
    }

    /**
     * Decodes the NDEF record. If the record type is unsupported by the
     * library, it creates a simple NDEF record without parsing the payload.
     *
     * @param header Header of the NDEF record.
     * @param stream NDEF record.
     * @return Returns the decoded NDEF record.
     * @throws NdefException Throws an NDEF exception if unable to decode the
     *     record.
     */
    @Override
    public AbstractRecord decode(@NotNull int header,
                                 @NotNull InputStream stream)
            throws NdefException {
        try {
            // Decode the record header.
            byte tnf = (byte) (header & NdefConstants.TNF_MASK);
            int typeLength = stream.read();
            int payloadLength = getPayloadLength((header & NdefConstants.SR) !=
                                                         0,
                                                 stream);
            int idLength = getIdLength((header & NdefConstants.IL) != 0,
                                       stream);
            boolean chunked = (header & NdefConstants.CF) != 0;
            byte[] type = Utils.getBytesFromStream(typeLength, stream);
            byte[] id = Utils.getBytesFromStream(idLength, stream);
            byte[] payload = Utils.getBytesFromStream(payloadLength, stream);

            // Check if library can decode the record.
            if (canDecode(type)) {
                AbstractRecord abstractRecord = recordPayloadDecoder.decode(
                        payload);
                abstractRecord.setId(id);
                abstractRecord.setRecordType(new RecordType(type));
                abstractRecord.setIsChunked(chunked);
                abstractRecord.setPayload(payload);
                abstractRecord.setHashCode();
                return abstractRecord;
            } else {
                // Creates an NDEF record if library is not supporting for
                // decoding the record.
                return new IfxNdefRecord(tnf, chunked, type, id, payload);
            }
        } catch (IOException | UtilException e) {
            throw new NdefException(e.getMessage(), e);
        }
    }

    /**
     * Gets the length of the record ID filed.
     *
     * @param idLengthPresent True if ID is present in the record bytes.
     * @param inputStream     From the ID length need to be read.
     * @return Returns the length of the record ID filed.
     * @throws IOException Throws an IO Exception, if unable to read the bytes
     * 					   from the input stream.
     */
    private static int getIdLength(boolean idLengthPresent,
                                   InputStream inputStream) throws IOException {
        if (idLengthPresent) {
            return inputStream.read();
        }
        return 0;
    }

    /**
     * Gets the length of the record payload.
     *
     * @param shortRecord True if it is short record.
     * @param inputStream From the record payload, length need to be read.
     * @return Returns the record payload length.
     * @throws IOException Throws an IO Exception, if unable to read the bytes
     * 					   from the input stream.
     */
    private static int getPayloadLength(boolean shortRecord,
                                        InputStream inputStream)
            throws IOException {
        if (shortRecord) {
            return inputStream.read();
        }
        byte[] buffer;
        try {
            buffer = Utils.getBytesFromStream(PAYLOAD_LENGTH_FIELD_LENGTH,
                                              inputStream);
        } catch (UtilException e) {
            throw new IOException(e.getMessage(), e);
        }
        return (((buffer[0] & 0xFF) << 24) | ((buffer[1] & 0xFF) << 16) |
                ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF));
    }
}
