// SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
// SPDX-License-Identifier: MIT

package com.infineon.css.nbt_adt_demonstrator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

/**
 * Splash screen only showing Infineon logo.
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends Activity {

    /**
     * Duration of splash screen in [ms].
     */
    private static final int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Start main activity after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }, SPLASH_TIME);
    }
}