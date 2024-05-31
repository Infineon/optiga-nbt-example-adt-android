// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.utils.NdefConstants;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Abstract well known type record is an abstract class, which represents a
 * parent container class for all NFC Forum well known type records defined in
 * the NFC Forum RTD specification [NFC RTD].
 */
public abstract class AbstractWellKnownTypeRecord extends AbstractRecord {
    /**
     * Constructor to create the NFC Forum well known type record.
     *
     * @param recordType Record type of string (For example, "T").
     */
    protected AbstractWellKnownTypeRecord(@NotNull final String recordType) {
        super(recordType, NdefConstants.TNF_WELL_KNOWN_TYPE);
    }
}
