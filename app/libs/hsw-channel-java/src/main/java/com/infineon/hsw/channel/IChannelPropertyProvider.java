// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.channel;

import java.util.Properties;

/**
 * Interface that has to be implemented by any PropertyProvider of a specific
 * communication channel type.
 */
public interface IChannelPropertyProvider {
    /**
     * @return all the channel properties for a given Channel Provider.
     */
    Properties getChannelProperties();

    /**
     * API to save the Channel properties for a given Channel Provider.
     */
    void saveChannelProperties();
}
