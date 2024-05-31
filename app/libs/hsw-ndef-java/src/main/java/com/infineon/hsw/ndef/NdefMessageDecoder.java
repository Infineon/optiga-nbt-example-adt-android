// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.RecordDecoder;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Decodes the NDEF (NFC Data Exchange Format) message. Returns a decoder
 * function that decodes an NDEF records from a file-like, byte-oriented stream
 * or a bytes object given by the stream_or_bytes argument.
 */
public final class NdefMessageDecoder {
    /**
     * Error if message begin record in the NDEF message is not present.
     */
    private static final String ERR_MESSAGE_MB_BIT =
            "Missing message begin record in the NDEF message";

    /**
     * Error if message end record in the NDEF message is not present.
     */
    private static final String ERR_MESSAGE_ME_BIT =
            "Missing message end record in the NDEF message";

    /**
     * Private instance of an NDEF message decoder to decode the NDEF message.
     */
    private static NdefMessageDecoder messageDecoder;

    /**
     * Private instance of record decoder to decode the NDEF record.
     */
    private final RecordDecoder recordDecoder = RecordDecoder.getInstance();

    /**
     * Private constructor to restrict object creation.
     */
    private NdefMessageDecoder() {
    }

    /**
     * Returns the instance of NDEF message decoder.
     *
     * @return Returns the singleton instance of NDEF message decoder.
     */
    public static synchronized NdefMessageDecoder getInstance() {
        if (messageDecoder == null) {
            messageDecoder = new NdefMessageDecoder();
        }
        return messageDecoder;
    }

    /**
     * Decodes the NDEF message bytes with the specified offset and the NDEF
     * message length.
     *
     * @param ndefMessage NDEF message as bytes.
     * @param offset      Start offset from which the data to be decoded.
     * @param length      Length of data to be decoded.
     * @return Returns the decoded NDEF message object.
     * @throws NdefException Throws an NDEF exception if unable to decode the
     *         NDEF message bytes.
     */
    public IfxNdefMessage decode(@NotNull byte[] ndefMessage,
                                 @NotNull int offset, @NotNull int length)
            throws NdefException {
        ByteArrayInputStream messageStream =
                new ByteArrayInputStream(ndefMessage, offset, length);
        return decode(messageStream);
    }

    /**
     * Decodes the NDEF message bytes.
     *
     * @param ndefMessage NDEF message as bytes.
     * @return Returns the decoded NDEF message object.
     * @throws NdefException Throws an NDEF exception if unable to decode the
     *         NDEF message bytes.
     */
    public IfxNdefMessage decode(@NotNull byte[] ndefMessage)
            throws NdefException {
        return decode(ndefMessage, 0, ndefMessage.length);
    }

    /**
     * Decodes the stream of input data of NDEF message and returns the
     * decoded NDEF message.
     *
     * @param stream Stream of input NDEF message.
     * @return Returns the NDEF message.
     * @throws NdefException Throws an NDEF exception if unable to decode stream
     * 						 of input data of the NDEF message.
     */
    public IfxNdefMessage decode(@NotNull InputStream stream)
            throws NdefException {
        List<AbstractRecord> records = new ArrayList<>();
        try {
            while (stream.available() > 0) {
                int header = stream.read();
                AbstractRecord abstractRecord = recordDecoder.decode(header,
                                                                     stream);
                if (records.isEmpty() && (header & NdefConstants.MB) == 0) {
                    throw new NdefException(ERR_MESSAGE_MB_BIT);
                }

                if (stream.available() == 0 &&
                    (header & NdefConstants.ME) == 0) {
                    throw new NdefException(ERR_MESSAGE_ME_BIT);
                }

                records.add(abstractRecord);
            }
        } catch (IOException e) {
            throw new NdefException(e.getMessage(), e);
        }

        return new IfxNdefMessage(records.toArray(new AbstractRecord[0]));
    }

    /**
     * Decodes the NDEF message input stream and returns the list of NDEF
     * records.
     *
     * @param in Input stream to be decoded.
     * @return Returns the collection of NDEF records that is decoded.
     * @throws NdefException Throws an NDEF exception if unable to decode stream
     *         of input data of NDEF message.
     */
    public List<AbstractRecord> decodeToRecords(@NotNull InputStream in)
            throws NdefException {
        return decode(in).getNdefRecords();
    }
}
