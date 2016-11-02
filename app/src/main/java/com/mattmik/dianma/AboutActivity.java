package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Displays app copyright and license information to the user.
 */
public class AboutActivity extends AppCompatActivity {

    private static final int STRING_VERSION_INFO = R.string.about_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set up app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(myToolbar);

        // Enable up navigation using the app bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Display version info in a TextView
        String versionInfo = String.format(getResources().getString(STRING_VERSION_INFO),
                BuildConfig.VERSION_NAME);
        TextView versionText = (TextView) findViewById(R.id.txt_about_app_version);
        versionText.setText(versionInfo);

    }

}
