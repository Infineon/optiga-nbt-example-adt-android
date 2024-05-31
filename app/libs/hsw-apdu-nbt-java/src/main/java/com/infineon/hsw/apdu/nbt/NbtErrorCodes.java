// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;
/**
 * Stores the error codes used in the NBT library.
 */
public final class NbtErrorCodes {
    /**
     * Incorrect LC value
     */
    public static final short INCORRECT_LC_LE = (short) 0x6700;

    /**
     * Application or file is not found.
     */
    public static final short APPLICATION_OR_FILE_NOT_FOUND = (short) 0x6A82;

    /**
     * Wrong TLV data format for select file with password data.
     */
    public static final short SELECT_FILE_WRONG_TLV = (short) 0x6A80;

    /**
     * Incorrect parameters in the command data field.
     */
    public static final short INCORRECT_DATA_PARAMETERS = (short) 0x6A80;

    /**
     * Unknown or unsupported data group, data object.
     */
    public static final short UNSUPPORTED_DATA = (short) 0x6A88;

    /**
     * Use of condition is not satisfied. If during personalization of
     * password, password ID already exists. If trying to personalize more
     * than 28 passwords.
     */
    public static final short CONDITIONS_NOT_SATISFIED_CREATE_PASSWORD =
            (short) 0x6985;

    /**
     * Incorrect P1-P2
     */
    public static final short WRONG_P1_P2 = (short) 0x6A86;

    /**
     * Security condition is not satisfied.
     */
    public static final short SECURITY_NOT_SATISFIED = (short) 0x6982;

    /**
     * CC file is updated for '02' to '0E' offset is not allowed.
     */
    public static final short UPDATE_ACCESS_DENIED = (short) 0x6985;

    /**
     * Command is not allowed.
     */
    public static final short COMMAND_NOT_ALLOWED = (short) 0x6986;

    /**
     * Conditions of use is not satisfied the password ID already present,
     * when trying to personalize more than 28 passwords.
     */
    public static final short CONDITIONS_NOT_SATISFIED = (short) 0x6985;

    /**
     * Wrong data, invalid FileID, or format mismatch in the FAP policy.
     */
    public static final short INCORRECT_DATA = (short) 0x6A80;

    /**
     * Instruction byte is not supported.
     */
    public static final short INS_NOT_SUPPORTED = (short) 0x6D00;

    /**
     * Class byte is not supported.
     */
    public static final short CLA_NOT_SUPPORTED = (short) 0x6E00;

    /**
     * Wrong length if Lc is present or if Le is not equal to '00'
     */
    public static final short GET_DATA_WRONG_LE = (short) 0x6700;

    /**
     * Reference data is not found.
     */
    public static final short DATA_NOT_FOUND = (short) 0x6A88;

    /**
     * Conditions of use is not satisfied.
     */
    public static final short CONDITIONS_USE_UNSATISFIED = (short) 0x6985;

    /**
     * Lc is 0x00 in case of personalize data command.
     */
    public static final short INVALID_LC_PERSONALIZE_DATA = (short) 0x6700;

    /**
     * Exception message if read password length is not equal to 4-byte.
     */
    public static final String ERR_R_PWD_LEN = "Password should be 4 bytes";

    /**
     * Exception message if read policy byte length is not equal to 4-byte.
     */
    public static final String
            ERR_POLICY_BYTES_LEN = "Policy bytes should be 4 bytes";

    /**
     * Exception message if data bytes are null.
     */
    public static final String
            ERR_DATA_NULL = "Input data bytes cannot be null";

    /**
     * Error message if unable to read the bytes.
     */
    public static final String ERR_SECURITY_ACCESS_DENIED_READ_BYTE =
            "Security status not satisfied, if master password is applied for "
            + "read file and password is not verified.";

    /**
     * APDU error message if the status word is not known to the NBT applet.
     */
    public static final String ERR_UNKNOWN_SW = "Unknown error code:";

    /**
     * APDU error message if the status word is incorrect Lc value.
     */
    public static final String
            ERR_INCORRECT_LC_LE_SELECT_FILE = "Incorrect Lc/Le value";

    /**
     * APDU error message if file or application is not found.
     */
    public static final String
            ERR_FILE_APPLICATION_NOT_FOUND = "File or application is not found";

    /**
     * APDU error message if wrong TLV data format for the select file with
     * password data.
     */
    public static final String ERR_INCORRECT_TLV_SELECT_FILE =
            "Incorrect parameters in the command data field. Example: Wrong TLV"
            + " data format for select file with password data.";

    /**
     * APDU error message if command is not allowed (EF is not selected).
     */
    public static final String
            ERR_INVALID_COMMAND = "Command not Allowed (EF is not selected)";

    /**
     * APDU error message if offset is greater than the file size\n, if Le is
     * absent\n, or if Le is coded on extended length.
     */
    public static final String ERR_INVALID_LENGTH =
            "Offset is greater than the file size or If Le is absent or If Le "
            + "is coded on extended length";

    /**
     * APDU error message if the security status is not satisfied.
     */
    public static final String
            ERR_SECURITY_STATUS_NOT_SATISFIED = "Security status not satisfied";

    /**
     * APDU error message if the password ID is not present.
     */
    public static final String
            ERR_PASSWORD_ID_NOT_PRESENT = "Password ID not present";

    /**
     * APDU error message if wrong length, if Lc is other than 09 and 0D bytes,
     * or if Le is present.
     */
    public static final String ERR_WRONG_LENGTH_CREATE_PASSWORD =
            "Wrong command length, if Lc is other than 09 and 0D bytes or if "
            + "Le is present";

    /**
     * APDU error message if the conditions of use is not satisfied.
     */
    public static final String ERR_CONDITIONS_NOT_SATISFIED_CREATE_PASSWORD =
            "Conditions of use is not satisfied Password ID already present or "
            + "if trying to personalize more than 28 passwords";

    // cSpell:ignore FFFE
    /**
     * APDU error message if incorrect data : Password response is 0000 and
     * FFFF.
     */
    public static final String ERR_INCORRECT_DATA_CREATE_PASSWORD =
            "Incorrect Data : Password response is 0000 and FFFF. or password "
            + "ID is 00 or Password limit value is 0000 or 0080 to FFFE.";

    /**
     * APDU error message if security status is not satisfied, authentication
     * with respective password is not successful, and file is locked (the
     * access policy is never for write).
     */
    public static final String ERR_SECURITY_AUTH_PASSWORD_NOT_SUCCESSFUL =
            "Security condition not satisfied, authentication with respective "
            + "password is not successful and file is locked";

    /**
     * APDU error message if CC file is updated for '02' to '0E'
     */
    public static final String ERR_SECURITY_UPDATE_ACCESS_DENIED =
            "CC file is updated for '02' to '0E' offset is not allowed.";

    /**
     * APDU error message if CC file is updated with wrong data, invalid FileID
     * or format mismatch in FAP policy.
     */
    public static final String ERR_INCORRECT_DATA =
            "Wrong data, Invalid FileID or format mismatch in FAP policy";

    /**
     * APDU error message if P1-P2 is greater than file size. Not applicable for
     * FAP update.
     */
    public static final String ERR_INCORRECT_P1_P2 =
            "Incorrect P1-P2: If P1-P2 is greater than file size. Not "
            + "applicable for FAP update.";

    /**
     * Exception message for invalid length of FAP bytes.
     */
    public static final String
            ERR_INVALID_BYTE_LENGTH = "FAP Policy bytes must be 52 bytes";

    /**
     * Exception message for invalid access condition of FAP bytes.
     */
    public static final String ERR_INVALID_ACCESS_CONDITION_TYPE =
            "Access condition should not of password-protected type";

    /**
     * Error message if unable to read the bytes.
     */
    public static final String ERR_READ_BYTE =
            "Incorrect data, Failed to read the input stream of bytes";

    /**
     * APDU error message if conditions are not satisfied.
     */
    public static final String ERR_CONDITIONS_NOT_SATISFIED =
            "Conditions of use is not satisfied: ECC private Key not "
            + "personalized";

    /**
     * APDU error message if conditions of use is not satisfied.
     */
    public static final String ERR_CONDITIONS_USE_UNSATISFIED =
            "Conditions of use is not satisfied.";

    /**
     * APDU error message if LC or LE are wrong.
     */
    public static final String ERR_WRONG_LC_OR_LE_AUTH_TAG =
            "Wrong length: If challenge length is 00, if Le is 00, or if Lc is "
            + "coded on extended length";

    /**
     * APDU error message if change password conditions are not satisfied.
     */
    public static final String ERR_CHANGE_CONDITION_NOT_SATISFIED =
            "Conditions of use is not satisfied. If Master password is applied "
            + "on FAP update and master password is not present. If Master "
            + "password is blocked. If requested password is blocked in case "
            + "of Change Password command.";

    /**
     * APDU error message if P1-P2 are other than 0x00.
     */
    public static final String ERR_WRONG_P1_P2_AUTH_TAG =
            "Incorrect parameters P1-P2, If P1-P2 are other than 00.";

    /**
     * APDU error message if password ID is not valid.
     */
    public static final String ERR_DATA_NOT_FOUND =
            "Reference of data not found. Password ID not valid.";

    /**
     * APDU error message if Lc is invalid.
     */
    public static final String ERR_INVALID_LC =
            "Wrong length, if Lc is other than 04 and 08 in case of change "
            + "password.";

    /**
     * APDU error message if unblock password conditions are not satisfied.
     */
    public static final String ERR_UNBLOCK_CONDITION_NOT_SATISFIED =
            "Conditions of use is not satisfied. If Master password is applied "
            + "on FAP update and master password is not present. If Master "
            + "password is blocked. If requested password is blocked in case "
            + "of Change Password command.";

    /**
     * APDU error message if tag are incorrect for the unblock password.
     */
    public static final String ERR_UNBLOCK_PASSWORD_WRONG_P1_P2 =
            "Incorrect P1-P2. If P1 is other than 00. If P2 has b8-b7 is other "
            + "than 00 and 40";

    /**
     * APDU error message if P1-P2 are other than 'DF3A' and 'DF3B'.
     */
    public static final String ERR_WRONG_P1_P2_GET_DATA = "Incorrect P1-P2";

    /**
     * Wrong length, if Lc is present or if Le is not equal to '00'.
     */
    public static final String
            ERR_WRONG_LE_GET_DATA = "Wrong length/Incorrect Lc value";

    /**
     * Wrong data length for the personalize data command.
     */
    public static final String ERR_WRONG_PERSONALIZE_DATA_LENGTH =
            "If FAP update content is other than 42 bytes, if Personalized "
            + "password length is other than 9 bytes, AES key length is other "
            + "than 16 bytes, ECC key length is other than 32 bytes, NDEF/"
            + "Proprietary EF personalization is out of file size, if Le is "
            + "present, if additional data is present in command, if password "
            + "response is 0000 and FFFF";

    /**
     * Wrong (data group identifier) DGI for the personalize data command.
     */
    public static final String ERR_UNSUPPORTED_DATA_GROUP =
            "Unknown or unsupported data group, data object";

    /**
     * Conditions not satisfied for the personalize data command.
     */
    public static final String ERR_CONDITION_NOT_SATISFIED_PERSONALIZE_DATA =
            "If during personalization of password, password ID already exists,"
            + " if trying to personalize more than 28 passwords, if "
            + "personalize data command is sent during operational phase";

    /**
     * Wrong Lc value as 0x00 for the personalize data command.
     */
    public static final String
            ERR_WRONG_LC_PERSONALIZE_DATA = "Wrong length, if Lc is 00";

    /**
     * Wrong value of P1 or P2 other than 0x0000 for the personalize data
     * command.
     */
    public static final String ERR_INVALID_P1_P2_PERSONALIZE_DATA =
            "Incorrect P1-P2, if P1-P2 is other than 00";

    /**
     * Wrong value of P1 or P2 other than 0x0000 for the set and get
     * configurations.
     */
    public static final String
            ERR_INVALID_P1_P2_CONFIGURATION = "Incorrect parameters P1-P2";

    /**
     * Incorrect parameters in the command data field.
     */
    public static final String ERR_WRONG_DATA_CONFIGURATOR =
            "Incorrect parameters in the command data field";

    /**
     * Incorrect instruction byte is not supported.
     */
    public static final String ERR_INVALID_INS_CONFIGURATION =
            "Incorrect INS, instruction byte is not supported";

    /**
     * Incorrect class byte is not supported.
     */
    public static final String ERR_INVALID_CLA_CONFIGURATION =
            "Incorrect CLA, CLA value not supported";

    private NbtErrorCodes() {
    }
}
