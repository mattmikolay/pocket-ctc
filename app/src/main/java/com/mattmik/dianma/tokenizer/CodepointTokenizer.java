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
package com.mattmik.dianma.tokenizer;

import java.util.NoSuchElementException;

/**
 * CodepointTokenizer tokenizes a given string of text into individual Unicode codepoints. A token
 * in this class is defined as any single Unicode codepoint.
 * <p>
 * Objects returned by {@link #nextToken()} are of type {@link Integer}.
 */
public class CodepointTokenizer implements Tokenizer {

    private String mInputText;
    private int mCurrIndex;

    /**
     * Constructs a new CodepointTokenizer instance.
     * @param inputText the text to be tokenized
     */
    public CodepointTokenizer(String inputText) {
        mInputText = inputText;
        mCurrIndex = 0;
    }

    @Override
    public String getInput() {
        return mInputText;
    }

    @Override
    public boolean hasMoreTokens() {
        return (mCurrIndex < mInputText.length());
    }

    @Override
    public Object nextToken() {

        if(mCurrIndex >= mInputText.length())
            throw new NoSuchElementException("No tokens are available.");

        int codepoint = mInputText.codePointAt(mCurrIndex);
        mCurrIndex += Character.charCount(codepoint);

        return codepoint;

    }

}
