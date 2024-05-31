// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.utils;

import com.infineon.hsw.utils.annotation.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Utility class for string related manipulations.
 */
public final class Utils {
    /** Array containing hex digits for toString conversions of byte arrays */
    private static final char[] HEX_DIGIT = { '0', '1', '2', '3', '4', '5',
                                              '6', '7', '8', '9', 'A', 'B',
                                              'C', 'D', 'E', 'F' };

    /** Default delimiter */
    private static final String SPACE = " ";

    private static final String
            ILLEGAL_CHARACTER = "Illegal character in hex string";

    /** Logger instance of for all library packages */
    private static final Logger logger = Logger.getLogger("com.infineon.hsw");

    /**
     * Private default constructor - prevents instantiation.
     */
    private Utils() {
        // do nothing
    }

    /**
     * Gives single instance of this class
     *
     * @return Logger instance
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Transforms a fraction of a byte array into an ASCII string.
     *
     * @param abyArray array containing ASCII string
     * @param iOffset  offset of string in byte array
     * @param iLength  length of ASCII string
     * @return ASCII string representation of byte array fraction
     */
    public static String toString(byte[] abyArray, int iOffset, int iLength) {
        StringBuilder oDesc = new StringBuilder(iLength);
        oDesc.setLength(iLength);

        for (int i = 0; i < iLength; i++) {
            oDesc.setCharAt(i, (char) abyArray[iOffset]);
            iOffset += 1;
        }

        return oDesc.toString();
    }

    /**
     * Extracts data bytes from a source array at specified offset and length
     *
     * @param data   source array
     * @param offset offset at which the data to be extracted
     * @param length length of data to be extracted
     * @return extracted data bytes
     */

    public static byte[] extractBytes(@NotNull byte[] data, int offset,
                                      int length) {
        byte[] extracted = new byte[length];
        System.arraycopy(data, offset, extracted, 0, length);
        return extracted;
    }

    /**
     * Convert the content of a byte array into a hex string with space between
     * each byte.
     *
     * @param value byte array to be converted.
     * @return hex string representation of byte array.
     */
    public static String toHexString(byte[] value) {
        return toHexString(value, 0, value.length, SPACE);
    }

    /**
     * Convert a byte array into a hex string. The formatting of the string may
     * be influenced by a delimiter string which is inserted before each byte.
     * For the first byte, all delimiter characters before ',', '.', ':', ';' or
     * ' ' are skipped.
     *
     * @param value     byte array to be converted.
     * @param offset    offset of data within byte array
     * @param length    of data to be converted
     * @param delimiter delimiter string like ", " or " ", ", 0x", ":", ",
     *         (byte)0x"
     * @return resulting hex string
     */
    public static String toHexString(byte[] value, int offset, int length,
                                     String delimiter) {
        int i;

        // create string buffer. Size will be length * (2 + delimiter.length())
        StringBuilder strValue = new StringBuilder();

        // go backwards until dedicated delimiter is found
        for (i = delimiter.length(); i > 0; i--) {
            if (isDedicatedDelimiter(delimiter.charAt(i - 1)))
                break;
        }

        if (i < delimiter.length()) {
            // append first delimiter string portion
            strValue.append(delimiter.substring(i));
        }

        // transform byte by byte
        for (i = 0; i < length; i++, offset++) {
            // append hex value
            strValue.append(HEX_DIGIT[(value[offset] >> 4) & 0xF])
                    .append(HEX_DIGIT[value[offset] & 0xF]);

            // append delimiter if not last value
            if (i < (length - 1))
                strValue.append(delimiter);
        }

        // return resulting string
        return strValue.toString();
    }

    /**
     * Utility method to convert a variety of objects into a byte array. The
     * supported
     * reference types are:
     * <ul>
     * <li>null is converted into an empty byte array</li>
     * <li>byte[] is returned unaltered</li>
     * <li>Integer is returned as byte array of 4 bytes with MSB first</li>
     * <li>Short is returned as byte array of 2 bytes with MSB first</li>
     * <li>Byte is returned as byte array of 1 byte</li>
     * <li>hex string is converted into its byte array representation</li>
     * <li>for any other object the <code>toString()</code> method is called and
     * the result is treated as hex string</li>
     * </ul>
     * Hex strings are accepted in various input formats e.g. "ABCDEF", "AB cd
     * EF", "0xab:0xc:0xde", "ab, C, DE". ASCII strings may be included if
     * surrounded by hyphens, e.g 'My String'.
     *
     * @param stream object to be converted into a byte array.
     * @return byte array representation of stream object.
     * @throws UtilException if conversion into a byte array fails.
     */
    public static byte[] toBytes(Object stream) throws UtilException {
        // check if null reference
        if (stream == null)
            return new byte[0];

        // check if already byte array
        if (stream instanceof byte[])
            return (byte[]) stream;

        // check if TLV object
        if (stream instanceof Tlv)
            return ((Tlv) stream).toBytes();

        // check if integer value
        if (stream instanceof Integer)
            return toBytes((Integer) stream, 4);

        if (stream instanceof Double)
            return toBytes(((Double) stream).intValue(), 4);

        // check if short value
        if (stream instanceof Short)
            return toBytes(((Short) stream).intValue(), 2);

        // check if byte value
        if (stream instanceof Byte)
            return toBytes(((Byte) stream).intValue(), 1);

        // check if String
        if (stream instanceof String)
            return toByteArray((String) stream);

        // try to convert object into string and from there to byte array
        return toByteArray(stream.toString());
    }

    /**
     * Helper function to convert a primitive value into a byte array.
     *
     * @param value  integer value to be converted.
     * @param length length of resulting array.
     * @return byte array representation of integer value (MSB first).
     */
    public static byte[] toBytes(int value, int length) {
        byte[] abValue = new byte[length];

        while (length > 0) {
            length -= 1;
            abValue[length] = (byte) value;
            value >>= 8;
        }

        return abValue;
    }
    public static byte[] toByteArrayFromString(String data)
            throws UtilException {
        return toByteArray(data);
    }

    /**
     * Converts a hex string into a byte array. The hex string may have various
     * formats
     * e.g. "ABCDEF", "AB cd EF", "0xab:0xc:0xde", "ab, C, DE". ASCII strings
     * may be included if surrounded by hyphens, e.g 'My String'.
     *
     * @param data hex string to be converted
     * @return byte array with converted hex string
     * @throws UtilException if conversion fails for syntactical reasons.
     */
    private static byte[] toByteArray(String data) throws UtilException {
        int i;
        int iOffset;
        int iLength = data.length();
        byte[] abyValue = new byte[iLength];
        boolean bOddNibbleCountAllowed = false;

        for (i = 0, iOffset = 0; i < iLength; i++) {
            char c = data.charAt(i);
            int iValue = -1;

            if ((c >= '0') && (c <= '9')) {
                iValue = c - '0';
            } else if ((c >= 'A') && (c <= 'F')) {
                iValue = c - 'A' + 10;
            } else if ((c >= 'a') && (c <= 'f')) {
                iValue = c - 'a' + 10;
            } else if (((c == 'x') || (c == 'X')) && ((iOffset & 1) == 1)) {
                if (abyValue[iOffset >> 1] == 0) {
                    bOddNibbleCountAllowed = true;

                    // ignore 0x..
                    iOffset--;
                } else {
                    // x but not 0x found
                    throw new UtilException(ILLEGAL_CHARACTER);
                }
            } else if (c >= 'A') {
                // character cannot be delimiter
                throw new UtilException(ILLEGAL_CHARACTER);
            } else if (c == '\'') {
                // read ASCII values
                for (i++; i < iLength; i++) {
                    c = data.charAt(i);
                    if (c == '\'')
                        break;

                    abyValue[iOffset >> 1] = (byte) c;
                    iOffset += 2;
                }

                if (((iOffset & 1) != 0) || (c != '\'')) {
                    // character cannot be start of ASCII string
                    throw new UtilException(ILLEGAL_CHARACTER);
                }
            } else if ((iOffset & 1) == 1) {
                if (!bOddNibbleCountAllowed && isDedicatedDelimiter(c))
                    bOddNibbleCountAllowed = true;

                if (bOddNibbleCountAllowed) {
                    // delimiter found, so just one nibble specified (e.g.
                    // 0xA:0xB...)
                    iOffset++;
                }
            }

            if (iValue >= 0) {
                abyValue[iOffset >> 1] = (byte) ((abyValue[iOffset >> 1] << 4) |
                                                 iValue);
                iOffset++;
            }
        }

        if (!bOddNibbleCountAllowed && ((iOffset & 1) != 0)) {
            throw new UtilException("Hex string has odd nibble count");
        }

        // calculate length of stream
        iLength = (iOffset + 1) >> 1;

        return Arrays.copyOf(abyValue, iLength);
    }

    /**
     * Check if character is a dedicated delimiter character
     *
     * @param c character to be checked.
     * @return true if dedicated delimiter character
     */
    private static boolean isDedicatedDelimiter(char c) {
        switch (c) {
        case ',':
        case '.':
        case ':':
        case ';':
            return true;
        default: {
            // do nothing
        }
        }

        return (c <= ' ');
    }

    /**
     * Gives The length of the data value in TLV
     *
     * @param currentIndex  offset at which length coding starts
     * @param data          bytes in which this offset comes
     * @param x80forLength80 true if any length &gt;= 0x80 is encoded by
     *         additional byte '0x80' in given TLV
     * @return length of the data value
     */
    public static int getTLVLengthAt(int currentIndex, byte[] data,
                                     boolean x80forLength80) {
        if (data[currentIndex] == (byte) 0x80) {
            if (x80forLength80)
                currentIndex++;
            return byteToInt(data[currentIndex]);
        } else if (data[currentIndex] == (byte) 0x81) {
            currentIndex++;
            return byteToInt(data[currentIndex]);
        } else if (data[currentIndex] == (byte) 0x82) {
            currentIndex++;
            BigInteger bgInt = toBigInteger(
                    extractBytes(data, currentIndex, 2));
            return bgInt.intValue();
        } else if (data[currentIndex] == (byte) 0x83) {
            currentIndex++;
            BigInteger bgInt = toBigInteger(
                    extractBytes(data, currentIndex, 4));
            return bgInt.intValue();
        } else
            return byteToInt(data[currentIndex]);
    }

    /**
     * Gives total number of bytes the length is coded in a TLV
     *
     * @param currentIndex  offset at which the length byte(s) start
     * @param data          offset at which length coding starts
     * @param x80forLength80 true if any length &gt;= 0x80 is encoded by
     *         additional byte '0x80' in given TLV
     * @return number of bytes the length is coded including the qualifiers
     */
    public static int getTLVLengthSize(int currentIndex, byte[] data,
                                       boolean x80forLength80) {
        if (data[currentIndex] == (byte) 0x80) {
            if (x80forLength80)
                return 2;
            else
                return 1;
        } else if (data[currentIndex] == (byte) 0x81)
            return 2;
        else if (data[currentIndex] == (byte) 0x82)
            return 3;
        else if (data[currentIndex] == (byte) 0x83)
            return 5;
        else
            return 1;
    }

    /**
     * Converts a byte to integer
     *
     * @param i byte value
     * @return integer
     */
    public static int byteToInt(byte i) {
        if (i < 0)
            return (i + 256);
        else
            return (i);
    }

    /**
     * Extracts the Data from a LV or TLV
     *
     * @param data           the data bytes
     * @param offset         offset at which length starts
     * @param x80forLength80 true if 80 allowed
     * @return the extracted data value
     */
    public static byte[] getDataWithLength(byte[] data, int offset,
                                           boolean x80forLength80) {
        int lengthSize = getTLVLengthSize(offset, data, x80forLength80);
        offset = offset + lengthSize;
        int length = getTLVLengthAt(offset, data, x80forLength80);
        byte[] outData = new byte[length];
        System.arraycopy(data, offset, outData, 0, length);
        return outData;
    }

    /**
     * Returns byte[] format for given int. Only minimum number of required
     * bytes returned
     *
     * @param intValue integer value
     * @return byte array
     */
    public static byte[] getBytes(int intValue) {
        byte[] abArray;
        if ((intValue & 0xFF) == intValue)
            abArray = Utils.toBytes(intValue, 1);
        else if ((intValue & 0xFFFF) == intValue)
            abArray = Utils.toBytes(intValue, 2);
        else if ((intValue & 0xFFFFFF) == intValue)
            abArray = Utils.toBytes(intValue, 3);
        else
            abArray = Utils.toBytes(intValue, 4);
        return abArray;
    }

    /**
     * Concatenate the content of two arrays and return resulting array.
     *
     * @param array1 first array
     * @param array2 second array
     * @return resulting array which is the concatenation of array1 and array2.
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);

        return result;
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
     * Converts an unsigned integer in a byte array into a BigInteger object.
     *
     * @param unsignedValue byte array containing unsigned integer (big endian).
     * @return reference of BigInteger object.
     */
    public static BigInteger toBigInteger(byte[] unsignedValue) {
        if (unsignedValue[0] < 0) {
            byte[] signedValue = new byte[unsignedValue.length + 1];
            System.arraycopy(unsignedValue, 0, signedValue, 1,
                             unsignedValue.length);
            unsignedValue = signedValue;
        }

        return new BigInteger(unsignedValue);
    }

    /**
     * Converts given hex string into integer
     *
     * @param value any value
     * @return integer
     * @throws UtilException If there is utility related failure
     */
    public static int toInteger(Object value) throws UtilException {
        if (value instanceof Integer)
            return (Integer) value;
        else if (value instanceof Double)
            return ((Double) value).intValue();
        else {
            byte[] bytes = toBytes(value);
            byte[] bts = new byte[4];
            if (bytes.length < 4)
                System.arraycopy(bytes, 0, bts, bts.length - bytes.length,
                                 bytes.length);
            else
                bts = bytes;
            return getIntFromArray(bts, 0);
        }
    }

    /**
     * Creates new sub array adjusting the data length appropriately
     *
     * @param data       source byte array
     * @param dataOffset offset within the source array
     * @param dataLength data length to be considered for creating sub array
     * @return sub array of given original byte array
     */
    public static byte[] subArray(byte[] data, int dataOffset, int dataLength) {
        checkArrayBounds(data, dataOffset, dataLength);
        Objects.requireNonNull(data);

        if (dataOffset + dataLength > data.length) {
            dataLength = data.length - dataOffset;
        }

        byte[] apdu = new byte[dataLength];
        System.arraycopy(data, dataOffset, apdu, 0, dataLength);
        return apdu;
    }

    private static void checkArrayBounds(byte[] b, int ofs, int len) {
        if ((ofs < 0) || (len < 0)) {
            throw new IllegalArgumentException(
                    "Offset and length must not be negative");
        }
        if (b == null && ((ofs != 0) && (len != 0))) {
            throw new IllegalArgumentException(
                    "offset and length must be 0 if array is null");
        }
    }

    /**
     * Extracts integer from given byte array
     *
     * @param paramArrayOfByte byte array from which the integer has to be
     *         extracted
     * @param paramInt         offset within the byte array at which the int has
     *         to be extracted
     * @return extracted integer
     */
    public static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return paramArrayOfByte[paramInt] << 24 & 0xFF000000 |
                paramArrayOfByte[(paramInt + 1)] << 16 & 0xFF0000 |
                paramArrayOfByte[(paramInt + 2)] << 8 & 0xFF00 |
                paramArrayOfByte[(paramInt + 3)] & 0xFF;
    }

    /**
     *
     * @param value  source value to be set
     * @param b      byte destination array
     * @param offset offset
     * @param len    length
     */
    public static void setIntLittleEndian(int value, byte[] b, int offset,
                                          int len) {
        for (int n = offset; n < offset + len; n++) {
            b[n] = (byte) value;
            value >>= 8;
        }
    }

    /**
     * @param b      source array
     * @param offset offset
     * @param len    length
     * @return value of int
     */
    public static int getIntLittleEndian(byte[] b, int offset, int len) {
        int i = 0;
        for (int n = offset + len - 1; n >= offset; n--) {
            i <<= 8;
            i |= (0xff & b[n]);
        }
        return i;
    }

    /**
     * Gives a boolean map of bits within a byte array.
     *
     * @param bytes byte array
     * @return boolean array. true for 1
     */
    public static boolean[] getBitsMap(byte[] bytes) {
        boolean[] bits = new boolean[bytes.length * 8];
        int j = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            for (int b = 0; b < 8; b++) {
                if (((1 << b) & (bytes[i] & 0xFF)) > 0)
                    bits[j] = true;
                j++;
            }
        }
        return bits;
    }

    /**
     * Gives 2 byte integer
     *
     * @param data   bytes
     * @param offset offset
     * @return 2 byte integer
     */
    public static int getUINT16(byte[] data, int offset) {
        return ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
    }

    /**
     * Gives 1 byte integer
     *
     * @param data   bytes
     * @param offset offset
     * @return 1 byte integer
     */
    public static int getUINT8(byte[] data, int offset) {
        return data[offset] & 0xFF;
    }

    /**
     * Computes 16-bits CRC over given input byte array. Eg: as used in
     * ISO-14443-3 Type-A CRC.
     *
     * @param initValue  Initial value to be considered
     * @param inputData  Input byte stream
     * @param invertBits true if the bits in resulting bytes to be reverted
     * @return CRC bytes
     * @throws UtilException throws exception in case of input data in null
     */
    public static byte[] computeCRC(short initValue, byte[] inputData,
                                    boolean invertBits) throws UtilException {
        if (inputData == null) {
            throw new UtilException("input data cannot be null");
        }
        byte curByte;
        byte[] pDat = inputData.clone();
        int len = inputData.length;
        short crc = initValue;
        int pDatInc = 0;
        byte first;
        byte second;
        if (invertBits) {
            while (len > 0) {
                curByte = reverseBitsOrder(pDat[pDatInc]);
                pDatInc++;
                crc = update(curByte, crc);
                len--;
            }

            first = reverseBitsOrder((byte) (crc & 0xFF));
            second = reverseBitsOrder((byte) ((crc >> 8) & 0xFF));
        } else {
            while (len > 0) {
                curByte = pDat[pDatInc];
                pDatInc++;
                crc = update(curByte, crc);
                len--;
            }

            first = (byte) (crc & 0xFF);
            second = (byte) ((crc >> 8) & 0xFF);
        }
        // for SAM Key Files Attributes
        return new byte[] { second, first };
    }

    private static short update(int dataByte, int crc) {
        dataByte = (dataByte ^ (crc & 0x00FF));
        dataByte = dataByte & 0x00FF;
        dataByte = (dataByte ^ (dataByte << 4));
        dataByte = dataByte & 0x00FF;
        crc = crc & 0x0000FFFF;
        crc = ((crc >> 8) ^ (dataByte << 8) ^ (dataByte << 3) ^
               (dataByte >> 4));
        crc = crc & 0xFFFF;
        return (short) crc;
    }

    /**
     * Reverses given byte's bits and gives resulting byte value
     *
     * @param ch byte for which the bits to be reverted
     * @return resulting byte
     */
    public static byte reverseBitsOrder(byte ch) {
        // java way to reverse the bits in the byte
        return (byte) (Integer.reverse(ch) >>> (Integer.SIZE - Byte.SIZE));
    }

    /**
     * Copies source array to destination array from specified offsets and
     * length.
     *
     * @param src        Source array
     * @param srcOffset  Source offset
     * @param dest       destination array
     * @param destOffset destination offset
     * @param length     length of bytes to copy
     * @return The next offset within destination array after copying
     */
    public static int arrayCopy(byte[] src, int srcOffset, byte[] dest,
                                int destOffset, int length) {
        System.arraycopy(src, srcOffset, dest, destOffset, length);
        return destOffset + length;
    }

    /**
     *
     * @param length : required length of byte array.
     * @param stream   : source of byte stream.
     * @return byte array.
     * @throws UtilException throws util exception in case of IO exception
     */

    public static byte[] getBytesFromStream(int length, InputStream stream)
            throws UtilException {
        try {
            byte[] bytes = new byte[length];
            stream.read(bytes, 0, bytes.length);
            return bytes;
        } catch (IOException e) {
            throw new UtilException("IO exception occurred", e);
        }
    }
}