// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

/**
 * Container for an APDU channel related event triggered by an APDU command.
 */
public class ApduEvent extends StateChangeEvent {
    /** Successful MANAGE CHANNEL operation performed */
    public static final int EV_MANAGE_CHANNEL = 0x00010001;
    /** Successful SELECT operation performed */
    public static final int EV_SELECT = 0x00010002;

    /** Reference of command APDU triggering event */
    private ApduCommand command;
    /** Reference of response APDU triggering event */
    private ApduResponse response;

    /**
     * Constructor.
     *
     * @param eventID  event ID of event
     * @param command  APDU command triggering event.
     * @param response APDU response triggering event.
     */
    public ApduEvent(int eventID, ApduCommand command, ApduResponse response) {
        super(eventID);

        this.command = command;
        this.response = response;
    }

    /**
     * Constructor.
     *
     * @param eventID event ID for event.
     * @param source  object triggering event.
     */
    public ApduEvent(int eventID, Object source) {
        super(eventID, source);
    }

    /**
     * Get APDU command that triggered the event.
     *
     * @return APDU command object.
     */
    public ApduCommand getCommand() {
        return command;
    }

    /**
     * Get APDU response that triggered the event.
     *
     * @return APDU response object.
     */
    public ApduResponse getResponse() {
        return response;
    }

    /**
     * Get logical channel associated with command.
     *
     * @return logical channel number.
     */
    public int getLogChannel() {
        return command.getLogChannel();
    }
}
