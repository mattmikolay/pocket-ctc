package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
    private String mPendingText;
    private StringBuilder mResultBuilder;

    private Handler mResponseHandler;

    public TranslatorThread(Handler responseHandler) {

        super(TAG);

        mInputText = "";
        mMode = MODE_HAN_TO_TELE;
        mUsesTraditional = false;

        mState = STATE_DONE;
        mPendingText = "";
        mResultBuilder = null;

        mResponseHandler = responseHandler;

    }

    /**
     * Requests translation of the given string. The translated result will be returned in a
     * {@link TranslateActivity#MSG_TRANSLATE_SUCCESS} event sent to the response Handler.
     * @param inputText a string of text to be translated
     */
    public void requestTranslation(String inputText) {
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

            if(!mPendingText.equals(mInputText))
                mState = STATE_INIT;

            switch(mState) {

                case STATE_INIT:
                    init();
                    mState = STATE_PROCESSING;
                    break;

                case STATE_PROCESSING:

                    stepTranslation();

                    if(isTranslationFinished())
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
        mPendingText = mInputText;
        mResultBuilder = new StringBuilder();
    }

    /**
     * Performs a single unit of translation work. If translation has already finished, this is a
     * no-op.
     */
    private void stepTranslation() {
        // TODO Code me!
        mResultBuilder.append(mPendingText.toUpperCase());
    }

    /**
     * Returns true if the translation has finished.
     * @return true if translation has finished, false otherwise
     */
    private boolean isTranslationFinished() {
        // TODO Code me!
        return true;
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
