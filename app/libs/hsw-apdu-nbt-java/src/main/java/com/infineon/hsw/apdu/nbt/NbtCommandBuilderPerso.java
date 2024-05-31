// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

import com.infineon.hsw.apdu.ApduCommand;
import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.utils.UtilException;
import com.infineon.hsw.utils.Utils;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Command builder to build the APDUs used for personalization of NBT
 * application.
 */
public class NbtCommandBuilderPerso {
    /**
     * Constructor for personalize data command.
     */
    /* default */ NbtCommandBuilderPerso() {
    }

    /**
     * Enumeration stores the data group index for personalize data command.
     */
    protected enum Personalize_Data_Dgi {
        // cspell:ignore CMAC
        /**
         * AES-128-CMAC key for online brand protection (BMK)
         */
        A001((short) 0xA001),
        /**
         * EC private key for offline brand protection (BSK)
         */
        A002((short) 0xA002),
        /**
         * Password Data ([Password ID (1 byte)] [Password Data (4 bytes)]
         * [Password Response (2 bytes)] [Password Limit (2 bytes)])
         */
        A003((short) 0xA003),
        /**
         * NDEF File content ([Offset (2 bytes)] [NDEF file content])
         */
        E104((short) 0xE104),
        /**
         * Proprietary file content ([Offset (2 bytes)] [Proprietary file
         * content])
         */
        E1A1((short) 0xE1A1),
        /**
         * Proprietary file content ([Offset (2 bytes)] [Proprietary file
         * content])
         */
        E1A2((short) 0xE1A2),
        /**
         * Proprietary file content ([Offset (2 bytes)] [Proprietary file
         * content])
         */
        E1A3((short) 0xE1A3),
        /**
         * Proprietary file content ([Offset (2 bytes)] [Proprietary file
         * content])
         */
        E1A4((short) 0xE1A4),
        /**
         * Configuration data (FAP) (Set of "[FileID (2 bytes)]
         * [Config byte for I2C read] [Config byte for I2C write]
         * [Config byte for NFC read] [Config byte for NFC write]")
         */
        E1AF((short) 0xE1AF),
        /**
         * Data object to indicate end of personalization and also referred to
         * as FINALIZE PERSONALIZATION. Data field is absent.
         */
        BF63((short) 0xBF63);

        private final short dgi;

        /**
         * Constructor for Personalize_Data_Dgi.
         *
         * @param dgi Short data type for DGI value.
         */
        Personalize_Data_Dgi(short dgi) {
            this.dgi = dgi;
        }

        /**
         * Public function to return the current DGI value.
         *
         * @return Returns the short data type DGI.
         */
        public short getDgi() {
            return this.dgi;
        }
    }

    /**
     * Builds the personalize data command, which is used to personalize the
     * data elements of the applet.
     *
     * @param dgi Data group identifier of the data to be personalized.
     * @param personalizeData Data to be personalized.
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failed.
     * @throws UtilException Throws an utility exception, in case of
     *         communication problems or build command failed.
     */
    public ApduCommand personalizeData(@NotNull short dgi,
                                       @NotNull byte[] personalizeData)
            throws ApduException, UtilException {
        if (personalizeData == null) {
            throw new IllegalArgumentException(
                    "Personalize Data cannot be null.");
        }
        byte[] dgiByte = Utils.toBytes(dgi);
        return new ApduCommand(
                NbtConstants.CLA, NbtConstants.INS_PERSONALIZE_DATA,
                NbtConstants.P1_DEFAULT, NbtConstants.P2_DEFAULT,
                Utils.concat(dgiByte,
                             Utils.concat(new byte[] { (byte) personalizeData
                                                               .length },
                                          personalizeData)),
                NbtConstants.LE_ABSENT);
    }

    /**
     * Builds the finalize personalization command, which is used to finalize
     * the personalization state and card transitions to operational state.
     *
     * @return Returns the APDU command.
     * @throws ApduException Throws an APDU exception, in case of communication
     *         problems or build command failed.
     */
    public ApduCommand finalizePersonalization() throws ApduException {
        byte[] finalizePersoCmdData = new byte[] { (byte) 0xBF, (byte) 0x63,
                                                   (byte) 0x00 };
        return new ApduCommand(NbtConstants.CLA,
                               NbtConstants.INS_PERSONALIZE_DATA,
                               NbtConstants.P1_DEFAULT, NbtConstants.P2_DEFAULT,
                               finalizePersoCmdData, NbtConstants.LE_ABSENT);
    }
}
