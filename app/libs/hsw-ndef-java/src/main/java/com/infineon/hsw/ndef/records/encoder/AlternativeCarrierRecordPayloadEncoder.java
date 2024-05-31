// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.exceptions.InvalidCarrierDataException;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.model.DataReference;
import com.infineon.hsw.ndef.records.rtd.AlternativeCarrierRecord;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.List;

/**
 * Encodes the alternative carrier record type to payload byte array.
 */
public class AlternativeCarrierRecordPayloadEncoder
        implements IRecordPayloadEncoder {
    /**
     * Defines carrier data reference size.
     */
    private static final int EMPTY_CARRIER_DATA_SIZE = 0;

    /**
     * Defines auxiliary data size.
     */
    private static final int EMPTY_AUXILIARY_DATA_SIZE = 0;

    /**
     * Error message if carrier data reference bytes are null or not present.
     */
    private static final String ERR_MESSAGE_EMPTY_CARRIER_DATA =
            "Carrier data references bytes are empty";

    /**
     * AlternativeCarrierRecordPayloadEncoder constructor.
     */
    public AlternativeCarrierRecordPayloadEncoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Encodes the alternative carrier record data structure into the record
     * payload byte array.
     *
     * @param wellKnownRecord WellKnownRecord AlternativeCarrierRecord
     * @return Record payload byte array
     * @throws NdefException If unable to decode the record payload
     */
    @Override
    public byte[] encode(@NotNull AbstractRecord wellKnownRecord)
            throws NdefException {
        if (!(wellKnownRecord instanceof AlternativeCarrierRecord)) {
            throw new NdefException(ERR_UNSUPPORTED_TYPE);
        }

        final AlternativeCarrierRecord alternativeCarrierRecord =
                (AlternativeCarrierRecord) wellKnownRecord;

        DataReference carrierDataReference = alternativeCarrierRecord
                                                     .getCarrierDataReference();
        if (carrierDataReference == null ||
            carrierDataReference.getData() == null ||
            carrierDataReference.getData().length == EMPTY_CARRIER_DATA_SIZE) {
            throw new InvalidCarrierDataException(
                    ERR_MESSAGE_EMPTY_CARRIER_DATA);
        }

        byte[] alternativeCarrierRecordPayloadAsBytes =
                Utils.concat(new byte[] { alternativeCarrierRecord.getCps() },
                             encodeDataReference(carrierDataReference));

        List<DataReference> auxiliaryDataReferences =
                alternativeCarrierRecord.getAuxiliaryDataReferences();
        alternativeCarrierRecordPayloadAsBytes =
                Utils.concat(alternativeCarrierRecordPayloadAsBytes,
                             encodeAuxiliaryDataReferences(
                                     auxiliaryDataReferences));

        return alternativeCarrierRecordPayloadAsBytes;
    }

    /**
     * Encodes the auxiliary data reference list in the byte array.
     *
     * @param auxiliaryDataReferences Auxiliary data reference list
     * @return Auxiliary data reference byte array
     */
    private static byte[] encodeAuxiliaryDataReferences(
            @NotNull List<DataReference> auxiliaryDataReferences) {
        byte[] alternativeCarrierRecordPayloadAsBytes = new byte[0];
        if (auxiliaryDataReferences != null &&
            !auxiliaryDataReferences.isEmpty()) {
            alternativeCarrierRecordPayloadAsBytes = Utils.concat(
                    alternativeCarrierRecordPayloadAsBytes,
                    new byte[] { (byte) auxiliaryDataReferences.size() });
            for (DataReference auxiliaryDataReference :
                 auxiliaryDataReferences) {
                alternativeCarrierRecordPayloadAsBytes =
                        Utils.concat(alternativeCarrierRecordPayloadAsBytes,
                                     encodeDataReference(
                                             auxiliaryDataReference));
            }

        } else {
            alternativeCarrierRecordPayloadAsBytes =
                    Utils.concat(alternativeCarrierRecordPayloadAsBytes,
                                 new byte[] {
                                         (byte) EMPTY_AUXILIARY_DATA_SIZE });
        }
        return alternativeCarrierRecordPayloadAsBytes;
    }

    /**
     * Encodes the data reference byte array.
     *
     * @param dataReference Data reference
     * @return Data reference byte array
     */
    private static byte[] encodeDataReference(
            @NotNull DataReference dataReference) {
        return Utils.concat(new byte[] { (byte) dataReference.getLength() },
                            dataReference.getData());
    }
}
