package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Locale;

public class TranslatorThread extends Thread {

    public static final String TAG = "TranslatorThread";

    /** Translation mode for conversion from Chinese characters to telegraph code. */
    public static final int MODE_HAN_TO_TELE = 0;

    /** Translation mode for conversion from telegraph code to Chinese characters.  */
    public static final int MODE_TELE_TO_HAN = 1;

    private static final int STATE_INIT = 0;
    private static final int STATE_PROCESSING = 1;
    private static final int STATE_SEND = 2;
    private static final int STATE_DONE = 3;

    private volatile String mInputText;
    private volatile int mMode;
    private volatile boolean mUsesTraditional;

    private int mState;
    private Tokenizer mTokenizer;
    private StringBuilder mResultBuilder;

    private Handler mResponseHandler;

    private CodeDictionary mDictionary;

    public TranslatorThread(Handler responseHandler, Resources resources) {

        super(TAG);

        mInputText = "";
        mMode = MODE_HAN_TO_TELE;
        mUsesTraditional = false;

        mState = STATE_DONE;
        mTokenizer = null;
        mResultBuilder = null;

        mResponseHandler = responseHandler;

        mDictionary = new CodeDictionary(resources);

    }

    /**
     * Requests translation of the given string. The translated result will be returned in a
     * {@link TranslateActivity#MSG_TRANSLATE_SUCCESS} event sent to the response Handler.
     * @param inputText a string of text to be translated
     */
    public void requestTranslation(String inputText) {

        if(inputText == null)
            throw new IllegalArgumentException("Translation cannot be performed on a null value.");

        mInputText = inputText;

    }

    /**
     * Sets whether or not traditional Chinese characters should be used for translation.
     * @param isTraditionalEnabled true if traditional Chinese characters should be used for
     *                             translation, false if simplified Chinese character should be
     *                             used for translation
     */
    public void setTraditionalEnabled(boolean isTraditionalEnabled) {
        mUsesTraditional = isTraditionalEnabled;
    }

    /**
     * Sets the translation mode for this thread.
     * @param mode a translate mode. Valid values are {@link #MODE_HAN_TO_TELE} and
     *             {@link #MODE_TELE_TO_HAN}.
     */
    public void setTranslateMode(int mode) {
        mMode = mode;
    }

    @Override
    public void run() {

        while(!isInterrupted()) {

            if(mTokenizer == null || !mInputText.equals(mTokenizer.getInput()))
                mState = STATE_INIT;

            switch(mState) {

                case STATE_INIT:
                    init();
                    mState = STATE_PROCESSING;
                    break;

                case STATE_PROCESSING:

                    stepTranslation();

                    if(!mTokenizer.hasMoreTokens())
                        mState = STATE_SEND;

                    break;

                case STATE_SEND:
                    sendResult();
                    mState = STATE_DONE;
                    break;

                case STATE_DONE:
                    // Do nothing!
                    break;

            }

        }

    }

    /**
     * Initializes fields to prepare for translating a given text.
     */
    private void init() {

        // Load data needed by the conversion dictionary, if needed
        if(!mDictionary.isLoaded())
            mDictionary.loadAllData();

        // Set up a tokenizer for the input string based upon the given translation mode
        switch(mMode) {

            case MODE_HAN_TO_TELE:
                mTokenizer = new CodepointTokenizer(mInputText);
                break;

            case MODE_TELE_TO_HAN:
                mTokenizer = new NumberTokenizer(mInputText);
                break;

            default:
                throw new IllegalStateException("Translation mode is set to an invalid value.");

        }

        mResultBuilder = new StringBuilder();

    }

    /**
     * Performs a single unit of translation work. If translation has already finished, this is a
     * no-op.
     */
    private void stepTranslation() {

        switch(mMode) {

            case MODE_HAN_TO_TELE:
                stepHanToTele();
                break;

            case MODE_TELE_TO_HAN:
                stepTeleToHan();
                break;

            default:
                throw new IllegalStateException("Translation mode is set to an invalid value.");

        }

    }

    /**
     * Performs a single unit of work toward converting a string of Chinese characters into
     * telegraph code.
     */
    private void stepHanToTele() {

        if(!mTokenizer.hasMoreTokens())
            return;

        int codepoint = (Integer) mTokenizer.nextToken();

        Integer tele = (!mUsesTraditional) ? mDictionary.simplifiedToTelegraph(codepoint) :
                mDictionary.traditionalToTelegraph(codepoint);

        if(tele == null) {

            // If dictionary returned null, just append the original character code
            mResultBuilder.appendCodePoint(codepoint);

        } else {

            // Append telephony code as a four digit string of numbers
            String formatted = String.format(Locale.US, "%04d", tele);
            mResultBuilder.append(formatted);
            mResultBuilder.append(' ');

        }

    }

    /**
     * Performs a single unit of work toward converting a string of telegraph codes into Chinese
     * characters.
     */
    private void stepTeleToHan() {

        if(!mTokenizer.hasMoreTokens())
            return;

        String token = (String) mTokenizer.nextToken();

        try {

            int value = Integer.parseInt(token);

            Integer codepoint = (!mUsesTraditional) ? mDictionary.telegraphToSimplified(value) :
                    mDictionary.telegraphToTraditional(value);

            if(codepoint == null) {

                // This integer value does not correspond to a Chinese character, so just append the
                // original token to the result string
                mResultBuilder.append(token);

            } else {

                // Append the corresponding Chinese character codepoint
                mResultBuilder.appendCodePoint(codepoint);

            }

        } catch(NumberFormatException e) {

            // Append this non-numeric token to the result string
            mResultBuilder.append(token);

        }

    }

    /**
     * Sends the translated result text to the main thread using the response handler.
     */
    private void sendResult() {

        Log.d(TAG, "Sending translated text to response handler.");

        int what = TranslateActivity.MSG_TRANSLATE_SUCCESS;
        String result = mResultBuilder.toString();
        Message msg = mResponseHandler.obtainMessage(what, result);
        mResponseHandler.sendMessage(msg);

    }

}
