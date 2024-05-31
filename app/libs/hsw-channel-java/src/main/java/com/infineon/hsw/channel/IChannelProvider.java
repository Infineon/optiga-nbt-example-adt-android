// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.channel;

/**
 * Interface that has to be implemented by any provider of a specific
 * communication channel type.
 */
public interface IChannelProvider {
    /**
     * Get name of this provider.
     *
     * @return friendly name of this communication channel provider
     * (channel type)
     */
    String getProviderName();

    /**
     * Return a list of all friendly communication channels
     * available through this provider. <br>
     *
     * @return array of friendly channel names.
     */
    String[] getChannelNames();

    /**
     * Create (or return) communication channel with the given friendly name
     * and potential additional channel properties. <br>
     *
     * @param channelName friendly name of communication channel. <br>
     * @param channelProperties additional channel properties
     * (implementation specific). <br>
     * @return reference of requested channel or null if channel cannot
     * be provided.
     */
    IChannel getChannel(String channelName, String channelProperties);
}
