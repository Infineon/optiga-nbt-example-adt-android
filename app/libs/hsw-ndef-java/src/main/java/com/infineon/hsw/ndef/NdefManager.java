// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.ndef.records.AbstractRecord;
import com.infineon.hsw.ndef.records.RecordDecoder;
import com.infineon.hsw.ndef.records.RecordEncoder;
import com.infineon.hsw.ndef.records.decoder.*;
import com.infineon.hsw.ndef.records.encoder.*;
import com.infineon.hsw.ndef.records.model.RecordType;
import com.infineon.hsw.ndef.records.rtd.*;
import com.infineon.hsw.utils.annotation.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// cSpell:ignore NLEN
/**
 * Provides methods to encode and decode the NDEF messages.
 */
public final class NdefManager {
    /*
     * Singleton instance of NdefManager.
     */
    private static NdefManager instance;

    private static final String
            ERR_ENCODER_IS_NULL = "Encoder Should not be null";

    /**
     * Private instance of the record encoder to encode the NDEF record.
     */
    private static final RecordEncoder recordEncoder;

    /**
     * Private instance of the record decoder to decode the NDEF record.
     */
    private static final RecordDecoder recordDecoder;

    /**
     * Private instance to store the payload encoders with respect to the record
     * type.
     */
    private static final Map<RecordType, IRecordPayloadEncoder> encoderMap =
            new HashMap<>();

    /**
     * Private instance to store the payload decoders with respect to the record
     * type.
     */
    private static final Map<RecordType, IRecordPayloadDecoder> decoderMap =
            new HashMap<>();

    static {
        // Initializing the record header encoder and decoder.
        recordEncoder = RecordEncoder.getInstance();
        recordDecoder = RecordDecoder.getInstance();

        // Adding the supported encoders and decoders
        encoderMap.put(new RecordType(UriRecord.URI_TYPE),
                       new UriRecordPayloadEncoder());
        decoderMap.put(new RecordType(UriRecord.URI_TYPE),
                       new UriRecordPayloadDecoder());

        encoderMap.put(new RecordType(
                               HandoverSelectRecord.HANDOVER_SELECT_TYPE),
                       new HandoverSelectRecordPayloadEncoder());
        decoderMap.put(new RecordType(
                               HandoverSelectRecord.HANDOVER_SELECT_TYPE),
                       new HandoverSelectRecordPayloadDecoder());

        encoderMap.put(new RecordType(ErrorRecord.ERROR_RECORD_TYPE),
                       new ErrorRecordPayloadEncoder());
        decoderMap.put(new RecordType(ErrorRecord.ERROR_RECORD_TYPE),
                       new ErrorRecordPayloadDecoder());

        encoderMap.put(new RecordType(AlternativeCarrierRecord
                                              .ALTERNATIVE_CARRIER_RECORD_TYPE),
                       new AlternativeCarrierRecordPayloadEncoder());
        decoderMap.put(new RecordType(AlternativeCarrierRecord
                                              .ALTERNATIVE_CARRIER_RECORD_TYPE),
                       new AlternativeCarrierRecordPayloadDecoder());

        encoderMap.put(new RecordType(BluetoothLeRecord.BLE_TYPE),
                       new BluetoothLeRecordPayloadEncoder());
        decoderMap.put(new RecordType(BluetoothLeRecord.BLE_TYPE),
                       new BluetoothLeRecordPayloadDecoder());

        encoderMap.put(new RecordType(BluetoothRecord.BLUETOOTH_TYPE),
                       new BluetoothRecordPayloadEncoder());
        decoderMap.put(new RecordType(BluetoothRecord.BLUETOOTH_TYPE),
                       new BluetoothRecordPayloadDecoder());
    }

    /**
     * Private constructor to restrict object creation.
     */
    private NdefManager() {
        super();
    }

    /*
     * Creates a thread-safe singleton instance of NdefManager.
     */
    public static synchronized NdefManager getInstance() {
        if (instance == null) {
            instance = new NdefManager();
        }
        return instance;
    }

    /**
     * Creates an NDEF message along with {@link IfxNdefRecord}(s) using
     * the raw byte array data.
     *
     * NLEN field should not be present in the input NDEF message.
     *
     * @param data Raw byte array data.
     * @return Returns the {@link IfxNdefMessage} <br>
     *         NDEF message along with {@link IfxNdefRecord}(s)
     * @throws NdefException Throws an NDEF exception if unable to decode NDEF
     * 						 message bytes.
     */
    public IfxNdefMessage decode(@NotNull byte[] data) throws NdefException {
        return new IfxNdefMessage(data);
    }

    /**
     * Encodes an NDEF message with the collection of NDEF records.
     *
     * @param message {@link IfxNdefMessage} <br>
     *                NDEF message along with {@link IfxNdefRecord}(s)
     * @return Returns the {@link IfxNdefMessage} <br>
     *         NDEF message along with {@link IfxNdefRecord}(s)
     * @throws NdefException Throws an NDEF exception if unable to encode the
     *         NDEF message bytes.
     * @throws IOException Throws an IO exception in case of issues in writing
     *         to the byte stream.
     */
    public byte[] encode(@NotNull IfxNdefMessage message)
            throws NdefException, IOException {
        return message.toByteArray(false);
    }

    /**
     * Encodes an NDEF message with the collection of NDEF records and includes
     * the NLEN field as prefix.
     *
     * @param message {@link IfxNdefMessage} <br>
     *                NDEF message along with {@link IfxNdefRecord}(s)
     * @param includeLength Indicates whether the NLEN field should be prefixed
     *         in the NDEF message.
     * @return Returns the {@link IfxNdefMessage} <br>
     *         NDEF message along with {@link IfxNdefRecord}(s)
     * @throws NdefException Throws an NDEF exception if unable to encode the
     *         NDEF message bytes.
     * @throws IOException Throws an IO exception in case of issues in writing
     *         to the byte stream.
     */
    public byte[] encode(@NotNull IfxNdefMessage message,
                         @NotNull boolean includeLength)
            throws NdefException, IOException {
        return message.toByteArray(includeLength);
    }

    /**
     * Decodes the encoded record. Known record types are decoded into
     * instances of their implementation class and can be directly encoded as
     * part of a message.
     *
     * @param ndefRecord Encoded form of NDEF record [{@link IfxNdefRecord}].
     * @return Returns the {@link AbstractRecord} decoded form of NDEF record.
     * @throws NdefException Throws NDEF exception if unable to decode the
     *         record.
     */
    public AbstractRecord decodeRecord(@NotNull byte[] ndefRecord)
            throws NdefException {
        InputStream inputStream = new ByteArrayInputStream(ndefRecord);
        try {
            return recordDecoder.decode(inputStream.read(), inputStream);
        } catch (IOException e) {
            throw new NdefException(e.getMessage(), e);
        }
    }

    /**
     * Encodes the record. Known record types are encoded.
     *
     * @param abstractRecord Decoded form of NDEF record {@link AbstractRecord
     *         }.
     * @return Returns the {@link IfxNdefRecord} Encoded form of NDEF record.
     * @throws NdefException Throws an NDEF exception if unable to encode the
     *     record.
     */
    public byte[] encodeRecord(@NotNull AbstractRecord abstractRecord)
            throws NdefException {
        return recordEncoder.encode(abstractRecord);
    }

    /**
     * Gets the payload encoder from the class. This method gets the
     * encoder from the encoder map and returns it.
     *
     * @param recordType Type of record.
     * @return Returns the record payload encoder.
     */
    public IRecordPayloadEncoder getPayloadEncoder(
            @NotNull RecordType recordType) {
        return encoderMap.get(recordType);
    }

    /**
     * Gets the payload decoder. This method gets the decoder from the
     * decoder map and returns it.
     *
     * @param recordType Type of record.
     * @return Returns the record payload decoder.
     */
    public IRecordPayloadDecoder getPayloadDecoder(
            @NotNull RecordType recordType) {
        return decoderMap.get(recordType);
    }

    /**
     * Registers the payload encoder. This method registers the
     * encoder for the given record type to the encoderMap. Note: If record type
     * is already present it will replace with a new encoder.
     *
     * @param recordType    Type of record.
     * @param recordEncoder Record specific payload encoder.
     * @throws NdefException Throws an NDEF exception if the encoder is NULL.
     */
    public void registerEncoder(@NotNull RecordType recordType,
                                @NotNull IRecordPayloadEncoder recordEncoder)
            throws NdefException {
        if (recordEncoder == null) {
            throw new NdefException(ERR_ENCODER_IS_NULL);
        }
        encoderMap.put(recordType, recordEncoder);
    }

    /**
     * Registers the payload decoder. This method registers the
     * decoder for the given record type to the decoder map. Note: if record
     * type is already present it will replace with a new decoder.
     *
     * @param recordType    Record type for which decoder is to be added to the
     *                      decoder map.
     * @param recordDecoder Record specific payload decoder.
     * @throws NdefException Throws an NDEF exception if the encoder is NULL.
     */
    public void registerDecoder(@NotNull RecordType recordType,
                                @NotNull IRecordPayloadDecoder recordDecoder)
            throws NdefException {
        if (recordDecoder == null) {
            throw new NdefException(ERR_ENCODER_IS_NULL);
        }
        decoderMap.put(recordType, recordDecoder);
    }

    /**
     * Deregister the payload encoder and decoder of a record type.
     *
     * @param recordType    Type of record.
     */
    public void deregister(@NotNull RecordType recordType) {
        if (encoderMap.containsKey(recordType)) {
            encoderMap.remove(recordType);
        }
        if (decoderMap.containsKey(recordType)) {
            decoderMap.remove(recordType);
        }
    }
}
