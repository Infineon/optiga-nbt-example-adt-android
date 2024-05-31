// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.channel.ChannelException;
import com.infineon.hsw.channel.IChannel;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Communication channel for APDU structured data packages. The APDUs can be
 * pre- and post processed by multiple services which apply logical channel
 * information or secure messaging.
 */
public class ApduChannel {
    // cSpell:ignore Apdus
    /** Bit flag for automatic GET RESPONSE handling in T=0 */
    public static final int FLAG_HANDLE_GET_RESPONSE = 1;

    /** Bit flag for logging of automatic protocol APDUs in T=0 */
    public static final int FLAG_LOG_GET_RESPONSE = 2;

    /**
     * Bit flag to keep logical channel information for GET RESPONSE command in
     * T=0
     */
    public static final int FLAG_KEEP_LOG_CHANNEL = 4;

    /** Bit flag to keep original class byte for GET RESPONSE command in T=0 */
    public static final int FLAG_KEEP_CLASS_BYTE = 8;

    /** Reference of APDU logger instance */
    protected final ApduLogger
            logger = new ApduLogger("com.infineon.hsw.apdu.ApduChannel", null);

    /** Reference to synchronous communication channel */
    protected IChannel channel;

    /** Marker if communication channel has been opened by this instance */
    private boolean openDone = false;

    /** Marker if GET RESPONSE shall be handled automatically */

    private boolean handleGetResponse = true;
    /** Marker if GET RESPONSE APDUs shall be logged */
    private boolean logProtocolApdus = true;

    /** Flag if logical channel info is kept in CLA of GET RESPONSE command */
    private boolean keepChannelBits = true;

    /** Flag if original class byte is kept for GET RESPONSE command */
    private boolean keepClassByte = false;

    /** List of all registered state listeners */
    private final List<WeakReference<IStateListener>> statelisteners =
            new ArrayList<>();

    /** Timer for sending delayed idle notifications */
    private final Timer timer = new Timer();

    /** Timer task for delayed idle notifications */
    private TimerTask idleTask;

    /** Marker for idle / busy state */
    private boolean busy;

    /**
     * Default constructor to allow derived class different handling of logging.
     */
    public ApduChannel() {
        // do nothing here
    }

    /**
     * Create an APDU based logical channel.
     *
     * @param channel synchronous communication channel.
     */
    public ApduChannel(IChannel channel) {
        // set channel
        setChannel(channel);
    }

    /**
     * Get logger instance.
     *
     * @return instance of APDU logger associated with channel.
     */
    public ApduLogger getLogger() {
        return logger;
    }

    /**
     * Set channel object associated with APDU channel. This method allows to
     * change the terminal which is used to send APDUs without loosing all
     * internal states.
     *
     * @param channel new channel associated with APDU channel
     */
    public final void setChannel(IChannel channel) {
        // set channel
        this.channel = channel;

        // signal that channel changed
        fireStateChanged(
                new StateChangeEvent(StateChangeEvent.EV_CHANNEL_CHANGE));
    }

    /**
     * Add a listener to the channel state. The listener will be informed of any
     * change in the channel state in case of e.g. disconnect or reset event.
     *
     * @param listener reference of listener object.
     */
    public void addStateListener(IStateListener listener) {
        removeStateListener(listener);
        statelisteners.add(new WeakReference<>(listener));
    }

    /**
     * Remove a listener to the channel state.
     *
     * @param listener reference of registered listener object.
     */
    public void removeStateListener(IStateListener listener) {
        for (WeakReference<IStateListener> wr : statelisteners) {
            if (listener.equals(wr.get())) {
                statelisteners.remove(wr);
                break;
            }
        }
    }

    /**
     * Get communication channel associated with logger.
     *
     * @return reference of communication channel.
     */
    public IChannel getChannel() {
        return channel;
    }

    /**
     * Send APDU command and receive response. If enabled, this method takes
     * care of handling T=0 protocol related APDUs like sending GET RESPONSE
     * commands or enveloping extended length APDUs.
     *
     * @param apduCommand APDU command to be sent
     * @return APDU response received from card.
     * @throws ApduException in case of communication problems.
     */
    public ApduResponse send(ApduCommand apduCommand) throws ApduException {
        ApduResponse apduResponse = new ApduResponse("0000", 0);

        // signal that channel is busy
        setBusy();

        try {
            // use temp variable for command APDU
            ApduCommand cmd = apduCommand;

            if (!logProtocolApdus)
                logger.info("", cmd);

            while (true) {
                byte[] abResponse;

                // log partial APDU
                if (logProtocolApdus)
                    logger.info("", cmd);

                // send command and receive response
                long lExecTime = System.nanoTime();

                try {
                    // send the command
                    abResponse = channel.transmit(cmd.toBytes());

                } catch (ChannelException e) {
                    logger.info("ERR: " + e.getMessage());
                    disconnect();
                    throw new ApduException(e.getMessage(), e);
                }

                if (abResponse != null) {
                    lExecTime = System.nanoTime() - lExecTime;

                    // log partial response
                    if (logProtocolApdus)
                        logger.info("",
                                    new ApduResponse(abResponse, lExecTime));

                    // append data to response
                    apduResponse.appendResponse(abResponse, lExecTime);

                    // check for 61xx or 6Cxx
                    if (handleGetResponse && (abResponse.length >= 2)) {
                        // handle GET RESPONSE
                        switch (abResponse[abResponse.length - 2]) {
                        case 0x61: {
                            ApduCommand origCmd = cmd;
                            cmd = new ApduCommand("00C00000");
                            int le = (abResponse[abResponse.length - 1] & 0xFF);
                            if (le == 0) {
                                le = 256;
                            }
                            cmd.setLe(le);
                            if (keepClassByte)
                                cmd.setCLA(origCmd.getCLA());
                            else if (keepChannelBits)
                                cmd.setLogChannel(origCmd.getLogChannel());
                        }
                            continue;

                        case 0x6C: {
                            // create new command and adjust Le
                            cmd = new ApduCommand(cmd.getHeader())
                                          .setLe((abResponse[abResponse.length -
                                                             1] &
                                                  0xFF));
                        }
                            continue;
                        default: {
                            // do nothing
                        }
                        }
                    }

                    break;
                }
            }

            // log final APDU
            if (!logProtocolApdus)
                logger.info("", apduResponse);

            // fire events on successful manage channel or select
            switch (apduResponse.getSW() & 0xFF00) {
            case 0x6C00:
            case 0x9000: {
                if (isManageChannel(apduCommand)) {
                    fireStateChanged(new ApduEvent(ApduEvent.EV_MANAGE_CHANNEL,
                                                   apduCommand, apduResponse));
                    break;
                }
            }
            // fall through
            case 0x6100:
            case 0x6200:
            case 0x6300: {
                if (isSelect(apduCommand))
                    fireStateChanged(new ApduEvent(ApduEvent.EV_SELECT,
                                                   apduCommand, apduResponse));
            } break;
            default: {
                // Do nothing
            }
            }
        } finally {
            // now we are idle again
            setIdle();
        }

        return apduResponse;
    }

    /**
     * Helper method to send an IDLE event to all listeners after a certain
     * delay.
     */
    private void setIdle() {
        // create new task
        idleTask = new TimerTask() {
            @Override
            public void run() {
                // remove reference to task
                idleTask = null;

                // send notification
                fireStateChanged(
                        new StateChangeEvent(StateChangeEvent.EV_IDLE));
            }
        };

        // signal that we are idle in a while
        timer.schedule(idleTask, 200);

        // change internal state
        busy = false;
    }

    /**
     * Helper method to send a BUSY event to all listeners
     */
    private void setBusy() {
        // signal we are busy
        busy = true;

        // get local copy of reference as reference is cleared in a separate
        // thread
        TimerTask busyTask = idleTask;

        // stop timer task or send signal
        if (busyTask != null)
            busyTask.cancel();
        else {
            busyTask = new TimerTask() {
                @Override
                public void run() {
                    // send notification
                    fireStateChanged(
                            new StateChangeEvent(StateChangeEvent.EV_BUSY));
                }
            };

            // send notification via timer to guarantee that events are fired in
            // correct order
            timer.schedule(busyTask, 0);
        }
    }

    /**
     * Gives the channel status.
     *
     * @return true if busy
     */
    public boolean isBusy() {
        return busy;
    }

    /**
     * Return connection status of channel.
     *
     * @return true if connection to server is established.
     */
    public boolean isConnected() {
        if (channel != null)
            return channel.isConnected();

        return false;
    }

    /**
     * Connect to the card.
     *
     * @return ATR of card.
     * @throws ApduException if connecting fails for some reason.
     */
    public ATR connect() throws ApduException {
        return connect(null);
    }

    /**
     * Connect to the card
     *
     * @param data Data to be used for connect (e.g. SHARED / EXCLUSIVE or
     *         DIRECT
     *             mode)
     * @return ATR of card.
     * @throws ApduException if connecting fails for some reason.
     */
    public ATR connect(byte[] data) throws ApduException {
        if (channel == null)
            throw new ApduException("No channel specified");

        try {
            // check if channel is open
            if (!channel.isOpen()) {
                // open channel
                channel.open(false);
                openDone = true;
            }

            // connect to card
            ATR atr = new ATR(channel.connect(data));

            // log the ATR
            logger.info("", atr);

            // signal state change
            fireStateChanged(new StateChangeEvent(StateChangeEvent.EV_CONNECT));
            return atr;
        } catch (ChannelException ce) {
            throw new ApduException(ce.getMessage(), ce);
        }
    }

    /**
     * Disconnect from terminal.
     *
     * @throws ApduException if disconnect fails for some reason.
     */
    public void disconnect() throws ApduException {
        try {
            // do nothing if not open
            if ((channel != null) && channel.isOpen()) {
                // disconnect if connected
                if (channel.isConnected())
                    channel.disconnect(null);

                // check if channel was opened by this instance
                if (openDone) {
                    // release channel
                    openDone = false;
                    channel.close();
                }
            }
        } catch (ChannelException ce) {
            throw new ApduException(ce.getMessage(), ce);
        } finally {
            fireStateChanged(
                    new StateChangeEvent(StateChangeEvent.EV_DISCONNECT));
        }
    }

    /**
     * Perform a reset on the card.
     *
     * @param warmReset if true a warm reset will be requested.
     * @return ATR of card.
     * @throws ApduException if resetting card fails for some reasons.
     */
    public ATR reset(boolean warmReset) throws ApduException {
        if (channel == null)
            throw new ApduException("No channel specified");

        try {
            ATR atr = new ATR(channel.reset(warmReset ? new byte[] { 1 }
                                                      : new byte[] { 0 }));
            logger.info("", atr);

            // signal that we are connected again
            fireStateChanged(new StateChangeEvent(StateChangeEvent.EV_CONNECT));

            return atr;
        } catch (ChannelException ce) {
            throw new ApduException(ce.getMessage(), ce);
        }
    }

    /**
     * Enable or disable automatic handling of GET RESPONSE in T=0 protocol.
     *
     * @param enable if true, ApduChannel will automatically handle SW 61xx and
     *               6Cxx.
     */
    public void enableAutoGetResponse(boolean enable) {
        handleGetResponse = enable;
    }

    /**
     * Helper method to signal a state change in case of a connect, disconnect
     * etc. event.
     *
     * @param event reference of event on communication channel.
     */
    public void fireStateChanged(StateChangeEvent event) {
        Object[] listeners = statelisteners.toArray();
        for (Object wr : listeners) {
            @SuppressWarnings("unchecked")
            IStateListener sl = ((WeakReference<IStateListener>) wr).get();

            if (sl != null)
                sl.notify(event);
            else
                statelisteners.remove(wr);
        }
    }

    /**
     * Set options how T=0 responses 6Cxx and 61xx are handled.
     *
     * @param options bit mask of response options
     */
    public void setResponseHandling(int options) {
        handleGetResponse = (options & FLAG_HANDLE_GET_RESPONSE) != 0;
        logProtocolApdus = (options & FLAG_LOG_GET_RESPONSE) != 0;
        keepChannelBits = (options & FLAG_KEEP_LOG_CHANNEL) != 0;
        keepClassByte = (options & FLAG_KEEP_CLASS_BYTE) != 0;
    }

    /**
     * Helper method that checks if an APDU is a SELECT command.
     *
     * @param apdu APDU command to be evaluated.
     * @return true if APDU is a SELECT command
     */
    public boolean isSelect(ApduCommand apdu) {
        return (apdu.getINS() == (byte) 0xA4);
    }

    /**
     * Helper method that checks if an APDU is a MANAGE CHANNEL command.
     *
     * @param apdu APDU command to be evaluated.
     * @return true if APDU is a MANAGE CHANNEL command
     */
    public boolean isManageChannel(ApduCommand apdu) {
        return (apdu.getINS() == (byte) 0x70);
    }
}
