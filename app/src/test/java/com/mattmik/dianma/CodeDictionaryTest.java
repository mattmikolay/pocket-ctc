package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class CodeDictionaryTest {

    private CodeDictionary mDictionary;

    @Before
    public void setUp() {
        mDictionary = new CodeDictionary(RuntimeEnvironment.application.getResources());
    }

    @Test
    public void testIsLoaded() {
        assertFalse(mDictionary.isLoaded());
        mDictionary.loadAllData();
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
        // TODO
    }

    @Test
    public void testTelegraphToTraditionalLoaded() {
        mDictionary.loadAllData();
        // TODO
    }

    @Test
    public void testSimplifiedToTelegraphLoaded() {
        mDictionary.loadAllData();
        // TODO
    }

    @Test
    public void testTraditionalToTelegraphLoaded() {
        mDictionary.loadAllData();
        // TODO
    }

}
