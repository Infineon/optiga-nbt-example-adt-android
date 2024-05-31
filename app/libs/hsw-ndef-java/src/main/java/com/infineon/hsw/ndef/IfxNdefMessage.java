// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.rtd.IfxNdefRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * NDEF messages are the basic "transportation" mechanism for NDEF records, with
 * each message containing one or more NDEF records. This class represents an
 * NDEF records which is a collection of NDEF records and also provides the
 * methods to encode and decode the NDEF message. An NDEF message is a container
 * for one or more NDEF records.
 */
public final class IfxNdefMessage {
    /**
     * Collection of the NDEF records.
     */
    private List<AbstractRecord> ndefRecords = new ArrayList<>();

    /**
     * Private instance of NDEF message decoder to decode the NDEF message.
     */
    private final NdefMessageEncoder encoder = NdefMessageEncoder.getInstance();

    /**
     * Creates a new NDEF message with the list of NDEF records.
     *
     * @param ndefRecords List of NDEF records.
     */
    public IfxNdefMessage(AbstractRecord... ndefRecords) {
        this.ndefRecords.addAll(Arrays.asList(ndefRecords));
    }

    /**
     * Creates a new NDEF message with the list of NDEF records.
     *
     * @param ndefRecords List of NDEF records.
     */
    public IfxNdefMessage(List<AbstractRecord> ndefRecords) {
        this.ndefRecords.addAll(ndefRecords);
    }

    /**
     * Creates a new NDEF message with {@link IfxNdefRecord}(s) using the raw
     * byte array data.
     *
     * @param ndefMessage Gets the NDEF message bytes.
     * @throws NdefException Throws an NDEF exception if unable to encode the
     *         NDEF
     * 						 message bytes.
     */
    public IfxNdefMessage(@NotNull byte[] ndefMessage) throws NdefException {
        //  NDEF message decoder
        NdefMessageDecoder decoder = NdefMessageDecoder.getInstance();
        ndefRecords = decoder.decode(ndefMessage).getNdefRecords();
    }

    /**
     * Adds the NDEF records to the NDEF message.
     *
     * @param ndefRecords The collection of NDEF records.
     */
    public void addNdefRecords(AbstractRecord... ndefRecords) {
        this.ndefRecords.addAll(Arrays.asList(ndefRecords));
    }

    /**
     * Adds the NDEF record to the NDEF message.
     *
     * @param ndefRecord The collection of NDEF records.
     */
    public void addNdefRecord(@NotNull AbstractRecord ndefRecord) {
        this.ndefRecords.add(ndefRecord);
    }

    /**
     * Returns the collection of NDEF records available in the NDEF
     * Message.
     *
     * @return Returns the collection of NDEF records list.
     */
    public List<AbstractRecord> getNdefRecords() {
        return Collections.unmodifiableList(ndefRecords);
    }

    /**
     * Removes the NDEF record available in the NDEF message at
     * specified positions.
     *
     * @param index Position at which record has to be removed.
     * @return Returns the abstract record last removed NDEF record.
     */
    public AbstractRecord removeNdefRecordAt(@NotNull int index) {
        return ndefRecords.remove(index);
    }

    /**
     * Encodes the NDEF records and returns the raw byte array data.
     *
     * @return Returns the NDEF record byte array data.
     * @throws NdefException Throws an NDEF exception if unable to encode NDEF
     * 						 message bytes.
     */
    public byte[] toByteArray() throws NdefException {
        return encoder.encode(ndefRecords.toArray(new AbstractRecord[0]));
    }

    // cSpell:ignore NLEN
    /**
     * Encodes the NDEF records and returns the raw byte array data
     * with NDEF message length prepended with the NDEF message.
     * @param includeLength Indicates whether the NLEN field should be prefixed
     *         in the NDEF message.
     * @return Returns the NDEF Record byte array data.
     * @throws NdefException Throws an NDEF exception if unable to encode NDEF
     * 						 message bytes.
     * @throws IOException Throws an IO exception in case of issues in writing
     * 					   to the byte stream.
     */
    public byte[] toByteArray(@NotNull boolean includeLength)
            throws NdefException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] ndefMessage = toByteArray();
        if (includeLength) {
            byte[] ndefMessageLength =
                    Utils.toBytes(ndefMessage.length,
                                  NdefConstants.NDEF_MESSAGE_LENGTH_LIMIT);
            try {
                outputStream.write(ndefMessageLength);
            } catch (IOException e) {
                throw new IOException(e);
            }
        }
        try {
            outputStream.write(ndefMessage);
        } catch (IOException e) {
            throw new IOException(e);
        }

        return outputStream.toByteArray();
    }
}
