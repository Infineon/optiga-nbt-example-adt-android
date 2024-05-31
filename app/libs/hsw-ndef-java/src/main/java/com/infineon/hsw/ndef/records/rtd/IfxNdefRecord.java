// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * NFC data exchange format (NDEF) record. Contains the following parameters:
 * <br> <br> <b><i>Payload length</i></b>: Regardless of the relationship of a
 * record to other records, the payload length always indicates the length of
 * the payload encapsulated in this record. <br> <br> <b><i>Payload
 * type</i></b>: The payload type of a record indicates the kind of data being
 * carried in the payload of that record. This may be used to guide the
 * processing of the payload at the discretion of the user application. <br>
 * <br>
 * <b><i>Payload identification</i></b>: The optional payload identifier allows
 * user applications to identify the payload carried within an NDEF record. By
 * providing a payload identifier, it becomes possible for other payloads
 * supporting URI-based linking technologies to refer to that payload. <br>
 * <br>
 * <b><i>Type name format (TNF)</i></b>: The TNF field value indicates the
 * structure of the value of the TYPE field.The TNF field is a 3-bit field. <br>
 * Following are the types of TNF supported: <br>
 * <i>Empty (0x00)</i> <br>
 * <i>NFC Forum well known type [NFC RTD](0x01)</i> <br>
 * <i>Media-type as defined in RFC 2046 [RFC 2046](0x02)</i> <br>
 * <i>Absolute URI as defined in RFC 3986 [RFC 3986](0x03)</i> <br>
 * <i>NFC Forum external type [NFC RTD](0x04)</i> <br>
 * <i>Unknown(0x05)</i> <br>
 * <i>Unchanged (0x06)</i> <br>
 * <i>Reserved(0x07)</i> <br>
 * <br>
 * <b><i>Type name format (TNF)</i></b>:Application data that has been
 * partitioned into multiple chunks each carried in a separate NDEF record,
 * where each of these records except the last has the CF flag set to 1. This
 * facility can be used to carry dynamically generated content for which the
 * payload size is not known in advance or very large entities that do not fit
 * into a single NDEF record.
 */
public final class IfxNdefRecord extends AbstractRecord {
    /**
     * Maximum length of the identifier field
     */
    private static final int MAX_ID_LENGTH = 0xFF;

    /**
     * Error message if exception is not thrown correctly for ID length.
     */
    private static final String EXCEPTION_ERR_MESSAGE_ID_LENGTH =
            "Expected record id length <= MAX_ID_LENGTH";

    private static final Logger logger = Logger.getLogger(
            IfxNdefRecord.class.getName());

    /**
     * Constructor to create a NDEF record. Each record is made up of
     * a header, which contains metadata about the record, such as the record
     * type, length, and so forth, and the payload, which contains the content
     * of the message.
     *
     * @param tnf     Record type name format
     * @param type    Record type
     * @param id      Record ID
     * @param payload Record payload data
     */
    public IfxNdefRecord(@NotNull byte tnf, @NotNull byte[] type, byte[] id,
                         @NotNull byte[] payload) {
        super(type, tnf);
        if (id != null && id.length > MAX_ID_LENGTH) {
            throw new IllegalArgumentException(EXCEPTION_ERR_MESSAGE_ID_LENGTH);
        }
        this.id = id;
        this.payload = payload.clone();
    }

    /**
     * Constructor to create an NDEF record. Each record is made up
     * of a header, which contains metadata about the record, such as the record
     * type, length, and so forth, and the payload, which contains the content
     * of the message.
     *
     * @param tnf     Record type name format
     * @param chunked Specifies whether data is in chunk
     * @param type    Record type
     * @param id      Record ID
     * @param payload Record payload data
     */
    public IfxNdefRecord(@NotNull byte tnf, @NotNull boolean chunked,
                         @NotNull byte[] type, byte[] id,
                         @NotNull byte[] payload) {
        this(tnf, type, id, payload);
        this.isChunked = chunked;
    }

    /**
     * Returns the record payload bytes.
     *
     * @return Returns the record payload bytes.
     */
    @Override
    public byte[] getPayload() {
        return payload == null ? null : payload.clone();
    }

    /**
     * Sets the record payload bytes.
     *
     * @param payload The record payload bytes.
     */
    @Override
    public void setPayload(@NotNull byte[] payload) {
        this.payload = payload;
    }

    /**
     * Sets the type name format for a record.
     *
     * @param tnf Sets the type name format.
     */
    public void setTnf(@NotNull byte tnf) {
        this.tnf = tnf;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String format = "";
        format += "tnf = " + Utils.toHexString(new byte[] { tnf }) + ",";
        try {
            format += "type = " + (char) Utils.toInteger(recordType.getType()) +
                      ",";
        } catch (UtilException e) {
            logger.log(Level.SEVERE,
                       "Utils.toInteger failed with Util Exception.", e);
        }
        format += "payload = " + Utils.toHexString(payload) + ",";
        format += "chunked = " + isChunked + ",";
        if (id != null) {
            format += "id = " + Utils.toHexString(id);
        }
        return "[" + format + "]";
    }
}
