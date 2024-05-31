// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Abstract MIME type record is an abstract class, which represents a parent
 * container class for all media-type records as defined in the RFC 2046 [RFC
 * 2046].
 */
public abstract class AbstractMimeTypeRecord extends AbstractRecord {
    /**
     * Constructor to create the media-type record as defined in the RFC 2046
     * [RFC 2046].
     *
     * @param recordType Record type of string (For example, "T").
     */
    protected AbstractMimeTypeRecord(@NotNull final String recordType) {
        super(recordType, NdefConstants.TNF_MEDIA_TYPE);
    }
}
