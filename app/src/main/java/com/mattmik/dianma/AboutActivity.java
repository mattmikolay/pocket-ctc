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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Displays app copyright and license information to the user.
 */
public class AboutActivity extends AppCompatActivity {

    public static final String TAG = "AboutActivity";

    private static final int STRING_VERSION_INFO = R.string.about_version;
    private static final int STRING_GITHUB_SHORT_URL = R.string.app_github_short_url;
    private static final int STRING_GITHUB_FULL_URL = R.string.app_github_full_url;

    private FirebaseAnalytics mFirebaseAnalytics;

    private TextView mGitHubUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set up app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(myToolbar);

        // Enable up navigation using the app bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Display version info in a TextView
        TextView appVersion = (TextView) findViewById(R.id.txt_about_app_version);
        String versionInfo = String.format(getString(STRING_VERSION_INFO),
                BuildConfig.VERSION_NAME);
        appVersion.setText(versionInfo);

        mGitHubUrl = (TextView) findViewById(R.id.txt_about_github_url);
        formatGitHubUrlTextView();

        // When the user clicks the TextView containing the GitHub URL, launch a web browser to the
        // web page. We handle this manually so we can log a corresponding event.
        mGitHubUrl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(TAG, "Launching GitHub URL in web browser.");

                mFirebaseAnalytics.logEvent(AnalyticsEvents.VIEW_GITHUB_PAGE, null);

                String url = getString(STRING_GITHUB_FULL_URL);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);

            }

        });

    }

    /**
     * Formats the TextView containing Pocket CTC's GitHub URL to appear as a link.
     */
    private void formatGitHubUrlTextView() {

        final CharSequence shortUrl = getString(STRING_GITHUB_SHORT_URL);

        SpannableString spanString = new SpannableString(shortUrl);
        spanString.setSpan(new URLSpan(""), 0, spanString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mGitHubUrl.setText(spanString, TextView.BufferType.SPANNABLE);

    }

}
