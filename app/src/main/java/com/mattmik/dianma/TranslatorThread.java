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

import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mattmik.dianma.tokenizer.CodepointTokenizer;
import com.mattmik.dianma.tokenizer.NumberTokenizer;
import com.mattmik.dianma.tokenizer.Tokenizer;

import java.util.Locale;

/**
 * Performs translation from Chinese characters to telephony codes, or vice-versa, asynchronously.
 *
 * Translated result text will be reported back to a given {@link Handler} inside a {@link Message}
 * with value {@link TranslateActivity#MSG_TRANSLATE_SUCCESS}.
 */
public class TranslatorThread extends Thread {

    public static final String TAG = "TranslatorThread";

    private static final int STATE_INIT = 0;
    private static final int STATE_PROCESSING = 1;
    private static final int STATE_SEND = 2;
    private static final int STATE_DONE = 3;

    private volatile String mInputText;
    private volatile int mMode;
    private volatile boolean mUsesTraditional;

    private int mState;
    private int mInProcessMode;
    private boolean mInProcessUsesTraditional;
    private Tokenizer mTokenizer;
    private StringBuilder mResultBuilder;

    private Handler mResponseHandler;

    private CodeDictionary mDictionary;

    /**
     * Constructs a TranslatorThread.
     * @param responseHandler a Handler to receive translated result text
     * @param resources application's package Resources
     */
    public TranslatorThread(Handler responseHandler, Resources resources) {

        super(TAG);

        mInputText = "";
        mMode = mInProcessMode = TranslateMode.HAN_TO_TELE;
        mUsesTraditional = mInProcessUsesTraditional = false;

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
     * @param mode a translate mode. Valid values are {@link TranslateMode#HAN_TO_TELE} and
     *             {@link TranslateMode#TELE_TO_HAN}.
     */
    public void setTranslateMode(int mode) {
        mMode = mode;
    }

    @Override
    public void run() {

        while(!isInterrupted()) {

            // Start a new translation if:
            // 1. A tokenizer has not been initialized OR
            // 2. The given input text differs from the text currently being tokenized OR
            // 3. The given translation mode differs from the in progress translation mode OR
            // 4. The given Chinese character set differs from the in progress character set
            if(mTokenizer == null || !mInputText.equals(mTokenizer.getInput())
                    || mInProcessMode != mMode || mInProcessUsesTraditional != mUsesTraditional) {
                mState = STATE_INIT;
            }

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

        mInProcessMode = mMode;
        mInProcessUsesTraditional = mUsesTraditional;

        // Set up a tokenizer for the input string based upon the given translation mode
        switch(mInProcessMode) {

            case TranslateMode.HAN_TO_TELE:
                mTokenizer = new CodepointTokenizer(mInputText);
                break;

            case TranslateMode.TELE_TO_HAN:
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

        switch(mInProcessMode) {

            case TranslateMode.HAN_TO_TELE:
                stepHanToTele();
                break;

            case TranslateMode.TELE_TO_HAN:
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

        Integer tele = (!mInProcessUsesTraditional) ? mDictionary.simplifiedToTelegraph(codepoint) :
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

            Integer codepoint = (!mInProcessUsesTraditional) ?
                    mDictionary.telegraphToSimplified(value) :
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
