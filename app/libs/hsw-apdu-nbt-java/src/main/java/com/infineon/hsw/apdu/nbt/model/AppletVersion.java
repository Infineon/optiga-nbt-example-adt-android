// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt.model;

import com.infineon.hsw.utils.Tlv;
import com.infineon.hsw.utils.TlvParser;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.List;

/**
 * Parses the version of NBT applet.
 */
public class AppletVersion {
    /**
     * Offset position for the major version.
     */
    private static final int OFFSET_MAJOR_VERSION = 0;

    /**
     * Offset position for the minor version.
     */
    private static final int OFFSET_MINOR_VERSION = 1;

    /**
     * Offset position for the build number.
     */
    private static final int OFFSET_BUILD_NUMBER = 2;

    /**
     * Major version in file control information.
     */
    private byte majorVersion;

    /**
     * Minor version in file control information.
     */
    private byte minorVersion;

    /**
     * Build number in file control information.
     */
    private short buildNumber;

    /**
     * Constructor for creating an instance with the applet version.
     *
     * @param data Data bytes received from the secure element.
     * @throws NbtException Throws the NBT exception, if failed to parse the
     *                      applet version.
     */
    public AppletVersion(@NotNull byte[] data) throws NbtException {
        try {
            byte[] version = parseAppletVersionBytes(data);
            this.setBuildNumber(
                    (short) Utils.getUINT16(version, OFFSET_BUILD_NUMBER));
            this.setMajorVersion(version[OFFSET_MAJOR_VERSION]);
            this.setMinorVersion(version[OFFSET_MINOR_VERSION]);
        } catch (UtilException exception) {
            throw new NbtException("Failed to parse applet version data",
                                   exception);
        }
    }

    private static byte[] parseAppletVersionBytes(@NotNull byte[] data)
            throws UtilException {
        TlvParser parser = new TlvParser(data);
        List<Object> tlvList = parser.parseTlvStructure();
        Tlv tlv6f = (Tlv) tlvList.get(0);
        tlvList = new TlvParser(tlv6f.getValue()).parseTlvStructure();
        Tlv tlvDf3a = (Tlv) tlvList.get(0);
        return tlvDf3a.getValue();
    }

    /**
     * Getter for the major version.
     *
     * @return Returns the major version.
     */
    public byte getMajorVersion() {
        return this.majorVersion;
    }

    /**
     * Setter for the major version.
     *
     * @param majorVersion Sets the major version.
     */
    private void setMajorVersion(@NotNull byte majorVersion) {
        this.majorVersion = majorVersion;
    }

    /**
     * Getter for the minor version.
     *
     * @return Returns the minor version.
     */
    public byte getMinorVersion() {
        return this.minorVersion;
    }

    /**
     * Setter for the minor version.
     *
     * @param minorVersion Sets the minor version.
     */
    private void setMinorVersion(@NotNull byte minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * Getter for the build number.
     *
     * @return Returns the build number.
     */
    public short getBuildNumber() {
        return this.buildNumber;
    }

    /**
     * Setter for the build number.
     *
     * @param buildNumber Sets the build number.
     */
    private void setBuildNumber(@NotNull short buildNumber) {
        this.buildNumber = buildNumber;
    }
}
