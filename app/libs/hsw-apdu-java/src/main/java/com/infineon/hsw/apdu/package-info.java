// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

/**
 * Basic functionality for all command sets which communicate
 * to a device via ISO7816-4 APDU commands. The central ApduChannel class
 * wraps the byte oriented generic communication channel and builds an APDU
 * interface on top with APDU related logging and formatting and specific
 * container classes for APDU commands and responses.
 * <p>
 * Any APDU related command set has to extend (or wrap) the ApduChannel
 * class directly or indirectly. For convenience this package implements
 * the SystemCommands class which contains the APDU commands typically
 * handled by the card operating system. These are in detail:
 * </p>
 * <ul>
 * <li>SELECT</li>
 * <li>MANAGE CHANNEL</li>
 * </ul>
 * Most command sets will extend the SystemCommands class instead of directly
 * extending the APDU channel class.
 */
package com.infineon.hsw.apdu;
