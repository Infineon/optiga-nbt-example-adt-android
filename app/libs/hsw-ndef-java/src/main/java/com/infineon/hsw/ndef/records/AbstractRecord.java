// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records;

import com.infineon.hsw.ndef.records.model.RecordType;
import com.infineon.hsw.utils.annotation.NotNull;
import java.nio.charset.StandardCharsets;

/**
 * Each NDEF record is an abstract record. Abstract record is used to get/set
 * the record type. Gets the record ID, gets and sets the key value, and gets
 * and sets the type name format.
 */
public abstract class AbstractRecord {
    /**
     * Pre calculated hash code while decoding the record.
     */
    protected int hashCode;

    /**
     * Record identifier meta-data
     */
    protected byte[] id = new byte[0];

    /**
     * The variable length type field.
     */
    protected RecordType recordType;

    /**
     * Type name format (TNF) mask. The TNF field value indicates the structure
     * of the value of the type field.
     */
    protected byte tnf;

    /**
     * The actual payload.
     */
    protected byte[] payload;

    /**
     * Application data that has been partitioned into multiple chunks each
     * carried in a separate NDEF record, where each of these records except the
     * last has the CF flag set to 1. This facility can be used to carry
     * dynamically generated content for which the payload size is not known in
     * advance or very large entities that do not fit into a single NDEF record.
     */
    protected boolean isChunked;

    /**
     * Constructor to create an abstract record.
     *
     * @param recordType Record type of string (For example, "T").
     * @param tnf        TNF of record
     */
    protected AbstractRecord(@NotNull final String recordType,
                             @NotNull byte tnf) {
        this.tnf = tnf;
        this.recordType = new RecordType(recordType);
    }

    /**
     * Constructor to create an abstract record.
     *
     * @param recordType Record type of string (For example, "T").
     * @param tnf        TNF of record
     */
    protected AbstractRecord(@NotNull final byte[] recordType,
                             @NotNull byte tnf) {
        this.tnf = tnf;
        this.recordType = new RecordType(recordType);
    }

    /**
     * Gets the type of an NDEF record.
     *
     * @return Returns the type of the record.
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets to a specific record type.
     *
     * @param recordType Sets the type of record.
     */
    public void setRecordType(@NotNull RecordType recordType) {
        this.recordType = recordType;
    }

    /**
     * Returns the specific record type bytes.
     *
     * @return Returns the variable length type field.
     */
    public byte[] getType() {
        if (recordType != null) {
            return recordType.getType();
        }
        return new byte[0];
    }

    /**
     * Gets the NDEF record identifier (ID).
     *
     * @return Returns the record ID.
     */
    public byte[] getId() {
        return id;
    }

    /**
     * Sets the ID for an NDEF record.
     *
     * @param id Sets the record ID.
     */
    public void setId(byte[] id) {
        this.id = id;
    }

    /**
     * Sets the record ID for an NDEF record as string.
     *
     * @param id Record ID for an NDEF record as string.
     */
    public void setId(String id) {
        if (id != null) {
            this.id = id.getBytes(StandardCharsets.UTF_8);
        } else {
            this.id = new byte[0];
        }
    }

    /**
     * Gets the record ID for an NDEF record as string.
     *
     * @return Returns the record identifier (ID) for an NDEF record as string.
     */
    public String getIdString() {
        return new String(id, StandardCharsets.UTF_8);
    }

    /**
     * Returns the TNF: Type name format field. The type name format
     * or TNF field of an NDEF record is a 3-bit value that describes the record
     * type, and sets the expectation for the structure and content of the rest
     * of the record.
     *
     * @return Returns the 3-bit TNF.
     */
    public byte getTnf() {
        return tnf;
    }

    //@formatter:off
    /**
     * Returns the record payload bytes.
     *
     * @return Returns the record payload bytes.
     *
     * Note: Use encode payload() method to convert payload to the byte array.
     * This method will return the existing payload if present and the payload
     * content will be out-of-sync if the record fields are updated.
     * The state of the payload byte array is updated only during encode.
     */
    //@formatter:on
    protected byte[] getPayload() {
        if (payload == null) {
            return new byte[0];
        }
        return payload.clone();
    }

    /**
     * Sets the record payload bytes.
     *
     * @param payload The record payload bytes.
     */
    protected void setPayload(@NotNull byte[] payload) {
        this.payload = payload;
    }

    /**
     * The CF flag indicates if this is the first record chunk or a middle
     * record chunk.
     *
     * @return Returns true, if record is chunked.
     */
    public boolean isChunked() {
        return isChunked;
    }

    /**
     * The CF flag indicates if this is the first record chunk or a middle
     * record chunk
     *
     * @param isChunked The CF flag indicates record is chunked.
     */
    public void setIsChunked(@NotNull boolean isChunked) {
        this.isChunked = isChunked;
    }

    /**
     * Returns true, if the record has a record ID in it.
     *
     * @return Returns a status based on key availability.
     */
    public boolean hasId() {
        return id != null && id.length > 0;
    }

    /**
     * Gets the calculated hash code while decoding the record.
     *
     * @return Returns the pre calculated hash code while decoding the record.
     */
    protected int getHashCode() {
        return hashCode;
    }

    /**
     * Sets the calculated hash code while decoding the record.
     *
     * @param hashCode Calculated hash code while decoding the record.
     */
    protected void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    /**
     * Sets the calculated hash code while decoding the record.
     */
    protected void setHashCode() {
        this.setHashCode(this.hashCode());
    }
}
