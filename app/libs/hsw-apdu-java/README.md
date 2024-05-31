# APDU Library - Java

This library offers utilities to encode and decode [APDU](https://en.wikipedia.org/wiki/Smart_card_application_protocol_data_unit) objects as well as their responses.

In smart card applications, a host typically sends binary `APDU` data to a secure element and reads back a response. This binary data may be somewhat difficult to create and understand depending on the length of the data, etc. This library can be used to work with `APDU`'s in a more generic and structured way.

To know more on the functions supported by this library, please check the API documentation. The API documentation can be generated using `gradle javadoc` command and documentation can be found in `build/docs/index.html`.

## Example

1. Include headers

   ```java
   import com.infineon.hsw.apdu.ApduException;
   import com.infineon.hsw.apdu.ApduResponse;
   import com.infineon.hsw.apdu.ApduCommand;
   import com.infineon.hsw.apdu.ApduCommandSet;
   import com.infineon.hsw.apdu.ApduChannel;
   ```

2. Build a C-APDU and send it via the initialised channel and and receive the R-APDU. R-APDU is parsed and its status word (SW) is extracted

   ```java
   static final byte CLA = (byte)0x00;
   static final byte INS = (byte)0xA4;
   static final byte P1 = (byte)0x04;
   static final byte P2 = (byte)0x00;
   static final byte LC = (byte)0x07;
   static final byte LE = (byte)0x00;
   static final byte[] AID = { (byte) 0xD2, (byte) 0x76, (byte) 0x00,(byte) 0x00, (byte) 0x85, (byte) 0x01,(byte) 0x01 };

   ApduCommand selectCmd = new ApduCommand(CLA, INS_SELECT, P1, P2, AID, LE);
   // Initialize the APDU channel with communication channel (For example, PCSC )
   ApduChannel apduChannel = new ApduChannel(channel);
   ApduCommandSet commandSet = new ApduCommandSet(apduChannel, 0);
   apduChannel.connect();
   ApduResponse apduResponse;
   apduResponse = commandSet.send();
   apduResponse.checkStatus();
   apduResponse.getSW();

   ```
