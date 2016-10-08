package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

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
