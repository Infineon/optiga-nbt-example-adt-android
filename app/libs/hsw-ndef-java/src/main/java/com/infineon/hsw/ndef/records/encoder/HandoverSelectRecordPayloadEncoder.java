// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.NdefMessageEncoder;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.rtd.HandoverSelectRecord;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Encodes the handover select record type to payload byte array.
 */
public class HandoverSelectRecordPayloadEncoder
        implements IRecordPayloadEncoder {
    /**
     * HandoverSelectRecordPayloadEncoder constructor.
     */
    public HandoverSelectRecordPayloadEncoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Encodes the handover select record data structure into the record
     * payload byte array.
     *
     * @param wellKnownRecord WellKnownRecord HandoverSelectRecord
     * @return Record payload byte array
     * @throws NdefException If unable to encode the record payload
     */
    @Override
    public byte[] encode(@NotNull AbstractRecord wellKnownRecord)
            throws NdefException {
        if (!(wellKnownRecord instanceof HandoverSelectRecord)) {
            throw new NdefException(ERR_UNSUPPORTED_TYPE);
        }

        byte version;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        List<AbstractRecord> records = new ArrayList<>();
        HandoverSelectRecord handoverSelectRecord = (HandoverSelectRecord)
                wellKnownRecord;

        if (handoverSelectRecord.getAlternativeCarrierRecords() != null) {
            records.addAll(handoverSelectRecord.getAlternativeCarrierRecords());
        }

        if (handoverSelectRecord.getErrorRecord() != null) {
            records.add(handoverSelectRecord.getErrorRecord());
        }
        version = (byte) ((handoverSelectRecord.getMajorVersion() << 4) |
                          (handoverSelectRecord.getMinorVersion() & 0xff));
        stream.write(version);
        NdefMessageEncoder.getInstance().encode(records, stream);
        return stream.toByteArray();
    }
}
