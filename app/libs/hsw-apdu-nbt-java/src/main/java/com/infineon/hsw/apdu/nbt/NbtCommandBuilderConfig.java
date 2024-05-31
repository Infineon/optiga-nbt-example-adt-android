// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

import com.infineon.hsw.apdu.ApduCommand;
import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.utils.Tlv;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Command builder to build the APDUs for NBT configurator application.
 * These APDUs are used to get and set the product configuration.
 */
public class NbtCommandBuilderConfig {
    /**
     * Constructor for NBT configuration command builder.
     */
    /* default */ NbtCommandBuilderConfig() {
    }

    /**
     * Builds the select command to select the product configuration applet.
     *
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, in case select
     *         application command fails.
     */
    public ApduCommand selectConfiguratorApplication() throws ApduException {
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_SELECT,
                               NbtConstants.P1_SELECT_APPLICATION,
                               NbtConstants.P2_DEFAULT,
                               NbtConstants.CONFIGURATOR_AID,
                               NbtConstants.LE_ANY);
    }

    /**
     * Builds the set configuration command. This command can be used to set a
     * specific product configuration data in the NBT configurator
     * application.
     *
     * @param tag  Tag to set configuration: Configuration tags enumeration can
     *         be used.
     * @param data Configuration data
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, in case set configuration
     *         command fails.
     * @throws UtilException Throws an utility exception, in case of
     *         communication problems or build command failed.
     */
    public ApduCommand setConfigData(@NotNull short tag, @NotNull byte[] data)
            throws ApduException, UtilException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        byte[] tlvBytes = Tlv.buildDgiTlv(tag, data);
        return new ApduCommand(NbtConstants.CLA_CONFIGURATION,
                               NbtConstants.INS_SET_CONFIGURATION,
                               NbtConstants.P1_DEFAULT, NbtConstants.P2_DEFAULT,
                               tlvBytes, NbtConstants.LE_ABSENT);
    }

    /**
     * Builds the get configuration command. Reads the configuration data from
     * the NBT configurator application.
     *
     * @param tag Tag to retrieve configuration: Configuration tags enumeration
     *         can be used.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, in case get configuration
     *         command fails.
     * @throws UtilException Throws an utility exception, in case of
     *         communication problems or build command failed.
     */
    public ApduCommand getConfigData(@NotNull short tag)
            throws ApduException, UtilException {
        byte[] tagBytes = Utils.toBytes(tag);
        return new ApduCommand(NbtConstants.CLA_CONFIGURATION,
                               NbtConstants.INS_GET_CONFIGURATION,
                               NbtConstants.P1_DEFAULT, NbtConstants.P2_DEFAULT,
                               tagBytes, NbtConstants.LE_ABSENT);
    }
}
