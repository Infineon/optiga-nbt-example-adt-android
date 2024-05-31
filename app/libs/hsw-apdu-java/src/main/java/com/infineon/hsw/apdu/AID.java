// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Container class for an application (or package) AID.
 */
public class AID {
    /** Reference of AID bytes */
    private final byte[] aidByteArray;

    /**
     * Logger to log exceptions
     */
    /* default */ Logger logger;

    /**
     * Constructor for AID object.
     *
     * @param aid AID bytes associated with application (or package).
     * @throws UtilException if AID object cannot be converted to a byte array
     */
    public AID(byte[] aid) throws UtilException {
        aidByteArray = aid;
        logger = Logger.getLogger(AID.class.getName());
    }

    /**
     * Checks if the AID bytes of an application equal the AID bytes of this
     * application.
     *
     * @param aid AID to be checked for identity.
     * @return true if AID bytes are identical, false otherwise.
     */
    public boolean equals(byte[] aid) {
        return Arrays.equals(aidByteArray, aid);
    }

    /**
     * Calculate the hash of this AID
     *
     * @return int : hash of the AID
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(aidByteArray);
        return result;
    }

    /**
     * Checks if this AID partially equals the given AID. To get a partial
     * match, the length of AID bytes of the passed <code>aid</code> parameter
     * must be less or equal to the AID bytes of this AID object. All bytes of
     * the passed AID must match to the first AID bytes of this AID object.
     *
     * @param aid Partial AID for matching.
     * @return true if this AID partially matches the given AID.
     */
    public boolean partialEquals(byte[] aid) {
        byte[] aidBytes = aid;

        // return false if partial AID invalid or longer than this AID.
        if ((aidBytes == null) || (aidBytes.length == 0) ||
            (aidBytes.length > aidByteArray.length))
            return false;

        // return false on any mismatch in first AID bytes
        for (int i = 0; i < aidBytes.length; i++)
            if (aidByteArray[i] != aidBytes[i])
                return false;

        return true;
    }

    /**
     * Checks if the RID (first five bytes of AID) match.
     *
     * @param aid AID to be checked for same RID.
     * @return true if RID matches, false otherwise.
     * @throws UtilException exception in thrown in case of toBytes throws utils
     *                       exception.
     */
    public boolean ridEquals(byte[] aid) throws UtilException {
        byte[] aidBytes = aid;

        // return false if partial AID invalid or longer than this AID.
        if ((aidBytes == null) || (aidBytes.length < 5))
            return false;

        // return false on any mismatch in first AID bytes
        for (int i = 0; i < 5; i++)
            if (aidByteArray[i] != aidBytes[i])
                return false;
        return true;
    }

    /**
     * Retrieve the byte array representation of the AID bytes.
     *
     * @return byte array containing AID bytes.
     */
    public byte[] toBytes() {
        return aidByteArray != null ? aidByteArray.clone() : null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return Utils.toHexString(aidByteArray);
    }

    /**
     * Helper method to convert an object into AID bytes.
     *
     * @param aid object representing AID bytes.
     * @return byte array with AID bytes or null if conversion fails.
     */
    private static byte[] toBytes(Object aid) throws UtilException {
        if (aid instanceof AID)
            return ((AID) aid).aidByteArray;

        return Utils.toBytes(aid);
    }
}
