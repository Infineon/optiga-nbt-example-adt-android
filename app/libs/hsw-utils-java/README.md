# Utilities Java Library

> Offers utility functions to convert, encode, decode, and parse data structures.

This utility library provides various functionalities to handle tasks such as conversion from one data structure to another, parse tlv structures, etc. Examples include conversion of Short to data bytes, conversion of data bytes to hexadecimal string, concatenation of data bytes, etc.

To know more on the functions supported by this library, please check the API documentation. The API documentation can be generated using `gradle javadoc` command and documentation can be found in `build/docs/index.html`.

## Example

The below example illustrates the usage for getting subdata bytes from a byte array based on offset and length.

1. Include headers

   ```java
   import com.infineon.hsw.utils.Tlv;
   import com.infineon.hsw.utils.Utils;
   import java.util.Vector;
   import java.util.List;
   ```

2. Sample usage of Utils method to encode a DGI TLV list

   ```java
   try {
   	byte[] DGI_TLV_DATA_MULTIPLE_TLV = new byte[] {
   		(byte) 0xA0, (byte) 0x01, (byte) 0x01, (byte) 0x0C, (byte) 0xC0,
   		(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0xC5,
   		(byte) 0x06, (byte) 0x01, (byte) 0x08
   	};

   	List<Tlv> tlvList = new Vector<>();
   	tlvList.add(new Tlv(0xA001, null, new byte[] { (byte) 0x0C }));
   	tlvList.add(new Tlv(0xC001, null, new byte[] { (byte) 0x03, (byte) 0x04 }));
   	tlvList.add(new Tlv(0xC506, null, new byte[] { (byte) 0x08 }));

   	byte[] encodedTLVs = Tlv.buildDgiTlvList(tlvList);

   } catch (Exception e) {

   }
   ```
