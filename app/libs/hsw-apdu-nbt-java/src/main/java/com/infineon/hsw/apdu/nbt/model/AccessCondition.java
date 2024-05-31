// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt.model;

import com.infineon.hsw.apdu.nbt.NbtErrorCodes;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Model class for storing the access condition and password ID.
 */
public class AccessCondition {
    /**
     * Password field mask
     */
    public static final byte PASSWORD_ID_MASK = (byte) 0x1F;

    /**
     * Password ID (Optional in case of password is not present.)
     */
    private byte passwordId = 0;

    /**
     * Enumeration for access condition of file access policy.
     */
    private final AccessConditionType accessConditionType;

    /**
     * Constructor to create instance with access condition type.
     * This constructor should be used, if access condition is not
     * password-protected type.
     *
     * @param accessConditionType Enumeration for access condition
     * @throws FileAccessPolicyException Throws an FAP exception, if access
     *                                   condition is not password-protected
     * type.
     */
    public AccessCondition(@NotNull AccessConditionType accessConditionType)
            throws FileAccessPolicyException {
        if (accessConditionType == AccessConditionType.PASSWORD_PROTECTED) {
            throw new FileAccessPolicyException(
                    NbtErrorCodes.ERR_INVALID_ACCESS_CONDITION_TYPE);
        }
        this.accessConditionType = accessConditionType;
    }

    /**
     * Constructor to create instance with password ID. This constructor should
     * be used, if access condition is password-protected.
     *
     * @param passwordId Password ID
     */
    public AccessCondition(@NotNull byte passwordId) {
        this.passwordId = (byte) (PASSWORD_ID_MASK & passwordId);
        this.accessConditionType = AccessConditionType.PASSWORD_PROTECTED;
    }

    /**
     * Returns the password access condition byte.
     *
     * @return Returns the password access condition byte.
     * @throws FileAccessPolicyException Throws an FAP exception, if unable to
     *                                   parse the access condition object.
     */
    public byte getAccessByte() throws FileAccessPolicyException {
        if (accessConditionType == AccessConditionType.PASSWORD_PROTECTED &&
            passwordId == 0) {
            throw new FileAccessPolicyException(
                    NbtErrorCodes.ERR_INVALID_ACCESS_CONDITION_TYPE);
        }
        if (accessConditionType == AccessConditionType.PASSWORD_PROTECTED) {
            return (byte) (passwordId |
                           AccessConditionType.PASSWORD_PROTECTED.getValue());
        }
        return accessConditionType.getValue();
    }

    /**
     * Getter for the password ID
     *
     * @return Returns the byte value respective of password ID.
     */
    public byte getPasswordId() {
        return this.passwordId;
    }

    /**
     * Getter for the access condition type
     *
     * @return Returns the access condition type.
     */
    public AccessConditionType getAccessConditionType() {
        return this.accessConditionType;
    }
}
