// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Represents a container for extended inquiry response data object. Each
 * EIR structure consists of an EIR Length field of 1 octet, an EIR Type field,
 * and an EIR Data field.
 */
public class EirData {
    /**
     * Defines the size of short value.
     */
    private static final int SIZE_SHORT = 2;

    /**
     * Defines the offset of tag.
     */
    private static final int OFFSET_TAG = 0;

    /**
     * Defines the offset of value.
     */
    private static final int OFFSET_VALUE = 1;

    /**
     * Illegal argument exception message if the EIR data field is null.
     */
    protected static final String
            ERR_MESSAGE_EIR_DATA = "The EIR data field should not be null";

    /**
     * Illegal argument exception message if the EIR data type field is null.
     */
    protected static final String ERR_MESSAGE_EIR_DATA_TYPE =
            "The EIR data type field should not be null";

    /**
     * The EIR type field
     */
    private byte type;

    /**
     * The EIR data field
     */
    private byte[] data;

    /**
     * Constructor to create a new extended inquiry response model.
     *
     * @param dataType  The EIR type field
     * @param dataBytes The EIR data field
     */
    public EirData(@NotNull final byte dataType,
                   @NotNull final byte[] dataBytes) {
        if (dataBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_EIR_DATA);
        }
        this.type = dataType;
        this.data = dataBytes.clone();
    }

    /**
     * Constructor to create a new extended inquiry response model.
     *
     * @param eirBytes Bytes representing extended inquiry response (EIR) data
     *                 with tag and value.
     */
    public EirData(@NotNull final byte[] eirBytes) {
        if (eirBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_EIR_DATA);
        }
        this.type = eirBytes[OFFSET_TAG];
        this.data = Utils.extractBytes(eirBytes, OFFSET_VALUE,
                                       eirBytes.length - 1);
    }

    /**
     * Constructor to create a new extended inquiry response model.
     *
     * @param eirType   The EIR type field
     * @param dataBytes The EIR data field
     */
    public EirData(@NotNull final DataTypes eirType,
                   @NotNull final byte[] dataBytes) {
        if (dataBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_EIR_DATA);
        }
        if (eirType == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_EIR_DATA_TYPE);
        }
        this.type = eirType.getValue();
        this.data = dataBytes.clone();
    }

    /**
     * Gets the EIR type field.
     *
     * @return Returns the EIR type field byte
     */
    public byte getType() {
        return this.type;
    }

    /**
     * Sets the EIR type field.
     *
     * @param dataType The EIR type field byte
     */
    public void setType(@NotNull final byte dataType) {
        this.type = dataType;
    }

    /**
     * Sets the EIR type field.
     *
     * @param eirType The EIR type field byte
     */
    public void setType(@NotNull final DataTypes eirType) {
        if (eirType == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_EIR_DATA_TYPE);
        }
        this.type = eirType.getValue();
    }

    /**
     * Gets the EIR data field.
     *
     * @return Returns EIR data field bytes.
     */
    public byte[] getData() {
        if (this.data == null) {
            return new byte[0];
        }
        return this.data.clone();
    }

    /**
     * Sets the EIR data field.
     *
     * @param dataBytes The EIR data field bytes
     */
    public void setData(@NotNull final byte[] dataBytes) {
        if (dataBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_EIR_DATA);
        }
        this.data = dataBytes.clone();
    }

    /**
     * Sets the AD data field.
     *
     * @param data AD data field as short
     */
    public void setData(@NotNull final short data) {
        this.data = Utils.toBytes(data, SIZE_SHORT);
    }

    /**
     * Sets the AD data field.
     *
     * @param data AD data field as short
     */
    public void setData(@NotNull final byte data) {
        this.data = new byte[] { data };
    }

    /**
     * Gets the EIR data length.
     *
     * @return Returns the length of EIR data.
     */
    public int getLength() {
        if (this.data != null) {
            return this.data.length;
        }
        return 0;
    }

    /**
     * Gets the EIR data field as byte.
     *
     * @return Returns the EIR data field byte.
     */
    public byte getDataByte() {
        return (byte) Utils.getUINT8(this.data, 0);
    }

    /**
     * Gets the EIR data field as short.
     *
     * @return Returns the EIR data field short.
     */
    public short getDataShort() {
        return (short) Utils.getUINT16(this.data, 0);
    }

    /**
     * Encodes the EIR format data to byte.
     *
     * @return Returns the encoded EIR format data bytes.
     */
    public byte[] toBytes() {
        if (this.data != null) {
            byte[] header = new byte[] { (byte) (this.data.length + 1),
                                         this.type };
            return Utils.concat(header, this.data);
        }
        return new byte[0];
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EirData)) {
            return false;
        }
        EirData other = (EirData) obj;
        if (this.type != other.type) {
            return false;
        }
        byte[] thisByteArray = this.data;
        byte[] otherByteArray = other.data;
        if (thisByteArray.length != otherByteArray.length) {
            return false;
        }
        for (int index = 0; index < thisByteArray.length; index++) {
            if (thisByteArray[index] != otherByteArray[index]) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int prime = 17;
        prime = 31 * prime + type;
        byte[] byteArrayAttr = this.data;
        for (int index = 0; index < byteArrayAttr.length; index++) {
            prime = 31 * prime + byteArrayAttr[index];
        }
        return prime;
    }
}
