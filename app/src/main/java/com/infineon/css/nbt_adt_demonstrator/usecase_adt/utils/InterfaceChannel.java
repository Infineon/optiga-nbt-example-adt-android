// SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
// SPDX-License-Identifier: MIT

package com.infineon.css.nbt_adt_demonstrator.usecase_adt.utils;

import android.nfc.tech.IsoDep;
import androidx.annotation.NonNull;
import com.infineon.hsw.channel.IChannel;
import java.io.IOException;

/**
 *The InterfaceChannel class acts as the communication channel interface for the NBT APDU library. This
 * channel provides interfaces such as initialize, transceive and terminate, etc. to communicate with an external
 * application or product.
 */
public class InterfaceChannel {

    /**
     * Initializes a IChannel for the NFC interface
     * (could also be used with other communication channels like a PCSC or Aardvark reader on other platforms)
     *
     * @param com The Isodep interface to the tag
     * @return The NFC channel
     */
    public static IChannel InitializeChannel(@NonNull IsoDep com) {

        return new IChannel() {
            @Override
            public void open(boolean b) {
            }

            @Override
            public void close() {
                try {
                    com.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public byte[] connect(byte[] bytes) {
                try {
                    com.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new byte[0];
            }

            @Override
            public byte[] disconnect(byte[] bytes) {
                return new byte[0];
            }

            @Override
            public byte[] reset(byte[] bytes) {
                return new byte[0];
            }

            @Override
            public byte[] transmit(byte[] bytes) {
                try {
                    return com.transceive(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new byte[0];
            }

            @Override
            public byte[] control(byte[] bytes) {
                return new byte[0];
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public boolean isConnected() {
                return com.isConnected();
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }
}
