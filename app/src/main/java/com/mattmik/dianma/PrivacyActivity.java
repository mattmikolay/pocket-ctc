/*
 * Copyright (c) 2016-2017 Matthew Mikolay
 *
 * This file is part of Pocket CTC.
 *
 * Pocket CTC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pocket CTC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pocket CTC.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mattmik.dianma;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Displays app privacy policy to the user.
 */
public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        // Set up app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.privacy_toolbar);
        setSupportActionBar(toolbar);

        // Enable up navigation using the app bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

}
