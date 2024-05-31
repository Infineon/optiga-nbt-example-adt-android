// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

import com.infineon.hsw.apdu.ApduChannel;
import com.infineon.hsw.apdu.ApduCommand;
import com.infineon.hsw.apdu.ApduCommandSet;
import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.apdu.ApduResponse;
import com.infineon.hsw.apdu.ApduUtils;
import com.infineon.hsw.apdu.nbt.decoder.FapDecoder;
import com.infineon.hsw.apdu.nbt.model.AppletVersion;
import com.infineon.hsw.apdu.nbt.model.AvailableMemory;
import com.infineon.hsw.apdu.nbt.model.FileAccessPolicy;
import com.infineon.hsw.apdu.nbt.model.FileAccessPolicyException;
import com.infineon.hsw.apdu.nbt.model.NbtException;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.List;
import java.util.logging.Logger;

/**
 * Collection of commands supported by the NBT application.
 */
public class NbtCommandSet extends ApduCommandSet {
    private final Logger logger;

    /**
     * Logs a create password message.
     */
    private static final String
            LOG_MESSAGE_CREATE_PASSWORD = "Create a new password";

    /**
     * Logs a select file message.
     */
    private static final String LOG_MESSAGE_SELECT_FILE =
            "Select by file with FileID and R/W password";

    /**
     * Logs an update binary message.
     */
    private static final String LOG_MESSAGE_UPDATE_BINARY = "Update binary";

    /**
     * Logs a read binary message.
     */
    private static final String LOG_MESSAGE_READ_BINARY = "Read binary";

    /**
     * Logs a delete password message.
     */
    private static final String LOG_MESSAGE_DELETE_PASSWORD = "Delete password";

    /**
     * Logs a get data message.
     */
    private static final String LOG_MESSAGE_GET_DATA = "Get data";

    /**
     * Logs a change password message.
     */
    private static final String LOG_MESSAGE_CHANGE_PASSWORD = "Change password";

    /**
     * Logs a select AID message.
     */
    private static final String LOG_MESSAGE_SELECT_AID = "Select AID";

    /**
     * Logs an authenticate tag message.
     */
    private static final String
            LOG_MESSAGE_AUTHENTICATE_TAG = "Authenticate Tag";

    /**
     * Logs a select FileID message.
     */
    private static final String LOG_MESSAGE_SELECT_FID = "Select fileID";

    /**
     * Logs a read FAP file message.
     */
    private static final String LOG_MESSAGE_READ_FAP_FILE = "Read FAP file";

    /**
     * Logs an update FAP file message.
     */
    private static final String LOG_MESSAGE_UPDATE_FAP_FILE = "Update FAP file";

    /**
     * Logs an unblock password message.
     */
    private static final String
            LOG_MESSAGE_UNBLOCK_PASSWORD = "Unblock Password";

    /**
     * Instance of NBT command builder.
     */
    private final NbtCommandBuilder commandBuilder;

    /**
     * Constructor of NBT command set to configure the reference of
     * communication channel and log channel number.
     *
     * @param channel          Reference of communication channel associated
     *         with command handler.
     * @param logChannelNumber Number of logical channel or zero for basic
     *         channel.
     * @throws ApduException Throws an APDU exception, if AID object cannot be
     *         converted into a byte array.
     * @throws UtilException Throws an utility exception, if instance is unable
     *         to create the NBT command builder.
     */
    public NbtCommandSet(@NotNull ApduChannel channel,
                         @NotNull int logChannelNumber)
            throws ApduException, UtilException {
        super(NbtConstants.AID, channel, logChannelNumber);
        commandBuilder = new NbtCommandBuilder();
        logger = getLogger();
    }

    /**
     * Selects the NBT application.
     *
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case command creation
     *         or communication fails.
     */
    public NbtApduResponse selectApplication() throws ApduException {
        logger.info(LOG_MESSAGE_SELECT_AID);
        return sendCommand(commandBuilder.selectApplication());
    }

    /**
     * Selects the elementary file (EF) with the FileID.
     *
     * @param fileId 2-byte FileID of the file to be selected.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case command creation
     *         or communication fails.
     */
    public NbtApduResponse selectFile(@NotNull short fileId)
            throws ApduException {
        logger.info(LOG_MESSAGE_SELECT_FID);
        return sendCommand(commandBuilder.selectFile(fileId));
    }

    /**
     * Selects the elementary file (EF) with the FileID, optionally this might
     * contain the 4-byte password value to authenticate to perform password
     * protected Read/Write operations on the selected EF.
     *
     * @param fileId        2-byte FileID of the file to be selected.
     * @param readPassword  4-byte password for read operation (Optional- Null
     *         if not required)
     * @param writePassword 4-byte password for write operation (Optional- Null
     *         if not required)
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or Objects cannot be converted into a byte array.
     */
    public NbtApduResponse selectFile(@NotNull short fileId,
                                      byte[] readPassword, byte[] writePassword)
            throws ApduException {
        logger.info(LOG_MESSAGE_SELECT_FILE);
        return sendCommand(
                commandBuilder.selectFile(fileId, readPassword, writePassword));
    }

    /**
     * Reads the binary data from the currently selected elementary file.
     *
     * @param offset      2-byte start offset from which the data should be
     *         read.
     * @param expectedLen Expected length of data
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse readBinary(@NotNull short offset,
                                      @NotNull short expectedLen)
            throws ApduException, UtilException {
        logger.info(LOG_MESSAGE_READ_BINARY);
        return sendCommand(commandBuilder.readBinary(offset, expectedLen));
    }

    /**
     * Creates a new password. If create password command is password-protected
     * then the password to authenticate will be passed in the command data as
     * master password.
     *
     * @param masterPassword   4-byte master password for verification. Required
     *         if this password is already used with password-protected access
     *         condition (Optional).
     * @param newPasswordId    5-bit new password ID is of range '01' to '1F'.
     * @param newPassword      4-byte new password.
     * @param passwordResponse 2-byte password response.
     * @param passwordLimit    2-byte password try limit, it should be in range
     *         of '0001' to '7FFF'.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         and command creation problems.
     */
    public NbtApduResponse createPassword(byte[] masterPassword,
                                          @NotNull byte newPasswordId,
                                          @NotNull byte[] newPassword,
                                          @NotNull short passwordResponse,
                                          @NotNull short passwordLimit)
            throws ApduException {
        logger.info(LOG_MESSAGE_CREATE_PASSWORD);
        return sendCommand(
                commandBuilder.createPassword(masterPassword, newPasswordId,
                                              newPassword, passwordResponse,
                                              passwordLimit));
    }

    /**
     * Creates a new password. If create password command is password-protected
     * then the password to authenticate will be passed in the command data as
     * master password.
     *
     * @param masterPassword   4-byte master password for verification. Required
     *         if this password is already used with password-protected access
     *         condition (Optional).
     * @param newPasswordId    5-bit new password ID is of range '01' to '1F'.
     * @param newPassword      4-byte new Password.
     * @param passwordResponse 2-byte password response.
     * @param passwordLimit    2-byte password try limit, it should be in range
     *         of '0001' to '7FFF'.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         and command creation problems.
     */
    public NbtApduResponse createPassword(String masterPassword,
                                          @NotNull byte newPasswordId,
                                          @NotNull String newPassword,
                                          @NotNull short passwordResponse,
                                          @NotNull short passwordLimit)
            throws ApduException {
        return createPassword(ApduUtils.toBytes(masterPassword), newPasswordId,
                              ApduUtils.toBytes(newPassword), passwordResponse,
                              passwordLimit);
    }

    /**
     * Deletes an existing password, when the FAP file update operation is
     * allowed with ALWAYS access condition, then no need to authenticate with
     * the master password.
     *
     * @param passwordId 1-byte password ID is of range '01' to '1F'.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     */
    public NbtApduResponse deletePassword(@NotNull byte passwordId)
            throws ApduException {
        return deletePassword(passwordId, null);
    }

    /**
     * Updates the binary data into the currently selected elementary file.
     *
     * @param offset 2-byte start offset from which the data should be updated.
     * @param data   Binary data to be updated.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse updateBinary(@NotNull short offset,
                                        @NotNull byte[] data)
            throws ApduException, UtilException {
        logger.info(LOG_MESSAGE_UPDATE_BINARY);
        return sendCommand(commandBuilder.updateBinary(offset, data));
    }

    /**
     * Deletes an existing password, where FAP file is password-protected.
     *
     * <em>Note that the status word of the command is not checked by this
     * method.</em>
     *
     * @param passwordId     1-byte password ID is of range '01' to '1F'.
     * @param masterPassword 4-byte master password to authenticate FAP file.
     *         Required if this password is already used with password-protected
     *         access condition (Optional).
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     */
    public NbtApduResponse deletePassword(@NotNull byte passwordId,
                                          byte[] masterPassword)
            throws ApduException {
        logger.info(LOG_MESSAGE_DELETE_PASSWORD);
        return sendCommand(
                commandBuilder.deletePassword(passwordId, masterPassword));
    }

    /**
     * Issues a get data command to retrieve the NBT application specific
     * information like applet version and available memory. GET_DATA constants
     * that can be passed with an example: get data (TAG_AVAILABLE_MEMORY,
     * TAG_APPLET_VERSION)
     *
     * @param tag 2-byte tag used as reference control parameter (P1P2).
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, if the creation of the
     *         command is failed.
     * @throws NbtException  Throws the NBT exception, in case of data can not
     *         parse into parser.
     */
    public NbtApduResponse getData(@NotNull short tag)
            throws ApduException, NbtException {
        logger.info(LOG_MESSAGE_GET_DATA);
        ApduCommand getDataCommand = commandBuilder.getData(tag);
        return sendCommand(getDataCommand);
    }

    /**
     * Issues a get data command to retrieve the available memory information.
     *
     * @return Returns the available memory.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws NbtException  Throws the NBT exception, if failed to parse the
     *         available memory.
     */
    public AvailableMemory getDataAvailableMemory()
            throws ApduException, NbtException {
        NbtApduResponse apduResponse = getData(
                NbtConstants.TAG_AVAILABLE_MEMORY);
        return new AvailableMemory(apduResponse.getData());
    }

    /**
     * Issues a get data command to retrieve the applet version information.
     *
     * @return Returns the applet version.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws NbtException  Throws the NBT exception, if failed to parse the
     *         applet version.
     */
    public AppletVersion getDataAppletVersion()
            throws ApduException, NbtException {
        NbtApduResponse apduResponse = getData(NbtConstants.TAG_APPLET_VERSION);
        return new AppletVersion(apduResponse.getData());
    }

    /**
     * Changes an existing password with a new password. If the FAP file update
     * operation is password protected, the master password is required to
     * change the password.
     *
     * @param passwordId  5-bit password ID of the password to be changed.
     * @param newPassword New password to be updated.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     */
    public NbtApduResponse changePassword(@NotNull byte passwordId,
                                          @NotNull byte[] newPassword)
            throws ApduException {
        return changePassword(null, passwordId, newPassword);
    }

    /**
     * Changes an existing password with a new password. If the FAP file update
     * operation is password protected, the master password is required to
     * change the password.
     *
     * @param passwordId     5-bit password ID of the password to be changed
     * @param masterPassword Master password is required, if the FAP file update
     *         operation is password protected.
     * @param newPassword    New password to be updated.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     */
    public NbtApduResponse changePassword(byte[] masterPassword,
                                          @NotNull byte passwordId,
                                          @NotNull byte[] newPassword)
            throws ApduException {
        logger.info(LOG_MESSAGE_CHANGE_PASSWORD);
        return sendCommand(commandBuilder.changePassword(masterPassword,
                                                         passwordId,
                                                         newPassword));
    }

    /**
     * Issues an authenticate tag command, which generates the signature on the
     * challenge and can be used for brand protection use case in offline mode.
     *
     * @param challenge Data to be sent to authenticate.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     */
    public NbtApduResponse authenticateTag(@NotNull byte[] challenge)
            throws ApduException {
        logger.info(LOG_MESSAGE_AUTHENTICATE_TAG);
        return sendCommand(commandBuilder.authenticateTag(challenge));
    }

    /**
     * Reads the binary data from the FAP file, when the FAP file update
     * operation is allowed with ALWAYS access condition, then no need to
     * authenticate with the master password we can pass null.
     *
     * @param masterPassword 4-byte master password for verification. Required
     *         if this password is already used with password-protected access
     * condition (Optional).
     * @return Returns the response with status word.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or if command object cannot be converted into a byte
     *         stream.
     */
    public NbtApduResponse readFapBytes(byte[] masterPassword)
            throws ApduException, UtilException {
        NbtApduResponse apduResponse;

        logger.info(LOG_MESSAGE_READ_FAP_FILE);

        apduResponse = selectFile(NbtConstants.FAP_FILE_ID, masterPassword,
                                  null);
        apduResponse.checkStatus();
        return readBinary(NbtConstants.OFFSET_FILE_START, NbtConstants.LE_ANY);
    }

    /**
     * Reads the binary data from the FAP file, when the FAP file update
     * operation is allowed with ALWAYS access condition.
     *
     * @return Returns the response with status word.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or if command object cannot be converted into a byte
     *         stream.
     */
    public NbtApduResponse readFapBytes() throws ApduException, UtilException {
        return readFapBytes(null);
    }

    /**
     * Reads the FAP file and returns the list of file access policies, when the
     * FAP file update operation is allowed with ALWAYS access condition then no
     * need to authenticate with the master password we can pass null.
     *
     * @param masterPassword 4-byte master password for verification. Required
     *         if this password is already used with password-protected access
     * condition (Optional).
     * @return Returns the list of file access policies decoded from the FAP
     *         file bytes. Null if unable to read.
     * @throws UtilException             Throws an utility exception, in case of
     *         issues in parsing the select response.
     * @throws ApduException             Throws an APDU exception, in case of
     *         communication problems or if command object cannot be converted
     *         into a byte stream.
     * @throws FileAccessPolicyException Throws an FAP exception, if error in
     *         case of decoding of FAP file content decode.
     */
    public List<FileAccessPolicy> readFapList(byte[] masterPassword)
            throws ApduException, UtilException, FileAccessPolicyException {
        NbtApduResponse apduResponse;
        apduResponse = readFapBytes(masterPassword);
        return FapDecoder.getInstance().decode(apduResponse.getData());
    }

    /**
     * Reads the FAP file and returns the list of file access policies, when the
     * FAP file update operation is allowed with ALWAYS access condition.
     *
     * @return Returns the list of file access policies decoded from the FAP
     *         file bytes. Null if unable to read.
     * @throws UtilException             Throws an utility exception, in case of
     *         issues in parsing the select response.
     * @throws ApduException             Throws an APDU exception, in case of
     *         communication problems or if command object cannot be converted
     *         into a byte stream.
     * @throws FileAccessPolicyException Throws an FAP exception, if error in
     *         case of decoding of FAP file content decode.
     */
    public List<FileAccessPolicy> readFapList()
            throws ApduException, UtilException, FileAccessPolicyException {
        return readFapList(null);
    }

    /**
     * Updates the binary data to the FAP file, when the FAP file update
     * operation is allowed with ALWAYS access condition, then no need to
     * authenticate with the master password we can pass null.
     *
     * @param fileId         2-byte FileID for which access policy to be set.
     * @param policyBytes    4-byte access policy binary data to be updated.
     * @param masterPassword 4-byte master password for verification. Required
     *         if this password is already used with password-protected access
     * condition (Optional).
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or if invalid policy bytes.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse updateFapBytes(@NotNull short fileId,
                                          @NotNull byte[] policyBytes,
                                          byte[] masterPassword)
            throws ApduException, UtilException {
        NbtApduResponse apduResponse;

        logger.info(LOG_MESSAGE_UPDATE_FAP_FILE);
        commandBuilder.validatePolicyBytes(policyBytes);

        apduResponse = selectFile(NbtConstants.FAP_FILE_ID, null,
                                  masterPassword);
        apduResponse.checkStatus();
        apduResponse = updateBinary(
                NbtConstants.OFFSET_FILE_START,
                Utils.concat(Utils.toBytes(fileId, NbtConstants.FILE_ID_LENGTH),
                             policyBytes));
        return apduResponse;
    }

    /**
     * Updates the binary data to the FAP file, when the FAP file update
     * operation is allowed with ALWAYS access condition.
     *
     * @param fileId      2-byte FileID for which access policy to be set.
     * @param policyBytes 4-byte access policy binary data to be updated.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or if invalid policy bytes.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse updateFapBytes(@NotNull short fileId,
                                          @NotNull byte[] policyBytes)
            throws ApduException, UtilException {
        return updateFapBytes(fileId, policyBytes, null);
    }

    /**
     * Updates the file access policy to the FAP file, when the FAP file update
     * operation is allowed with ALWAYS access condition.
     *
     * @param fapPolicy Access policy to be updated.
     * @return Returns the response with status word.
     * @throws ApduException             Throws an APDU exception, in case of
     *         communication problems or if invalid policy bytes.
     * @throws UtilException             Throws an utility exception, in case of
     *         issues in parsing the select response.
     * @throws FileAccessPolicyException Throws an FAP exception, if unable to
     *         get access condition bytes.
     */
    public NbtApduResponse updateFap(@NotNull FileAccessPolicy fapPolicy)
            throws ApduException, UtilException, FileAccessPolicyException {
        return updateFapBytes(fapPolicy.getFileId(), fapPolicy.getAccessBytes(),
                              null);
    }

    /**
     * Updates the file access policy to the FAP file, when the FAP file update
     * operation is allowed with ALWAYS access condition, then no need to
     * authenticate with the master password we can pass null.
     *
     * @param fapPolicy      Access policy to be updated.
     * @param masterPassword 4-byte master password for verification. Required
     *         if this password is already used with password-protected access
     * condition (Optional).
     * @return Returns the response with status word.
     * @throws ApduException             Throws an APDU exception, in case of
     *         communication problems or if invalid policy bytes.
     * @throws UtilException             Throws an utility exception, in case of
     *         issues in parsing the select response.
     * @throws FileAccessPolicyException Throws an FAP exception, if unable to
     *         get access condition bytes.
     */
    public NbtApduResponse updateFap(@NotNull FileAccessPolicy fapPolicy,
                                     byte[] masterPassword)
            throws ApduException, UtilException, FileAccessPolicyException {
        return updateFapBytes(fapPolicy.getFileId(), fapPolicy.getAccessBytes(),
                              masterPassword);
    }

    /**
     * Unblocks a blocked password.
     *
     * @param passwordId Password ID for password to be unblocked.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     */
    public NbtApduResponse unblockPassword(@NotNull byte passwordId)
            throws ApduException {
        return unblockPassword(passwordId, null);
    }

    /**
     * Unblocks a blocked password.
     *
     * @param passwordId     Password ID for password to be unblocked.
     * @param masterPassword Password to authenticate the FAP file, if it is
     *         password-protected.
     * @return Returns the response with status word.
     * @throws ApduException In case of communication problems or build command
     *         failure.
     */
    public NbtApduResponse unblockPassword(@NotNull byte passwordId,
                                           byte[] masterPassword)
            throws ApduException {
        logger.info(LOG_MESSAGE_UNBLOCK_PASSWORD);
        return sendCommand(
                commandBuilder.unblockPassword(passwordId, masterPassword));
    }

    /**
     * Reads the NDEF file with password and returns the NDEF message byte data.
     *
     * @param ndefFileId   2-byte FileID of the NDEF file to be selected.
     * @param readPassword 4-byte password for read operation (Optional - Null
     *         if not required)
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response..
     */
    public NbtApduResponse readNdefMessage(@NotNull short ndefFileId,
                                           @NotNull byte[] readPassword)
            throws ApduException, UtilException {
        NbtApduResponse apduResponse = selectFile(ndefFileId, readPassword,
                                                  null);
        if (apduResponse.isSwError()) {
            return apduResponse;
        }
        return recursiveReadNdefMessage(NbtConstants.FILE_START_OFFSET,
                                        NbtConstants.MAX_LE);
    }

    /**
     * Reads the NDEF file and returns the NDEF message byte data.
     *
     * @param ndefFileId 2-byte FileID of the NDEF file to be selected.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse readNdefMessage(@NotNull short ndefFileId)
            throws ApduException, UtilException {
        return readNdefMessage(ndefFileId, null);
    }

    /**
     * Reads the NDEF file with default NDEF FileID and password and returns the
     * NDEF message byte data.
     *
     * @param readPassword 4-byte password for read operation (Optional - Null
     *         if not required)
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse readNdefMessage(@NotNull byte[] readPassword)
            throws ApduException, UtilException {
        return readNdefMessage(NbtConstants.NDEF_FILE_ID, readPassword);
    }

    /**
     * Reads the NDEF file with default NDEF FileID and password and returns the
     * NDEF message byte data.
     *
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse readNdefMessage()
            throws ApduException, UtilException {
        return readNdefMessage(NbtConstants.NDEF_FILE_ID, null);
    }

    /**
     * Updates the NDEF file with password and returns the NDEF message byte
     * data.
     *
     * @param ndefFileId    2-byte FileID of the NDEF file to be selected.
     * @param writePassword 4-byte password for write operation (Optional - Null
     *         if not required)
     * @param dataBytes     Bytes to be written in the binary file.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse updateNdefMessage(@NotNull short ndefFileId,
                                             @NotNull byte[] writePassword,
                                             @NotNull byte[] dataBytes)
            throws ApduException, UtilException {
        if (dataBytes == null) {
            throw new ApduException(NbtErrorCodes.ERR_DATA_NULL);
        }
        NbtApduResponse apduResponse = selectFile(ndefFileId, null,
                                                  writePassword);
        if (apduResponse.isSwError()) {
            return apduResponse;
        }
        return recursiveUpdateBinary(NbtConstants.FILE_START_OFFSET, dataBytes);
    }

    /**
     * Updates the NDEF file with password and returns the NDEF message byte
     * data.
     *
     * @param writePassword 4-byte password for write operation (Optional - Null
     *         if not required)
     * @param dataBytes     Bytes to be written in the binary file.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse updateNdefMessage(byte[] writePassword,
                                             @NotNull byte[] dataBytes)
            throws ApduException, UtilException {
        return updateNdefMessage(NbtConstants.NDEF_FILE_ID, writePassword,
                                 dataBytes);
    }

    /**
     * Updates the NDEF file with password and return the NDEF message byte
     * data.
     *
     * @param ndefFileId 2-byte FileID of the NDEF file to be selected.
     * @param dataBytes  Bytes to be written in the binary file.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse updateNdefMessage(@NotNull short ndefFileId,
                                             @NotNull byte[] dataBytes)
            throws ApduException, UtilException {
        return updateNdefMessage(ndefFileId, null, dataBytes);
    }

    /**
     * Updates the NDEF file with password and returns the NDEF message byte
     * data.
     *
     * @param dataBytes Bytes to be written in the binary file
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failure.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    public NbtApduResponse updateNdefMessage(@NotNull byte[] dataBytes)
            throws ApduException, UtilException {
        return updateNdefMessage(NbtConstants.NDEF_FILE_ID, null, dataBytes);
    }

    /**
     * Reads the NDEF binary file in recursive pattern.
     *
     * @param offset           Offset position for the read binary file.
     * @param totalBytesToRead Size of data to be read in bytes.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    private NbtApduResponse recursiveReadNdefMessage(@NotNull short offset,
                                                     @NotNull
                                                     short totalBytesToRead)
            throws ApduException, UtilException {
        short totalBytesRemainsToRead = (short) (totalBytesToRead - offset);
        NbtApduResponse apduResponse =
                readBinary(offset,
                           (totalBytesRemainsToRead > NbtConstants.MAX_LE)
                                   ? NbtConstants.MAX_LE
                                   : totalBytesRemainsToRead);

        if (apduResponse.isSwError()) {
            return apduResponse;
        }

        // Calculates the total bytes to be read from fist two bytes of NDEF
        // file.
        if (offset == 0) {
            // Updates the total_bytes_to_read with first 2 bytes from file.
            totalBytesToRead =
                    (short) Utils.getUINT16(apduResponse.getData(),
                                            NbtConstants.FILE_START_OFFSET);
            int dataLength = apduResponse.getDataLength() - 2;
            // If NDEF message size is less than the response length.
            if (totalBytesToRead < (apduResponse.getDataLength())) {
                dataLength = (totalBytesToRead);
            }
            // Shifts all data by 2-byte left in response data.
            byte[] data =
                    Utils.extractBytes(apduResponse.getData(),
                                       NbtConstants.T4T_NDEF_MSG_START_OFFSET,
                                       dataLength);
            data = Utils.concat(data, new byte[] { (byte) 0x90, 0x00 });
            ApduResponse apduResponse2 =
                    new ApduResponse(data, apduResponse.getExecutionTime());

            apduResponse = new NbtApduResponse(apduResponse2, (byte) 0);
            // Calculates the next iteration expected length and offset.
            offset = (short) (offset + dataLength + 2);
            // +2 for adding 2 bytes of length.
            totalBytesToRead += 2;
        } else {
            // Calculates next iteration expected length and offset.
            offset = (short) (offset + apduResponse.getDataLength());
        }

        if (totalBytesToRead > offset) {
            NbtApduResponse newApduResponse =
                    recursiveReadNdefMessage(offset, totalBytesToRead);
            apduResponse.appendResponse(newApduResponse, 0);
        }

        return apduResponse;
    }

    /**
     * Updates the binary file in a loop.
     *
     * @param offset    Offset for write the binary file.
     * @param dataBytes Bytes to be written in the binary file.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception.
     * @throws UtilException Throws an utility exception, in case of issues in
     *         parsing the select response.
     */
    private NbtApduResponse recursiveUpdateBinary(@NotNull short offset,
                                                  @NotNull byte[] dataBytes)
            throws ApduException, UtilException {
        // Adding file size at the beginning of file data.
        if (offset == 0) {
            byte[] ndefFileSize = Utils.toBytes(((short) dataBytes.length));
            dataBytes = Utils.concat(ndefFileSize, dataBytes);
        }

        // Extracting block of data to be written.
        int totalRemainingDataSize = dataBytes.length - offset;
        byte[] dataBlock = Utils.extractBytes(dataBytes, offset,
                                              (totalRemainingDataSize >
                                               NbtConstants.MAX_LC)
                                                      ? NbtConstants.MAX_LC
                                                      : totalRemainingDataSize);

        // Updates the sub set of data.
        NbtApduResponse apduResponse = updateBinary(offset, dataBlock);

        if (apduResponse.isSwError()) {
            return apduResponse;
        }

        // Calculates the next iteration expected offset and total remaining
        // data size.
        offset = (short) (offset + dataBlock.length);
        totalRemainingDataSize = dataBytes.length - offset;
        // Next iteration is required or not.
        if (totalRemainingDataSize > 0) {
            NbtApduResponse newApduResponse = recursiveUpdateBinary(offset,
                                                                    dataBytes);
            apduResponse.appendResponse(newApduResponse, 0);
        }

        return apduResponse;
    }

    /**
     * Sends a command and waits for response. This method modifies the APDU
     * response by adding an error message if response status word is not 9000.
     *
     * @param command APDU command containing command.
     * @return Returns the response with status word.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or if command object cannot be converted into a byte
     *         stream.
     */
    private NbtApduResponse sendCommand(@NotNull ApduCommand command)
            throws ApduException {
        ApduResponse apduResponse = super.send(command);
        return new NbtApduResponse(apduResponse, (byte) command.getINS());
    }
}