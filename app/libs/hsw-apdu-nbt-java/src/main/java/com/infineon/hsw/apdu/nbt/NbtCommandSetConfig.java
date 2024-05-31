// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

import com.infineon.hsw.apdu.ApduChannel;
import com.infineon.hsw.apdu.ApduCommand;
import com.infineon.hsw.apdu.ApduCommandSet;
import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.apdu.ApduResponse;
import com.infineon.hsw.utils.Tlv;
import com.infineon.hsw.utils.TlvParser;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.List;
import java.util.logging.Logger;

/**
 * Collection of commands supported by the NBT configurator application.
 */
public class NbtCommandSetConfig extends ApduCommandSet {
    private final Logger logger;

    /**
     * Logs a select AID message.
     */
    private static final String
            LOG_MESSAGE_SELECT_AID_CONFIGURATOR = "Select Configurator AID";

    /**
     * Logs a set configurator message.
     */
    private static final String LOG_MESSAGE_SET_CONFIGURATOR = "Set data";

    /**
     * Logs a get configurator message.
     */
    private static final String LOG_MESSAGE_GET_CONFIGURATOR = "Get data";

    private final NbtCommandBuilderConfig commandBuilder;

    /**
     * Constructor of NBT configuration command set to configure the reference
     * of communication channel and log channel number.
     *
     * @param channel          Reference of communication channel associated
     *         with command handler.
     * @param logChannelNumber Number of logical channel or zero for basic
     *         channel
     * @throws ApduException Throws an APDU exception, if AID object cannot be
     *         converted into a byte array.
     * @throws UtilException Throws an utility exception, if instance is unable
     *         to create configuration command builder.
     */
    public NbtCommandSetConfig(@NotNull ApduChannel channel,
                               @NotNull int logChannelNumber)
            throws ApduException, UtilException {
        super(NbtConstants.CONFIGURATOR_AID, channel, logChannelNumber);
        commandBuilder = new NbtCommandBuilderConfig();
        logger = getLogger();
    }

    /**
     * Selects the NBT configurator application.
     *
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case command creation
     *         or communication fails.
     */
    public NbtApduResponse selectConfiguratorApplication()
            throws ApduException {
        logger.info(LOG_MESSAGE_SELECT_AID_CONFIGURATOR);
        return sendCommand(commandBuilder.selectConfiguratorApplication());
    }

    /**
     * Issues a set configuration data command. This command can be used to set
     * a specific product configuration in the NBT configurator application.
     *
     * @param tag  Tag to set configuration: Configuration tags enumeration can
     *         be used.
     * @param data Configuration data
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case set configuration
     *         command fails.
     * @throws UtilException Throws an utility exception, if instance is unable
     *         to set configuration data.
     */
    public NbtApduResponse setConfigData(@NotNull short tag, @NotNull byte data)
            throws ApduException, UtilException {
        logger.info(LOG_MESSAGE_SET_CONFIGURATOR);
        byte[] dataArr = new byte[] { data };
        return sendCommand(commandBuilder.setConfigData(tag, dataArr));
    }

    /**
     * Issues a set configuration data command. This command can be used to set
     * a specific product configuration in the NBT configurator application.
     *
     * @param tag  Tag to set configuration: Configuration tags enumeration can
     *         be used.
     * @param data Configuration data
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case set configuration
     *         command fails.
     * @throws UtilException Throws an utility exception, if instance is unable
     *         to set configuration data.
     */
    public NbtApduResponse setConfigData(@NotNull short tag,
                                         @NotNull byte[] data)
            throws ApduException, UtilException {
        logger.info(LOG_MESSAGE_SET_CONFIGURATOR);
        return sendCommand(commandBuilder.setConfigData(tag, data));
    }

    /**
     * Issues a get configuration data command. This command can be used to get
     * a specific product configuration from the NBT configurator application.
     *
     * @param tag Tag to retrieve configuration: Configuration tags enumeration
     *         can be used.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case get configuration
     *         command fails.
     * @throws UtilException Throws an utility exception, if instance is unable
     *         to get configuration data.
     */
    public NbtApduResponse getConfigData(@NotNull short tag)
            throws ApduException, UtilException {
        logger.info(LOG_MESSAGE_GET_CONFIGURATOR);
        return sendCommand(commandBuilder.getConfigData(tag));
    }

    /**
     * Issues a get configuration data command. This command can be used to get
     * a specific product configuration from the NBT configurator application.
     *
     * @param tag Tag to retrieve configuration: Configuration tags enumeration
     *         can be used.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case get configuration
     *         command fails.
     * @throws UtilException Throws an utility exception, if instance is unable
     *         to get configuration data.
     */
    /* default */ byte[] getConfigDataBytes(@NotNull short tag)
            throws ApduException, UtilException {
        NbtApduResponse response = getConfigData(tag);

        if (response.getSW() == ApduResponse.SW_NO_ERROR) {
            TlvParser parser = new TlvParser(response.getData());
            List<Object> tlvList = parser.parseDgiTlvStructure();
            if (tlvList.isEmpty()) {
                return new byte[] {};
            }
            return ((Tlv) tlvList.get(0)).getValue();
        } else {
            throw new ApduException(
                    "Could not read tag data, command returned error SW");
        }
    }

    /**
     * Sends a command and waits for response. This method modifies the
     * APDU response by adding an error message if response status word
     * is not 9000.
     *
     * @param command APDU command containing command.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or if command object cannot be converted into a byte
     *         stream.
     */
    private NbtApduResponse sendCommand(@NotNull ApduCommand command)
            throws ApduException {
        ApduResponse apduResponse = super.send(command);
        return new NbtApduResponse(apduResponse, (byte) command.getINS());
    }
}
