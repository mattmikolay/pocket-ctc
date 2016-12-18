/*
 * Copyright (c) 2016 Matthew Mikolay
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Allows the user to input Chinese characters for translation into telegraphy codes, and
 * vice-versa.
 */
public class TranslateActivity extends AppCompatActivity
        implements SwitcherView.OnTranslateModeSwitchListener {

    public static final String TAG = "TranslateActivity";

    /**
     * Received by this activity's handler when new text is available for translation.
     */
    public static final int MSG_REQUEST_TRANSLATION = 1;

    /**
     * Received by this activity's handler when a translated text is returned by
     * {@link TranslateRunnable}.
     */
    public static final int MSG_TRANSLATE_SUCCESS = 2;

    private static final int UPDATE_DELAY = 200;

    private static final String STATE_INPUT_TEXT = "inputText";
    private static final String STATE_TRANSLATE_MODE = "translateMode";

    private int mTranslateMode;
    private boolean mUseTraditional;

    private EditText mInputText;
    private TextView mOutputText;
    private Handler mHandler;
    private CodeDictionary mDictionary;
    private ExecutorService mExecutor;

    private final TextWatcher mInputWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Empty!
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // Remove any pending requests for translation, as they are now out of date
            mHandler.removeMessages(MSG_REQUEST_TRANSLATION);

            // Send a request for translation after UPDATE_DELAY milliseconds
            Message msg = mHandler.obtainMessage(MSG_REQUEST_TRANSLATION, s.toString());
            mHandler.sendMessageDelayed(msg, UPDATE_DELAY);

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Empty!
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        // Set up app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        // Set up dictionary used for translation
        mDictionary = new CodeDictionary(getResources());
        mDictionary.loadAllData();

        // Add a TextWatcher to this Activity's main EditText, allowing us to detect when the user
        // stops typing
        mInputText = (EditText) findViewById(R.id.txt_input);
        mInputText.addTextChangedListener(mInputWatcher);

        mOutputText = (TextView) findViewById(R.id.txt_output);

        mHandler = new Handler(getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {

                switch(msg.what) {

                    case MSG_REQUEST_TRANSLATION: {
                        String text = (String) msg.obj;
                        requestTranslation(text);
                        break;
                    }

                    case MSG_TRANSLATE_SUCCESS: {
                        String text = (String) msg.obj;
                        mOutputText.setText(text);
                        break;
                    }

                    default:
                        super.handleMessage(msg);
                        break;

                }

            }

        };

        // Default to conversion from Chinese characters to telegraph code, but restore previous
        // translation mode if needed
        mTranslateMode = TranslateMode.HAN_TO_TELE;
        if(savedInstanceState != null) {

            Log.d(TAG, "Restoring translation mode from activity recreation.");

            mTranslateMode = savedInstanceState.getInt(STATE_TRANSLATE_MODE,
                    TranslateMode.HAN_TO_TELE);

        }

        // Default to simplified characters. The user's preference will be checked in onResume.
        mUseTraditional = false;

        mExecutor = Executors.newSingleThreadExecutor();

        SwitcherView modeSwitcher = (SwitcherView) findViewById(R.id.langset_toolbar);
        modeSwitcher.setOnModeChangeListener(this);
        modeSwitcher.setTranslateMode(mTranslateMode);
        refreshInputTextHints();

        // If the user is sharing into this app, load the EditText with the shared text. Translation
        // will be triggered by the registered TextWatcher.
        Intent intent = getIntent();
        loadInputTextFromIntent(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {

        // Launch mode is singleTask, so Intent containing shared text will be passed to this method
        // if TranslateActivity already exists at time of share.
        super.onNewIntent(intent);
        loadInputTextFromIntent(intent);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        Log.d(TAG, "Restoring text saved from activity recreation.");

        // Restore the user-specified text. Translation will be triggered by the registered
        // TextWatcher.
        String savedText = savedInstanceState.getString(STATE_INPUT_TEXT, "");
        mInputText.setText(savedText);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save input text specified by user
        savedInstanceState.putString(STATE_INPUT_TEXT, mInputText.getText().toString());

        // Save current translation mode
        savedInstanceState.putInt(STATE_TRANSLATE_MODE, mTranslateMode);

        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onResume() {

        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String language = userPrefs.getString(SettingsActivity.KEY_LANGUAGE, "");
        final boolean needsRefresh;

        // Switch character set (simplified or traditional) based upon user's settings
        if("zh-Hans".equals(language)) {

            Log.d(TAG, "Simplified character set will be used for translation.");
            needsRefresh = mUseTraditional;
            mUseTraditional = false;

        } else if("zh-Hant".equals(language)) {

            Log.d(TAG, "Traditional character set will be used for translation.");
            needsRefresh = !mUseTraditional;
            mUseTraditional = true;

        } else {

            Log.d(TAG, "Invalid language specified. Defaulting to simplified.");
            needsRefresh = !mUseTraditional;
            mUseTraditional = false;

        }

        if(needsRefresh) {
            Log.d(TAG, "Refreshing translation due to character set preference change.");
            requestTranslation(mInputText.getText().toString());
        }

        super.onResume();

    }

    @Override
    protected void onDestroy() {

        // This will interrupt any currently executing TranslateRunnables
        mExecutor.shutdownNow();
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                launchSettingsActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     * Loads input text optionally stored in the given Intent.
     * @param intent a given Intent
     */
    private void loadInputTextFromIntent(Intent intent) {

        if(intent == null)
            return;

        String action = intent.getAction();
        String type = intent.getType();
        if(Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            mInputText.setText(sharedText);
        }

    }

    /**
     * Launches a new instance of {@link SettingsActivity}.
     */
    private void launchSettingsActivity() {

        Intent launchIntent = new Intent(this, SettingsActivity.class);
        startActivity(launchIntent);

    }

    /**
     * Refreshes the hints for the EditText used to receive user input.
     */
    private void refreshInputTextHints() {

        int hintResource = (mTranslateMode == TranslateMode.HAN_TO_TELE) ?
                R.string.hint_input_chinese : R.string.hint_input_telephony;
        mInputText.setHint(hintResource);

    }

    /**
     * Submits a new {@link TranslateRunnable} for execution to translate the given text.
     * @param inputText the given input text
     */
    private void requestTranslation(final String inputText) {

        Runnable runnable = new TranslateRunnable(mHandler, mDictionary, inputText, mTranslateMode,
                mUseTraditional);
        mExecutor.submit(runnable);

    }

    @Override
    public void onTranslateModeSwitched(int mode) {

        mTranslateMode = mode;
        requestTranslation(mInputText.getText().toString());
        refreshInputTextHints();

    }

}
