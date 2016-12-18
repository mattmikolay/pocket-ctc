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
import static org.junit.Assert.assertTrue;

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

    @Test
    public void englishTokenizationTest() {

        Tokenizer tokenizer = new NumberTokenizer("Lorem ipsum dolor sit amet.");

        assertTrue(tokenizer.hasMoreTokens());

        assertEquals("L", tokenizer.nextToken());
        assertEquals("o", tokenizer.nextToken());
        assertEquals("r", tokenizer.nextToken());
        assertEquals("e", tokenizer.nextToken());
        assertEquals("m", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("i", tokenizer.nextToken());
        assertEquals("p", tokenizer.nextToken());
        assertEquals("s", tokenizer.nextToken());
        assertEquals("u", tokenizer.nextToken());
        assertEquals("m", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("d", tokenizer.nextToken());
        assertEquals("o", tokenizer.nextToken());
        assertEquals("l", tokenizer.nextToken());
        assertEquals("o", tokenizer.nextToken());
        assertEquals("r", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("s", tokenizer.nextToken());
        assertEquals("i", tokenizer.nextToken());
        assertEquals("t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("a", tokenizer.nextToken());
        assertEquals("m", tokenizer.nextToken());
        assertEquals("e", tokenizer.nextToken());
        assertEquals("t", tokenizer.nextToken());
        assertEquals(".", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());

    }

    @Test
    public void chineseTokenizationTest() {

        Tokenizer tokenizer = new NumberTokenizer("你好，地球！");

        assertTrue(tokenizer.hasMoreTokens());

        assertEquals("你", tokenizer.nextToken());
        assertEquals("好", tokenizer.nextToken());
        assertEquals("，", tokenizer.nextToken());
        assertEquals("地", tokenizer.nextToken());
        assertEquals("球", tokenizer.nextToken());
        assertEquals("！", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());

    }

    @Test
    public void numberTokenizationTest() {

        Tokenizer tokenizer = new NumberTokenizer("7220 0350 9756 653330 4973 0001 00 6108");

        assertTrue(tokenizer.hasMoreTokens());

        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("7220", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("0350", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("9756", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("653330", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("4973", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("0001", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("00", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("6108", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());

    }

    @Test
    public void whitespaceOnlyTokenizationTest() {

        Tokenizer tokenizer = new NumberTokenizer(" \t \t\t   \n \n\n   \t       \t\n");

        assertTrue(tokenizer.hasMoreTokens());

        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());

    }

    @Test
    public void whitespaceAndDigitsTokenizationTest() {

        Tokenizer tokenizer = new NumberTokenizer(" \t \t8900\t   \n 42\n\n   \t   109    \t\n");

        assertTrue(tokenizer.hasMoreTokens());

        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("8900", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("42", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("109", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("\t", tokenizer.nextToken());
        assertEquals("\n", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());

    }

    @Test
    public void mixedTokenizationTest() {

        Tokenizer tokenizer = new NumberTokenizer("你好。Test555. Test 0129哦! Wow,67. 001 98a.");

        assertTrue(tokenizer.hasMoreTokens());

        assertEquals("你", tokenizer.nextToken());
        assertEquals("好", tokenizer.nextToken());
        assertEquals("。", tokenizer.nextToken());
        assertEquals("T", tokenizer.nextToken());
        assertEquals("e", tokenizer.nextToken());
        assertEquals("s", tokenizer.nextToken());
        assertEquals("t", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("555", tokenizer.nextToken());
        assertEquals(".", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("T", tokenizer.nextToken());
        assertEquals("e", tokenizer.nextToken());
        assertEquals("s", tokenizer.nextToken());
        assertEquals("t", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("0129", tokenizer.nextToken());
        assertEquals("哦", tokenizer.nextToken());
        assertEquals("!", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("W", tokenizer.nextToken());
        assertEquals("o", tokenizer.nextToken());
        assertEquals("w", tokenizer.nextToken());
        assertEquals(",", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("67", tokenizer.nextToken());
        assertEquals(".", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("001", tokenizer.nextToken());
        assertEquals(" ", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("98", tokenizer.nextToken());
        assertEquals("a", tokenizer.nextToken());
        assertEquals(".", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());

    }

}
