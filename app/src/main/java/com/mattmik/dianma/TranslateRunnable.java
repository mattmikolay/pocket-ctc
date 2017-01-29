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

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mattmik.dianma.tokenizer.CodepointTokenizer;
import com.mattmik.dianma.tokenizer.NumberTokenizer;
import com.mattmik.dianma.tokenizer.Tokenizer;

import java.util.Locale;

/**
 * Performs translation from Chinese characters to telephony codes, or vice-versa.
 *
 * Translated result text will be reported back to a given {@link Handler} inside a {@link Message}
 * with value {@link TranslateActivity#MSG_TRANSLATE_SUCCESS}.
 */
public class TranslateRunnable implements Runnable {

    private static final String TAG = "TranslateRunnable";

    private final Handler mResponseHandler;
    private final CodeDictionary mDictionary;
    private final String mInputText;
    private final int mTranslateMode;
    private final boolean mUseTraditional;

    /**
     * Constructs a new TranslateRunnable.
     *
     * @param responseHandler the Handler that will receive the result
     * @param dictionary      the CodeDictionary used for translation
     * @param inputText       the input string to be translated
     * @param translateMode   the translation mode. See {@link TranslateMode}.
     * @param useTraditional  true if translation should use traditional
     *                        characters, false if translation should use
     *                        simplified characters
     */
    public TranslateRunnable(Handler responseHandler, CodeDictionary dictionary, String inputText,
                             int translateMode, boolean useTraditional) {
        mResponseHandler = responseHandler;
        mDictionary = dictionary;
        mInputText = inputText;
        mTranslateMode = translateMode;
        mUseTraditional = useTraditional;
    }

    @Override
    public void run() {

        final Tokenizer tokenizer = makeTokenizer();
        final StringBuilder resultBuilder = new StringBuilder();

        while(tokenizer.hasMoreTokens() && !Thread.currentThread().isInterrupted())
            stepTranslation(tokenizer, resultBuilder);

        sendResult(resultBuilder);

    }

    /**
     * Creates a tokenizer for the given input text and translation mode.
     * @return a Tokenizer
     */
    private Tokenizer makeTokenizer() {

        final Tokenizer tokenizer;

        switch(mTranslateMode) {

            case TranslateMode.HAN_TO_TELE:
                tokenizer = new CodepointTokenizer(mInputText);
                break;

            case TranslateMode.TELE_TO_HAN:
                tokenizer = new NumberTokenizer(mInputText);
                break;

            default:
                throw new IllegalStateException("Translation mode is set to an invalid value.");

        }

        return tokenizer;

    }

    /**
     * Performs a single unit of translation work. If translation has already finished, this is a
     * no-op.
     */
    private void stepTranslation(final Tokenizer tokenizer, final StringBuilder resultBuilder) {

        if(!tokenizer.hasMoreTokens())
            return;

        switch(mTranslateMode) {

            case TranslateMode.HAN_TO_TELE:
                stepHanToTele(tokenizer, resultBuilder);
                break;

            case TranslateMode.TELE_TO_HAN:
                stepTeleToHan(tokenizer, resultBuilder);
                break;

            default:
                throw new IllegalStateException("Translation mode is set to an invalid value.");

        }

    }

    /**
     * Steps conversion of a string of Chinese characters into telegraph code.
     */
    private void stepHanToTele(final Tokenizer tokenizer, final StringBuilder resultBuilder) {

        int codepoint = (Integer) tokenizer.nextToken();

        Integer tele = (!mUseTraditional) ? mDictionary.simplifiedToTelegraph(codepoint) :
                mDictionary.traditionalToTelegraph(codepoint);

        if(tele == null) {

            // If dictionary returned null, just append the original character code
            resultBuilder.appendCodePoint(codepoint);

        } else {

            // Append telephony code as a four digit string of numbers
            String formatted = String.format(Locale.US, "%04d", tele);
            resultBuilder.append(formatted);
            resultBuilder.append(' ');

        }

    }

    /**
     * Steps conversion of a string of telegraph codes into Chinese characters.
     */
    private void stepTeleToHan(final Tokenizer tokenizer, final StringBuilder resultBuilder) {

        String token = (String) tokenizer.nextToken();

        try {

            int value = Integer.parseInt(token);

            Integer codepoint = (!mUseTraditional) ? mDictionary.telegraphToSimplified(value) :
                    mDictionary.telegraphToTraditional(value);

            if(codepoint == null) {

                // This integer value does not correspond to a Chinese character, so just append the
                // original token to the result string
                resultBuilder.append(token);

            } else {

                // Append the corresponding Chinese character codepoint
                resultBuilder.appendCodePoint(codepoint);

            }

        } catch(NumberFormatException e) {

            // Append this non-numeric token to the result string
            resultBuilder.append(token);

        }

    }

    /**
     * Sends the translated result text to the main thread using the response handler.
     */
    private void sendResult(final StringBuilder resultBuilder) {

        Log.d(TAG, "Sending translated text to response handler.");

        int what = TranslateActivity.MSG_TRANSLATE_SUCCESS;
        String result = resultBuilder.toString();
        Message msg = mResponseHandler.obtainMessage(what, result);
        mResponseHandler.sendMessage(msg);

    }

}
