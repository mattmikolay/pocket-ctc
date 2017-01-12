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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Wraps {@link SettingsFragment} for display in an Activity.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";

    public static final String KEY_LANGUAGE = "pref_language";

    private FirebaseAnalytics mFirebaseAnalytics;

    private SharedPreferences mUserPrefs;

    private final SharedPreferences.OnSharedPreferenceChangeListener mPrefChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if(KEY_LANGUAGE.equals(key)) {

                Log.d(TAG, "Character set preference changed.");

                // Record a metric for this character set change
                final Bundle params = new Bundle();
                final String charSet = sharedPreferences.getString(key, "");
                params.putString(AnalyticsParams.CHARACTER_SET, charSet);
                mFirebaseAnalytics.logEvent(AnalyticsEvents.CHANGE_HAN_SET, params);

            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set up app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);

        // Enable up navigation using the app bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mUserPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Show SettingsFragment as primary content
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserPrefs.registerOnSharedPreferenceChangeListener(mPrefChangeListener);
    }

    @Override
    protected void onPause() {
        mUserPrefs.unregisterOnSharedPreferenceChangeListener(mPrefChangeListener);
        super.onPause();
    }

}

