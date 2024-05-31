// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.records.model.DataTypes;
import com.infineon.hsw.ndef.records.model.EirData;
import com.infineon.hsw.utils.annotation.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The NFC Local Type Name for the action is 'application/vnd.bluetooth.ep.oob'.
 * Bluetooth carrier configuration record is a record that stores the bluetooth
 * secure simple pairing OOB data that can be exchanged in connection handover
 * request and/or select messages as alternative carrier information.
 */
public class BluetoothRecord extends AbstractMimeTypeRecord {
    /**
     * Defines the MIME type (defined in [NDEF], [RTD]) for the Bluetooth
     * carrier configuration record.
     */
    public static final String
            BLUETOOTH_TYPE = "application/vnd.bluetooth.ep.oob";

    /**
     * Illegal argument exception message if LE bluetooth device address is
     * null.
     */
    protected static final String ERR_MESSAGE_DEVICE_ADDRESS =
            "Bluetooth device address should not be null";

    /**
     * The 6 octets Bluetooth device address is encoded in Little Endian order.
     */
    private byte[] address;

    /**
     * The local name is the user-friendly name presented over Bluetooth
     * technology.
     */
    private EirData name;

    /**
     * The simple pairing hash C.
     */
    private EirData simplePairingHash;

    /**
     * The simple pairing randomizer R.
     */
    private EirData simplePairingRandomizer;

    /**
     * Service class UUID. Service class information is used to identify the
     * supported Bluetooth services of the device.
     */
    private EirData serviceClassUUIDs;

    /**
     * The class of device information is to be used to provide a graphical
     * representation to the user as part of UI involving operations with
     * Bluetooth devices. For example, it may provide a particular icon to
     * present the device.
     */
    private EirData deviceClass;

    /**
     * There are a number of EIR data types defined by the Bluetooth SIG. List
     * of OOB optional data other than appropriate for the Connection Handover
     * scenario extended inquiry response (EIR) data.
     */
    private final ArrayList<EirData> otherEIRs = new ArrayList<>();

    /**
     * Constructor to create a new Bluetooth Carrier Configuration record with
     * bluetooth device address bytes.
     *
     * @param deviceAddress Bluetooth device address bytes
     */
    public BluetoothRecord(@NotNull final byte[] deviceAddress) {
        super(BLUETOOTH_TYPE);
        if (deviceAddress == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
        this.address = deviceAddress.clone();
    }

    /**
     * Gets the Bluetooth device address bytes.
     *
     * @return Returns the Bluetooth device address bytes.
     */
    public final byte[] getAddress() {
        if (this.address == null) {
            return new byte[0];
        }
        return this.address.clone();
    }

    /**
     * Sets the Bluetooth device address.
     *
     * @param deviceAddress Bluetooth device address
     */
    public final void setAddress(@NotNull final byte[] deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
        this.address = deviceAddress.clone();
    }

    /**
     * Gets the Bluetooth device UTF_8 decoded local name.
     *
     * @return Returns the Bluetooth device local name.
     */
    public final String getNameString() {
        if (this.name == null || this.name.getData() == null) {
            return null;
        }
        return new String(this.name.getData(), StandardCharsets.UTF_8);
    }

    /**
     * Gets the Bluetooth device local name.
     *
     * @return Returns the Bluetooth device local name.
     */
    public final EirData getName() {
        return this.name;
    }

    /**
     * Sets the Bluetooth device local name.
     *
     * @param dataTypes The EIR data field defines Shortened or complete
     * 					Bluetooth local Name.
     * @param localName Bluetooth device local name
     */
    public final void setName(@NotNull final DataTypes dataTypes,
                              @NotNull final String localName) {
        if (localName == null) {
            this.name = null;
            return;
        }
        this.name = new EirData(dataTypes,
                                localName.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gets the simple pairing hash bytes.
     *
     * @return Returns the simple pairing hash bytes.
     * .
     */
    public final byte[] getSimplePairingHashBytes() {
        if (this.simplePairingHash == null) {
            return new byte[0];
        }
        return this.simplePairingHash.getData();
    }

    /**
     * Gets the simple pairing hash.
     *
     * @return Returns the simple pairing hash.
     */
    public final EirData getSimplePairingHash() {
        return this.simplePairingHash;
    }

    /**
     * Sets the simple pairing hash.
     *
     * @param dataTypes              The EIR data field defines the simple
     * 								 pairing hash
     * type.
     * @param simplePairingHashBytes Simple pairing hash
     */
    public final void setSimplePairingHash(
            @NotNull final DataTypes dataTypes,
            final byte[] simplePairingHashBytes) {
        if (simplePairingHashBytes == null) {
            this.simplePairingHash = null;
            return;
        }
        this.simplePairingHash = new EirData(dataTypes, simplePairingHashBytes);
    }

    /**
     * Gets the simple pairing randomizer bytes.
     *
     * @return Returns the simple pairing randomizer bytes.
     */
    public final byte[] getSimplePairingRandomizerBytes() {
        if (this.simplePairingRandomizer == null) {
            return new byte[0];
        }
        return this.simplePairingRandomizer.getData();
    }

    /**
     * Gets the simple pairing randomizer.
     *
     * @return Returns the simple pairing randomizer.
     */
    public final EirData getSimplePairingRandomizer() {
        return this.simplePairingRandomizer;
    }

    /**
     * Sets the simple pairing randomizer.
     *
     * @param dataTypes                    The EIR data field defines simple
     *                                     pairing hash type.
     * @param simplePairingRandomizerBytes Simple pairing randomizer
     */
    public final void setSimplePairingRandomizer(
            @NotNull DataTypes dataTypes,
            @NotNull final byte[] simplePairingRandomizerBytes) {
        if (simplePairingRandomizerBytes == null) {
            this.simplePairingRandomizer = null;
            return;
        }
        this.simplePairingRandomizer =
                new EirData(dataTypes, simplePairingRandomizerBytes);
    }

    /**
     * Gets the service class UUIDs.
     *
     * @return Returns the service class UUID bytes.
     */
    public final EirData getServiceClassUUIDs() {
        return this.serviceClassUUIDs;
    }

    /**
     * Sets the service class UUIDs.
     *
     * @param serviceClassUUIDBytes Service class UUIDs
     * @param dataTypes             The EIR data field service class UUID
     *         encoding
     *                              type
     */
    public final void setServiceClassUUIDs(
            @NotNull DataTypes dataTypes,
            @NotNull final byte[] serviceClassUUIDBytes) {
        if (serviceClassUUIDBytes == null) {
            this.serviceClassUUIDs = null;
            return;
        }
        this.serviceClassUUIDs = new EirData(dataTypes, serviceClassUUIDBytes);
    }

    /**
     * Gets the device class bytes.
     *
     * @return Returns the device class bytes.
     */
    public final byte[] getDeviceClassBytes() {
        if (this.deviceClass == null) {
            return new byte[0];
        }
        return this.deviceClass.getData();
    }

    /**
     * Gets the device class.
     *
     * @return Returns the service class device class.
     */
    public final EirData getDeviceClass() {
        return this.deviceClass;
    }

    /**
     * Sets the device class bytes.
     *
     * @param deviceClassBytes Device class bytes
     */
    public final void setDeviceClass(@NotNull final byte[] deviceClassBytes) {
        if (deviceClassBytes == null) {
            this.deviceClass = null;
            return;
        }
        this.deviceClass = new EirData(DataTypes.DEVICE_CLASS,
                                       deviceClassBytes);
    }

    /**
     * Gets the List of other optional EIR data.
     *
     * @return Returns the optional EIR data list.
     */
    public final EirData[] getOtherEIRList() {
        return this.otherEIRs.toArray(new EirData[0]);
    }

    /**
     * Adds the other optional EIR data.
     *
     * @param optionalEIR Optional EIR data.
     */
    public final void addOtherEIResponseList(
            @NotNull final EirData optionalEIR) {
        this.otherEIRs.add(optionalEIR);
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
        result = prime * result + Arrays.hashCode(address);
        if (name != null) {
            result = prime * result + name.hashCode();
        }
        if (simplePairingHash != null) {
            result = prime * result + simplePairingHash.hashCode();
        }
        if (simplePairingRandomizer != null) {
            result = prime * result + simplePairingRandomizer.hashCode();
        }
        if (serviceClassUUIDs != null) {
            result = prime * result + serviceClassUUIDs.hashCode();
        }
        if (deviceClass != null) {
            result = prime * result + deviceClass.hashCode();
        }
        result = prime * result + otherEIRs.hashCode();
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
        if (!(obj instanceof BluetoothRecord)) {
            return false;
        }

        BluetoothRecord other = (BluetoothRecord) obj;
        if (!Arrays.equals(address, other.address))
            return false;
        else if (!name.equals(other.name))
            return false;
        else if (!simplePairingHash.equals(other.simplePairingHash))
            return false;
        else if (!simplePairingRandomizer.equals(other.simplePairingRandomizer))
            return false;
        else if (!serviceClassUUIDs.equals(other.serviceClassUUIDs))
            return false;
        else if (!deviceClass.equals(other.deviceClass)) {
            return false;
        } else if (!otherEIRs.equals(other.otherEIRs)) {
            return false;
        }
        return (super.equals(obj));
    }
}
