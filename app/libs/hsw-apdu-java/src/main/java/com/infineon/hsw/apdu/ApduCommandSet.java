// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.utils.UtilException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Collection of commands supported by any operating system on a smart card.
 */
public class ApduCommandSet implements IStateListener {
    /** Reference of logical channel service */
    protected LogicChannelService logChannel;

    /** AID of application */
    protected AID applicationIdentifier;

    /** Reference of APDU channel */
    protected ApduChannel apduChannel;

    /** List of registered services */
    protected final List<IApduService> services = new ArrayList<>();

    /** Marker if application is currently selected */
    protected boolean selected = false;

    /**
     * Protected default constructor to allow subclasses to implement their own
     * logic
     */
    protected ApduCommandSet() {
    }

    /**
     * Constructor of basic Apdu command handler.
     *
     * @param aid              AID of application associated with command
     *         handler.
     * @param channel          Reference of communication channel associated
     *         with
     *                         command handler.
     * @param logChannelNumber Number of logical channel or zero for basic
     *         channel.
     * @throws ApduException if AID object cannot be converted into a byte
     *         array.
     * @throws UtilException util exception is thrown in case of configuring AID
     *         failed.
     */
    public ApduCommandSet(byte[] aid, ApduChannel channel, int logChannelNumber)
            throws ApduException, UtilException {
        // set communication channel
        apduChannel = channel;

        // register for any terminal state changes
        if (channel != null)
            channel.addStateListener(this);

        // set the AID
        setAID(aid);

        // create logical channel service and register it at APDU channel
        logChannel = new LogicChannelService(logChannelNumber);
        registerService(logChannel);
    }

    /**
     * Set the application identifier associated with this command handler.
     *
     * @param aid reference of AID to be set.
     * @throws ApduException if AID object cannot be converted into a byte
     *         array.
     * @throws UtilException Util exception is thrown in case of AID object
     *         failure.
     */
    public final void setAID(byte[] aid) throws ApduException, UtilException {
        // build application identifier
        applicationIdentifier = new AID(ApduUtils.toBytes(aid));
    }

    /**
     * Return AID associated with command handler.
     *
     * @return AID object associated with command handler.
     */
    public AID getAID() {
        return applicationIdentifier;
    }

    /**
     * Return connection status of channel.
     *
     * @return true if connection to server is established.
     */
    public boolean isConnected() {
        return apduChannel.isConnected();
    }

    /**
     * Connect to the card.
     *
     * @param data Data to determine connection type
     * @return ATR of card.
     * @throws ApduException if connecting to card fails.
     */
    public ATR connect(byte[] data) throws ApduException {
        return apduChannel.connect(data);
    }

    /**
     * Connect to the card.
     *
     * @return ATR of card.
     * @throws ApduException if connecting to card fails.
     */
    public ATR connect() throws ApduException {
        return apduChannel.connect();
    }

    /**
     * Disconnect from terminal.
     *
     * @throws ApduException if disconnecting from card fails.
     */
    public void disconnect() throws ApduException {
        apduChannel.disconnect();
    }

    /**
     * Perform a reset on the card.
     *
     * @param warmReset if true a warm reset will be requested.
     * @return ATR of card.
     * @throws ApduException if resetting card fails.
     */
    public ATR reset(boolean warmReset) throws ApduException {
        return apduChannel.reset(warmReset);
    }

    /**
     * Returns true if the application is currently selected.
     *
     * @return true if application is selected.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set selection status of command handler. Normally this is implicitly done
     * when a
     * SELECT command is issued, but there may be circumstances where this
     * mechanism does not work as expected.
     *
     * @param isSelected selection status of this command handler
     */
    public void setSelected(boolean isSelected) {
        selected = isSelected;
    }

    /**
     * Set channel object associated with APDU channel. This method allows to
     * change the terminal which is used to send APDUs without loosing all
     * internal states.
     *
     * @param channel new channel associated with APDU channel
     */
    public void setChannel(ApduChannel channel) {
        if (channel != null)
            channel.addStateListener(this);

        // set channel
        apduChannel = channel;
    }

    /**
     * Get communication channel associated with logger.
     *
     * @return reference of communication channel.
     */
    public ApduChannel getChannel() {
        return apduChannel;
    }

    /**
     * Return associated logic channel number.
     *
     * @return logic channel number.
     */
    public int getLogChannelNumber() {
        return logChannel.getChannelNumber();
    }

    /**
     * Set associated logic channel number.
     *
     * @param logChannel logic channel number.
     */
    public void setLogChannelNumber(int logChannel) {
        this.logChannel.setChannelNumber(logChannel);
    }

    /**
     * Retrieve logger associated with this channel.
     *
     * @return reference of logger object.
     */
    public Logger getLogger() {
        return apduChannel.getLogger();
    }

    /**
     * Send command and wait for card response. This method does not alter the
     * CLA byte to set the logical channel bits.
     *
     * @param command object containing command.
     * @return card response.
     * @throws ApduException in case of communication problems or if command
     *         object
     *                       cannot be converted into a byte stream.
     */
    public ApduResponse sendAsIs(byte[] command) throws ApduException {
        // send APDU without any processing by services except for logging
        ApduCommand apduCommand = new ApduCommand(ApduUtils.toBytes(command));
        return send(IApduService.SVC_COM_CHN_LOGGER, apduCommand);
    }

    /**
     * Send command and wait for card response. This method modifies the CLA
     * byte to set the logical channel bits if the object is assigned to a
     * supplementary logical channel. For the base channel no modification is
     * performed.
     *
     * @param command object containing command.
     * @return card response.
     * @throws ApduException in case of communication problems or if command
     *         object
     *                       cannot be converted into a byte stream.
     */
    public ApduResponse send(byte[] command) throws ApduException {
        ApduCommand apduCommand = new ApduCommand(ApduUtils.toBytes(command));
        return send(IApduService.SVC_ALL, apduCommand);
    }

    /**
     * Send command and wait for card response. This method modifies the CLA
     * byte to set the logical channel bits if the object is assigned to a
     * supplementary logical channel. For the base channel no modification is
     * performed.
     *
     * @param command object containing command.
     * @return card response.
     * @throws ApduException in case of communication problems or if command
     *         object
     *                       cannot be converted into a byte stream.
     */
    public ApduResponse send(ApduCommand command) throws ApduException {
        return send(IApduService.SVC_ALL, command);
    }

    /**
     * Send command and wait for card response. This method modifies the CLA
     * byte to set the logical channel bits if the object is assigned to a
     * supplementary logical channel. For the base channel no modification is
     * performed.
     *
     * @param serviceMask bit mask of service types to be applied before / after
     *                    sending command
     * @param command     object containing command.
     * @return card response.
     * @throws ApduException if command object cannot be converted into a byte
     *                       stream.
     */
    public ApduResponse send(int serviceMask, ApduCommand command)
            throws ApduException {
        IApduService oService;
        ApduCommand apduCommand;

        // determine number of registered services
        int i;
        int iNoOfServices = services.size();

        apduCommand = command;

        // run through all services in reverse order (last is called first)
        for (i = iNoOfServices - 1; i >= 0; i--) {
            oService = services.get(i);

            // check that service is not masked
            if ((oService.getServiceType() | serviceMask) == serviceMask)
                apduCommand = oService.processCommand(apduCommand);
        }

        // now send the APDU and receive the response
        ApduResponse apduResponse = apduChannel.send(apduCommand);

        // run through all services in normal order
        for (i = 0; i < iNoOfServices; i++) {
            oService = services.get(i);

            // check that service is not masked
            if ((oService.getServiceType() | serviceMask) == serviceMask)
                apduResponse = services.get(i).processResponse(apduResponse);
        }

        // send modified command
        return apduResponse;
    }

    /**
     * Add a service to the channel.
     *
     * @param service service to be added.
     * @return true if service was added, false otherwise.
     */
    public final boolean registerService(IApduService service) {
        // add service to list
        return services.add(service);
    }

    /**
     * Select an application by AID. Note that this method does not check the
     * status word.
     *
     * @param aid  reference of application identifier.
     * @param next if true a SELECT(next occurrence) is sent otherwise a
     *             SELECT(first)
     * @return card response.
     * @throws ApduException in case of communication problems or AID object
     *         cannot
     *                       be converted into a byte array.
     */
    public ApduResponse selectByAID(byte[] aid, boolean next)
            throws ApduException {
        // send SELECT by AID
        getLogger().info("Select by AID...");
        return send(
                new ApduCommand(0x00, 0xA4, 0x04, next ? 0x02 : 0x00, null, 256)
                        .appendData(aid));
    }

    /**
     * Select application associated with this command handler.
     *
     * @return response to SELECT command.
     * @throws ApduException in case of a communication error.
     */
    public ApduResponse select() throws ApduException {
        return selectByAID(applicationIdentifier.toBytes(), false);
    }

    /**
     * Notify command handler of new state of communication channel (e.g.
     * channel was disconnected or reset). The change in the state of the
     * communication channel may reset internal states of the command handler
     * (e.g. secure channels etc.)
     *
     * @param event event that triggers a state change.
     */
    public void notify(StateChangeEvent event) {
        switch (event.getEventID()) {
        case StateChangeEvent.EV_DISCONNECT:
        case StateChangeEvent.EV_CONNECT:
            selected = false;
            break;

        case ApduEvent.EV_MANAGE_CHANNEL: {
            if (event instanceof ApduEvent) {
                ApduEvent apduEvent = (ApduEvent) event;
                if (apduEvent.getLogChannel() == logChannel.getChannelNumber())
                    selected = false;
            }
        } break;

        case ApduEvent.EV_SELECT: {
            ApduEvent ae = (ApduEvent) event;

            if (ae.getLogChannel() == logChannel.getChannelNumber()) {
                ApduCommand cmd = ae.getCommand();

                // check for select by AID
                if ((cmd.getP1() == 0x04) && ((cmd.getP2() & 0xF0) == 0)) {
                    selected = applicationIdentifier.partialEquals(
                            cmd.getData());
                }
            }
        } break;
        default: {
            break;
        }
        }
    }
}
