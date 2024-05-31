// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.channel;

/**
 * Interface for a generic synchronous communication channel.
 * An example for this type of channel is a channel to a smart card via a
 * terminal.
 */
public interface IChannel {
    /**
     * Request access to communication port either exclusively or shared with
     * other processes.
     * @param exclusive if true exclusive access to this channel is requested.
     * @throws ChannelException if opening the channel failed.
     */
    void open(boolean exclusive) throws ChannelException;

    /**
     * Release communication port.
     * @throws ChannelException if releasing the communication port failed.
     */
    void close() throws ChannelException;

    /**
     * Establish connection to server.
     * @param request optional request data to be used for connecting to server
     *         or null if no data required.
     * @return optional response to connection request.
     * @throws ChannelException if connection could not be established.
     */
    byte[] connect(byte[] request) throws ChannelException;

    /**
     * Close connection to server.
     * @param request optional request data to be used for closing the
     *         connection or null if no data required.
     * @return optional response to connection request.
     * @throws ChannelException if disconnecting failed.
     */
    byte[] disconnect(byte[] request) throws ChannelException;

    /**
     * Reset communication channel.
     * @param request optional request data to be used for resetting the
     *         connection or null if no data required.
     * @return optional response to reset request.
     * @throws ChannelException if reset request failed.
     */
    byte[] reset(byte[] request) throws ChannelException;

    /**
     * Send a byte stream via the channel and return response stream.
     * @param stream byte array with stream to be sent.
     * @return byte array with received response steam.
     * @throws ChannelException if any communication problem occurred.
     */
    byte[] transmit(byte[] stream) throws ChannelException;

    /**
     * Send a control byte stream via the channel and return response stream.
     * @param stream byte array with control stream to be sent.
     * @return byte array with received control response steam.
     * @throws ChannelException if any communication problem occurred.
     */
    byte[] control(byte[] stream) throws ChannelException;

    /**
     * Return port status of channel.
     * @return true if communication port is opened.
     */
    boolean isOpen();

    /**
     * Return connection status of channel.
     * @return true if connection to server is established.
     */
    boolean isConnected();

    /**
     * Return friendly name of channel.
     * @return Friendly name of channel.
     */
    String getName();
}
