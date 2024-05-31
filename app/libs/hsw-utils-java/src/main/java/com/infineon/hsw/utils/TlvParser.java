// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for parsing TLV structures.
 */
public class TlvParser {
    private static final String EOF = "EOF";
    private static final String BOOLEAN = "BOOLEAN";
    private static final String INTEGER = "INTEGER";
    private static final String SEQUENCE = "SEQUENCE";
    private static final String SET = "SET";
    private static final String NULL = "NULL";
    private static final String BIT_STRING = "BIT STRING";
    private static final String OCTET_STRING = "OCTET STRING";

    /** Reference to input byte array */
    private final byte[] structure;
    /** Current parser offset in input byte array */
    private int offset;
    private boolean indef = false;

    /**
     * Constructor of new TLV parser object.
     *
     * @param structure reference of input byte array containing a TLV or LV
     *                  structure.
     */
    public TlvParser(byte[] structure) {
        this.structure = structure != null ? structure.clone() : null;
    }

    /**
     * Constructor of new TLV parser object.
     *
     * @param structure         reference of input byte array containing a TLV
     *         or LV
     *                          structure.
     * @param indefiniteSupport if true, indefinite length coding is supported
     */
    public TlvParser(byte[] structure, boolean indefiniteSupport) {
        this.structure = structure != null ? structure.clone() : null;
        this.indef = indefiniteSupport;
    }

    /**
     * Check the parsed length against the bounds of the complete structure.
     *
     * @param length parsed length.
     * @throws UtilException if length is larger than remaining data in input
     *                       structure.
     */
    private void checkLength(int length) throws UtilException {
        if (offset + length > structure.length)
            throw new UtilException("Invalid TLV structure");
    }

    /**
     * Check if parsing has finished and no more data is left for parsing.
     *
     * @return true if parser has reached the end of data.
     */
    public boolean isFinished() {
        return offset >= structure.length;
    }

    /**
     * Read tag from the structure.
     *
     * @return parsed tag.
     * @throws UtilException if tag format is invalid.
     */
    public int parseTag() throws UtilException {
        checkLength(1);
        int tag = structure[offset] & 0xFF;
        offset++;
        // check if two byte tag
        if ((tag & 0x1F) == 0x1F) {
            checkLength(1);
            tag = (tag << 8) | (structure[offset] & 0xFF);
            offset++;
            // check if three byte tag
            if ((tag & 0x80) != 0) {
                checkLength(1);
                tag = (tag << 8) | (structure[offset] & 0xFF);
                offset++;
            }
        }

        return tag;
    }

    /**
     * Read tag from the structure. DGI formatted data is similar to a
     * simple tlv format but with 2 bytes TAG.
     *
     * @return parsed DGI tag.
     * @throws UtilException if tag format is invalid.
     */
    public int parseDgiTag() throws UtilException {
        checkLength(2);
        int tag = structure[offset] & 0xFF;
        offset++;
        tag = (tag << 8) | (structure[offset] & 0xFF);
        offset++;
        return tag;
    }

    /**
     * Read Dgi length from the structure.
     *
     * @return parsed length of value.
     * @throws UtilException if the length structure is invalid.
     */
    public int parseDgiLength() throws UtilException {
        checkLength(1);
        int length = structure[offset];
        offset++;
        if ((length & 0xFF) == 0xFF) {
            checkLength(2);
            length = structure[offset];
            offset++;
            length <<= 8;
            length |= (structure[offset] & 0xFF);
            offset++;
        }
        return length;
    }

    /**
     * Parse the Dgi value in the given length and extract it as byte array.
     *
     * @param length length of value to parse.
     * @return byte array containing value part.
     * @throws UtilException if more data shall be parsed as available in the
     *                       structure.
     */
    public byte[] parseDgiValue(int length) throws UtilException {
        if (length == 0) {
            return new byte[] {};
        }
        checkLength(length);
        offset += length;
        return Arrays.copyOfRange(structure, offset - length, offset);
    }

    /**
     * Read length from the structure.
     *
     * @return parsed length of value.
     * @throws UtilException if the length structure is invalid.
     */
    public int parseLength() throws UtilException {
        checkLength(1);
        int length = structure[offset];
        offset++;

        switch ((byte) length) {
        case (byte) 0x81: {
            checkLength(1);
            length = (structure[offset] & 0xFF);
            offset++;
        } break;

        case (byte) 0x82: {
            checkLength(2);
            length = (structure[offset] & 0xFF) << 8;
            offset++;
            length |= (structure[offset] & 0xFF);
            offset++;
        } break;

        case (byte) 0x83: {
            checkLength(3);
            length = (structure[offset] & 0xFF) << 16;
            offset++;
            length |= (structure[offset] & 0xFF) << 8;
            offset++;
            length |= (structure[offset] & 0xFF);
            offset++;
        } break;

        case (byte) 0x84: {
            checkLength(4);
            length = (structure[offset] & 0xFF) << 24;
            offset++;
            length |= (structure[offset] & 0xFF) << 16;
            offset++;
            length |= (structure[offset] & 0xFF) << 8;
            offset++;
            length |= (structure[offset] & 0xFF);
            offset++;
        } break;

        case (byte) 0x80:
            if (indef) {
                length = -1;
            }
            break;
        default:
            length &= 0xFF;
        }

        return length;
    }

    /**
     * Parse the value in the given length and extract it as byte array.
     *
     * @param length length of value to parse.
     * @return byte array containing value part.
     * @throws UtilException if more data shall be parsed as available in the
     *                       structure.
     */
    public byte[] parseValue(int length) throws UtilException {
        if (indef && (length < 0)) {
            TlvParser parser =
                    new TlvParser(Arrays.copyOfRange(structure, offset,
                                                     structure.length),
                                  indef);
            byte[] data = new byte[0];
            for (Object o : parser.parseTlvStructure()) {
                data = Utils.concat(data, Utils.toBytes(o));
            }
            offset += parser.offset;
            return data;
        }

        checkLength(length);
        offset += length;
        return Arrays.copyOfRange(structure, offset - length, offset);
    }

    /**
     * Parse a complete TLV structure.
     *
     * @return vector containing all TLV structures contained in the input data.
     * @throws UtilException if parsing fails for any reason.
     */
    public List<Object> parseTlvStructure() throws UtilException {
        ArrayList<Object> values = new ArrayList<>();

        while (!isFinished()) {
            int tag = parseTag();
            int len = parseLength();
            if (indef && (tag == 0) && (len == 0))
                break;

            byte[] value = parseValue(len);
            values.add(new Tlv(tag, getType(tag), value));
        }

        return values;
    }

    /**
     * Parse a complete DGI TLV structure, which is similar to a simple tlv
     * format but with 2 bytes TAG.
     *
     * @return vector containing all TLV structures contained in the input data.
     * @throws UtilException if parsing fails for any reason.
     */
    public List<Object> parseDgiTlvStructure() throws UtilException {
        ArrayList<Object> values = new ArrayList<>();

        while (!isFinished()) {
            int tag = parseDgiTag();
            int len = parseDgiLength();
            if (indef && (tag == 0) && (len == 0))
                break;

            byte[] value = parseDgiValue(len);
            values.add(new Tlv(tag, getType(tag), value));
        }

        return values;
    }

    private static String getType(int tag) {
        switch (tag) {
        case 0x00:
            return EOF;
        case 0x01:
            return BOOLEAN;
        case 0x02:
            return INTEGER;
        case 0x03:
            return BIT_STRING;
        case 0x04:
            return OCTET_STRING;
        case 0x05:
            return NULL;
        case 0x30:
            return SEQUENCE;
        case 0x31:
            return SET;
        default: {
            // do nothing
        }
        }
        return null;
    }
}
