// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.model.DataReference;
import com.infineon.hsw.ndef.records.rtd.AlternativeCarrierRecord;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;

/**
 * Decodes the payload byte[] array of alternative carrier record type
 */
public class AlternativeCarrierRecordPayloadDecoder
        implements IRecordPayloadDecoder {
    /**
     * Defines the minimum length of alternative carrier record payload data.
     */
    private static final int MIN_LENGTH = 1;

    /**
     * Alternative carrier record payload decoder constructor.
     */
    public AlternativeCarrierRecordPayloadDecoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Decodes the alternative carrier record payload byte array into the
     * record data structure.
     *
     * @param payload Alternative carrier record payload byte array
     * @return Abstract record data structure
     * @throws NdefException In case of errors
     */
    @Override
    public AbstractRecord decode(@NotNull byte[] payload) throws NdefException {
        try {
            validate(payload, MIN_LENGTH);
            ByteArrayInputStream payloadInputStream = new ByteArrayInputStream(
                    payload);
            byte cps = (byte) payloadInputStream.read();
            DataReference carrierDataReferences = readCarrierDataReference(
                    payloadInputStream);
            AlternativeCarrierRecord alternativeCarrierRecord =
                    new AlternativeCarrierRecord(cps, carrierDataReferences);
            readAuxiliaryDataReference(payloadInputStream,
                                       alternativeCarrierRecord);
            return alternativeCarrierRecord;
        } catch (RuntimeException e) {
            throw new NdefException(ERR_MESSAGE_INVALID_PAYLOAD, e);
        }
    }

    /**
     * Decodes the auxiliary data references.
     *
     * @param payloadInputStream Input stream to be decoded.
     * @param alternativeCarrierRecord Decoded auxiliary data will be added to
     *         the provided alternative carrier record.
     * @throws NdefException In case of errors in reading the input stream
     */
    private static void readAuxiliaryDataReference(
            @NotNull ByteArrayInputStream payloadInputStream,
            @NotNull AlternativeCarrierRecord alternativeCarrierRecord)
            throws NdefException {
        byte auxiliaryDataReferencesLength = (byte) payloadInputStream.read();
        for (int i = 0; i < auxiliaryDataReferencesLength; i++) {
            byte auxiliaryDataReferenceLength = (byte) payloadInputStream
                                                        .read();
            byte[] data;
            try {
                data = Utils.getBytesFromStream(auxiliaryDataReferenceLength,
                                                payloadInputStream);
            } catch (UtilException e) {
                throw new NdefException(e.getMessage(), e);
            }
            DataReference auxiliaryDataReference = new DataReference(data);
            alternativeCarrierRecord.addAuxiliaryDataReference(
                    auxiliaryDataReference);
        }
    }

    /**
     * Decodes the carrier data references.
     *
     * @param payloadInputStream Input stream to be decoded.
     * @return Data reference.
     * @throws NdefException In case of errors in reading the input stream
     */
    private static DataReference readCarrierDataReference(
            @NotNull ByteArrayInputStream payloadInputStream)
            throws NdefException {
        byte carrierDataReferencesLength = (byte) payloadInputStream.read();
        if (carrierDataReferencesLength >= MIN_LENGTH) {
            try {
                return new DataReference(
                        Utils.getBytesFromStream(carrierDataReferencesLength,
                                                 payloadInputStream));
            } catch (UtilException e) {
                throw new NdefException(e.getMessage(), e);
            }
        }
        return null;
    }
}
