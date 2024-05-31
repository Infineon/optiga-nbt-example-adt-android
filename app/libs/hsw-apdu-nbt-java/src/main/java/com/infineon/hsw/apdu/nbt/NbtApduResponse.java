// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.apdu.ApduResponse;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Container class for NBT APDU responses. This response consists of response
 * data (optional), status word (mandatory), and response error (optional).
 */
public class NbtApduResponse extends ApduResponse {
    /**
     * Delimiter user to separate the key instruction byte + SW.
     */
    protected static final String DELIMITER = "_";

    /**
     * Stores the list of error messages available in NBT applet.
     * Key = instruction byte + SW, which are separated by '_'.
     */
    private static final Map<String, String> errorMap = new HashMap<>();

    static {
        errorMap.put(NbtConstants.INS_SELECT + DELIMITER +
                             NbtErrorCodes.INCORRECT_LC_LE,
                     NbtErrorCodes.ERR_INCORRECT_LC_LE_SELECT_FILE);

        errorMap.put(NbtConstants.INS_SELECT + DELIMITER +
                             NbtErrorCodes.APPLICATION_OR_FILE_NOT_FOUND,
                     NbtErrorCodes.ERR_FILE_APPLICATION_NOT_FOUND);

        errorMap.put(NbtConstants.INS_SELECT + DELIMITER +
                             NbtErrorCodes.SELECT_FILE_WRONG_TLV,
                     NbtErrorCodes.ERR_INCORRECT_TLV_SELECT_FILE);

        errorMap.put(NbtConstants.INS_UPDATE_BINARY + DELIMITER +
                             NbtErrorCodes.COMMAND_NOT_ALLOWED,
                     NbtErrorCodes.ERR_INVALID_COMMAND);

        errorMap.put(NbtConstants.INS_UPDATE_BINARY + DELIMITER +
                             NbtErrorCodes.INCORRECT_LC_LE,
                     NbtErrorCodes.ERR_INVALID_LENGTH);

        errorMap.put(NbtConstants.INS_UPDATE_BINARY + DELIMITER +
                             NbtErrorCodes.SECURITY_NOT_SATISFIED,
                     NbtErrorCodes.ERR_SECURITY_AUTH_PASSWORD_NOT_SUCCESSFUL);

        errorMap.put(NbtConstants.INS_UPDATE_BINARY + DELIMITER +
                             NbtErrorCodes.UPDATE_ACCESS_DENIED,
                     NbtErrorCodes.ERR_SECURITY_UPDATE_ACCESS_DENIED);

        errorMap.put(NbtConstants.INS_UPDATE_BINARY + DELIMITER +
                             NbtErrorCodes.INCORRECT_DATA,
                     NbtErrorCodes.ERR_INCORRECT_DATA);

        errorMap.put(NbtConstants.INS_UPDATE_BINARY + DELIMITER +
                             NbtErrorCodes.WRONG_P1_P2,
                     NbtErrorCodes.ERR_INCORRECT_P1_P2);

        errorMap.put(NbtConstants.INS_READ_BINARY + DELIMITER +
                             NbtErrorCodes.COMMAND_NOT_ALLOWED,
                     NbtErrorCodes.ERR_INVALID_COMMAND);

        errorMap.put(NbtConstants.INS_READ_BINARY + DELIMITER +
                             NbtErrorCodes.INCORRECT_LC_LE,
                     NbtErrorCodes.ERR_INVALID_LENGTH);

        errorMap.put(NbtConstants.INS_READ_BINARY + DELIMITER +
                             NbtErrorCodes.SECURITY_NOT_SATISFIED,
                     NbtErrorCodes.ERR_SECURITY_ACCESS_DENIED_READ_BYTE);

        errorMap.put(NbtConstants.INS_CREATE_PWD + DELIMITER +
                             NbtErrorCodes.WRONG_P1_P2,
                     NbtErrorCodes.ERR_INVALID_LENGTH);

        errorMap.put(NbtConstants.INS_CREATE_PWD + DELIMITER +
                             NbtErrorCodes.SECURITY_NOT_SATISFIED,
                     NbtErrorCodes.ERR_SECURITY_STATUS_NOT_SATISFIED);

        errorMap.put(NbtConstants.INS_CREATE_PWD + DELIMITER +
                             NbtErrorCodes.INCORRECT_LC_LE,
                     NbtErrorCodes.ERR_WRONG_LENGTH_CREATE_PASSWORD);

        errorMap.put(
                NbtConstants.INS_CREATE_PWD + DELIMITER +
                        NbtErrorCodes.CONDITIONS_NOT_SATISFIED_CREATE_PASSWORD,
                NbtErrorCodes.ERR_CONDITIONS_NOT_SATISFIED_CREATE_PASSWORD);

        errorMap.put(NbtConstants.INS_CREATE_PWD + DELIMITER +
                             NbtErrorCodes.INCORRECT_DATA_PARAMETERS,
                     NbtErrorCodes.ERR_INCORRECT_DATA_CREATE_PASSWORD);

        errorMap.put(NbtConstants.INS_DELETE_PWD + DELIMITER +
                             NbtErrorCodes.SECURITY_NOT_SATISFIED,
                     NbtErrorCodes.ERR_SECURITY_STATUS_NOT_SATISFIED);

        errorMap.put(NbtConstants.INS_DELETE_PWD + DELIMITER +
                             NbtErrorCodes.UNSUPPORTED_DATA,
                     NbtErrorCodes.ERR_PASSWORD_ID_NOT_PRESENT);

        errorMap.put(NbtConstants.INS_AUTHENTICATE_TAG + DELIMITER +
                             NbtErrorCodes.CONDITIONS_NOT_SATISFIED,
                     NbtErrorCodes.ERR_CONDITIONS_NOT_SATISFIED);

        errorMap.put(NbtConstants.INS_AUTHENTICATE_TAG + DELIMITER +
                             NbtErrorCodes.INCORRECT_LC_LE,
                     NbtErrorCodes.ERR_WRONG_LC_OR_LE_AUTH_TAG);

        errorMap.put(NbtConstants.INS_AUTHENTICATE_TAG + DELIMITER +
                             NbtErrorCodes.WRONG_P1_P2,
                     NbtErrorCodes.ERR_WRONG_P1_P2_AUTH_TAG);

        errorMap.put(NbtConstants.INS_UNBLOCK_PASSWORD + DELIMITER +
                             NbtErrorCodes.DATA_NOT_FOUND,
                     NbtErrorCodes.ERR_DATA_NOT_FOUND);

        errorMap.put(NbtConstants.INS_UNBLOCK_PASSWORD + DELIMITER +
                             NbtErrorCodes.INCORRECT_LC_LE,
                     NbtErrorCodes.ERR_INVALID_LC);

        errorMap.put(NbtConstants.INS_UNBLOCK_PASSWORD + DELIMITER +
                             NbtErrorCodes.CONDITIONS_NOT_SATISFIED,
                     NbtErrorCodes.ERR_UNBLOCK_CONDITION_NOT_SATISFIED);

        errorMap.put(NbtConstants.INS_UNBLOCK_PASSWORD + DELIMITER +
                             NbtErrorCodes.WRONG_P1_P2,
                     NbtErrorCodes.ERR_UNBLOCK_PASSWORD_WRONG_P1_P2);

        errorMap.put(NbtConstants.INS_GET_DATA + DELIMITER +
                             NbtErrorCodes.WRONG_P1_P2,
                     NbtErrorCodes.ERR_WRONG_P1_P2_GET_DATA);

        errorMap.put(NbtConstants.INS_GET_DATA + DELIMITER +
                             NbtErrorCodes.GET_DATA_WRONG_LE,
                     NbtErrorCodes.ERR_WRONG_LE_GET_DATA);

        errorMap.put(NbtConstants.INS_PERSONALIZE_DATA + DELIMITER +
                             NbtErrorCodes.INCORRECT_DATA_PARAMETERS,
                     NbtErrorCodes.ERR_WRONG_PERSONALIZE_DATA_LENGTH);

        errorMap.put(NbtConstants.INS_PERSONALIZE_DATA + DELIMITER +
                             NbtErrorCodes.UNSUPPORTED_DATA,
                     NbtErrorCodes.ERR_UNSUPPORTED_DATA_GROUP);

        errorMap.put(NbtConstants.INS_PERSONALIZE_DATA + DELIMITER +
                             NbtErrorCodes.CONDITIONS_NOT_SATISFIED,
                     NbtErrorCodes
                             .ERR_CONDITION_NOT_SATISFIED_PERSONALIZE_DATA);

        errorMap.put(NbtConstants.INS_PERSONALIZE_DATA + DELIMITER +
                             NbtErrorCodes.INVALID_LC_PERSONALIZE_DATA,
                     NbtErrorCodes.ERR_WRONG_LC_PERSONALIZE_DATA);

        errorMap.put(NbtConstants.INS_PERSONALIZE_DATA + DELIMITER +
                             NbtErrorCodes.WRONG_P1_P2,
                     NbtErrorCodes.ERR_INVALID_P1_P2_PERSONALIZE_DATA);

        errorMap.put(NbtConstants.INS_SET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.INCORRECT_LC_LE,
                     NbtErrorCodes.ERR_WRONG_LE_GET_DATA);

        errorMap.put(NbtConstants.INS_SET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.CONDITIONS_USE_UNSATISFIED,
                     NbtErrorCodes.ERR_CONDITIONS_USE_UNSATISFIED);

        errorMap.put(NbtConstants.INS_SET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.INCORRECT_DATA_PARAMETERS,
                     NbtErrorCodes.ERR_WRONG_DATA_CONFIGURATOR);

        errorMap.put(NbtConstants.INS_SET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.WRONG_P1_P2,
                     NbtErrorCodes.ERR_INVALID_P1_P2_CONFIGURATION);

        errorMap.put(NbtConstants.INS_SET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.INS_NOT_SUPPORTED,
                     NbtErrorCodes.ERR_INVALID_INS_CONFIGURATION);

        errorMap.put(NbtConstants.INS_SET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.CLA_NOT_SUPPORTED,
                     NbtErrorCodes.ERR_INVALID_CLA_CONFIGURATION);

        errorMap.put(NbtConstants.INS_GET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.CONDITIONS_USE_UNSATISFIED,
                     NbtErrorCodes.ERR_CONDITIONS_USE_UNSATISFIED);

        errorMap.put(NbtConstants.INS_GET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.INCORRECT_DATA_PARAMETERS,
                     NbtErrorCodes.ERR_WRONG_DATA_CONFIGURATOR);

        errorMap.put(NbtConstants.INS_GET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.INS_NOT_SUPPORTED,
                     NbtErrorCodes.ERR_INVALID_INS_CONFIGURATION);

        errorMap.put(NbtConstants.INS_GET_CONFIGURATION + DELIMITER +
                             NbtErrorCodes.CLA_NOT_SUPPORTED,
                     NbtErrorCodes.ERR_INVALID_CLA_CONFIGURATION);
    }

    /**
     * String containing response status word: error message
     */
    private String error;

    /**
     * Constructor for NBT APDU response.
     *
     * @param response APDU response
     * @param ins      Instruction code for which response is generated.
     * @throws ApduException Throws an APDU exception, in case of error in
     *         parsing Apdu response.
     */
    public NbtApduResponse(@NotNull ApduResponse response,
                           @NotNull final byte ins) throws ApduException {
        super(response.toBytes(), response.getExecutionTime());
        checkSwError(ins);
    }

    /**
     * Returns the response error message string, if status word is other
     * than 9000. If no error, returns an empty string.
     *
     * @return Returns the response error message string, if status word is
     *         other than 9000.
     */
    public String getError() {
        return error;
    }

    /**
     * If status word is not equals to 0x9000, then build the error message.
     * If error code is not in map, then message "Unknown error code: XXXX"
     *
     * @param ins Instruction code for which the response is generated.
     */
    private void checkSwError(@NotNull byte ins) {
        if (getSW() == ApduResponse.SW_NO_ERROR) {
            error = NbtConstants.EMPTY_STRING;
        } else {
            error = errorMap.get(ins + DELIMITER + getSW());
            if (error == null) {
                error = NbtErrorCodes.ERR_UNKNOWN_SW +
                        Utils.toHexString(Utils.getBytes(getSW()));
            }
        }
    }

    /**
     * Returns true, if the APDU status word denotes an error.
     *
     * @return Returns true in case of status word not 0x9000.
     */
    public boolean isSwError() {
        return (getSW() != SW_NO_ERROR);
    }
}
