// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.model;

import com.infineon.hsw.utils.annotation.NotNull;

/**
 * Enumeration represent appearance category assigned values within the assigned
 * ranges of appearance categories.
 */
public enum AppearanceCategory {
    // cspell:ignore HVAC, OLED, Troffer, Oximeter
    /**
     * Unknown appearance category
     */
    UNKNOWN((short) 0x0000),
    /**
     * Phone appearance category
     */
    PHONE((short) 0x0040),
    /**
     * Computer appearance category
     */
    COMPUTER((short) 0x0080),
    /**
     * Desktop workstation appearance category
     */
    DESKTOP_WORKSTATION((short) 0x0081),
    /**
     * Server class computer appearance category
     */
    SERVER_CLASS_COMPUTER((short) 0x0082),
    /**
     * Laptop appearance category
     */
    LAPTOP((short) 0x0083),
    /**
     * Handheld PC/PDA (clamshell) appearance category
     */
    HANDHELD_PC_OR_PDA_CLAMSHELL((short) 0x0084),
    /**
     * Palm size PC/PDA appearance category
     */
    PALM_SIZE_PC_OR_PDA((short) 0x0085),
    /**
     * Wearable computer (watch size) appearance category
     */
    WEARABLE_COMPUTER_WATCH_SIZE((short) 0x0086),
    /**
     * Tablet appearance category
     */
    TABLE((short) 0x0087),
    /**
     * Docking station appearance category
     */
    DOCKING_STATION((short) 0x0088),
    /**
     * All in one appearance category
     */
    ALL_IN_ONE((short) 0x0089),
    /**
     * Blade server appearance category
     */
    BLADE_SERVER((short) 0x008A),
    /**
     * Convertible appearance category
     */
    CONVERTIBLE((short) 0x008B),
    /**
     * Detachable appearance category
     */
    DETACHABLE((short) 0x008C),
    /**
     * IoT gateway appearance category
     */
    IOT_GATEWAY((short) 0x008D),
    /**
     * Mini PC appearance category
     */
    MINI_PC((short) 0x008E),
    /**
     * Stick PC appearance category
     */
    STICK_PC((short) 0x008F),
    /**
     * Watch appearance category
     */
    WATCH((short) 0x00C0),
    /**
     * Sports watch appearance category
     */
    SPORTS_WATCH((short) 0x00C1),
    /**
     * Smart watch appearance category
     */
    SMARTWATCH((short) 0x00C2),
    /**
     * Clock appearance category
     */
    CLOCK((short) 0x0100),
    /**
     * Display appearance category
     */
    DISPLAY((short) 0x0140),
    /**
     * Remote control appearance category
     */
    REMOTE_CONTROL((short) 0x0180),
    /**
     * Eye-glasses appearance category
     */
    EYE_GLASSES((short) 0x01C0),
    /**
     * Tag appearance category
     */
    TAG((short) 0x0200),
    /**
     * Keyring appearance category
     */
    KEYRING((short) 0x0240),
    /**
     * Media player appearance category
     */
    MEDIA_PLAYER((short) 0x0280),
    /**
     * Generic barcode scanner appearance category
     */
    GENERIC_BARCODE_SCANNER((short) 0x02C0),
    /**
     * Thermometer appearance category
     */
    THERMOMETER((short) 0x0300),
    /**
     * Ear thermometer appearance category
     */
    EAR_THERMOMETER((short) 0x0301),
    /**
     * Heart rate sensor appearance category
     */
    HEART_RATE_SENSOR((short) 0x0340),
    /**
     * Heart rate Belt appearance category
     */
    HEART_RATE_BELT((short) 0x0341),
    /**
     * Blood pressure appearance category
     */
    BLOOD_PRESSURE((short) 0x0380),
    /**
     * Arm blood pressure appearance category
     */
    ARM_BLOOD_PRESSURE((short) 0x0381),
    /**
     * Wrist blood pressure appearance category
     */
    WRIST_BLOOD_PRESSURE((short) 0x0382),
    /**
     * Human interface device appearance category
     */
    HUMAN_INTERFACE_DEVICE((short) 0x03C0),
    /**
     * Keyboard appearance category
     */
    KEYBOARD((short) 0x03C1),
    /**
     * Mouse appearance category
     */
    MOUSE((short) 0x03C2),
    /**
     * Joystick appearance category
     */
    JOYSTICK((short) 0x03C3),
    /**
     * Game pad appearance category
     */
    GAME_PAD((short) 0x03C4),
    /**
     * Digitizer tablet appearance category
     */
    DIGITIZER_TABLET((short) 0x03C5),
    /**
     * Card reader appearance category
     */
    CARD_READER((short) 0x03C6),
    /**
     * Digital pen appearance category
     */
    DIGITAL_PEN((short) 0x03C7),
    /**
     * Barcode scanner appearance category
     */
    BARCODE_SCANNER((short) 0x03C8),
    /**
     * Touch pad appearance category
     */
    TOUCH_PAD((short) 0x03C9),
    /**
     * Presentation remote appearance category
     */
    PRESENTATION_REMOTE((short) 0x03CA),
    /**
     * Glucose meter appearance category
     */
    GLUCOSE_METER((short) 0x0400),
    /**
     * Running walking sensor appearance category
     */
    RUNNING_WALKING_SENSOR((short) 0x0440),
    /**
     * In shoe running walking sensor appearance category
     */
    IN_SHOE_RUNNING_WALKING_SENSOR((short) 0x0441),
    /**
     * On shoe running walking sensor appearance category
     */
    ON_SHOE_RUNNING_WALKING_SENSOR((short) 0x0442),
    /**
     * On hip running walking sensor appearance category
     */
    ON_HIP_RUNNING_WALKING_SENSOR((short) 0x0443),
    /**
     * Cycling appearance category
     */
    CYCLING((short) 0x0480),
    /**
     * Cycling compute appearance category
     */
    CYCLING_COMPUTE((short) 0x0481),
    /**
     * Speed sensor appearance category
     */
    SPEED_SENSOR((short) 0x0482),
    /**
     * Cadence sensor appearance category
     */
    CADENCE_SENSOR((short) 0x0483),
    /**
     * Power sensor appearance category
     */
    POWER_SENSOR((short) 0x0484),
    /**
     * Speed and cadence sensor appearance category
     */
    SPEED_AND_CADENCE_SENSOR((short) 0x0485),
    /**
     * Control device appearance category
     */
    CONTROL_DEVICE((short) 0x04C0),
    /**
     * Switch appearance category
     */
    SWITCH((short) 0x04C1),
    /**
     * Multi switch appearance category
     */
    MULTI_SWITCH((short) 0x04C2),
    /**
     * Button appearance category
     */
    BUTTON((short) 0x04C3),
    /**
     * Slider appearance category
     */
    SLIDER((short) 0x04C4),
    /**
     * Rotary switch appearance category
     */
    ROTARY_SWITCH((short) 0x04C5),
    /**
     * Touch panel appearance category
     */
    TOUCH_PANEL((short) 0x04C6),
    /**
     * Single switch appearance category
     */
    SINGLE_SWITCH((short) 0x04C7),
    /**
     * Double switch appearance category
     */
    DOUBLE_SWITCH((short) 0x04C8),
    /**
     * Triple switch appearance category
     */
    TRIPLE_SWITCH((short) 0x04C9),
    /**
     * Battery switch appearance category
     */
    BATTERY_SWITCH((short) 0x04CA),
    /**
     * Energy harvesting Switch appearance category
     */
    ENERGY_HARVESTING_SWITCH((short) 0x04CB),
    /**
     * Push button appearance category
     */
    PUSH_BUTTON((short) 0x04CC),
    /**
     * Network device appearance category
     */
    NETWORK_DEVICE((short) 0x0500),
    /**
     * Access point appearance category
     */
    ACCESS_POINT((short) 0x0501),
    /**
     * Mesh device appearance category
     */
    MESH_DEVICE((short) 0x0502),
    /**
     * Mesh network Proxy appearance category
     */
    MESH_NETWORK_PROXY((short) 0x0503),
    /**
     * Sensor appearance category
     */
    SENSOR((short) 0x0540),
    /**
     * Motion sensor appearance category
     */
    MOTION_SENSOR((short) 0x0541),
    /**
     * Air quality sensor appearance category
     */
    AIR_QUALITY_SENSOR((short) 0x0542),
    /**
     * Temperature sensor appearance category
     */
    TEMPERATURE_SENSOR((short) 0x0543),
    /**
     * Humidity sensor appearance category
     */
    HUMIDITY_SENSOR((short) 0x0544),
    /**
     * Leak sensor appearance category
     */
    LEAK_SENSOR((short) 0x0545),
    /**
     * Smoke sensor appearance category
     */
    SMOKE_SENSOR((short) 0x0546),
    /**
     * Occupancy sensor appearance category
     */
    OCCUPANCY_SENSOR((short) 0x0547),
    /**
     * Contact sensor appearance category
     */
    CONTACT_SENSOR((short) 0x0548),
    /**
     * Carbon monoxide sensor appearance category
     */
    CARBON_MONOXIDE_SENSOR((short) 0x0549),
    /**
     * Carbon dioxide sensor appearance category
     */
    CARBON_DIOXIDE_SENSOR((short) 0x054A),
    /**
     * Ambient light sensor appearance category
     */
    AMBIENT_LIGHT_SENSOR((short) 0x054B),
    /**
     * Energy sensor appearance category
     */
    ENERGY_SENSOR((short) 0x054C),
    /**
     * Color light sensor appearance category
     */
    COLOR_LIGHT_SENSOR((short) 0x054D),
    /**
     * Rain sensor appearance category
     */
    RAIN_SENSOR((short) 0x054E),
    /**
     * Fire sensor appearance category
     */
    FIRE_SENSOR((short) 0x054F),
    /**
     * Wind sensor appearance category
     */
    WIND_SENSOR((short) 0x0550),
    /**
     * Proximity sensor appearance category
     */
    PROXIMITY_SENSOR((short) 0x0551),
    /**
     * Multi sensor appearance category
     */
    MULTI_SENSOR_A((short) 0x0552),
    /**
     * Flush mounted sensor appearance category
     */
    FLUSH_MOUNTED_SENSOR((short) 0x0553),
    /**
     * Ceiling mounted sensor appearance category
     */
    CEILING_MOUNTED_SENSOR((short) 0x0554),
    /**
     * Wall mounted sensor appearance category
     */
    WALL_MOUNTED_SENSOR((short) 0x0555),
    /**
     * Multi sensor appearance category
     */
    MULTI_SENSOR_B((short) 0x0556),
    /**
     * Energy meter appearance category
     */
    ENERGY_METER((short) 0x0557),
    /**
     * Flame detector appearance category
     */
    FLAME_DETECTOR((short) 0x0558),
    /**
     * Vehicle tire pressure sensor appearance category
     */
    VEHICLE_TIRE_PRESSURE_SENSOR((short) 0x0559),
    /**
     * Light fixtures appearance category
     */
    LIGHT_FIXTURES((short) 0x0580),
    /**
     * Wall light appearance category
     */
    WALL_LIGHT((short) 0x0581),
    /**
     * Ceiling light appearance category
     */
    CEILING_LIGHT((short) 0x0582),
    /**
     * Floor light appearance category
     */
    FLOOR_LIGHT((short) 0x0583),
    /**
     * Cabinet light appearance category
     */
    CABINET_LIGHT((short) 0x0584),
    /**
     * Desk light appearance category
     */
    DESK_LIGHT((short) 0x0585),
    /**
     * Troffer light appearance category
     */
    TROFFER_LIGHT((short) 0x0586),
    /**
     * Pendant light appearance category
     */
    PENDANT_LIGHT((short) 0x0587),
    /**
     * In ground light appearance category
     */
    IN_GROUND_LIGHT((short) 0x0588),
    /**
     * Flood light appearance category
     */
    FLOOD_LIGHT((short) 0x0589),
    /**
     * Underwater light appearance category
     */
    UNDERWATER_LIGHT((short) 0x058A),
    /**
     * Bollard with light appearance category
     */
    BOLLARD_WITH_LIGHT((short) 0x058B),
    /**
     * Pathway light appearance category
     */
    PATHWAY_LIGHT((short) 0x058C),
    /**
     * Garden light appearance category
     */
    GARDEN_LIGHT((short) 0x058D),
    /**
     * Pole top light appearance category
     */
    POLE_TOP_LIGHT((short) 0x058E),
    /**
     * Spot light appearance category
     */
    SPOTLIGHT((short) 0x058F),
    /**
     * Linear light appearance category
     */
    LINEAR_LIGHT((short) 0x0590),
    /**
     * Street light appearance category
     */
    STREET_LIGHT((short) 0x0591),
    /**
     * Shelves light appearance category
     */
    SHELVES_LIGHT((short) 0x0592),
    /**
     * Bay light appearance category
     */
    BAY_LIGHT((short) 0x0593),
    /**
     * Emergency exit light appearance category
     */
    EMERGENCY_EXIT_LIGHT((short) 0x0594),
    /**
     * Light controller appearance category
     */
    LIGHT_CONTROLLER((short) 0x0595),
    /**
     * Light driver appearance category
     */
    LIGHT_DRIVER((short) 0x0596),
    /**
     * Bulb appearance category
     */
    BULB((short) 0x0597),
    /**
     * Low bay light appearance category
     */
    LOW_BAY_LIGHT((short) 0x0598),
    /**
     * High bay light appearance category
     */
    HIGH_BAY_LIGHT((short) 0x0599),
    /**
     * Fan appearance category
     */
    FAN((short) 0x05C0),
    /**
     * Ceiling Fan appearance category
     */
    CEILING_FAN((short) 0x05C1),
    /**
     * Axial fan appearance category
     */
    AXIAL_FAN((short) 0x05C2),
    /**
     * Exhaust fan appearance category
     */
    EXHAUST_FAN((short) 0x05C3),
    /**
     * Pedestal fan appearance category
     */
    PEDESTAL_FAN((short) 0x05C4),
    /**
     * Desk fan appearance category
     */
    DESK_FAN((short) 0x05C5),
    /**
     * Wall fan appearance category
     */
    WALL_FAN((short) 0x05C6),
    /**
     * HVAC appearance category
     */
    HVAC((short) 0x0600),
    /**
     * Thermostat appearance category
     */
    THERMOSTAT((short) 0x0601),
    /**
     * Humidifier appearance category
     */
    HUMIDIFIER((short) 0x0602),
    /**
     * Dehumidifier appearance category
     */
    DE_HUMIDIFIER((short) 0x0603),
    /**
     * Heater appearance category
     */
    HEATER((short) 0x0604),
    /**
     * HVAC radiator appearance category
     */
    HVAC_RADIATOR((short) 0x0605),
    /**
     * HVAC boiler appearance category
     */
    HVAC_BOILER((short) 0x0606),
    /**
     * HVAC heat pump appearance category
     */
    HEAT_PUMP((short) 0x0607),
    /**
     * HVAC infrared heater appearance category
     */
    HVAC_INFRARED_HEATER((short) 0x0608),
    /**
     * HVAC radiant panel heater appearance category
     */
    HVAC_RADIANT_PANEL_HEATER((short) 0x0609),
    /**
     * HVAC fan heater appearance category
     */
    HVAC_FAN_HEATER((short) 0x060A),
    /**
     * HVAC air curtain appearance category
     */
    HVAC_AIR_CURTAIN((short) 0x060B),
    /**
     * Air conditioning appearance category
     */
    AIR_CONDITIONING((short) 0x0640),
    /**
     * Generic humidifier appearance category
     */
    GENERIC_HUMIDIFIER((short) 0x0680),
    /**
     * Heating appearance category
     */
    HEATING((short) 0x06C0),
    /**
     * Heating radiator appearance category
     */
    HEATING_RADIATOR((short) 0x06C1),
    /**
     * Heating boiler appearance category
     */
    HEATING_BOILER((short) 0x06C2),
    /**
     * Heating heat pump appearance category
     */
    HEATING_HEAT_PUMP((short) 0x06C3),
    /**
     * Heating infrared heater appearance category
     */
    HEATING_INFRARED_HEATER((short) 0x06C4),
    /**
     * Heating radiant panel heater appearance category
     */
    HEATING_RADIANT_PANEL_HEATER((short) 0x06C5),
    /**
     * Heating fan heater appearance category
     */
    HEATING_FAN_HEATER((short) 0x06C6),
    /**
     * Heating air curtain appearance category
     */
    HEATING_AIR_CURTAIN((short) 0x06C7),
    /**
     * Access control appearance category
     */
    ACCESS_CONTROL((short) 0x0700),
    /**
     * Access door appearance category
     */
    ACCESS_DOOR((short) 0x0701),
    /**
     * Garage door appearance category
     */
    GARAGE_DOOR((short) 0x0702),
    /**
     * Emergency exit door appearance category
     */
    EMERGENCY_EXIT_DOOR((short) 0x0703),
    /**
     * Access lock appearance category
     */
    ACCESS_LOCK((short) 0x0704),
    /**
     * Elevator appearance category
     */
    ELEVATOR((short) 0x0705),
    /**
     * Window appearance category
     */
    WINDOW((short) 0x0706),
    /**
     * Entrance gate appearance category
     */
    ENTRANCE_GATE((short) 0x0707),
    /**
     * Door lock appearance category
     */
    DOOR_LOCK((short) 0x0708),
    /**
     * Locker appearance category
     */
    LOCKER((short) 0x0709),
    /**
     * Motorized device appearance category
     */
    MOTORIZED_DEVICE((short) 0x0740),
    /**
     * Motorized gate appearance category
     */
    MOTORIZED_GATE((short) 0x0741),
    /**
     * Awning appearance category
     */
    AWNING((short) 0x0742),
    /**
     * Blinds or shades appearance category
     */
    BLINDS_OR_SHADES((short) 0x0743),
    /**
     * Curtains appearance category
     */
    CURTAINS((short) 0x0744),
    /**
     * Screen appearance category
     */
    SCREEN((short) 0x0745),
    /**
     * Power device appearance category
     */
    POWER_DEVICE((short) 0x0780),
    /**
     * Power outlet appearance category
     */
    POWER_OUTLET((short) 0x0781),
    /**
     * Power strip appearance category
     */
    POWER_STRIP((short) 0x0782),
    /**
     * Plug appearance category
     */
    PLUG((short) 0x0783),
    /**
     * Power Supply appearance category
     */
    POWER_SUPPLY((short) 0x0784),
    /**
     * LED Driver appearance category
     */
    LED_DRIVER((short) 0x0785),
    /**
     * Fluorescent Lamp Gear appearance category
     */
    FLUORESCENT_LAMP_GEAR((short) 0x0786),
    /**
     * HID lamp Gear appearance category
     */
    HID_LAMP_GEAR((short) 0x0787),
    /**
     * Charge case appearance category
     */
    CHARGE_CASE((short) 0x0788),
    /**
     * Power bank appearance category
     */
    POWER_BANK((short) 0x0789),
    /**
     * Light source appearance category
     */
    LIGHT_SOURCE((short) 0x07C0),
    /**
     * Incandescent Light Bulb appearance category
     */
    INCANDESCENT_LIGHT_BULB((short) 0x07C1),
    /**
     * LED lamp appearance category
     */
    LED_LAMP((short) 0x07C2),
    /**
     * HID lamp appearance category
     */
    HID_LAMP((short) 0x07C3),
    /**
     * Fluorescent Lamp appearance category
     */
    FLUORESCENT_LAMP((short) 0x07C4),
    /**
     * LED Array appearance category
     */
    LED_ARRAY((short) 0x07C5),
    /**
     * MultiColor LED Array appearance category
     */
    MULTICOLOR_LED_ARRAY((short) 0x07C6),
    /**
     * Low voltage halogen appearance category
     */
    LOW_VOLTAGE_HALOGEN((short) 0x07C7),
    /**
     * Organic light emitting diode (OLED) appearance category
     */
    ORGANIC_LIGHT_EMITTING_DIODE_OLED((short) 0x07C8),
    /**
     * Window covering appearance category
     */
    WINDOW_COVERING((short) 0x0800),
    /**
     * Window shades appearance category
     */
    WINDOW_SHADES((short) 0x0801),
    /**
     * Window blinds appearance category
     */
    WINDOW_BLINDS((short) 0x0802),
    /**
     * Window awning appearance category
     */
    WINDOW_AWNING((short) 0x0803),
    /**
     * Window curtain appearance category
     */
    WINDOW_CURTAIN((short) 0x0804),
    /**
     * Exterior shutter appearance category
     */
    EXTERIOR_SHUTTER((short) 0x0805),
    /**
     * Exterior screen appearance category
     */
    EXTERIOR_SCREEN((short) 0x0806),
    /**
     * Audio sink appearance category
     */
    AUDIO_SINK((short) 0x0840),
    /**
     * Standalone speaker appearance category
     */
    STANDALONE_SPEAKER((short) 0x0841),
    /**
     * Soundbar appearance category
     */
    SOUNDBAR((short) 0x0842),
    /**
     * Bookshelf Speaker appearance category
     */
    BOOKSHELF_SPEAKER((short) 0x0843),
    /**
     * Stand mounted Speaker appearance category
     */
    STAND_MOUNTED_SPEAKER((short) 0x0844),
    /**
     * Speaker phone appearance category
     */
    SPEAKER_PHONE((short) 0x0845),
    /**
     * Audio Source appearance category
     */
    AUDIO_SOURCE((short) 0x0880),
    /**
     * Microphone appearance category
     */
    MICROPHONE((short) 0x0881),
    /**
     * Alarm appearance category
     */
    ALARM((short) 0x0882),
    /**
     * Bell appearance category
     */
    BELL((short) 0x0883),
    /**
     * Horn appearance category
     */
    HORN((short) 0x0884),
    /**
     * Broadcasting Device appearance category
     */
    BROADCASTING_DEVICE((short) 0x0885),
    /**
     * Service Desk appearance category
     */
    SERVICE_DESK((short) 0x0886),
    /**
     * Kiosk appearance category
     */
    KIOSK((short) 0x0887),
    /**
     * Broadcasting Room appearance category
     */
    BROADCASTING_ROOM((short) 0x0888),
    /**
     * Auditorium appearance category
     */
    AUDITORIUM((short) 0x0889),
    /**
     * Motorized vehicle appearance category
     */
    MOTORIZED_VEHICLE((short) 0x08C0),
    /**
     * Car appearance category
     */
    CAR((short) 0x08C1),
    /**
     * Large goods vehicle appearance category
     */
    LARGE_GOODS_VEHICLE((short) 0x08C2),
    /**
     * 2 Wheeled vehicle appearance category
     */
    TWO_WHEELED_VEHICLE((short) 0x08C3),
    /**
     * Motorbike appearance category
     */
    MOTORBIKE((short) 0x08C4),
    /**
     * Scooter appearance category
     */
    SCOOTER((short) 0x08C5),
    /**
     * Moped appearance category
     */
    MOPED((short) 0x08C6),
    /**
     * 3 Wheeled vehicle appearance category
     */
    THREE_WHEELED_VEHICLE((short) 0x08C7),
    /**
     * Light Vehicle appearance category
     */
    LIGHT_VEHICLE((short) 0x08C8),
    /**
     * Quad bike appearance category
     */
    QUAD_BIKE((short) 0x08C9),
    /**
     * Minibus appearance category
     */
    MINIBUS((short) 0x08CA),
    /**
     * Bus appearance category
     */
    BUS((short) 0x08CB),
    /**
     * Trolley appearance category
     */
    TROLLEY((short) 0x08CC),
    /**
     * Agricultural vehicle appearance category
     */
    AGRICULTURAL_VEHICLE((short) 0x08CD),
    /**
     * Camper OR caravan appearance category
     */
    CAMPER_OR_CARAVAN((short) 0x08CE),
    /**
     * Recreational vehicle or motor home appearance category
     */
    RECREATIONAL_VEHICLE_OR_MOTOR_HOME((short) 0x08CF),
    /**
     * Domestic appliance appearance category
     */
    DOMESTIC_APPLIANCE((short) 0x0900),
    /**
     * Refrigerator appearance category
     */
    REFRIGERATOR((short) 0x0901),
    /**
     * Freezer appearance category
     */
    FREEZER((short) 0x0902),
    /**
     * Oven appearance category
     */
    OVEN((short) 0x0903),
    /**
     * Microwave appearance category
     */
    MICROWAVE((short) 0x0904),
    /**
     * Toaster appearance category
     */
    TOASTER((short) 0x0905),
    /**
     * Washing Machine appearance category
     */
    WASHING_MACHINE((short) 0x0906),
    /**
     * Dryer appearance category
     */
    DRYER((short) 0x0907),
    /**
     * Coffee maker appearance category
     */
    COFFEE_MAKER((short) 0x0908),
    /**
     * Clothes iron appearance category
     */
    CLOTHES_IRON((short) 0x0909),
    /**
     * Curling iron appearance category
     */
    CURLING_IRON((short) 0x090A),
    /**
     * Hair dryer appearance category
     */
    HAIR_DRYER((short) 0x090B),
    /**
     * Vacuum cleaner appearance category
     */
    VACUUM_CLEANER((short) 0x090C),
    /**
     * Robotic vacuum cleaner appearance category
     */
    ROBOTIC_VACUUM_CLEANER((short) 0x090D),
    /**
     * Rice cooker appearance category
     */
    RICE_COOKER((short) 0x090E),
    /**
     * Clothes steamer appearance category
     */
    CLOTHES_STEAMER((short) 0x090F),
    /**
     * Wearable audio device appearance category
     */
    WEARABLE_AUDIO_DEVICE((short) 0x0940),
    /**
     * Earbud appearance category
     */
    EARBUD((short) 0x0941),
    /**
     * Headset appearance category
     */
    HEADSET((short) 0x0942),
    /**
     * Headphones appearance category
     */
    HEADPHONES((short) 0x0943),
    /**
     * Neck band appearance category
     */
    NECK_BAND((short) 0x0944),
    /**
     * Aircraft appearance category
     */
    AIRCRAFT((short) 0x0980),
    /**
     * Light aircraft appearance category
     */
    LIGHT_AIRCRAFT((short) 0x0981),
    /**
     * Microlight appearance category
     */
    MICROLIGHT((short) 0x0982),
    /**
     * Paraglider appearance category
     */
    PARAGLIDER((short) 0x0983),
    /**
     * Large passenger aircraft appearance category
     */
    LARGE_PASSENGER_AIRCRAFT((short) 0x0984),
    /**
     * AV equipment appearance category
     */
    AV_EQUIPMENT((short) 0x09C0),
    /**
     * Amplifier appearance category
     */
    AMPLIFIER((short) 0x09C1),
    /**
     * Receiver appearance category
     */
    RECEIVER((short) 0x09C2),
    /**
     * Radio appearance category
     */
    RADIO((short) 0x09C3),
    /**
     * Tuner appearance category
     */
    TUNER((short) 0x09C4),
    /**
     * Turntable appearance category
     */
    TURNTABLE((short) 0x09C5),
    /**
     * CD player appearance category
     */
    CD_PLAYER((short) 0x09C6),
    /**
     * DVD player appearance category
     */
    DVD_PLAYER((short) 0x09C7),
    /**
     * Blu ray player appearance category
     */
    BLU_RAY_PLAYER((short) 0x09C8),
    /**
     * Optical disc player appearance category
     */
    OPTICAL_DISC_PLAYER((short) 0x09C9),
    /**
     * Set top box appearance category
     */
    SET_TOP_BOX((short) 0x09CA),
    /**
     * Display equipment appearance category
     */
    DISPLAY_EQUIPMENT((short) 0x0A00),
    /**
     * Television appearance category
     */
    TELEVISION((short) 0x0A01),
    /**
     * Monitor appearance category
     */
    MONITOR((short) 0x0A02),
    /**
     * Projector appearance category
     */
    PROJECTOR((short) 0x0A03),
    /**
     * Hearing aid appearance category
     */
    HEARING_AID((short) 0x0A40),
    /**
     * In ear hearing aid appearance category
     */
    IN_EAR_HEARING_AID((short) 0x0A41),
    /**
     * Behind ear hearing aid appearance category
     */
    BEHIND_EAR_HEARING_AID((short) 0x0A42),
    /**
     * Cochlear implant appearance category
     */
    COCHLEAR_IMPLANT((short) 0x0A43),
    /**
     * Generic gaming appearance category
     */
    GENERIC_GAMING((short) 0x0A80),
    /**
     * Home video game console appearance category
     */
    HOME_VIDEO_GAME_CONSOLE((short) 0x0A81),
    /**
     * Portable handheld console appearance category
     */
    PORTABLE_HANDHELD_CONSOLE((short) 0x0A82),
    /**
     * Generic signage appearance category
     */
    GENERIC_SIGNAGE((short) 0x0AC0),
    /**
     * Digital signage appearance category
     */
    DIGITAL_SIGNAGE((short) 0x0AC1),
    /**
     * Electronic label appearance category
     */
    ELECTRONIC_LABEL((short) 0x0AC2),
    /**
     * Generic pulse oximeter appearance category
     */
    GENERIC_PULSE_OXIMETER((short) 0x0C40),
    /**
     * Fingertip pulse oximeter appearance category
     */
    FINGERTIP_PULSE_OXIMETER((short) 0x0C41),
    /**
     * Wrist worn pulse oximeter appearance category
     */
    WRIST_WORN_PULSE_OXIMETER((short) 0x0C42),
    /**
     * Generic weight scale appearance category
     */
    GENERIC_WEIGHT_SCALE((short) 0x0C80),
    /**
     * Generic personal mobility device appearance category
     */
    GENERIC_PERSONAL_MOBILITY_DEVICE((short) 0x0CC0),
    /**
     * Powered wheelchair appearance category
     */
    POWERED_WHEELCHAIR((short) 0x0CC1),
    /**
     * Mobility scooter appearance category
     */
    MOBILITY_SCOOTER((short) 0x0CC2),
    /**
     * Generic continuous glucose monitor appearance category
     */
    GENERIC_CONTINUOUS_GLUCOSE_MONITOR((short) 0x0D00),
    /**
     * Generic insulin pump appearance category
     */
    GENERIC_INSULIN_PUMP((short) 0x0D40),
    /**
     * Insulin pump and durable pump appearance category
     */
    INSULIN_PUMP_AND_DURABLE_PUMP((short) 0x0D41),
    /**
     * Insulin pump and patch pump appearance category
     */
    INSULIN_PUMP_AND_PATCH_PUMP((short) 0x0D44),
    /**
     * Insulin pen appearance category
     */
    INSULIN_PEN((short) 0x0D48),
    /**
     * Generic medication delivery appearance category
     */
    GENERIC_MEDICATION_DELIVERY((short) 0x0D80),
    /**
     * Generic outdoor sports activity appearance category
     */
    GENERIC_OUTDOOR_SPORTS_ACTIVITY((short) 0x1440),
    /**
     * Location display appearance category
     */
    LOCATION_DISPLAY((short) 0x1441),
    /**
     * Location and navigation display appearance category
     */
    LOCATION_AND_NAVIGATION_DISPLAY((short) 0x1442),
    /**
     * Location pod appearance category
     */
    LOCATION_POD((short) 0x1443),
    /**
     * Location and navigation pod appearance category
     */
    LOCATION_AND_NAVIGATION_POD((short) 0x1444);

    /**
     * Stores the category assigned value.
     */
    private final Short value;

    /**
     * Private enumeration constructor
     *
     * @param value Category assigned value
     */
    AppearanceCategory(@NotNull final Short value) {
        this.value = value;
    }

    /**
     * Gets the category assigned value.
     *
     * @return Returns the category assigned value
     */
    public Short getValue() {
        return value;
    }

    /**
     * Returns the appearance category name.
     *
     * @return Returns the appearance category name matching with respect to the
     *     value.
     */
    public String getName() {
        return this.name().replace('_', ' ');
    }

    /**
     * Gets the enumeration with respect to the value.
     *
     * @param value Known enumeration value
     * @return Returns the enumeration registers with respect to the value.
     */
    public static AppearanceCategory getEnumByValue(@NotNull short value) {
        for (AppearanceCategory enumCategory : AppearanceCategory.values()) {
            if (enumCategory.getValue().equals(value)) {
                return enumCategory;
            }
        }
        return null;
    }
}
