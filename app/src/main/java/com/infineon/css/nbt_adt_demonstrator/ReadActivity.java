// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
// SPDX-License-Identifier: MIT

package com.infineon.css.nbt_adt_demonstrator;

import static com.infineon.css.nbt_adt_demonstrator.usecase_adt.utils.InterfaceChannel.InitializeChannel;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.infineon.css.nbt_adt_demonstrator.usecase_adt.AdtUsecase;
import com.infineon.hsw.apdu.ApduChannel;
import com.infineon.hsw.channel.IChannel;

/**
 * ReadActivity() allows the user to test the Asynchronous Data Transfer use case by reading a
 * simple light state (on/off) from the NFC device
 */
public class ReadActivity extends AppCompatActivity {

    /**
     * GUI text view printing the overall nfc read out state
     */
    TextView textViewState;

    /**
     * GUI text view printing the overall nfc read out state
     */
    TextView textViewUserInteraction;

    /**
     * Button to return to the main activity
     */
    ImageButton buttonReturn;

    /**
     * Button to control LED
     */
    ImageView imageState;

    /**
     * Waiting time until activity resets
     */
    private static final int WAITING_TIME = 3000;

    /**
     * onCreate() initializes variables and essential GUI elements
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        //GUI initialization
        imageState = findViewById(R.id.image_state);
        imageState.setImageResource(R.drawable.icon_light_lightgrey);
        textViewState = findViewById(R.id.textView_state);
        textViewUserInteraction = findViewById(R.id.textView_cardTap);
        buttonReturn = findViewById(R.id.button_return);

        //Return button allows the user to return to the main activity
        buttonReturn.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Setting intent to recognized NFC Type A tags with NFC interface
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        IntentFilter[] intentFiltersArray = new IntentFilter[] {ndef, };
        String[][] techListsArray = new String[][] { new String[] { NfcA.class.getName() } };
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    /**
     * Intent is called when a NFC tag is detected. Communication channel is opened and further
     * use case steps are triggered
     *
     * @param intent NFC tag intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            IsoDep com = IsoDep.get(tag);
            IChannel channel = InitializeChannel(com);
            ApduChannel apduChannel = new ApduChannel(channel);

            handleUsecase(apduChannel);

        }
    }

    /**
     * NFC communication channel is used to trigger further steps of the use case
     *
     * @param apduChannel NFC communication channel
     */
    private void handleUsecase(ApduChannel apduChannel) {

        try {
            apduChannel.connect();

            AdtUsecase adtUsecase = new AdtUsecase(apduChannel);

            //set GUI according to verification state
            if (adtUsecase.getStateByAdt()) {
                setGuiOn();
            } else {
                setGuiOff();
            }

            apduChannel.disconnect();

        } catch (Exception e) {
            runOnUiThread(() -> {
                textViewState.setText(R.string.string_error);
                textViewState.append("\n\n" + e.getMessage());
            });
            resetFromError();
        }
    }


    /**
     * Sets the user output and the GUI according to the read device state
     */
    public void setGuiOff() {
        runOnUiThread(() -> {
            textViewState.setText(R.string.string_led_off);
            imageState.setImageResource(R.drawable.icon_light_lightgrey);
        });
    }

    /**
     * Sets the user output and the GUI according to the read device state
     */
    public void setGuiOn() {
        runOnUiThread(() -> {
            textViewState.setText(R.string.string_led_on);
            imageState.setImageResource(R.drawable.icon_light_green);
        });
    }

    /**
     * In case of error the user instruction will be reset after a few seconds
     */
    private void resetFromError() {
        runOnUiThread(() -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
            textViewUserInteraction.setText(R.string.string_read_tap);
            imageState.setImageResource(R.drawable.icon_light_lightgrey);
        }, WAITING_TIME));
    }
}

