// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Data reference to point to another NDEF record.
 */
public class DataReference {
    /**
     * Data reference characters
     */
    private byte[] data;

    /**
     * Constructor to create a new data reference model.
     *
     * @param data Data reference characters
     */
    public DataReference(@NotNull byte[] data) {
        this.data = data.clone();
    }

    /**
     * Gets the data reference characters.
     *
     * @return Returns the data reference characters.
     */
    public byte[] getData() {
        return this.data.clone();
    }

    /**
     * Gets the data reference characters length.
     *
     * @return Returns the data reference characters length.
     */
    public int getLength() {
        return this.data != null ? this.data.length : 0;
    }

    /**
     * Sets the data reference characters.
     *
     * @param data Returns the data reference characters.
     */
    public void setData(@NotNull byte[] data) {
        this.data = data.clone();
    }
}
