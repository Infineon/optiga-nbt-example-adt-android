// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

/**
 * Interface to be implemented by any class interested in the current state
 * of the communication channel. Typically this interface is implemented by
 * command sets which have to reset internal states when a card is reset or
 * un-powered. It also may be used to inform the user about a change in the
 * channel state in a graphic user interface.
 */
public interface IStateListener {
    /**
     * Notify the listener of a state change in the communication channel.
     *
     * @param event event that triggers a state change.
     */
    void notify(StateChangeEvent event);
}
