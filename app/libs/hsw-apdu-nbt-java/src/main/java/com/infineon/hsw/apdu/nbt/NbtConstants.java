// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.apdu.nbt;

/**
 * Stores the constants used in the NBT library.
 */
public final class NbtConstants {
    /**
     * Constructor for NBT constants.
     */
    /* default */ NbtConstants() {
    }

    /**
     * AID of the NBT application
     */
    static final byte[] AID = { (byte) 0xD2, (byte) 0x76, (byte) 0x00,
                                (byte) 0x00, (byte) 0x85, (byte) 0x01,
                                (byte) 0x01 };

    /**
     * AID of the NBT product configuration application
     */
    static final byte[] CONFIGURATOR_AID = {
        (byte) 0xD2, (byte) 0x76, (byte) 0x00, (byte) 0x00, (byte) 0x04,
        (byte) 0x15, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x0B,
        (byte) 0x00, (byte) 0x01, (byte) 0x01
    };

    /**
     * First five bits of byte are set to mask password ID for
     * change/unblock password.
     */
    public static final byte ID_MASK_BITS = (byte) 0x1F;

    /**
     * FAP FileID
     */
    public static final short FAP_FILE_ID = (short) 0xE1AF;

    /**
     * NDEF FileID
     */
    public static final short NDEF_FILE_ID = (short) 0xE104;

    /**
     * Class byte for the NBT command: General.
     */
    public static final byte CLA = (byte) 0x00;

    /**
     * Class byte for the product configuration Set/Get APDU.
     */
    public static final byte CLA_CONFIGURATION = (byte) 0x20;

    /**
     * Instruction byte for the NBT command: Select file.
     */
    public static final byte INS_SELECT = (byte) 0xA4;

    /**
     * Instruction byte for the NBT command: Create password.
     */
    public static final byte INS_CREATE_PWD = (byte) 0xE1;

    /**
     * Instruction byte for the NBT command: Personalize data.
     */
    public static final byte INS_PERSONALIZE_DATA = (byte) 0xE2;

    /**
     * Instruction byte for the NBT command: Update binary.
     */
    public static final byte INS_UPDATE_BINARY = (byte) 0xD6;

    /**
     * Instruction byte for the NBT command: Read binary.
     */
    public static final byte INS_READ_BINARY = (byte) 0xB0;

    /**
     * Instruction byte for the NBT command: Delete password.
     */
    public static final byte INS_DELETE_PWD = (byte) 0xE4;

    /**
     * Instruction byte for the NBT command: Authenticate tag.
     */
    public static final byte INS_AUTHENTICATE_TAG = (byte) 0x88;

    /**
     * Instruction byte for the NBT command: Unblock password.
     */
    public static final byte INS_UNBLOCK_PASSWORD = (byte) 0x24;

    /**
     * Instruction byte for the NBT command: Get data.
     */
    public static final byte INS_GET_DATA = (byte) 0x30;

    /**
     * Instruction byte for the NBT command: Change password.
     */
    public static final byte INS_CHANGE_PASSWORD = (byte) 0x24;

    /**
     * Instruction byte for the product configuration Set APDU.
     */
    public static final byte INS_SET_CONFIGURATION = (byte) 0x20;

    /**
     * Instruction byte for the product configuration Get APDU.
     */
    public static final byte INS_GET_CONFIGURATION = (byte) 0x30;

    /**
     * The reference control parameter P1 for the NBT command: Wherever 00 is
     * passed.
     */
    public static final byte P1_DEFAULT = (byte) 0x00;

    /**
     * P1 for the select application command.
     */
    public static final byte P1_SELECT_APPLICATION = (byte) 0x04;

    /**
     * The reference control parameter P2 for the NBT command: Wherever 00 is
     * passed.
     */
    public static final byte P2_DEFAULT = (byte) 0x00;

    /**
     * The reference control parameter tag for the NBT command: Get data with
     * applet version.
     */
    public static final short TAG_APPLET_VERSION = (short) 0xDF3A;

    /**
     * The reference control parameter tag for the NBT command: Get data with
     * available memory.
     */
    public static final short TAG_AVAILABLE_MEMORY = (short) 0xDF3B;

    /**
     * The reference control parameter P2 for the NBT command: Select only first
     * occurrence.
     */
    public static final byte P2_SELECT_FIRST = (byte) 0x0C;

    /**
     * The reference control parameter P2 for the NBT command: bit 7 is set for
     * change password.
     */
    public static final byte P2_CHANGE_PWD = 0x40;

    /**
     * The APDU expected response length(Le) for the NBT commands, if Le is not
     * needed.
     */
    public static final byte LE_ABSENT = (byte) 0x00;

    /**
     * The APDU expected response length(Le) for the NBT commands, if expected
     * length 0 is present in Le.
     */
    public static final short LE_ANY = 256;

    /**
     * Constant defines the length of read or write password field.
     */
    public static final byte PWD_LENGTH = (byte) 0x04;

    /**
     * Constant defines the length of policy bytes field.
     */
    public static final byte POLICY_FIELD_LENGTH = (byte) 0x04;

    /**
     * Constant defines the length of FileID.
     */
    public static final byte FILE_ID_LENGTH = (byte) 0x02;

    /**
     * Constant defines the LC as null, if LC is not present.
     */
    public static final Object LC_NOT_PRESENT = null;

    /**
     * TAG defines a password as read password.
     */
    public static final byte TAG_PWD_READ = (byte) 0x52;

    /**
     * TAG defines a password as write password.
     */
    public static final byte TAG_PWD_WRITE = (byte) 0x54;

    /**
     * Constant of the offset denoting the start of file.
     */
    public static final short OFFSET_FILE_START = (byte) 0x0000;

    /**
     * First five bits of byte are set to mask password ID passed by user in
     * case of change/block password.
     */
    public static final byte PASSWORD_ID_MASK = (byte) 0x1F;

    /**
     * Constant defines the length of password length bytes field.
     */
    public static final byte PASSWORD_LENGTH = (byte) 0x04;

    /**
     * Constant for the empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * Constant defines the offset value pointing to start of file.
     */
    public static final short FILE_START_OFFSET = (short) 0x0000;

    /**
     * Constant defines the offset value pointing to start of NDEF message in
     * file.
     */
    public static final short T4T_NDEF_MSG_START_OFFSET = (short) 0x0002;

    /**
     * Constant defines the maximum possible LE value.
     */
    public static final short MAX_LE = (short) 0x0100;

    /**
     * Constant defines the maximum possible LC value.
     */
    public static final short MAX_LC = (short) 0x00FF;

    // cSpell:ignore GPIO
    /**
     * Enumeration defines the list of tags and length of the configuration
     * data.
     */
    enum ConfigurationTags {
        /** Tag ID for the product short name */
        TAG_PRODUCT_SHORT_NAME((short) 0xC020, 16),
        /** Tag ID for the product life cycle */
        TAG_PRODUCT_LIFE_CYCLE((short) 0xC021, 2),
        /** Tag ID for the software version information */
        TAG_SW_VERSION_INFO((short) 0xC022, 8),
        /** Tag ID for the Flash Loader */
        TAG_FLASH_LOADER((short) 0xC02F, 2),
        /** Tag ID for the GPIO function */
        TAG_GPIO_FUNCTION((short) 0xC030, 1),
        /** Tag ID for the GPIO assert level */
        TAG_GPIO_ASSERT_LEVEL((short) 0xC031, 1),
        /** Tag ID for the GPIO output type */
        TAG_GPIO_OUTPUT_TYPE((short) 0xC032, 1),
        /** Tag ID for the GPIO pull type */
        TAG_GPIO_PULL_TYPE((short) 0xC033, 1),
        /** Tag ID for the I2C idle timeout */
        TAG_I2C_IDLE_TIMEOUT((short) 0xC040, 4),
        /** Tag ID for the I2C drive strength */
        TAG_I2C_DRIVE_STRENGTH((short) 0xC041, 1),
        /** Tag ID for the I2C speed */
        TAG_I2C_SPEED((short) 0xC042, 1),
        /** Tag ID for the NFC IRQ event type */
        TAG_NFC_IRQ_EVENT_TYPE((short) 0xC034, 1),
        /** Tag ID for the NFC ATS configuration */
        TAG_NFC_ATS_CONFIGURATION((short) 0xC050, 14),
        /** Tag ID for the NFC WTX mode */
        TAG_NFC_WTX_MODE((short) 0xC051, 1),
        /** Tag ID for the NFC RF hardware configuration */
        TAG_NFC_RF_HW_CONFIGURATION((short) 0xC052, 312),
        /** Tag ID for the NFC UID type for anti collision */
        TAG_NFC_UID_TYPE_FOR_ANTI_COLLISION((short) 0xC053, 1),
        /** Tag ID for the communication interface */
        TAG_COMMUNICATION_INTERFACE_ENABLE((short) 0xC060, 1);

        private final short tag;
        private final int length;

        /**
         * Constructor for the configuration tags.
         *
         * @param tag    Short data type for the tag.
         * @param length Length of the int data type.
         */
        ConfigurationTags(short tag, int length) {
            this.tag = tag;
            this.length = length;
        }

        /**
         * Public function to return the tag.
         *
         * @return Returns the short data type DGI.
         */
        public short getTag() {
            return this.tag;
        }

        /**
         * Public function to return the length.
         *
         * @return Returns the DGI length.
         */
        public int getLength() {
            return this.length;
        }
    }

    /**
     * Enumeration defines the tag values of product life cycle.
     *
     */
    enum ConfigProductLifeCycleState {
        /** Value of product life cycle - operational */
        PRODUCT_LIFE_CYCLE_OPERATIONAL((short) 0xC33C),
        /** Value of product life cycle - personalization */
        PRODUCT_LIFE_CYCLE_PERSONALIZATION((short) 0x5AA5);

        private final short value;

        /**
         * Constructor for configuration value
         *
         * @param value Short data type for value
         */
        ConfigProductLifeCycleState(short value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the short data type value.
         */
        public short getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values for Flash Loader.
     *
     * Note: Use this configuration value setting cautiously as this makes
     *       device go into Flash Loader mode.
     *       Reverting to application mode is possible, only in engineering
     *       samples and not in any other samples.
     */
    enum ConfigFlashLoader {
        /** Configuration value of Flash Loader - Enable */
        FLASH_LOADER_ENABLE((short) 0xAC95),
        /** Configuration value of Flash Loader - Disable */
        FLASH_LOADER_DISABLE((short) 0xFFFF);

        private final short value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Short data type for value
         */
        ConfigFlashLoader(short value) {
            this.value = value;
        }

        /**
         * Public function to return the configuration value.
         *
         * @return Returns the configuration value as short.
         */
        public short getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values of GPIO function.
     */
    enum ConfigGpioFunction {
        /** Configuration value for the GPIO function - Disabled */
        GPIO_FUNCTION_DISABLED((byte) 0x01),
        /** Configuration value for the GPIO function - NFC IRQ output */
        GPIO_FUNCTION_NFC_IRQ_OUTPUT((byte) 0x02),
        /**
         * Configuration value for the GPIO function - I2C data ready IRQ
         * output
         */
        GPIO_FUNCTION_I2C_DATA_READY_IRQ_OUTPUT((byte) 0x03),
        /**
         * Configuration value for the GPIO function - NFC I2C pass-through IRQ
         * output
         */
        GPIO_FUNCTION_NFC_I2C_PASS_THROUGH_IRQ_OUTPUT((byte) 0x04);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigGpioFunction(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values of GPIO assert level.
     *
     */
    enum ConfigGpioAssertLevel {
        /** Configuration value for the GPIO assert level - Low active */
        GPIO_ASSERT_LOW_LEVEL_ACTIVE((byte) 0x01),
        /** Configuration value for the GPIO assert level - High active */
        GPIO_ASSERT_HIGH_LEVEL_ACTIVE((byte) 0x02);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigGpioAssertLevel(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values of GPIO output type.
     *
     */
    enum ConfigGpioOutputType {
        /** Configuration value for the GPIO output type - Push pull */
        GPIO_OUTPUT_TYPE_PUSH_PULL((byte) 0x01),
        /** Configuration value for the GPIO output type - Open drain */
        GPIO_OUTPUT_TYPE_OPEN_DRAIN((byte) 0x02);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigGpioOutputType(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values of GPIO pull type.
     *
     */
    enum ConfigGpioPullType {
        /** Configuration value for the GPIO pull type - No pull */
        GPIO_NO_PULL((byte) 0x01),
        /** Configuration value for the GPIO pull type - Pull up */
        GPIO_PULL_UP((byte) 0x02),
        /** Configuration value for the GPIO pull type - Pull down */
        GPIO_PULL_DOWN((byte) 0x03);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigGpioPullType(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values of NFC IRQ event.
     *
     */
    enum ConfigNfcIrqEventType {
        /**
         * Configuration value for the NFC IRQ event type - Signal field
         * presence
         */
        NFC_IRQ_EVENT_SIGNAL_FIELD_PRESENCE((byte) 0x01),
        /**
         * Configuration value for the NFC IRQ event type - Signal layer 4
         * entry
         */
        NFC_IRQ_EVENT_SIGNAL_LAYER_4_ENTRY((byte) 0x02),
        /**
         * Configuration value for the NFC IRQ event type - Signal APDU
         * processing stage
         */
        NFC_IRQ_EVENT_SIGNAL_APDU_PROCESSING_STAGE((byte) 0x03);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigNfcIrqEventType(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the tag values of I2C drive strength.
     *
     */
    enum ConfigI2cDriveStrength {
        /** Configuration value for the I2C drive strength - weak */
        I2C_DRIVE_STRENGTH_WEAK((byte) 0x01),
        /** Configuration value for the I2C drive strength - strong */
        I2C_DRIVE_STRENGTH_STRONG((byte) 0x02);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigI2cDriveStrength(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values of I2C speed.
     *
     */
    enum ConfigI2cSpeed {
        /** Configuration value for the I2C speed - 400 kHz */
        I2C_SPEED_400_KHZ((byte) 0x01),
        /** Configuration value for the I2C speed - 1000 kHz */
        I2C_SPEED_1000_KHZ((byte) 0x02);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigI2cSpeed(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    /**
     * Enumeration defines the configuration values of NFC uid type for anti
     * collision.
     *
     */
    enum ConfigNfcUidTypeForAntiCollision {
        /**
         * Configuration value for the unique 7-byte device specific NFC UID
         * type for anti collision.
         */
        UNIQUE_DEVICE_SPECIFIC_7_BYTE_NFC_UID((byte) 0x00),
        /**
         * Configuration value for the random 4-byte NFC UID Type for anti
         * collision
         */
        RANDOM_4_BYTE_NFC_UID((byte) 0x01);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigNfcUidTypeForAntiCollision(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }

    // cSpell:ignore INTF
    /**
     * Enumeration defines the configuration values of communication interface.
     */
    enum ConfigCommunicationInterface {
        /**
         * Configuration value for the communication interface - NFC disabled,
         * I2C enabled
         */
        COMM_INTF_NFC_DISABLED_I2C_ENABLED((byte) 0x01),
        /**
         * Configuration value for the communication interface - NFC enabled,
         * I2C disabled
         */
        COMM_INTF_NFC_ENABLED_I2C_DISABLED((byte) 0x10),
        /**
         * Configuration value for the communication interface - NFC enabled,
         * I2C enabled
         */
        COMM_INTF_NFC_ENABLED_I2C_ENABLED((byte) 0x11);

        private final byte value;

        /**
         * Constructor for the configuration value.
         *
         * @param value Byte data type for value
         */
        ConfigCommunicationInterface(byte value) {
            this.value = value;
        }

        /**
         * Public function to return the value.
         *
         * @return Returns the byte data type value.
         */
        public byte getValue() {
            return this.value;
        }
    }
}
