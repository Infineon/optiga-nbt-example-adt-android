// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.utils.annotation.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * The NFC local type name for the action is 'Hs' (0x48, 0x73). Handover select
 * record is a record that stores the alternative carriers that the Handover
 * Selector selected from the list provided in the previous Handover Request
 * Message.
 */
public class HandoverSelectRecord extends AbstractWellKnownTypeRecord {
    /**
     * Defines the NFC Forum Well Known Type (defined in [NDEF], [RTD])
     * for the handover select record (in NFC binary encoding: 0x48, 0x73).
     */
    public static final String HANDOVER_SELECT_TYPE = "Hs";

    /**
     * Major version number of the connection handover specification that
     *conforms to specification version 1.5.
     **/
    public static final byte MAJOR_VERSION = 0x01;

    /**
     * Minor version number of the connection handover specification that
     * conforms to specification version 1.5.
     */
    public static final byte MINOR_VERSION = 0x05;

    /**
     * A 4-bit field contains the major version number of the connection
     * handover specification.
     */
    private byte majorVersion;

    /**
     * A 4-bit field contains the minor version number of the connection
     * handover specification.
     */
    private byte minorVersion;

    /**
     * List of the handover carrier record provides a unique identification of
     * an alternative carrier technology.
     */
    private List<AlternativeCarrierRecord> alternativeCarrierRecords =
            new ArrayList<>();

    /**
     * The optional error record to indicate that the handover selector failed
     * to successfully process the most recently received handover request
     * message.
     */
    private ErrorRecord errorRecord;

    /**
     * Constructor to create a new handover select record with default major and
     * minor version number of the connection handover specification.
     */
    public HandoverSelectRecord() {
        super(HANDOVER_SELECT_TYPE);
        setMinorVersion(MINOR_VERSION);
        setMajorVersion(MAJOR_VERSION);
        setErrorRecord(errorRecord);
    }

    /**
     * Constructor to create a new handover select record with default major and
     * minor version number of the connection handover specification.
     *
     * @param errorRecord The optional error record
     */
    public HandoverSelectRecord(@NotNull ErrorRecord errorRecord) {
        super(HANDOVER_SELECT_TYPE);
        setMinorVersion(MINOR_VERSION);
        setMajorVersion(MAJOR_VERSION);
        setErrorRecord(errorRecord);
    }

    /**
     * Gets the major version of the connection handover
     * specification.
     *
     * @return Returns the major version of the connection handover
     *         specification.
     */
    public byte getMajorVersion() {
        return this.majorVersion;
    }

    /**
     * Sets the major version of the connection handover
     * specification.
     *
     * @param majorVersion Major version of the connection handover
     *         specification.
     */
    public final void setMajorVersion(@NotNull byte majorVersion) {
        this.majorVersion = majorVersion;
    }

    /**
     * Gets the minor version of the connection handover specification.
     *
     * @return Returns the minor version of the connection handover
     *         specification.
     */
    public byte getMinorVersion() {
        return this.minorVersion;
    }

    /**
     * Sets the minor version of the connection handover specification.
     *
     * @param minorVersion Minor version of the connection handover
     *         specification.
     */
    public final void setMinorVersion(@NotNull byte minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * Gets the list of alternative carrier records.
     *
     * @return Returns the list of alternative carrier records.
     */
    public List<AlternativeCarrierRecord> getAlternativeCarrierRecords() {
        return new ArrayList<>(this.alternativeCarrierRecords);
    }

    /**
     * Sets the list of alternative carrier records.
     *
     * @param alternativeCarrierRecords The list of alternative carrier records.
     */
    public final void setAlternativeCarrierRecords(
            @NotNull List<AlternativeCarrierRecord> alternativeCarrierRecords) {
        this.alternativeCarrierRecords = alternativeCarrierRecords;
    }

    /**
     * Adds the alternative carrier record.
     *
     * @param alternativeCarrierRecord The alternative carrier record.
     */
    public void addAlternativeCarrierRecord(
            @NotNull AlternativeCarrierRecord alternativeCarrierRecord) {
        this.alternativeCarrierRecords.add(alternativeCarrierRecord);
    }

    /**
     * Gets the error record (optional).
     *
     * @return Returns the optional error record.
     */
    public ErrorRecord getErrorRecord() {
        return this.errorRecord;
    }

    /**
     * Sets the error record (optional).
     *
     * @param errorRecord The optional error record
     */
    public final void setErrorRecord(@NotNull ErrorRecord errorRecord) {
        this.errorRecord = errorRecord;
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
        result = prime * result + majorVersion;
        result = prime * result + minorVersion;
        result = prime * result +
                 ((alternativeCarrierRecords == null)
                          ? 0
                          : alternativeCarrierRecords.hashCode());
        result = prime * result +
                 ((errorRecord == null) ? 0 : errorRecord.hashCode());
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

        if (!(obj instanceof HandoverSelectRecord))
            return false;
        HandoverSelectRecord other = (HandoverSelectRecord) obj;
        if (majorVersion != other.majorVersion)
            return false;
        if (minorVersion != other.minorVersion)
            return false;
        if (alternativeCarrierRecords == null) {
            if (other.alternativeCarrierRecords != null)
                return false;
        } else if (!alternativeCarrierRecords.equals(
                           other.alternativeCarrierRecords))
            return false;
        if (errorRecord == null) {
            return (other.errorRecord == null);
        } else if (!errorRecord.equals(other.errorRecord))
            return false;
        return (super.equals(obj));
    }
}
