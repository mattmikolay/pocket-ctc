package com.mattmik.dianma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

// Copyright 2016 Matthew Mikolay. All rights reserved.

public class TranslateActivity extends AppCompatActivity {

    private final TextWatcher mInputWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Empty!
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // TODO Translate given text s

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
        EditText inputText = (EditText) findViewById(R.id.txt_input);
        inputText.addTextChangedListener(mInputWatcher);

    }

    @Override
    protected void onDestroy() {

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
