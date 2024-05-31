// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt.decoder;

import com.infineon.hsw.apdu.nbt.NbtErrorCodes;
import com.infineon.hsw.apdu.nbt.model.AccessCondition;
import com.infineon.hsw.apdu.nbt.model.AccessConditionType;
import com.infineon.hsw.apdu.nbt.model.FileAccessPolicy;
import com.infineon.hsw.apdu.nbt.model.FileAccessPolicyException;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * To decode the file access policy (FAP) file content.
 */
public final class FapDecoder {
    /**
     * Private FAP policy decoder object
     */
    private static FapDecoder fapPolicyDecoder;
    /**
     * Fixed length for the FAP policy bytes
     */
    private static final int FAP_BYTE_LENGTH = 6;

    /**
     * Offset for FAP bytes
     */
    private static final int FAP_BYTE_START_OFFSET = 0;

    /**
     * Password ID bytes length
     */
    public static final int FILE_ID_LENGTH = 2;

    /**
     * Private constructor for FAP policy decoder
     */
    private FapDecoder() {
    }

    /**
     * Static method to instantiate the FAP policy decoder object.
     *
     * @return Returns the instance of FAP decoder.
     */
    public static synchronized FapDecoder getInstance() {
        if (fapPolicyDecoder == null) {
            fapPolicyDecoder = new FapDecoder();
        }
        return fapPolicyDecoder;
    }

    /**
     * Decodes the FAP file content with the specified and the FAP byte length.
     *
     * @param policyBytes File access policy data bytes
     * @return Returns the list of decoded file access policies.
     * @throws FileAccessPolicyException Throws an FAP exception, in case error
     *                                   in decoding policy data bytes
     */
    public List<FileAccessPolicy> decode(@NotNull byte[] policyBytes)
            throws FileAccessPolicyException {
        if (policyBytes.length % FAP_BYTE_LENGTH != 0) {
            throw new FileAccessPolicyException(
                    NbtErrorCodes.ERR_INVALID_BYTE_LENGTH);
        }

        ArrayList<FileAccessPolicy> arrayListFapPolicies = new ArrayList<>();
        try {
            ByteArrayInputStream stream =
                    new ByteArrayInputStream(policyBytes, FAP_BYTE_START_OFFSET,
                                             policyBytes.length);

            while (stream.available() > 0) {
                FileAccessPolicy fapPolicy =
                        new FileAccessPolicy(getFileId(stream),
                                             getFileAccessCondition(stream),
                                             getFileAccessCondition(stream),
                                             getFileAccessCondition(stream),
                                             getFileAccessCondition(stream));
                arrayListFapPolicies.add(fapPolicy);
            }
        } catch (FileAccessPolicyException | IOException e) {
            throw new FileAccessPolicyException(NbtErrorCodes.ERR_READ_BYTE, e);
        }
        return arrayListFapPolicies;
    }

    /**
     * Takes the byte input stream of policy bytes data and decodes to a FileID.
     *
     * @param stream Byte input stream of policy bytes data
     * @return Returns the FileID from input stream.
     * @throws IOException In case of errors in reading input data stream
     */
    private static short getFileId(@NotNull InputStream stream)
            throws IOException {
        try {
            return (short) Utils
                    .getUINT16(Utils.getBytesFromStream(FILE_ID_LENGTH, stream),
                               FAP_BYTE_START_OFFSET);
        } catch (UtilException e) {
            throw new IOException(e);
        }
    }

    /**
     * Takes the byte input stream of policy bytes data and decodes to a access
     * condition.
     *
     * @param stream Byte input stream of policy bytes data
     * @return Returns the access condition from input stream.
     * @throws FileAccessPolicyException Throws an FAP exception, if unable to
     *                                   instantiate the access condition
     * object.
     */
    private static AccessCondition getFileAccessCondition(
            @NotNull InputStream stream)
            throws IOException, FileAccessPolicyException {
        byte accessByte = (byte) stream.read();
        if (accessByte == AccessConditionType.ALWAYS.getValue()) {
            return new AccessCondition(AccessConditionType.ALWAYS);
        } else if (accessByte == AccessConditionType.NEVER.getValue()) {
            return new AccessCondition(AccessConditionType.NEVER);
        } else {
            return new AccessCondition(accessByte);
        }
    }
}