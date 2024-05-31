// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.records.model.DataReference;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * The NFC Forum well known local type (defined in [NDEF], [RTD]) for the
 * alternative carrier record shall be "ac" (in NFC binary encoding: 0x61, 0x63)
 * The alternative carrier record is used within global handover NDEF records to
 * describe a single alternative carrier. It shall not be used elsewhere.
 */
public class AlternativeCarrierRecord extends AbstractWellKnownTypeRecord {
    /**
     * Constant defines the NFC Forum well known type (defined in [NDEF], [RTD])
     * for the alternative carrier record (in NFC binary encoding: 0x61, 0x63).
     */
    public static final String ALTERNATIVE_CARRIER_RECORD_TYPE = "ac";

    /**
     * Constant defines the carrier power state 'inactive', If the carrier is
     * currently off.
     */
    public static final byte INACTIVE = 0x00;

    /**
     * Mask for cps value of 2-bit.
     */
    public static final byte CPS_MASK = 0x03;

    /**
     * Constant defines the carrier power state 'active', If the carrier is
     * currently on.
     */
    public static final byte ACTIVE = 0x01;

    /**
     * Constant defines the carrier power state 'activating', If the device is
     * in the process of activating the carrier, but the carrier is not yet
     * active.
     */
    public static final byte ACTIVATING = 0x02;

    /**
     * Constant defines the carrier power state 'unknown', the device is only
     * reachable via the carrier through a router and does not directly support
     * an interface for the carrier. .
     */
    public static final byte UNKNOWN = 0x03;

    /**
     * A 2-bit field that indicates the carrier power state (CPS).
     */
    private byte cps;

    /**
     * List of 1-byte pointer to an NDEF record that uniquely identifies the
     * carrier technology.
     */
    private DataReference carrierDataReference;

    /**
     * List of 1-byte pointer to an NDEF record that gives additional
     * information about the alternative carrier. No limitations are imposed on
     * the NDEF payload type being pointed to.
     */
    private final ArrayList<DataReference> auxiliaryDataReferences =
            new ArrayList<>();

    /**
     * Constructor to create a new alternative carrier record.
     *
     * @param cps                  2-bit field that indicates the carrier power
     *                             state.
     * @param carrierDataReference 1-byte pointer to an NDEF record that
     *         uniquely
     *                             identifies the carrier technology.
     */
    public AlternativeCarrierRecord(
            @NotNull byte cps, @NotNull DataReference carrierDataReference) {
        super(ALTERNATIVE_CARRIER_RECORD_TYPE);
        setCps(cps);
        setCarrierDataReference(carrierDataReference);
    }

    /**
     * Gets the carrier power state.
     *
     * @return Returns the carrier power state.
     */
    public byte getCps() {
        return (byte) (this.cps & CPS_MASK);
    }

    /**
     * Sets the carrier power state.
     *
     * @param cps A 2-bit field carrier power state.
     */
    public final void setCps(@NotNull byte cps) {
        this.cps = cps;
    }

    /**
     * Gets the carrier data references.
     *
     * @return Returns the carrier data references.
     */
    public DataReference getCarrierDataReference() {
        return this.carrierDataReference;
    }

    /**
     * Sets the carrier data references.
     *
     * @param carrierDataReference Carrier data references.
     */
    public final void setCarrierDataReference(
            @NotNull DataReference carrierDataReference) {
        this.carrierDataReference = carrierDataReference;
    }

    /**
     * Gets the auxiliary data references.
     *
     * @return Returns the auxiliary data references.
     */
    @SuppressWarnings("unchecked")
    public List<DataReference> getAuxiliaryDataReferences() {
        return (List<DataReference>) this.auxiliaryDataReferences.clone();
    }

    /**
     * Gets the auxiliary data references count.
     *
     * @return Returns the auxiliary data references count.
     */
    public int getAuxiliaryDataReferencesCount() {
        return this.auxiliaryDataReferences.size();
    }

    /**
     * Add new auxiliary data reference to the list.
     *
     * @param auxiliaryDataReference Auxiliary data reference.
     */
    public void addAuxiliaryDataReference(
            @NotNull DataReference auxiliaryDataReference) {
        this.auxiliaryDataReferences.add(auxiliaryDataReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + cps;
        result = prime * result + ((carrierDataReference == null)
                                           ? 0
                                           : carrierDataReference.hashCode());
        result = prime * result + auxiliaryDataReferences.hashCode();
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
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlternativeCarrierRecord other = (AlternativeCarrierRecord) obj;
        if (cps != other.cps)
            return false;
        if (carrierDataReference == null) {
            if (other.carrierDataReference != null)
                return false;
        } else if (!carrierDataReference.equals(other.carrierDataReference))
            return false;
        return (auxiliaryDataReferences.equals(other.auxiliaryDataReferences));
    }
}
