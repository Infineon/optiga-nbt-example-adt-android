// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt.model;

import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Model class for storing the file access policy with respect to the FileID.
 */
public class FileAccessPolicy {
    /**
     * Constant defines the FileID length.
     */
    private static final int FILE_ID_LENGTH = 2;

    /**
     * FileID for the file access policy.
     */
    private final short fileId;

    /**
     * Access condition for the I2C write command.
     */
    private final AccessCondition accessConditionI2CW;

    /**
     * Access condition for the I2C read command.
     */
    private final AccessCondition accessConditionI2CR;

    /**
     * Access condition for the NFC write command.
     */
    private final AccessCondition accessConditionNfcW;

    /**
     * Access condition for the NFC read command.
     */
    private final AccessCondition accessConditionNfcR;

    /**
     * Constructor for creating an instance with the FileID and access
     * condition.
     *
     * @param fileId              2-byte FileID
     * @param accessConditionI2CR Access condition for the I2C read command.
     * @param accessConditionI2CW Access condition for the I2C write command.
     * @param accessConditionNfcR Access condition for the NFC read command.
     * @param accessConditionNfcW Access condition for the NFC write command.
     */
    public FileAccessPolicy(@NotNull short fileId,
                            @NotNull AccessCondition accessConditionI2CR,
                            @NotNull AccessCondition accessConditionI2CW,
                            @NotNull AccessCondition accessConditionNfcR,
                            @NotNull AccessCondition accessConditionNfcW) {
        this.fileId = fileId;
        this.accessConditionI2CR = accessConditionI2CR;
        this.accessConditionI2CW = accessConditionI2CW;
        this.accessConditionNfcR = accessConditionNfcR;
        this.accessConditionNfcW = accessConditionNfcW;
    }

    /**
     * Returns the byte array of FAP policy with FileID.
     *
     * @return Returns the the byte array of FAP policy.
     * @throws FileAccessPolicyException Throws an FAP exception, if access
     * condition is not password-protected.
     */
    public byte[] getBytes() throws FileAccessPolicyException {
        return Utils.concat(Utils.toBytes(fileId, FILE_ID_LENGTH),
                            getAccessBytes());
    }

    /**
     * Getter for FileID and returns the FileID.
     *
     * @return Returns the FileID associated with the FAP policy.
     */
    public short getFileId() {
        return this.fileId;
    }

    /**
     * Returns the byte array of the FAP access condition.
     *
     * @return Returns the the byte array of the FAP access condition.
     * @throws FileAccessPolicyException Throws an FAP exception, if access
     * condition is not password-protected.
     */
    public byte[] getAccessBytes() throws FileAccessPolicyException {
        return new byte[] { accessConditionI2CR.getAccessByte(),
                            accessConditionI2CW.getAccessByte(),
                            accessConditionNfcR.getAccessByte(),
                            accessConditionNfcW.getAccessByte() };
    }
}
