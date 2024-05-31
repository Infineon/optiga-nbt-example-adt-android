// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Abstract external type record is an abstract class which represents a parent
 * container class for all NDEF external type records as defined in the NFC
 * Forum record type definition (RTD) specification [NFC RTD]. A NDEF external
 * type record is a user-defined value, based on rules in the NFC Forum record
 * type definition specification. The payload does not need to follow any
 * specific structure like it does in other well known records types.
 */
public abstract class AbstractExternalTypeRecord extends AbstractRecord {
    /**
     * Constructor to create the NFC Forum external type [NFC RTD] record.
     *
     * @param recordType Record type of string (For example, "T").
     */
    protected AbstractExternalTypeRecord(@NotNull final String recordType) {
        super(recordType, NdefConstants.TNF_EXTERNAL_TYPE);
    }
}
