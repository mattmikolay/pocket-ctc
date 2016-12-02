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
