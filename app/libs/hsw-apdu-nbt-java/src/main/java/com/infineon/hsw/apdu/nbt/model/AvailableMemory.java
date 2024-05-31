// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt.model;

import com.infineon.hsw.utils.Tlv;
import com.infineon.hsw.utils.TlvParser;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.List;

/**
 * Parses the available memory information from the NBT applet.
 */
public class AvailableMemory {
    /**
     * Tag for available transient of clear on deselect (COD) type.
     */
    private static final byte TAG_COD_MEMORY = (byte) 0xC8;
    /**
     * Tag for available transient of clear on reset (COR) type.
     */
    private static final byte TAG_COR_MEMORY = (byte) 0xC7;
    /**
     * Tag for available persistent/NVM memory.
     */
    private static final byte TAG_NVM_MEMORY = (byte) 0xC6;

    /**
     * NVM/available persistent memory in the available memory.
     */
    private short nvmMemory;

    /**
     * Available transient of clear on reset type memory in the available
     * memory.
     */
    private short availableTransientCor;

    /**
     * Available transient of clear on deselect type memory in the available
     * memory.
     */
    private short availableTransientCod;

    /**
     * Constructor for creating an instance with the available memory.
     *
     * @param data Data bytes received from the secure element.
     * @throws NbtException Throws the NBT exception, if failed to parse the
     *                      available memory data.
     */
    public AvailableMemory(@NotNull byte[] data) throws NbtException {
        try {
            TlvParser parser = new TlvParser(data);

            // Extract 6f TLV
            List<Object> tlvList = parser.parseTlvStructure();
            Tlv tlv6f = (Tlv) tlvList.get(0);

            // Extract DF3B TLV
            tlvList = new TlvParser(tlv6f.getValue()).parseTlvStructure();
            Tlv tlvDf3b = (Tlv) tlvList.get(0);

            // Extract c6,c7,c8 TLVs
            tlvList = new TlvParser(tlvDf3b.getValue()).parseTlvStructure();

            for (Object tlvDf3bFragments : tlvList) {
                Tlv tlvDf3bFragment = (Tlv) tlvDf3bFragments;

                byte tag = (byte) tlvDf3bFragment.getTag();
                byte[] value = tlvDf3bFragment.getValue();

                if (tag == TAG_NVM_MEMORY) {
                    this.setNvmMemory((short) Utils.getUINT16(value, 0));
                }
                if (tag == TAG_COR_MEMORY) {
                    this.setAvailableTransientCor(
                            (short) Utils.getUINT16(value, 0));
                }
                if (tag == TAG_COD_MEMORY) {
                    this.setAvailableTransientCod(
                            (short) Utils.getUINT16(value, 0));
                }
            }
        } catch (UtilException ex) {
            throw new NbtException("Failed to parse available memory data", ex);
        }
    }

    /**
     * Getter for NVM memory
     *
     * @return Returns the NVM memory.
     */
    public short getNvmMemory() {
        return nvmMemory;
    }

    /**
     * Setter for NVM memory
     *
     * @param nvmMemory Sets the NVM memory.
     */
    private void setNvmMemory(@NotNull short nvmMemory) {
        this.nvmMemory = nvmMemory;
    }

    /**
     * Getter for available transient of COR memory type
     *
     * @return Gets the available transient of COR memory type.
     */
    public short getAvailableTransientCor() {
        return availableTransientCor;
    }

    /**
     * Setter for available transient of COR memory type
     *
     * @param availableTransientCor Sets the available transient of COR memory
     *     type.
     */
    private void setAvailableTransientCor(@NotNull
                                          short availableTransientCor) {
        this.availableTransientCor = availableTransientCor;
    }

    /**
     * Getter for available transient of COD memory type
     *
     * @return Gets the available transient of COD memory type.
     */
    public short getAvailableTransientCod() {
        return availableTransientCod;
    }

    /**
     * Setter for available transient of COD memory type.
     *
     * @param availableTransientCod Sets the available transient of COD memory
     *     type.
     */
    private void setAvailableTransientCod(@NotNull
                                          short availableTransientCod) {
        this.availableTransientCod = availableTransientCod;
    }
}
