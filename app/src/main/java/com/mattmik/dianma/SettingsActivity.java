package com.mattmik.dianma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// Copyright 2016 Matthew Mikolay. All rights reserved.

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Show SettingsFragment as main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

}

