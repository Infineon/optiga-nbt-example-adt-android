// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

/**
 * Container representing an APDU channel related event.
 */
public class StateChangeEvent {
    /** Communication channel is opened */
    public static final int EV_OPEN_CHANNEL = 0x00000001;
    /** Communication channel is closed */
    public static final int EV_CLOSE_CHANNEL = 0x00000002;
    /** Communication channel is connected (card powered) */
    public static final int EV_CONNECT = 0x00000003;
    /** Communication channel is disconnected (card un-powered or reset) */
    public static final int EV_DISCONNECT = 0x00000004;
    /** Communication channel is idle */
    public static final int EV_IDLE = 0x00000005;
    /** Communication channel is busy */
    public static final int EV_BUSY = 0x00000006;
    /** Communication channel changed */
    public static final int EV_CHANNEL_CHANGE = 0x00000007;
    /** Card or application content changed */
    public static final int EV_CONTENT_CHANGE = 0x00000008;

    /** Event ID for this event */
    private final int eventID;
    /** source triggering event */
    protected Object source;

    /**
     * Constructor.
     *
     * @param eventID event ID for event.
     */
    public StateChangeEvent(int eventID) {
        this.eventID = eventID;
    }

    /**
     * Constructor.
     *
     * @param eventID event ID for event.
     * @param source  object triggering event.
     */
    public StateChangeEvent(int eventID, Object source) {
        this.eventID = eventID;
        this.source = source;
    }

    /**
     * Get event ID of event.
     *
     * @return event ID
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Returns source of event or null if no source given.
     *
     * @return source triggering event or null.
     */
    public Object getSource() {
        return source;
    }
}
