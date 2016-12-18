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

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NumberTokenizerTest {

    @Test
    public void getInputTest() {

        Tokenizer tokenizer = new NumberTokenizer("1234567890");
        assertEquals("1234567890", tokenizer.getInput());

    }

    @Test
    public void emptyStringHasMoreTokensTest() {

        Tokenizer tokenizer = new NumberTokenizer("");
        assertFalse(tokenizer.hasMoreTokens());

    }

    @Test(expected = NoSuchElementException.class)
    public void emptyStringNextTokenTest() {

        new NumberTokenizer("").nextToken();

    }

}
