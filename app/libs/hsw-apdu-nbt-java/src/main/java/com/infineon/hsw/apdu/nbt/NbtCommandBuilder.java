// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

import com.infineon.hsw.apdu.ApduCommand;
import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.apdu.ApduUtils;
import com.infineon.hsw.apdu.nbt.model.NbtException;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Command builder to build the APDU commands supported by the NBT applet.
 */
public class NbtCommandBuilder {
    private static final int P1_INDEX = 0;
    private static final int P2_INDEX = 1;

    /**
     * Constructor for command builder.
     */
    /* default */ NbtCommandBuilder() {
    }

    /**
     * Builds the select application command for the NBT applet.
     *
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, in case select
     *         application command fails.
     */
    public ApduCommand selectApplication() throws ApduException {
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_SELECT,
                               NbtConstants.P1_SELECT_APPLICATION,
                               NbtConstants.P2_DEFAULT, NbtConstants.AID,
                               NbtConstants.LE_ANY);
    }

    /**
     * Builds the select elementary file command. This command is used to select
     * the personalized elementary file (EF). In case of select by FileID
     * without password verification data (lc=0x02), applet will ignore any Le
     * value that is no response will be sent by the applet even if any Le value
     * is present in select command.
     *
     * @param fileId 2-byte FileID of the file to be selected.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *     command.
     */
    public ApduCommand selectFile(@NotNull short fileId) throws ApduException {
        byte[] commandData = ApduUtils.toBytes(fileId);
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_SELECT,
                               NbtConstants.P1_DEFAULT,
                               NbtConstants.P2_SELECT_FIRST, commandData,
                               NbtConstants.LE_ABSENT);
    }

    /**
     * Builds the select file command to select an elementary file. This command
     * is used to select the personalized EF, optionally this might contain the
     * 4-byte password value to authenticate to perform password protected
     * Read/Write operations on the selected EF.
     *
     * @param fileId             2-byte FileID of the file to be selected.
     * @param readPasswordBytes  4-byte password for read operation (Optional)
     * @param writePasswordBytes 4-byte password for write operation (Optional)
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *     command.
     */
    public ApduCommand selectFile(@NotNull short fileId,
                                  byte[] readPasswordBytes,
                                  byte[] writePasswordBytes)
            throws ApduException {
        validatePassword(readPasswordBytes);
        validatePassword(writePasswordBytes);

        byte[] commandData = ApduUtils.toBytes(fileId);

        if (readPasswordBytes != null) {
            commandData = Utils.concat(commandData,
                                       (new byte[] {
                                               NbtConstants.TAG_PWD_READ }));
            commandData = Utils.concat(commandData,
                                       (new byte[] {
                                               NbtConstants.PWD_LENGTH }));
            commandData = Utils.concat(commandData, readPasswordBytes);
        }
        if (writePasswordBytes != null) {
            commandData = Utils.concat(commandData,
                                       (new byte[] {
                                               NbtConstants.TAG_PWD_WRITE }));
            commandData = Utils.concat(commandData,
                                       (new byte[] {
                                               NbtConstants.PWD_LENGTH }));
            commandData = Utils.concat(commandData, writePasswordBytes);
        }
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_SELECT,
                               NbtConstants.P1_DEFAULT,
                               NbtConstants.P2_SELECT_FIRST, commandData,
                               NbtConstants.LE_ANY);
    }

    /**
     * Builds the update binary command. This command is used to update the
     * binary data from the currently selected elementary file.
     *
     * @param offset 2-byte start offset from which the data should be updated.
     * @param data   Binary data to be updated
     * @return Returns the APDU command.
     * @throws ApduException            Throws an APDU exception, if updating of
     *                    APDU command object failed.
     * @throws UtilException            Throws an utility exception, in case of
     *                  offset can not parse into byte array.
     * @throws IllegalArgumentException If 'data' is null.
     */
    public ApduCommand updateBinary(@NotNull short offset, @NotNull byte[] data)
            throws ApduException, UtilException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        byte[] offsetBytes = Utils.toBytes(offset);
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_UPDATE_BINARY,
                               offsetBytes[P1_INDEX], offsetBytes[P2_INDEX],
                               data, NbtConstants.LE_ABSENT);
    }

    /**
     * Builds the read binary command. This command is used to read the binary
     * data from the currently selected elementary file.
     *
     * @param offset      2-byte start offset from which the data should be
     *         read.
     * @param expectedLen Expected length of data
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if creating of APDU
     *         command object failed.
     * @throws UtilException Throws an utility exception, in case of offset can
     * not parse into byte array.
     */
    public ApduCommand readBinary(@NotNull short offset,
                                  @NotNull short expectedLen)
            throws ApduException, UtilException {
        byte[] offsetBytes = Utils.toBytes(offset);
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_READ_BINARY,
                               offsetBytes[P1_INDEX], offsetBytes[P2_INDEX],
                               NbtConstants.LC_NOT_PRESENT, expectedLen);
    }

    /**
     * Builds the authenticate tag Command. This command is used to generate the
     * signature on the challenge sent by host, which can be used for brand
     * protection use case in offline mode.
     *
     * @param challenge Data to be sent to authenticate.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if creating of APDU
     *         command object failed.
     */
    public ApduCommand authenticateTag(@NotNull byte[] challenge)
            throws ApduException {
        if (challenge == null) {
            throw new IllegalArgumentException(
                    "Challenge for authenticate tag can not be null.");
        }
        return new ApduCommand(NbtConstants.CLA,
                               NbtConstants.INS_AUTHENTICATE_TAG,
                               NbtConstants.P1_DEFAULT, NbtConstants.P2_DEFAULT,
                               challenge, NbtConstants.LE_ANY);
    }

    /**
     * Builds the create password command, This command is used to create a new
     * password. If the FAP file is password protected then the master password
     * to authenticate will be passed in the command data as master password.
     *
     * @param masterPassword   4-byte master password to authenticate FAP file,
     *         if the FAP file is password protected.
     * @param newPasswordId    1-byte new password ID is of range from '01' to
     *         '1F'.
     * @param newPassword      4-byte new password
     * @param passwordResponse 2-byte success response, which will be sent on
     *         successful password verification.
     * @param passwordLimit    2-byte password try limit, it should be in range
     *         of '0001' to '7FFF'.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *         command.
     */
    public ApduCommand createPassword(byte[] masterPassword,
                                      @NotNull byte newPasswordId,
                                      @NotNull byte[] newPassword,
                                      @NotNull short passwordResponse,
                                      @NotNull short passwordLimit)
            throws ApduException {
        validatePassword(masterPassword);
        validatePassword(newPassword);
        byte[] commandData = prepareCreatePasswordCommandData(newPasswordId,
                                                              newPassword,
                                                              passwordResponse,
                                                              passwordLimit);
        if (masterPassword != null) {
            commandData = Utils.concat(masterPassword, commandData);
        }
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_CREATE_PWD,
                               NbtConstants.P1_DEFAULT, NbtConstants.P2_DEFAULT,
                               commandData, NbtConstants.LE_ABSENT);
    }

    /**
     * Builds the delete password command. This command is used to delete the
     * created password. If the FAP file is password protected then the password
     * to authenticate will be passed in the command data as master password.
     *
     * @param passwordId     1-byte new password ID is of range '01' to '1F'.
     * @param masterPassword 4-byte master password to authenticate FAP file
     *         (Optional).
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *         command.
     */
    public ApduCommand deletePassword(@NotNull byte passwordId,
                                      byte[] masterPassword)
            throws ApduException {
        validatePassword(masterPassword);
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_DELETE_PWD,
                               NbtConstants.P1_DEFAULT, passwordId,
                               masterPassword, NbtConstants.LE_ABSENT);
    }

    /**
     * Builds the get data command. This command is used to retrieve NBT
     * application specific information like applet version and available
     * memory.
     *
     * @param tag 2-byte tag used as reference control parameter
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *     command.
     * @throws NbtException Throws the NBT exception, if failed to process the
     *     input tag.
     */
    public ApduCommand getData(@NotNull short tag)
            throws ApduException, NbtException {
        byte[] offsetBytes;
        try {
            offsetBytes = Utils.toBytes(tag);

        } catch (UtilException ex) {
            throw new NbtException("Failed to process the input tag", ex);
        }
        return new ApduCommand(NbtConstants.CLA, NbtConstants.INS_GET_DATA,
                               offsetBytes[P1_INDEX], offsetBytes[P2_INDEX],
                               NbtConstants.LC_NOT_PRESENT,
                               NbtConstants.LE_ANY);
    }

    /**
     * Builds the change password command.
     *
     * @param passwordId     5-bit password ID of the password to be changed
     * @param masterPassword Password to authenticate FAP file if it is password
     *         protected
     * @param newPassword    New password to be changed.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *         command.
     */
    public ApduCommand changePassword(byte[] masterPassword,
                                      @NotNull byte passwordId,
                                      @NotNull byte[] newPassword)
            throws ApduException {
        if (newPassword == null) {
            throw new IllegalArgumentException("New Password cannot be null");
        }
        if (newPassword.length != NbtConstants.PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Incorrect password length");
        }
        if ((masterPassword != null) &&
            (masterPassword.length != NbtConstants.PASSWORD_LENGTH)) {
            throw new IllegalArgumentException("Incorrect password length");
        }

        byte[] data;
        if (masterPassword != null) {
            data = Utils.concat(masterPassword, newPassword);
        } else {
            data = newPassword;
        }

        return new ApduCommand(NbtConstants.CLA,
                               NbtConstants.INS_CHANGE_PASSWORD,
                               NbtConstants.P1_DEFAULT,
                               NbtConstants.P2_CHANGE_PWD |
                                       (passwordId &
                                        NbtConstants.PASSWORD_ID_MASK),
                               data, NbtConstants.LE_ABSENT);
    }

    /**
     * Method to validate parameters and prepare the create password command
     * data byte array.
     *
     * @param passwordID       1-byte new password ID is of range from '01' to
     *         '1F'.
     * @param newPassword      4-byte new password.
     * @param passwordResponse 2-byte password response.
     * @param passwordLimit    2-byte password try limit, it should be in range
     *         of '0001' to '7FFF'.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *         command.
     */
    private static byte[] prepareCreatePasswordCommandData(
            @NotNull byte passwordID, @NotNull byte[] newPassword,
            @NotNull short passwordResponse, @NotNull short passwordLimit)
            throws ApduException {
        byte[] commandData = new byte[] { passwordID };
        commandData = Utils.concat(commandData, newPassword);
        commandData = Utils.concat(commandData,
                                   ApduUtils.toBytes(passwordResponse));
        commandData = Utils.concat(commandData,
                                   ApduUtils.toBytes(passwordLimit));
        return commandData;
    }

    /**
     * Method to validate the password bytes.
     *
     * @param passwordBytes Password bytes to be validated.
     * @throws ApduException Throws an APDU exception, if invalid password
     *         bytes.
     */
    private static void validatePassword(@NotNull byte[] passwordBytes)
            throws ApduException {
        if (passwordBytes != null &&
            passwordBytes.length != NbtConstants.PWD_LENGTH) {
            throw new ApduException(NbtErrorCodes.ERR_R_PWD_LEN);
        }
    }

    /**
     * Builds the unblock password command.
     *
     * @param passwordId Password ID for password to be unblocked.
     * @param masterPassword Password to authenticate FAP file if it is password
     *         protected.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, if failed to build the
     *         command.
     */
    public ApduCommand unblockPassword(@NotNull byte passwordId,
                                       byte[] masterPassword)
            throws ApduException {
        if ((masterPassword != null) &&
            (masterPassword.length != NbtConstants.PASSWORD_LENGTH)) {
            throw new IllegalArgumentException(
                    "Length of master password should be 4.");
        }
        return new ApduCommand(NbtConstants.CLA,
                               NbtConstants.INS_UNBLOCK_PASSWORD,
                               NbtConstants.P1_DEFAULT,
                               passwordId & NbtConstants.ID_MASK_BITS,
                               masterPassword, NbtConstants.LE_ABSENT);
    }

    /**
     * Method to validate the policy bytes.
     *
     * @param policyBytes Policy bytes
     * @throws ApduException Throws an APDU exception, if invalid policy bytes.
     */
    public void validatePolicyBytes(@NotNull byte[] policyBytes)
            throws ApduException {
        if (policyBytes == null ||
            policyBytes.length != NbtConstants.POLICY_FIELD_LENGTH) {
            throw new ApduException(NbtErrorCodes.ERR_POLICY_BYTES_LEN);
        }
    }
}