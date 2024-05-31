// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Container class for TLV structures. This class allows to build simple and
 * constructed TLV structures.
 */
public class Tlv {
    /** Universal tag for boolean value */
    public static final int TAG_BOOLEAN = 0x01;
    /** Universal tag for integer value */
    public static final int TAG_INTEGER = 0x02;
    /** Universal tag for bit string */
    public static final int TAG_BIT_STRING = 0x03;
    /** Universal tag for octet string */
    public static final int TAG_OCTET_STRING = 0x04;
    /** Universal tag for NULL */
    public static final int TAG_NULL = 0x05;
    /** Universal tag for an object identifier */
    public static final int TAG_OID = 0x06;
    /** Universal tag for an embedded */
    public static final int TAG_EMBEDDED_PDV = 0x0B;
    /** Universal tag for an UTF-8 string */
    public static final int TAG_UTF8_STRING = 0x0C;
    /** Universal tag for a relative object identifier */
    public static final int TAG_RELATIVE_OID = 0x0D;
    /** Universal tag for a numeric string */
    public static final int TAG_NUMERIC_STRING = 0x12;
    /** Universal tag for a printable string */
    public static final int TAG_PRINTABLE_STRING = 0x13;
    /** Universal tag for a sequence */
    public static final int TAG_SEQUENCE = 0x30;
    /** Universal tag for a set */
    public static final int TAG_SET = 0x31;

    /** Type of tag is universal */
    public static final int TYPE_UNIVERSAL = 0x00;
    /** Type of tag is application specific */
    public static final int TYPE_APPLICATION = 0x40;
    /** Type of tag is context specific */
    public static final int TYPE_CONTEXT = 0x80;
    /** Type of tag is private */
    public static final int TYPE_PRIVATE = 0xC0;

    private static final int MASK_CONSTRUCTED = 0x20;

    /** Arbitrary name of TLV structure */
    protected String name;
    /** tag of TLV object */
    protected final int tag;
    /** Flag how to encode length '80' (128) */
    protected boolean allowLength80 = false;
    /** Flag how to parse length '80' (indefinite or 128) */
    protected boolean indefLength = false;
    /** Structured value of TLV object */
    protected ArrayList<Object> value = new ArrayList<>();

    /* default */ Logger logger;

    /**
     * Constructor of TLV object.
     *
     * @param tag   tag of TLV object or zero if object is an LV structure.
     * @param name  arbitrary name of TLV structure. This parameter may be null.
     * @param value initial value of TLV object. This parameter may be null for
     *         an empty object.
     * @throws UtilException If there is utility related exception
     */
    public Tlv(int tag, String name, Object value) throws UtilException {
        this.tag = tag;
        this.name = name;
        addValue(value);
        logger = Logger.getLogger(Tlv.class.getName());
    }

    /**
     * Get tag of structure. If the tag is zero, this structure degenerates to a
     * LV structure.
     *
     * @return tag of TLV object.
     */
    public int getTag() {
        return tag;
    }

    /**
     * Check if the tag indicates a constructed TLV.
     *
     * @return true if tag indicates a constructed TLV
     */
    public boolean isConstructed() {
        int tag = this.tag;
        while (tag > 0xFF)
            tag >>= 8;

        return (tag & MASK_CONSTRUCTED) != 0;
    }

    /**
     * Return total length of (constructed) value.
     *
     * @return total length of value.
     * @throws UtilException throws Util Exception in case error in length.
     */
    public int getLength() throws UtilException {
        int length = 0;

        for (Object v : value) {
            if (v instanceof Tlv) {
                length += ((Tlv) v).toBytes().length;
            } else {
                length += Utils.toBytes(v).length;
            }
        }

        return length;
    }

    /**
     * Retrieve byte array containing the (constructed) value of the TLV
     * structure.
     *
     * @return value of TLV structure as a byte array.
     * @throws UtilException throws util exception in case of error in
     *                       concatenation.
     */
    public byte[] getValue() throws UtilException {
        byte[] tlv = new byte[0];

        for (Object v : value) {
            if (v instanceof Tlv) {
                tlv = Utils.concat(tlv, ((Tlv) v).toBytes());
            } else {
                tlv = Utils.concat(tlv, Utils.toBytes(v));
            }
        }

        return tlv;
    }

    /**
     * Return byte array representation of TLV or LV structure including (tag
     * and) length.
     *
     * @return byte array representation of TLV or LV structure.
     */
    public byte[] toBytes() {
        try {
            // surround with (tag and) length
            return buildTlv(tag, getValue(), allowLength80);
        } catch (UtilException e) {
            logger.log(Level.SEVERE, "Build TLV Failed with Util Exception.",
                       e);
        }

        return new byte[0];
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return Utils.toHexString(toBytes());
    }

    /**
     * Add a value to the value part of the TLV structure. The value is appended
     * to the current value of the TLV object.
     *
     * @param value value to be added to the TLV object.
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws UtilException if value parameter cannot be converted to a byte
     *         array.
     */
    @SuppressWarnings("unchecked")
    public final Tlv addValue(Object value) throws UtilException {
        if (value != null) {
            // check value format
            if (value instanceof Vector<?>) {
                // add elements of vector
                for (Object v : (Vector<Object>) value)
                    addValue(v);
            } else {
                // check if value can be transformed to byte array
                if (!(value instanceof Tlv))
                    Utils.toBytes(value);

                this.value.add(value);
            }
        }

        return this;
    }

    /**
     * Get a list of all values that will be concatenated to the value part of
     * the TLV object.
     *
     * @return array of objects forming the value part of the TLV structure.
     */
    public Object[] getValues() {
        return value.toArray();
    }

    /**
     * Parse encoded Dgi TLV structure
     *
     * @param encoded encoded DGI TLV structures
     * @return root TLV object
     * @throws UtilException if parsing structure fails
     */
    public static List<Object> parseDgiTlv(byte[] encoded)
            throws UtilException {
        // parse TLV structure
        return new TlvParser(Utils.toBytes(encoded)).parseDgiTlvStructure();
    }

    /**
     * Build a TLV or LV structure from a given value.
     *
     * @param tag           tag for structure or zero if LV structure shall be
     *                      built.
     * @param value         value to be wrapped in TLV or LV structure.
     * @param allowLength80 code length of 128 (0x80) bytes as simple length
     *         '80' instead of '8180'.
     * @return byte array containing TLV structure.
     * @throws UtilException if value cannot be converted into a byte array.
     */
    public static byte[] buildTlv(int tag, Object value, boolean allowLength80)
            throws UtilException {
        byte[] valueArray = Utils.toBytes(value);
        byte[] tagArray;
        byte[] lengthArray;
        byte[] tlvArray;

        // build byte array of tag
        tagArray = encodeTag(tag);

        // build byte array of length
        lengthArray = encodeLength(valueArray.length, allowLength80);

        // build TLV structure
        tlvArray = Arrays.copyOf(tagArray, tagArray.length +
                                                   lengthArray.length +
                                                   valueArray.length);
        System.arraycopy(lengthArray, 0, tlvArray, tagArray.length,
                         lengthArray.length);
        System.arraycopy(valueArray, 0, tlvArray,
                         tagArray.length + lengthArray.length,
                         valueArray.length);

        return tlvArray;
    }

    /**
     * Build BER encoded tag.
     *
     * @param tag tag for structure or zero if LV structure shall be built.
     * @return Byte array containing tag information.
     */
    public static byte[] encodeTag(int tag) {
        byte[] abTag;
        if (tag == 0)
            abTag = new byte[0];
        else if ((tag & 0xFF) == tag)
            abTag = Utils.toBytes(tag, 1);
        else if ((tag & 0xFFFF) == tag)
            abTag = Utils.toBytes(tag, 2);
        else if ((tag & 0xFFFFFF) == tag)
            abTag = Utils.toBytes(tag, 3);
        else
            abTag = Utils.toBytes(tag, 4);
        return abTag;
    }

    /**
     * Build BER encoded length.
     *
     * @param length        Length to be encoded.
     * @param allowLength80 Code length of 128 bytes as simple length '80'
     *         instead of '8180'.
     * @return Byte array containing length information.
     */
    public static byte[] encodeLength(int length, boolean allowLength80) {
        byte[] abLength;
        if (((length & 0x7F) == length) ||
            ((length == 0x80) && allowLength80)) {
            // simple one byte length
            abLength = Utils.toBytes(length, 1);
        } else if ((length & 0xFF) == length) {
            // extended one byte length
            abLength = Utils.toBytes(length, 2);
            abLength[0] = (byte) 0x81;
        } else if ((length & 0xFFFF) == length) {
            // two byte length
            abLength = Utils.toBytes(length, 3);
            abLength[0] = (byte) 0x82;
        } else if ((length & 0xFFFFFF) == length) {
            // three byte length
            abLength = Utils.toBytes(length, 4);
            abLength[0] = (byte) 0x83;
        } else {
            // four byte length
            abLength = Utils.toBytes(length, 5);
            abLength[0] = (byte) 0x84;
        }
        return abLength;
    }

    /**
     * Build a DGI TLV from a given value.
     *
     * @param tag   tag for structure.
     * @param value value to be wrapped in TLV.
     * @return byte array containing TLV structure.
     * @throws UtilException if value cannot be converted into a byte array.
     */
    public static byte[] buildDgiTlv(short tag, Object value)
            throws UtilException {
        byte[] valueBytes = Utils.toBytes(value);
        byte[] tagBytes;
        byte[] lengthBytes;

        // build byte array of tag
        tagBytes = encodeDgiTag(tag);

        // build byte array of length
        lengthBytes = encodeDgiLength(valueBytes.length);

        byte[] abTLV = new byte[tagBytes.length + lengthBytes.length +
                                valueBytes.length];

        // build TLV structure
        System.arraycopy(tagBytes, 0, abTLV, 0, tagBytes.length);
        System.arraycopy(lengthBytes, 0, abTLV, tagBytes.length,
                         lengthBytes.length);
        System.arraycopy(valueBytes, 0, abTLV,
                         tagBytes.length + lengthBytes.length,
                         valueBytes.length);

        return abTLV;
    }

    /**
     * Parse (a concatenation of) DGI TLV structure(s) and return a resulting
     * TLV object
     *
     * @param tlvList tag, length and values in TLV structure.
     * @return byte array containing TLV structure.
     * @throws UtilException if value cannot be converted into a byte array.
     *
     */
    public static byte[] buildDgiTlvList(List<Tlv> tlvList)
            throws UtilException {
        ByteArrayOutputStream tlvStream = new ByteArrayOutputStream();
        try {
            for (Tlv tlv : tlvList) {
                byte[] encodedDgiTLV = Tlv.buildDgiTlv((short) tlv.getTag(),
                                                       tlv.getValue());
                tlvStream.write(encodedDgiTLV);
            }
            return tlvStream.toByteArray();
        } catch (IOException ex) {
            throw new UtilException("Failed to process the input TLVs", ex);
        }
    }

    /**
     * Build Dgi encoded tag.
     *
     * @param tag tag for structure.
     * @return Byte array containing tag information.
     */
    public static byte[] encodeDgiTag(short tag) {
        byte[] tagBytes;
        tagBytes = Utils.toBytes(tag, 2);
        return tagBytes;
    }

    /**
     * Build encoded Dgi length.
     *
     * @param length Length to be encoded.
     * @return Byte array containing length information.
     */
    public static byte[] encodeDgiLength(int length) {
        if ((length & 0xFF) == length) {
            return Utils.toBytes(length, 1);
        } else {
            byte[] lengthBytes = new byte[3];
            lengthBytes[0] = (byte) 0xFF;
            System.arraycopy(Utils.toBytes(length, 2), 0, lengthBytes, 1, 2);
            return lengthBytes;
        }
    }
}
