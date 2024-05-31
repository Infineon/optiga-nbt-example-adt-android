// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
// SPDX-License-Identifier: MIT

package com.infineon.css.nbt_adt_demonstrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * This demonstrator allows the user to test Asynchronous Data Transfer capabilities of a NBT device.
 * MainActivity() enables the user to choose between the write (enable or disable a LED) or read
 * (read current LED status) use case
 */
public class MainActivity extends AppCompatActivity {

    /**
     * GUI text view printing basic instructions/information to the user
     */
    TextView textViewUserInteraction;

    /**
     * Button to open ADT read activity
     */
    ImageButton buttonRead;

    /**
     * Button to open ADT write activity
     */
    ImageButton buttonWrite;

    /**
     * onCreate() initializes variables and essential GUI elements
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GUI initialization
        buttonRead = findViewById(R.id.image_read_state);
        buttonWrite = findViewById(R.id.button_write);
        textViewUserInteraction = findViewById(R.id.textView_cardTap);


        //This button starts the ADT read Activity
        buttonRead.setOnClickListener(v -> {
            Intent activityIntent = new Intent(getApplicationContext(), ReadActivity.class);
            startActivity(activityIntent);
        });

        //This button starts the ADT write Activity
        buttonWrite.setOnClickListener(v -> {
            Intent activityIntent = new Intent(getApplicationContext(), WriteActivity.class);
            startActivity(activityIntent);
        });
    }
}