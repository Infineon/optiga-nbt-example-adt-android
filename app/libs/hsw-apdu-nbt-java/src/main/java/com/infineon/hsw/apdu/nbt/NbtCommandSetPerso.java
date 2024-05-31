// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

import com.infineon.hsw.apdu.ApduChannel;
import com.infineon.hsw.apdu.ApduCommand;
import com.infineon.hsw.apdu.ApduCommandSet;
import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.apdu.ApduResponse;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.logging.Logger;

/**
 * Collection of personalization commands supported by the NBT application.
 */
public class NbtCommandSetPerso extends ApduCommandSet {
    private final Logger logger;

    private static final String
            LOG_MESSAGE_PERSONALIZE_DATA = "Personalize data";

    /**
     * Instance of personalization command builder for NBT application.
     */
    private final NbtCommandBuilderPerso commandBuilder;

    /**
     * Constructor of NBT personalization command set to configure the reference
     * of communication channel and log channel number.
     *
     * @param channel          Reference of communication channel associated
     *         with command handler.
     * @param logChannelNumber Number of logical channel or zero for basic
     *         channel.
     * @throws ApduException Throws an APDU exception, if AID object cannot be
     *         converted into a byte array.
     * @throws UtilException Throws an utility exception, if instance is unable
     *         to create NBT command builder.
     */
    public NbtCommandSetPerso(@NotNull ApduChannel channel,
                              @NotNull int logChannelNumber)
            throws ApduException, UtilException {
        super(NbtConstants.AID, channel, logChannelNumber);
        commandBuilder = new NbtCommandBuilderPerso();
        logger = getLogger();
    }

    /**
     * Personalizes the data elements of the applet.
     *
     * @param dgi             Data group identifier of the data to be
     *         personalized.
     * @param personalizeData Data to be personalized. Note: (Content of
     *         personalize data is not
     * validated by this library or null exception is validated.)
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of
     *         communication problems or build command failure.
     */
    public NbtApduResponse personalizeData(@NotNull short dgi,
                                           @NotNull byte[] personalizeData)
            throws ApduException, UtilException {
        logger.info(LOG_MESSAGE_PERSONALIZE_DATA);
        return sendCommand(
                commandBuilder.personalizeData(dgi, personalizeData));
    }

    /**
     * Finalizes the personalization state, thereby transitioning the secure
     * element to operational state.
     *
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     */
    public NbtApduResponse finalizePersonalization() throws ApduException {
        logger.info(LOG_MESSAGE_PERSONALIZE_DATA);
        return sendCommand(commandBuilder.finalizePersonalization());
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
