package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TranslatorThread extends Thread {

    public static final String TAG = "TranslatorThread";

    private String mPendingText;
    private volatile String mInputText;
    private Handler mResponseHandler;

    public TranslatorThread(Handler responseHandler) {
        super(TAG);
        mPendingText = "";
        mInputText = "";
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

    @Override
    public void run() {

        while(!isInterrupted()) {

            if(!mPendingText.equals(mInputText)) {

                mPendingText = mInputText;

                Log.d(TAG, "Sending translated text to response handler.");

                // For now, just convert input to uppercase
                int what = TranslateActivity.MSG_TRANSLATE_SUCCESS;
                String result = mPendingText.toUpperCase();
                Message msg = mResponseHandler.obtainMessage(what, result);
                mResponseHandler.sendMessage(msg);

            }

        }

    }

}
