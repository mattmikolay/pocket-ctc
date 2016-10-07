package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

/**
 * The Tokenizer interface defines signatures for methods guiding the tokenization of text strings.
 */
public interface Tokenizer {

    /**
     * Returns the string of text currently being tokenized.
     * @return the string currently being tokenized
     */
    String getInput();

    /**
     * Returns whether or not more tokens are available from this tokenizer.
     * @return true if more tokens are available, false otherwise
     */
    boolean hasMoreTokens();

    /**
     * Returns the next token from this tokenizer.
     * @return the next token
     */
    Object nextToken();

}
