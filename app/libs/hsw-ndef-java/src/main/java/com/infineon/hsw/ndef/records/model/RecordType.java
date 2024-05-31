// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.Arrays;

/**
 * Represent the type of the record. Record type can be any NDEF well known
 * record types. For example, smart poster record and text record.
 */
public class RecordType {
    private final byte[] type;

    /**
     * Constructor to set the record type with the give byte array.
     *
     * @param type Record type which is of byte array.
     */
    public RecordType(@NotNull byte[] type) {
        this.type = type.clone();
    }

    /**
     * Constructor to set the record type with the give string type.
     *
     * @param type Record type of string (For example, "T").
     */
    public RecordType(@NotNull String type) {
        this.type = type.getBytes(NdefConstants.DEFAULT_CHARSET);
    }

    /**
     * Gets the record type.
     *
     * @return Returns the type of the record.
     */
    public byte[] getType() {
        return type == null ? null : type.clone();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new String(type, NdefConstants.DEFAULT_CHARSET);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(type);
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        RecordType other = (RecordType) obj;
        return Arrays.equals(type, other.type);
    }
}
