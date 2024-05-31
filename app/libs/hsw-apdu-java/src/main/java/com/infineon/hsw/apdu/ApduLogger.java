// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger class for APDU related logging. This class provides convenience
 * methods on top of the standard Logger class allowing simple logging of
 * APDU related events.
 */
public class ApduLogger extends Logger implements IApduService {
    /**
     * Constructor.
     *
     * @param name           name of the logger.
     * @param resourceBundle resource bundle associated with logger.
     */
    public ApduLogger(String name, String resourceBundle) {
        super(name, resourceBundle);
        setUseParentHandlers(false);

        try {
            ConsoleHandler oHandler = new ConsoleHandler();
            oHandler.setFormatter(new ApduFormatter());
            addHandler(oHandler);
            setLevel(Level.INFO);

        } catch (SecurityException e) {
            log(Level.WARNING,
                "Initialization failed because of a security problem", e);
        }
    }

    /**
     * Log warning message without additional parameters.
     *
     * @param message warning message.
     */
    @Override
    public void warning(String message) {
        log(Level.WARNING, message);
    }

    /**
     * Log info message with optional parameters specified as parameter array.
     *
     * @param message info message.
     * @param param   message parameters in object array form.
     */
    public void info(String message, Object[] param) {
        log(Level.INFO, message, param);
    }

    /**
     * Log info message with one optional parameter.
     *
     * @param message info message.
     * @param param   message parameter.
     */
    public void info(String message, Object param) {
        log(Level.INFO, message, param);
    }

    /**
     * Log info message without additional parameters.
     *
     * @param message info message.
     */
    @Override
    public void info(String message) {
        log(Level.INFO, message);
    }

    @Override
    public ApduCommand processCommand(ApduCommand apdu) throws ApduException {
        info("", apdu);
        return apdu;
    }

    @Override
    public ApduResponse processResponse(ApduResponse apdu)
            throws ApduException {
        info("", apdu);
        return apdu;
    }

    @Override
    public int getServiceType() {
        return SVC_COM_CHN_LOGGER;
    }
}
