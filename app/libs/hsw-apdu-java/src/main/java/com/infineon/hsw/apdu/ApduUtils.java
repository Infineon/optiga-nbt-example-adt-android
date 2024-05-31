// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import java.util.logging.Logger;

/**
 * Class with static helper methods to make APDU functionality more convenient
 * to implement.
 */
public class ApduUtils {
    /**
     * Logger to log exceptions.
     */
    /* default */ static Logger logger = Logger.getLogger(
            ApduUtils.class.getName());

    /**
     * ApduUtils constructor
     */
    /* default */ ApduUtils() {
    }

    /**
     * Convert an object to a byte array. The object may either be a byte array,
     * a hex string, an ApduCommand object or an ApduResponse object.
     *
     * @param stream object containing byte stream data.
     * @return byte array containing data stream.
     * @throws ApduException if conversion could not be performed.
     */
    public static byte[] toBytes(Object stream) throws ApduException {
        // check if APDU command
        if (stream instanceof ApduCommand)
            return ((ApduCommand) stream).toBytes();

        // check if APDU response
        if (stream instanceof ApduResponse)
            return ((ApduResponse) stream).toBytes();

        if (stream instanceof AID)
            return ((AID) stream).toBytes();

        try {
            // try to convert string into byte array
            return Utils.toBytes(stream);
        } catch (UtilException e) {
            // throw exception at end of method
            throw new ApduException(e.getMessage(), e);
        }
    }

    /**
     * Helper function to extract a two byte value from a byte array.
     *
     * @param array  array with data
     * @param offset offset of two byte value in data array.
     * @return two byte value from byte array.
     */
    public static int getShort(byte[] array, int offset) {
        return ((array[offset] & 0xFF) << 8) | (array[offset + 1] & 0xFF);
    }
}
