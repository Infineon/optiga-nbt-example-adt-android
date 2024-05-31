// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Consists of all the constants required for the NFC data exchange format
 * (NDEF) use cases.
 */
public final class NdefConstants {
    /**
     * The message begin (MB) flag is a 1-bit field that when set indicates the
     * start of an NDEF message.
     */
    public static final int MB = 0x80;

    /**
     * The message end (ME) flag is a 1-bit field that when set indicates the
     * end of an NDEF message. Note that in case of a chunked payload, the ME
     * flag is set only in the terminating record chunk of that chunked payload.
     */
    public static final int ME = 0x40;

    /**
     * The chunk flag (CF) is a 1-bit field indicating that this is either the
     * first record chunk or a middle record chunk of a chunked payload.
     */
    public static final int CF = 0x20;

    /**
     * The short record (SR) flag is a 1-bit field indicating that the
     * PAYLOAD_LENGTH field is a single octet. This short record layout is
     * intended for compact encapsulation of small payloads which will fit
     * within PAYLOAD fields of size ranging between 0 to 255 octets.
     */
    public static final int SR = 0x10;

    /**
     * The ID length (IL) flag is a 1-bit field indicating that the ID_LENGTH
     * field is present in the header as a single octet. If the IL flag is zero,
     * the ID_LENGTH field is omitted from the record header and the ID field
     * is also omitted from the record.
     */
    public static final int IL = 0x08;
    /**
     * Type name format (TNF) mask. The TNF field value indicates that the
     * structure of the value of the type field.
     */
    public static final int TNF_MASK = 0x07;
    /**
     * An empty TNF byte data.
     */
    public static final byte TNF_EMPTY = 0;

    /**
     * An unchanged TNF is 6-byte data.
     */
    public static final byte TNF_UNCHANGED = 6;
    /**
     * NFC Forum well known type [NFC RTD] is 0x01.
     */
    public static final byte TNF_WELL_KNOWN_TYPE = 1;

    /**
     * Media-type RFC 2046 is 0x02.
     */
    public static final byte TNF_MEDIA_TYPE = 2;

    /**
     * NFC Forum external record type [NFC RTD] is 0x04.
     */
    public static final byte TNF_EXTERNAL_TYPE = 4;

    /**
     * Well known UTF type of "UTF-8" charset for the record creation.
     */
    public static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;

    /**
     * Well known default "US-ASCII" charset for the record creation.
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;

    /**
     * Maximum length of an NDEF message can be 2 bytes.
     */
    public static final int NDEF_MESSAGE_LENGTH_LIMIT = 0x02;

    /**
     * Private constructor to restrict object creation.
     */
    private NdefConstants() {
    }
}
