// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.encoder;

import com.infineon.hsw.ndef.exceptions.InvalidUriException;
import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.rtd.UriRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * Encodes the URI record type to payload byte array.
 */
public class UriRecordPayloadEncoder implements IRecordPayloadEncoder {
    /**
     * Error message if URI record is invalid.
     */
    private static final String
            ERR_MESSAGE_INVALID_URI = "UriRecord does not have URI";

    /**
     * UriRecordPayloadEncoder constructor.
     */
    public UriRecordPayloadEncoder() {
        /* Default constructor should be left empty. */
    }

    /**
     * Encodes the URI record data structure into the record payload byte
     * array.
     *
     * @param abstractRecord URI record
     * @return Record payload byte array
     * @throws NdefException If unable to encode the record payload
     */
    @Override
    public byte[] encode(@NotNull AbstractRecord abstractRecord)
            throws NdefException {
        if (!(abstractRecord instanceof UriRecord)) {
            throw new NdefException(ERR_UNSUPPORTED_TYPE);
        }

        UriRecord uriRecord = (UriRecord) abstractRecord;

        if (uriRecord.getUri() == null || uriRecord.getUri().isEmpty()) {
            throw new InvalidUriException(ERR_MESSAGE_INVALID_URI);
        }

        String uri = uriRecord.getUri();
        byte[] uriAsBytes = getUriAsBytes(uri);

        // URI payload contains URI identifier (1-byte) + URI value (n-bytes)
        byte[] payload = new byte[uriAsBytes.length + 1];
        payload[0] = (byte) uriRecord.getUriIdentifier().getIdentifierCode();
        System.arraycopy(uriAsBytes, 0, payload, 1, uriAsBytes.length);

        return payload;
    }

    /**
     * Converts URI to byte array.
     *
     * @param uri Uniform resource identifier (URI). For example:
     *            <i>https://www.company.com/</i>
     * @return Byte array of URI
     * @InvalidUriException If unable to convert the UTF-8 URL as bytes
     */
    private static byte[] getUriAsBytes(@NotNull String uri)
            throws InvalidUriException {
        try {
            return uri.getBytes(NdefConstants.UTF_8_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new InvalidUriException(e.getLocalizedMessage(), e);
        }
    }
}
