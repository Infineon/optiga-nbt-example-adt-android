// SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
// SPDX-License-Identifier: MIT

package com.infineon.css.nbt_adt_demonstrator.usecase_adt;

import static com.infineon.css.nbt_adt_demonstrator.usecase_adt.utils.NbtConstants.DEFAULT_OFFSET;
import static com.infineon.css.nbt_adt_demonstrator.usecase_adt.utils.NbtConstants.NBT_ID_PP1_FILE;
import static com.infineon.css.nbt_adt_demonstrator.usecase_adt.utils.NbtConstants.NBT_ID_PP2_FILE;

import androidx.annotation.NonNull;

import com.infineon.hsw.apdu.ApduChannel;
import com.infineon.hsw.apdu.ApduException;
import com.infineon.hsw.apdu.nbt.NbtApduResponse;
import com.infineon.hsw.apdu.nbt.NbtCommandSet;
import com.infineon.hsw.utils.UtilException;

/**
 * The Adt Class represents the ADT Usecase with a NBT sample.
 * In this example the asynchronous data transfer is used to enable or disable a LED on a MCU
 * equipped with an NBT. setStateByAdt() will set the LED state by writing to FILE_1 of the NBTs
 * T4T Application. getStateByAdt() will read the current LED state by reading from FILE_2 of the
 * NBTs T4T Application
 *
 */
public class AdtUsecase {

    /**
     * Instance of the APDU command set
     */
    private final NbtCommandSet commandSet;

    /**
     * Instance of the APDU response
     */
    private NbtApduResponse apduResponse;

    /**
     * Constructor validates parameters and saves them to members
     *
     * @param apduChannel APDU specific channel
     * @throws UtilException Thrown by libraries utils
     * @throws ApduException Thrown by command set of APDU library
     */
    public AdtUsecase(@NonNull ApduChannel apduChannel) throws UtilException, ApduException {
        this.apduResponse = null;
        this.commandSet = new NbtCommandSet(apduChannel, 0);
    }

    /**
     * Sets the state (LED on or off) in PP File1 of the device by sending data using ADT
     *
     * @param state LED state (on or off)
     * @throws UtilException Thrown by libraries utils
     *  @throws ApduException Thrown by command set of APDU library
     */
    public void setStateByAdt(boolean state) throws ApduException, UtilException {

        byte[] stateByte = new byte[]{(byte) 0x00};

        if(state){
            stateByte[0] = (byte)0x01;
        }

        this.apduResponse = this.commandSet.selectApplication();
        this.apduResponse.checkOK();

        this.apduResponse = this.commandSet.selectFile(NBT_ID_PP1_FILE);
        this.apduResponse.checkOK();

        this.apduResponse = this.commandSet.updateBinary(DEFAULT_OFFSET, stateByte);
        this.apduResponse.checkOK();
    }

    /**
     * Reads data from PP File2 of the NBT sample using ADT and returns the current state of the device
     *
     * @return Returns state (LED on or off)
     * @throws UtilException Thrown by libraries utils
     * @throws ApduException Thrown by command set of APDU library
     */
    public boolean getStateByAdt() throws ApduException, UtilException {

        this.apduResponse = this.commandSet.selectApplication();
        this.apduResponse.checkOK();

        this.apduResponse = this.commandSet.selectFile(NBT_ID_PP2_FILE);
        this.apduResponse.checkOK();

        short stateLength = 1;
        this.apduResponse = this.commandSet.readBinary(DEFAULT_OFFSET, stateLength);
        this.apduResponse.checkOK();

        byte[] response = this.apduResponse.getData();

        return response[0] != (byte) 0x00;
    }
}
