// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.decoder;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.rtd.UriRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Decodes the payload byte array of the URI record type.
 */
public class UriRecordPayloadDecoder implements IRecordPayloadDecoder {
    /**
     * UriRecordPayloadDecoder constructor.
     */
    public UriRecordPayloadDecoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Decodes the URI record payload byte array into the URI record
     * structure.
     *
     * @param payload URI record payload byte array
     * @return Abstract record data structure
     */
    @Override
    public AbstractRecord decode(@NotNull byte[] payload) throws NdefException {
        try {
            int identifierCode = payload[0];
            String uri = new String(payload, 1, payload.length - 1,
                                    NdefConstants.UTF_8_CHARSET);
            return new UriRecord(identifierCode, uri);
        } catch (RuntimeException e) {
            throw new NdefException(ERR_MESSAGE_INVALID_PAYLOAD, e);
        }
    }
}
