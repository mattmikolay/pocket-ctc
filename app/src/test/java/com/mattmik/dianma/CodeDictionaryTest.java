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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CodeDictionary}. Note that this class does not test the validity of data
 * produced by CodeDictionary; it only tests that conversions run as expected.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/test/AndroidManifest.xml", sdk = 23)
public class CodeDictionaryTest {

    private CodeDictionary mDictionary;

    @Before
    public void setUp() {
        mDictionary = new CodeDictionary(RuntimeEnvironment.application.getResources());
    }

    @Test
    public void testIsLoaded() {
        assertFalse(mDictionary.isLoaded());
        assertTrue(mDictionary.loadAllData());
        assertTrue(mDictionary.isLoaded());
    }

    @Test(expected = IllegalStateException.class)
    public void testTelegraphToSimplifiedNotLoaded() {
        mDictionary.telegraphToSimplified(1993);
    }

    @Test(expected = IllegalStateException.class)
    public void testTelegraphToTraditionalNotLoaded() {
        mDictionary.telegraphToTraditional(1993);
    }

    @Test(expected = IllegalStateException.class)
    public void testSimplifiedToTelegraphNotLoaded() {
        mDictionary.simplifiedToTelegraph(1993);
    }

    @Test(expected = IllegalStateException.class)
    public void testTraditionalToTelegraphNotLoaded() {
        mDictionary.traditionalToTelegraph(1993);
    }

    @Test
    public void testTelegraphToSimplifiedLoaded() {

        mDictionary.loadAllData();

        assertEquals(0x4F60, (int) mDictionary.telegraphToSimplified(2978));
        assertEquals(0x597D, (int) mDictionary.telegraphToSimplified(6350));
        assertEquals(0x5417, (int) mDictionary.telegraphToSimplified(2044));
        assertEquals(0xFF1F, (int) mDictionary.telegraphToSimplified(3315));
        assertEquals(0x6211, (int) mDictionary.telegraphToSimplified(2186));
        assertEquals(0x662F, (int) mDictionary.telegraphToSimplified(6212));
        assertEquals(0x7535, (int) mDictionary.telegraphToSimplified(7688));
        assertEquals(0x8111, (int) mDictionary.telegraphToSimplified(8930));
        assertEquals(0x3002, (int) mDictionary.telegraphToSimplified(6364));

        assertNull(mDictionary.telegraphToSimplified(4903));
        assertNull(mDictionary.telegraphToSimplified(9170));
        assertNull(mDictionary.telegraphToSimplified(6343));
        assertNull(mDictionary.telegraphToSimplified(9397));
        assertNull(mDictionary.telegraphToSimplified(8109));
        assertNull(mDictionary.telegraphToSimplified(5843));
        assertNull(mDictionary.telegraphToSimplified(2630));
        assertNull(mDictionary.telegraphToSimplified(2142));
        assertNull(mDictionary.telegraphToSimplified(1261));

    }

    @Test
    public void testTelegraphToTraditionalLoaded() {

        mDictionary.loadAllData();

        assertEquals(0x6211, (int) mDictionary.telegraphToTraditional(6583));
        assertEquals(0x5F88, (int) mDictionary.telegraphToTraditional(7239));
        assertEquals(0x597D, (int) mDictionary.telegraphToTraditional(7311));
        assertEquals(0xFF01, (int) mDictionary.telegraphToTraditional(2899));
        assertEquals(0x4F60, (int) mDictionary.telegraphToTraditional(2643));
        assertEquals(0x662F, (int) mDictionary.telegraphToTraditional(6372));
        assertEquals(0x624B, (int) mDictionary.telegraphToTraditional(7023));
        assertEquals(0x6A5F, (int) mDictionary.telegraphToTraditional(6375));
        assertEquals(0x3002, (int) mDictionary.telegraphToTraditional(9043));

        assertNull(mDictionary.telegraphToTraditional(3306));
        assertNull(mDictionary.telegraphToTraditional(6734));
        assertNull(mDictionary.telegraphToTraditional(7131));
        assertNull(mDictionary.telegraphToTraditional(3650));
        assertNull(mDictionary.telegraphToTraditional(9724));
        assertNull(mDictionary.telegraphToTraditional(7095));
        assertNull(mDictionary.telegraphToTraditional(4748));
        assertNull(mDictionary.telegraphToTraditional(64));
        assertNull(mDictionary.telegraphToTraditional(2354));

    }

    @Test
    public void testSimplifiedToTelegraphLoaded() {

        mDictionary.loadAllData();

        assertEquals(2978, (int) mDictionary.simplifiedToTelegraph(0x4F60));
        assertEquals(6350, (int) mDictionary.simplifiedToTelegraph(0x597D));
        assertEquals(2044, (int) mDictionary.simplifiedToTelegraph(0x5417));
        assertEquals(3315, (int) mDictionary.simplifiedToTelegraph(0xFF1F));
        assertEquals(2186, (int) mDictionary.simplifiedToTelegraph(0x6211));
        assertEquals(6212, (int) mDictionary.simplifiedToTelegraph(0x662F));
        assertEquals(7688, (int) mDictionary.simplifiedToTelegraph(0x7535));
        assertEquals(8930, (int) mDictionary.simplifiedToTelegraph(0x8111));
        assertEquals(6364, (int) mDictionary.simplifiedToTelegraph(0x3002));

        assertNull(mDictionary.simplifiedToTelegraph(0x8835));
        assertNull(mDictionary.simplifiedToTelegraph(0x4124));
        assertNull(mDictionary.simplifiedToTelegraph(0x9726));
        assertNull(mDictionary.simplifiedToTelegraph(0x1053));
        assertNull(mDictionary.simplifiedToTelegraph(0x3226));
        assertNull(mDictionary.simplifiedToTelegraph(0x9771));
        assertNull(mDictionary.simplifiedToTelegraph(0x6573));
        assertNull(mDictionary.simplifiedToTelegraph(0x2010));
        assertNull(mDictionary.simplifiedToTelegraph(0x0315));

    }

    @Test
    public void testTraditionalToTelegraphLoaded() {

        mDictionary.loadAllData();

        assertEquals(6583, (int) mDictionary.traditionalToTelegraph(0x6211));
        assertEquals(7239, (int) mDictionary.traditionalToTelegraph(0x5F88));
        assertEquals(7311, (int) mDictionary.traditionalToTelegraph(0x597D));
        assertEquals(2899, (int) mDictionary.traditionalToTelegraph(0xFF01));
        assertEquals(2643, (int) mDictionary.traditionalToTelegraph(0x4F60));
        assertEquals(6372, (int) mDictionary.traditionalToTelegraph(0x662F));
        assertEquals(7023, (int) mDictionary.traditionalToTelegraph(0x624B));
        assertEquals(6375, (int) mDictionary.traditionalToTelegraph(0x6A5F));
        assertEquals(9043, (int) mDictionary.traditionalToTelegraph(0x3002));

        assertNull(mDictionary.traditionalToTelegraph(0x9408));
        assertNull(mDictionary.traditionalToTelegraph(0x9434));
        assertNull(mDictionary.traditionalToTelegraph(0x6079));
        assertNull(mDictionary.traditionalToTelegraph(0x8851));
        assertNull(mDictionary.traditionalToTelegraph(0x7019));
        assertNull(mDictionary.traditionalToTelegraph(0x8325));
        assertNull(mDictionary.traditionalToTelegraph(0x1609));
        assertNull(mDictionary.traditionalToTelegraph(0x7974));
        assertNull(mDictionary.traditionalToTelegraph(0x1338));

    }

}
