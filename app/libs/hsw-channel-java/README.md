# APDU Channel Java Library

> Basis for all synchronous data stream communication to a security device.

A channel can be obtained by calling the static methods of the ChannelFactory class and APDUs can be transmitted and received via this channel.

To know more on the functions supported by this library, please check the API documentation. The API documentation can be generated using `gradle javadoc` command and documentation can be found in `build/docs/index.html`.

## Usage

1. Include headers

   ```java
   import com.infineon.hsw.channel.ChannelException;
   import com.infineon.hsw.channel.ChannelFactory;
   ```

2. Implement channel provider with the help of IChannel interface

   ```java
   // For eg; PcscChannelProvider()
   // code placeholder
   ```

3. Include channel provider headers

   ```java
   // import <channel_provider_headers>;
   ```

4. Implement a function to register the channel provider with the ChannelFactory.

## Example

Below example illustrates PCSC channel provider:

```java
   PcscChannelProvider pcscChannelProvider = new PcscChannelProvider();
   String channelNames[] = pcscChannelProvider.getChannelNames();
   ChannelFactory.registerProvider(pcscChannelProvider);
```

Initialise the channel provider using ChannelFactory. Below example illustrates PCSC channel provider:

```java
   PcscChannel channel = (PcscChannel) ChannelFactory.getChannel(channelNames[selectedChannelIndex]);
   byte[] response = channel.connect(null);
```

Transmit an C-APDU to select application via the initialised channel and receive R-APDU via the same channel:

```java
   byte[] commandSelectApplication = new byte[] {0x00, (byte)0xA4, 0x04, 0x00, 0x07, (byte) 0xD2, 0x76,0x00, 0x00, (byte)0x85, 0x01, 0x01, 0x00};
   byte[] apduResponse = channel.transmit(commandSelectApplication);
```
