package com.mattmik.dianma;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class TranslateActivity extends AppCompatActivity {

    public static final String TAG = "TranslateActivity";

    public static final int MSG_REQUEST_TRANSLATION = 1;
    public static final int MSG_TRANSLATE_SUCCESS = 2;

    private static final int UPDATE_DELAY = 200;

    private static final String STATE_INPUT_TEXT = "inputText";

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

        // Start the TranslatorThread to perform translation in the background
        mTranslator = new TranslatorThread(mHandler);
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

        super.onSaveInstanceState(savedInstanceState);

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

}
