// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Class represents a container for advertising and scan response data (AD)
 * format. Each AD structure consists of an AD length field of 1 octet, an AD
 * type field, and an AD data field.
 */
public class AdData {
    /**
     * Illegal argument exception message if the AD data field is null.
     */
    protected static final String
            ERR_MESSAGE_AD_DATA = "The AD data field should not be null.";

    /**
     * Illegal argument exception message if the AD data type field is null.
     */
    protected static final String ERR_MESSAGE_AD_DATA_TYPE =
            "The AD data type field should not be null.";

    /**
     * Defines the size of the short value.
     */
    private static final int SIZE_SHORT = 2;

    /**
     * Defines the offset of the tag.
     */
    private static final int OFFSET_TAG = 0;

    /**
     * Defines the offset of the value field.
     */
    private static final int OFFSET_VALUE = 1;

    /**
     * The AD type field
     */
    private byte type;

    /**
     * The AD data field
     */
    private byte[] data;

    /**
     * Constructor to create a new advertising and scan response data (AD)
     * response model.
     *
     * @param adType    The AD type field
     * @param dataBytes The AD data field
     */
    public AdData(@NotNull final byte adType, @NotNull final byte[] dataBytes) {
        if (dataBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_AD_DATA);
        }
        this.type = adType;
        this.data = dataBytes.clone();
    }

    /**
     * Constructor to create a new advertising and scan response data (AD)
     * response model.
     *
     * @param adBytes Bytes representing the advertising and scan response data
     *     (AD) with tag and value.
     */
    public AdData(@NotNull final byte[] adBytes) {
        if (adBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_AD_DATA);
        }
        this.type = adBytes[OFFSET_TAG];
        this.data = Utils.extractBytes(adBytes, OFFSET_VALUE,
                                       adBytes.length - 1);
    }

    /**
     * Constructor to create a new extended inquiry response model.
     *
     * @param adType    The AD type field
     * @param dataBytes The AD data field
     */
    public AdData(@NotNull final DataTypes adType,
                  @NotNull final byte[] dataBytes) {
        if (dataBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_AD_DATA);
        }
        if (adType == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_AD_DATA_TYPE);
        }
        this.type = adType.getValue();
        this.data = dataBytes.clone();
    }

    /**
     * Gets the AD type field.
     *
     * @return Returns the AD type field byte
     */
    public byte getType() {
        return this.type;
    }

    /**
     * Sets the AD type field.
     *
     * @param adType AD type field byte
     */
    public void setType(@NotNull final byte adType) {
        this.type = adType;
    }

    /**
     * Sets the AD type field.
     *
     * @param adType AD type field byte
     */
    public void setType(@NotNull final DataTypes adType) {
        if (adType == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_AD_DATA_TYPE);
        }
        this.type = adType.getValue();
    }

    /**
     * Gets the AD data field.
     *
     * @return Returns the AD data field bytes
     */
    public byte[] getData() {
        if (this.data == null) {
            return new byte[0];
        }
        return this.data.clone();
    }

    /**
     * Sets the AD data field.
     *
     * @param dataBytes AD data field bytes
     */
    public void setData(@NotNull final byte[] dataBytes) {
        if (dataBytes == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_AD_DATA);
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
     * Gets the AD data field as byte.
     *
     * @return Returns the AD data field byte.
     */
    public byte getDataByte() {
        return (byte) Utils.getUINT8(this.data, 0);
    }

    /**
     * Gets the AD data field as short.
     *
     * @return Returns the AD data field short.
     */
    public short getDataShort() {
        return (short) Utils.getUINT16(this.data, 0);
    }

    /**
     * Encodes the advertising and scan response data (AD) format to byte.
     *
     * @return Returns the encoded advertising and scan response data (AD)
     *         format data bytes.
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
        if (!(obj instanceof AdData)) {
            return false;
        }
        AdData other = (AdData) obj;
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
