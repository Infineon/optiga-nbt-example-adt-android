// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

/**
 * Implementation of an APDU service which modifies the class byte to represent
 * a specific logical channel.
 */
public class LogicChannelService implements IApduService {
    /** Logical channel associated with this object */
    private int logChannel;

    /**
     * Create an APDU based logical channel.
     *
     * @param channelNumber number of logical channel (0 = basic logical
     *         channel)
     */
    public LogicChannelService(int channelNumber) {
        logChannel = channelNumber;
    }

    /**
     * Return the channel number associated with logical channel.
     *
     * @return logical channel number.
     */
    public int getChannelNumber() {
        return logChannel;
    }

    /**
     * Set the channel number associated with logical channel.
     *
     * @param logChannel logical channel number.
     */
    public void setChannelNumber(int logChannel) {
        this.logChannel = logChannel;
    }

    // cSpell:ignore commandsets
    /*
     * (non-Javadoc)
     *
     * @see
     * com.infineon.tools.commandsets.apdu.IApduService#processCommand(ApduCommand)
     */
    public ApduCommand processCommand(ApduCommand apdu) throws ApduException {
        // modify CLA byte to reflect logical channel
        if (logChannel != 0)
            apdu.setLogChannel(logChannel);

        return apdu;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.infineon.tools.commandsets.apdu.IApduService#processResponse(
     * ApduResponse)
     */
    public ApduResponse processResponse(ApduResponse apdu)
            throws ApduException {
        // no modification on response necessary
        return apdu;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.infineon.tools.apdu.IApduService#getServiceType()
     */
    public int getServiceType() {
        return IApduService.SVC_LOG_CHANNEL;
    }
}
