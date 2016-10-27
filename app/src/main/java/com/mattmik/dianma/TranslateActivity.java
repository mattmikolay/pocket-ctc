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

// Copyright 2016 Matthew Mikolay. All rights reserved.

public class TranslateActivity extends AppCompatActivity
        implements SwitcherView.OnTranslateModeSwitchListener {

    public static final String TAG = "TranslateActivity";

    public static final int MSG_REQUEST_TRANSLATION = 1;
    public static final int MSG_TRANSLATE_SUCCESS = 2;

    private static final int UPDATE_DELAY = 200;

    private static final String STATE_INPUT_TEXT = "inputText";
    private static final String STATE_TRANSLATE_MODE = "translateMode";

    private int mTranslateMode;

    private EditText mInputText;
    private TextView mOutputText;
    private Handler mHandler;
    private TranslatorThread mTranslator;

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
                        mTranslator.requestTranslation(text);
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

        SwitcherView modeSwitcher = (SwitcherView) findViewById(R.id.langset_toolbar);
        modeSwitcher.setOnModeChangeListener(this);
        modeSwitcher.setTranslateMode(mTranslateMode);

        // Start the TranslatorThread to perform translation in the background
        mTranslator = new TranslatorThread(mHandler, getResources());
        mTranslator.setTranslateMode(mTranslateMode);
        mTranslator.setTraditionalEnabled(false);
        mTranslator.start();

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

        // Switch character set (simplified or traditional) based upon user's settings
        if("zh-Hans".equals(language)) {

            Log.d(TAG, "Simplified character set will be used for translation.");
            mTranslator.setTraditionalEnabled(false);

        } else if("zh-Hant".equals(language)) {

            Log.d(TAG, "Traditional character set will be used for translation.");
            mTranslator.setTraditionalEnabled(true);

        } else {

            Log.d(TAG, "Invalid language specified. Defaulting to simplified.");
            mTranslator.setTraditionalEnabled(false);

        }

        super.onResume();

    }

    @Override
    protected void onDestroy() {

        // Interrupt TranslatorThread to safely shut it down
        mTranslator.interrupt();

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
     * Launches a new instance of {@link SettingsActivity}.
     */
    private void launchSettingsActivity() {

        Intent launchIntent = new Intent(this, SettingsActivity.class);
        startActivity(launchIntent);

    }

    @Override
    public void onTranslateModeSwitched(int mode) {

        mTranslateMode = mode;
        mTranslator.setTranslateMode(mTranslateMode);

    }

}
