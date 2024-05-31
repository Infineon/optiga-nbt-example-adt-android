// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

/**
 * Interface defining a service which may modify APDU command and responses
 * transparently for the user. The typical use case for such a service is
 * secure messaging.
 */
public interface IApduService {
    /** Bit mask for no service */
    int SVC_NONE = 0x0000;

    /** Bit mask for any logger service */
    int SVC_LOGGER = 0x0001;

    /** Bit mask for a communication channel service */
    int SVC_COM_CHANNEL = 0x0002;

    /** Bit mask for a communication channel logger */
    int SVC_COM_CHN_LOGGER = 0x0003;

    /** Bit mask for a logical channel service */
    int SVC_LOG_CHANNEL = 0x0004;

    /** Bit mask for a logical channel logger */
    int SVC_LOG_CHN_LOGGER = 0x0005;

    /** Bit mask for a secure channel service */
    int SVC_SEC_CHANNEL = 0x0008;

    /** Bit mask for a secure channel logger */
    int SVC_SEC_CHN_LOGGER = 0x0009;

    /** Bit mask for a user service level 1 */
    int SVC_USER_1 = 0x0100;

    /** Bit mask for a user service level 2 */
    int SVC_USER_2 = 0x0200;

    /** Bit mask for a user service level 3 */
    int SVC_USER_3 = 0x0400;

    /** Bit mask for a user service level 4 */
    int SVC_USER_4 = 0x0800;

    /** Bit mask for a all kinds of services */
    int SVC_ALL = -1;

    /**
     * Do processing of command APDU before being sent to card.
     *
     * @param apdu byte array containing APDU
     * @return byte array containing processed APDU
     * @throws ApduException if processing the APDU command fails for any
     *         reason.
     */
    ApduCommand processCommand(ApduCommand apdu) throws ApduException;

    /**
     * Do processing of response APDU before being presented to the higher
     * layers.
     *
     * @param apdu byte array containing response APDU
     * @return byte array containing processed response APDU
     * @throws ApduException if processing of the APDU response fails for any
     *         reason.
     */
    ApduResponse processResponse(ApduResponse apdu) throws ApduException;

    /**
     * Return type of service (see SVC_xx constants).
     *
     * @return Type of service.
     */
    int getServiceType();
}
