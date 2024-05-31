// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.NdefMessageDecoder;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.rtd.AlternativeCarrierRecord;
import com.infineon.hsw.ndef.records.rtd.ErrorRecord;
import com.infineon.hsw.ndef.records.rtd.HandoverSelectRecord;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Decodes the payload byte array of handover select record type.
 */
public class HandoverSelectRecordPayloadDecoder
        implements IRecordPayloadDecoder {
    /**
     * Defines the minimum length of the handover select record data bytes.
     */
    private static final int MIN_LENGTH = 2;

    /**
     * Defines the mask for major version.
     */
    private static final int MAJOR_VERSION_MASK = 0xF0;

    /**
     * Defines the mask for minor version.
     */
    private static final int MINOR_VERSION_MASK = 0x0F;

    /**
     * Error message if unable to decode the payload.
     */
    public static final String ERR_MESSAGE_EMPTY_RECORDS =
            "Record needs at least 1 record. Both alternate-carrier and error is not present";

    /**
     * HandoverSelectRecordPayloadDecoder constructor.
     */
    public HandoverSelectRecordPayloadDecoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Decodes the handover select record payload byte array into
     * the handover select record structure.
     *
     * @param payload Handover select record payload byte array
     * @return Abstract record data structure
     * @throws NdefException Invalid payload
     */
    @Override
    public AbstractRecord decode(@NotNull byte[] payload) throws NdefException {
        validate(payload, MIN_LENGTH);
        try {
            ByteArrayInputStream payloadSInputStream = new ByteArrayInputStream(
                    payload);
            int version = payloadSInputStream.read();
            HandoverSelectRecord handoverSelectRecord =
                    new HandoverSelectRecord();

            handoverSelectRecord.setMinorVersion(
                    (byte) (version & MINOR_VERSION_MASK));
            handoverSelectRecord.setMajorVersion(
                    (byte) ((version & MAJOR_VERSION_MASK) >> 4));
            List<AbstractRecord> records = NdefMessageDecoder.getInstance()
                                                   .decode(payloadSInputStream)
                                                   .getNdefRecords();
            for (AbstractRecord abstractRecord : records) {
                if (abstractRecord instanceof AlternativeCarrierRecord) {
                    handoverSelectRecord.addAlternativeCarrierRecord(
                            (AlternativeCarrierRecord) abstractRecord);
                } else if (abstractRecord instanceof ErrorRecord) {
                    handoverSelectRecord.setErrorRecord(
                            (ErrorRecord) abstractRecord);
                }
                // Ignoring other record types as per the specification.
            }
            if (handoverSelectRecord.getAlternativeCarrierRecords().isEmpty() &&
                handoverSelectRecord.getErrorRecord() != null) {
                throw new NdefException(ERR_MESSAGE_EMPTY_RECORDS);
            }
            return handoverSelectRecord;
        } catch (RuntimeException e) {
            throw new NdefException(ERR_MESSAGE_INVALID_PAYLOAD, e);
        }
    }
}
