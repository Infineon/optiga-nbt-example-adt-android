// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.records.model.AdData;
import com.infineon.hsw.ndef.records.model.AppearanceCategory;
import com.infineon.hsw.ndef.records.model.BluetoothLeDeviceAddressType;
import com.infineon.hsw.ndef.records.model.DataTypes;
import com.infineon.hsw.ndef.records.model.LeRole;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * The NFC local type name for the action is 'application/vnd.bluetooth.le.oob'.
 * Bluetooth LE carrier configuration record is a record that stores the
 * Bluetooth LE security manager OOB required data types that can be exchanged
 * in connection handover request and/or select messages as alternative carrier
 * information.
 */
public class BluetoothLeRecord extends AbstractMimeTypeRecord {
    /**
     * Constant defines the length of the BLE address.
     */
    private static final int DEVICE_ADDRESS_LENGTH = 7;
    /**
     * Constant defines the offset of the BLE address type.
     */
    private static final int DEVICE_ADDRESS_TYPE_OFFSET = 6;

    /**
     * Illegal argument exception message if LE Bluetooth device address is
     * null.
     */
    protected static final String ERR_MESSAGE_DEVICE_ADDRESS =
            "LE bluetooth device address should not be null";

    /**
     * Illegal argument exception message if LE Bluetooth device address is
     * null.
     */
    protected static final String ERR_MESSAGE_DEVICE_ADDRESS_LENGTH =
            "LE bluetooth device address should be 7 bytes including 1 byte "
            + "address type (Refer section 1.16. of BLUETOOTH_CSS spec)";

    /**
     * Illegal argument exception message if LE Bluetooth device roll is null.
     */
    protected static final String
            ERR_MESSAGE_ROLE = "LE bluetooth device role should not be null";

    /**
     * Constant defines the Media-type as defined in RFC 2046 [RFC 2046] 0x02
     * type (defined in [NDEF], [RTD]) for the Bluetooth LE carrier
     * configuration record.
     */
    public static final String BLE_TYPE = "application/vnd.bluetooth.le.oob";

    /**
     * The 7-octets LE Bluetooth device address encoded in Little Endian order.
     */
    private AdData address;

    /**
     * The LE role data type.
     */
    private AdData role;

    /**
     * The security manager TK value.
     */
    private AdData securityManagerTKValue;

    /**
     * The appearance data type. The appearance characteristic defines the
     * representation of the external appearance of the device. For example, a
     * mouse, generic remote control, or keyboard.
     */
    private AdData appearance;

    /**
     * Flags data type contain information on which discoverable mode to use and
     * BR/EDR support and capability.
     */
    private AdData flags;

    /**
     * The local name is the user-friendly name presented over Bluetooth
     * technology.
     */
    private AdData name;

    /**
     * List of other optional advertising and scan response (AD) format.
     */
    private final ArrayList<AdData> optionalAdList = new ArrayList<>();

    /**
     * Constructor to create a new Bluetooth LE carrier configuration record.
     */
    public BluetoothLeRecord() {
        super(BLE_TYPE);
    }

    /**
     * Constructor to create a new Bluetooth LE Carrier Configuration record
     * with Bluetooth device address bytes.
     *
     * @param deviceAddress 7-octets LE Bluetooth device address including
     *         address type encoded in Little Endian order
     * @param leRole        LE role data type
     */
    public BluetoothLeRecord(@NotNull final byte[] deviceAddress,
                             @NotNull final LeRole leRole) {
        super(BLE_TYPE);
        if (deviceAddress == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
        if (deviceAddress.length != DEVICE_ADDRESS_LENGTH) {
            throw new IllegalArgumentException(
                    ERR_MESSAGE_DEVICE_ADDRESS_LENGTH);
        }
        if (leRole == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_ROLE);
        }
        this.address = new AdData(DataTypes.LE_BLUETOOTH_DEVICE_ADDRESS,
                                  deviceAddress);
        this.role = new AdData(DataTypes.LE_BLUETOOTH_ROLE,
                               new byte[] { leRole.getValue() });
    }

    /**
     * Constructor to create a new Bluetooth LE carrier configuration record
     * with bluetooth device address bytes.
     *
     * @param deviceAddress 6-octets LE Bluetooth device address excluding
     *         address type encoded in Little Endian order.
     * @param addressType   Enumeration represent LE Address type as then random
     *         or public device address.
     * @param leRole        LE role data type
     */
    public BluetoothLeRecord(@NotNull final byte[] deviceAddress,
                             @NotNull BluetoothLeDeviceAddressType addressType,
                             @NotNull final LeRole leRole) {
        super(BLE_TYPE);
        if (deviceAddress == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
        byte[] addressWithType = Utils.concat(deviceAddress,
                                              new byte[] {
                                                      addressType.getValue() });
        if (addressWithType.length != DEVICE_ADDRESS_LENGTH) {
            throw new IllegalArgumentException(
                    ERR_MESSAGE_DEVICE_ADDRESS_LENGTH);
        }
        if (leRole == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_ROLE);
        }
        this.address = new AdData(DataTypes.LE_BLUETOOTH_DEVICE_ADDRESS,
                                  addressWithType);
        this.role = new AdData(DataTypes.LE_BLUETOOTH_ROLE,
                               new byte[] { leRole.getValue() });
    }

    /**
     * Gets the 7-octets LE Bluetooth device address in bytes including
     * address type byte.
     *
     * @return Returns the 7-octets LE Bluetooth device address in bytes
     *         including address type.
     */
    public byte[] getAddressBytesWithType() {
        if (this.address == null) {
            return new byte[0];
        }
        return this.address.getData();
    }

    /**
     * Gets the 6-octets LE Bluetooth device address in bytes.
     *
     * @return Returns the 6-octets LE Bluetooth device address in bytes.
     */
    public byte[] getAddressBytes() {
        if (this.address == null) {
            return new byte[0];
        }
        return Utils.subArray(this.address.getData(), 0,
                              DEVICE_ADDRESS_LENGTH - 1);
    }

    /**
     * Gets the LE Bluetooth device address.
     *
     * @return Returns the LE Bluetooth device address.
     */
    public AdData getAddress() {
        return this.address;
    }

    /**
     * Gets the LE Bluetooth device address type.
     *
     * @return Returns the LE Bluetooth device address type.
     */
    public BluetoothLeDeviceAddressType getAddressType() {
        if (this.address == null || this.address.getData() == null ||
            this.address.getData().length != DEVICE_ADDRESS_LENGTH) {
            return null;
        }
        return BluetoothLeDeviceAddressType.getBluetoothLeDeviceAddressType(
                this.address.getData()[DEVICE_ADDRESS_TYPE_OFFSET]);
    }

    /**
     * Sets the 7-octets LE Bluetooth device address including address
     * type.
     *
     * @param deviceAddress 7-octets LE Bluetooth device address including
     *         address type encoded in Little Endian order.
     */
    public void setAddress(@NotNull final byte[] deviceAddress) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
        if (deviceAddress.length != DEVICE_ADDRESS_LENGTH) {
            throw new IllegalArgumentException(
                    ERR_MESSAGE_DEVICE_ADDRESS_LENGTH);
        }
        this.address = new AdData(DataTypes.LE_BLUETOOTH_DEVICE_ADDRESS,
                                  deviceAddress);
    }

    /**
     * Sets the 6-octets LE Bluetooth device address
     *
     * @param deviceAddress 6-octets LE bluetooth device address excluding
     *         address type byte.
     * @param addressType   Enumeration represent LE Address type as then random
     *         or public device address.
     */
    public void setAddress(@NotNull final byte[] deviceAddress,
                           @NotNull BluetoothLeDeviceAddressType addressType) {
        if (deviceAddress == null) {
            throw new IllegalArgumentException(ERR_MESSAGE_DEVICE_ADDRESS);
        }
        byte[] addressWithType = Utils.concat(deviceAddress,
                                              new byte[] {
                                                      addressType.getValue() });
        setAddress(addressWithType);
    }

    /**
     * Gets the LE Bluetooth device role enumeration.
     *
     * @return Returns the LE Bluetooth device role enumeration.
     */
    public LeRole getRoleEnum() {
        return LeRole.getLeRole(role.getDataByte());
    }

    /**
     * Gets the LE Bluetooth device role.
     *
     * @return Returns the LE Bluetooth device role.
     */
    public AdData getRole() {
        return this.role;
    }

    /**
     * Gets the LE Bluetooth device role byte.
     *
     * @return Returns the LE Bluetooth device role byte.
     */
    public byte getRoleByte() {
        return this.role.getDataByte();
    }

    /**
     * Sets the LE Bluetooth device role.
     *
     * @param leRole LE Bluetooth device role
     */
    public void setLeRole(@NotNull final LeRole leRole) {
        this.role = new AdData(DataTypes.LE_BLUETOOTH_ROLE,
                               new byte[] { leRole.getValue() });
    }

    /**
     * Sets the LE Bluetooth device role.
     *
     * @param leRole LE Bluetooth device role
     */
    public void setLeRole(@NotNull final byte leRole) {
        this.role = new AdData(DataTypes.LE_BLUETOOTH_ROLE,
                               new byte[] { leRole });
    }

    /**
     * Gets the security manager TK value in bytes.
     *
     * @return Returns the security manager TK value in bytes.
     */
    public byte[] getSecurityManagerTKValueBytes() {
        if (this.securityManagerTKValue == null) {
            return new byte[0];
        }
        return this.securityManagerTKValue.getData();
    }

    /**
     * Gets the security manager TK value.
     *
     * @return Returns the security manager TK value.
     */
    public AdData getSecurityManagerTKValue() {
        return this.securityManagerTKValue;
    }

    /**
     * Sets the security manager TK value.
     *
     * @param leSecurityManagerTKValue The security manager TK value
     */
    public void setSecurityManagerTKValue(
            @NotNull final byte[] leSecurityManagerTKValue) {
        if (leSecurityManagerTKValue == null) {
            this.securityManagerTKValue = null;
            return;
        }
        this.securityManagerTKValue =
                new AdData(DataTypes.SECURITY_MANAGER_TK_VALUE,
                           leSecurityManagerTKValue);
    }

    /**
     * Gets the appearance data type in short.
     *
     * @return Returns the appearance data type in short.
     */
    public short getAppearanceShort() {
        if (this.appearance == null) {
            return AppearanceCategory.UNKNOWN.getValue();
        }
        return this.appearance.getDataShort();
    }

    /**
     * Gets the appearance data type.
     *
     * @return Returns the appearance data type.
     */
    public AdData getAppearance() {
        return this.appearance;
    }

    /**
     * Gets the appearance category.
     *
     * @return Returns the appearance category.
     */
    public String getAppearanceCategory() {
        if (this.appearance != null) {
            AppearanceCategory category = AppearanceCategory.getEnumByValue(
                    this.appearance.getDataShort());
            if (category != null) {
                return category.getName();
            }
        }
        return AppearanceCategory.UNKNOWN.getName();
    }

    /**
     * Sets the appearance data type.
     *
     * @param leAppearance The appearance data type
     */
    public void setAppearance(@NotNull final short leAppearance) {
        this.appearance = new AdData(DataTypes.APPEARANCE, new byte[] { 0 });
        this.appearance.setData(leAppearance);
    }

    /**
     * Sets the appearance data type enumeration.
     *
     * @param leAppearance The appearance data type enumeration
     */
    public void setAppearance(@NotNull final AppearanceCategory leAppearance) {
        this.appearance = new AdData(DataTypes.APPEARANCE, new byte[] { 0 });
        this.appearance.setData(leAppearance.getValue());
    }

    /**
     * Gets the flags data type in bytes.
     *
     * @return Returns the flags data type in bytes.
     */
    public byte[] getFlagsBytes() {
        if (this.flags == null) {
            return new byte[0];
        }
        return this.flags.getData();
    }

    /**
     * Gets the flags data type.
     *
     * @return Returns the flags data type.
     */
    public AdData getFlags() {
        return this.flags;
    }

    /**
     * Sets the flags data type.
     *
     * @param leFlags The flags data type
     */
    public void setFlags(@NotNull final byte[] leFlags) {
        if (leFlags == null) {
            this.flags = null;
            return;
        }
        this.flags = new AdData(DataTypes.FLAGS, leFlags);
    }

    /**
     * Gets UTF_8 encoded the user-friendly local name presented over
     * Bluetooth technology.
     *
     * @return Returns the user-friendly local name.
     */
    public String getNameString() {
        if (this.name == null || this.name.getData() == null) {
            return null;
        }
        return new String(this.name.getData(), StandardCharsets.UTF_8);
    }

    /**
     * Gets local name presented over Bluetooth technology.
     *
     * @return Returns the local name presented over Bluetooth technology.
     */
    public AdData getName() {
        return this.name;
    }

    /**
     * Sets the user-friendly local name presented over Bluetooth
     * technology.
     *
     * @param dataTypes The EIR data field defines Shortened or complete
     *         Bluetooth local name.
     * @param localName The user-friendly local name
     */
    public void setName(@NotNull final DataTypes dataTypes,
                        @NotNull final String localName) {
        if (localName == null) {
            this.name = null;
            return;
        }
        this.name = new AdData(dataTypes,
                               localName.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gets the list of other optional advertising and scan response data
     * (AD) format.
     *
     * @return Returns the array list of other optional advertising and scan
     * 			response data (AD).
     */
    public AdData[] getOptionalADList() {
        return this.optionalAdList.toArray(new AdData[0]);
    }

    /**
     * Adds the other optional advertising and scan response data (AD)
     * format.
     *
     * @param optionalAD Advertising and scan response data (AD) format data
     *         (Optional)
     */
    public void addOptionalAD(final AdData optionalAD) {
        this.optionalAdList.add(optionalAD);
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
        if (name != null) {
            result = prime * result + name.hashCode();
        }
        if (address != null) {
            result = prime * result + address.hashCode();
        }
        if (role != null) {
            result = prime * result + role.hashCode();
        }
        if (securityManagerTKValue != null) {
            result = prime * result + securityManagerTKValue.hashCode();
        }
        if (appearance != null) {
            result = prime * result + appearance.hashCode();
        }
        if (flags != null) {
            result = prime * result + flags.hashCode();
        }
        result = prime * result + optionalAdList.hashCode();
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

        if (!(obj instanceof BluetoothLeRecord)) {
            return false;
        }
        BluetoothLeRecord other = (BluetoothLeRecord) obj;

        if (!address.equals(other.address))
            return false;
        else if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        else if (!securityManagerTKValue.equals(other.securityManagerTKValue))
            return false;
        else if (!appearance.equals(other.appearance))
            return false;
        else if (!flags.equals(other.flags))
            return false;
        else if (!name.equals(other.name)) {
            return false;
        } else if (!optionalAdList.equals(other.optionalAdList)) {
            return false;
        }
        return (super.equals(obj));
    }
}
