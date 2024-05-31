// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.utils.annotation.NotNull;
import java.util.Arrays;

/**
 * The NFC Forum well known local type (defined in [NDEF], [RTD]) for the Error
 * Record shall be "err" (in NFC binary encoding: 0x65, 0x72, 0x72). The Error
 * Record is included in a Handover Select Record to indicate that the handover
 * selector failed to successfully process the most recently received handover
 * request message. This record shall be used only in Handover Select record.
 */
public class ErrorRecord extends AbstractWellKnownTypeRecord {
    /**
     * Defines the NFC Forum well known type (defined in [NDEF], [RTD])
     * for the error record (in NFC binary encoding: 0x65, 0x72, 0x72).
     */
    public static final String ERROR_RECORD_TYPE = "err";

    /**
     * A 1-byte field that indicates the specific type of error that caused the
     * handover selector to return the error record.
     */
    private byte errorReason;

    /**
     * A sequence of octets that provide additional information about the
     * conditions that caused the handover selector to enter an erroneous state.
     */
    private byte[] errorData;

    /**
     * Constructor to create a new error record.
     *
     * @param errorReason A 1-byte field indicates the specific type of error
     *                    that caused the handover selector to return the Error
     *                    record.
     * @param errorData   A sequence of octets provides additional information
     *                    about the conditions that caused the handover selector
     *                    to enter an erroneous state.
     */
    public ErrorRecord(@NotNull byte errorReason, @NotNull byte[] errorData) {
        super(ERROR_RECORD_TYPE);
        setErrorReason(errorReason);
        setErrorData(errorData);
    }

    /**
     * Sets the error reason.
     *
     * @param errorReason The error reason
     */
    public final void setErrorReason(@NotNull byte errorReason) {
        this.errorReason = errorReason;
    }

    /**
     * Sets a sequence of octets that provide additional information
     * about the conditions that caused the handover selector to enter an
     * erroneous state.
     *
     * @param errorData The error data
     */
    public final void setErrorData(@NotNull byte[] errorData) {
        this.errorData = errorData != null ? errorData.clone() : null;
    }

    /**
     * Gets the error reason.
     *
     * @return Returns the error reason.
     */
    public byte getErrorReason() {
        return this.errorReason;
    }

    /**
     * Gets a sequence of octets that provide additional information about
     * the conditions that caused the handover selector to enter an erroneous
     * state.
     *
     * @return Returns the error data.
     */
    public byte[] getErrorData() {
        return this.errorData != null ? errorData.clone() : null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = prime * result + errorReason;
        result = prime * result + Arrays.hashCode(errorData);
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ErrorRecord))
            return false;
        ErrorRecord other = (ErrorRecord) obj;
        if (errorReason != other.errorReason)
            return false;
        if (!Arrays.equals(errorData, other.errorData))
            return false;
        return (super.equals(obj));
    }
}
