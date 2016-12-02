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
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * CodeDictionary is used to convert from Chinese characters to Chinese telegraph code, and
 * vice-versa.
 * <p>
 * Before any conversion can be performed, the dictionary data must be loaded using the
 * {@link #loadAllData()} method.
 */
public class CodeDictionary {

    public static final String TAG = "CodeDictionary";

    private static final int DATA_SIMPLIFIED = R.raw.simplified_codes;
    private static final int DATA_TRADITIONAL = R.raw.traditional_codes;

    private Resources mResources;

    private boolean mIsSimplifiedLoaded;
    private boolean mIsTraditionalLoaded;

    private CodeMap mSimplifiedMap;
    private CodeMap mTraditionalMap;

    /**
     * Constructs a CodeDictionary. Before any conversion can be performed, the returned instance
     * must be loaded using the {@link #loadAllData()} method.
     * @param resources application's package Resources
     */
    public CodeDictionary(Resources resources) {

        mResources = resources;

        mIsSimplifiedLoaded = false;
        mIsTraditionalLoaded = false;

        mSimplifiedMap = new CodeMap();
        mTraditionalMap = new CodeMap();

    }

    /**
     * Loads all data needed to perform conversions between Chinese characters and telegraph code.
     * @return true if data is loaded successfully, false otherwise
     */
    public boolean loadAllData() {

        if(!mIsSimplifiedLoaded)
            mIsSimplifiedLoaded = loadSimplifiedData();

        if(!mIsTraditionalLoaded)
            mIsTraditionalLoaded = loadTraditionalData();

        return (mIsSimplifiedLoaded && mIsTraditionalLoaded);

    }

    /**
     * Returns whether or not all data needed to perform conversions has been loaded.
     * @return true if all needed data has been loaded, false otherwise
     */
    public boolean isLoaded() {
        return (mIsSimplifiedLoaded && mIsTraditionalLoaded);
    }

    /**
     * Converts the given simplified Chinese character codepoint into the corresponding telegraph
     * code. For example, an input of 0x56FD will yield 948.
     * @param code a valid simplified Chinese character codepoint
     * @return the corresponding telegraph code, or null if no corresponding telegraph code is found
     */
    public Integer simplifiedToTelegraph(int code) {

        if(!mIsSimplifiedLoaded)
            throw new IllegalStateException("Simplified dictionary has not been loaded.");

        return mSimplifiedMap.hanToTelegraph(code);

    }

    /**
     * Converts the given traditional Chinese character codepoint into the corresponding telegraph
     * code. For example, an input of 0x56FD will yield 948.
     * @param code a valid traditional Chinese character codepoint
     * @return the corresponding telegraph code, or null if no corresponding telegraph code is found
     */
    public Integer traditionalToTelegraph(int code) {

        if(!mIsTraditionalLoaded)
            throw new IllegalStateException("Traditional dictionary has not been loaded.");

        return mTraditionalMap.hanToTelegraph(code);

    }

    /**
     * Converts the given telegraph code into the corresponding simplified Chinese character
     * codepoint. For example, an input of 948 will yield 0x56FD.
     * @param code a valid Chinese telegraph code
     * @return the corresponding simplified Chinese character codepoint, or null if no
     *         corresponding codepoint is found
     */
    public Integer telegraphToSimplified(int code) {

        if(!mIsSimplifiedLoaded)
            throw new IllegalStateException("Simplified dictionary has not been loaded.");

        return mSimplifiedMap.telegraphToHan(code);

    }

    /**
     * Converts the given telegraph code into the corresponding traditional Chinese character
     * codepoint. For example, an input of 948 will yield 0x570B.
     * @param code a valid Chinese telegraph code
     * @return the corresponding traditional Chinese character codepoint, or null if no
     *         corresponding codepoint is found
     */
    public Integer telegraphToTraditional(int code) {

        if(!mIsTraditionalLoaded)
            throw new IllegalStateException("Traditional dictionary has not been loaded.");

        return mTraditionalMap.telegraphToHan(code);

    }

    /**
     * Loads data for simplified character codes into the corresponding map.
     * @return true if data is loaded successfully, false otherwise
     */
    private boolean loadSimplifiedData() {

        InputStream stream = mResources.openRawResource(DATA_SIMPLIFIED);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {

            Log.d(TAG, "Loading simplified data.");

            String line;
            while((line = reader.readLine()) != null) {

                String[] tokens = line.split("\\s+");

                if(tokens.length != 2)
                    continue;

                // Each line is of the form "U+XXXX YYYY", where XXXX is a hexadecimal Unicode
                // codepoint and YYYY is a four digit telegraph code
                Integer codePoint = parseUnicodeCodepoint(tokens[0]);
                Integer ctcCode = Integer.valueOf(tokens[1]);

                mSimplifiedMap.put(codePoint, ctcCode);

            }

            Log.d(TAG, "Finished loading simplified data.");

        } catch (IOException e) {

            Log.e(TAG, "Error occurred while attempting to read simplified data resource!");
            e.printStackTrace();
            return false;

        } finally {

            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred while attempting to close simplified data resource!");
                e.printStackTrace();
            }

        }

        return true;

    }

    /**
     * Loads data for traditional character codes into the corresponding map.
     * @return true if data is loaded successfully, false otherwise
     */
    private boolean loadTraditionalData() {

        InputStream stream = mResources.openRawResource(DATA_TRADITIONAL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {

            Log.d(TAG, "Loading traditional data.");

            String line;
            while((line = reader.readLine()) != null) {

                String[] tokens = line.split("\\s+");

                if(tokens.length != 2)
                    continue;

                // Each line is of the form "U+XXXX YYYY", where XXXX is a hexadecimal Unicode
                // codepoint and YYYY is a four digit telegraph code
                Integer codePoint = parseUnicodeCodepoint(tokens[0]);
                Integer ctcCode = Integer.valueOf(tokens[1]);

                mTraditionalMap.put(codePoint, ctcCode);

            }

            Log.d(TAG, "Finished loading traditional data.");

        } catch (IOException e) {

            Log.e(TAG, "Error occurred while attempting to read traditional data resource!");
            e.printStackTrace();
            return false;

        } finally {

            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred while attempting to close traditional data resource!");
                e.printStackTrace();
            }

        }

        return true;

    }

    /**
     * Converts a string of the form "U+XXXX" to the corresponding integer representation. For
     * example, an input of "U+4E16" will yield 19990. The value -1 will be returned if the input
     * is invalid.
     * @param input a string of the form "U+XXXX", where XXXX is a hexadecimal substring
     * @return the corresponding integer representation, or -1 if input is invalid
     */
    private static int parseUnicodeCodepoint(String input) {

        if(input == null || input.isEmpty())
            return -1;

        // Remove the "U+" prefix and convert to integer
        String codeChars = input.substring(2);
        return Integer.valueOf(codeChars, 16);

    }

    /**
     * The CodeMap class abstracts a bidirectional map to convert Unicode codepoints to telegraph
     * codes, and vice-versa.
     */
    private static class CodeMap {

        private static int DEFAULT_CAPACITY = 9000;

        // Unicode codepoint is key, CTC code is value
        private Map<Integer, Integer> mHanToTelegraph;

        // CTC code is key, Unicode codepoint is value
        private Map<Integer, Integer> mTelegraphToHan;

        /**
         * Constructs an empty CodeMap.
         */
        public CodeMap() {
            mHanToTelegraph = new HashMap<>(DEFAULT_CAPACITY);
            mTelegraphToHan = new HashMap<>(DEFAULT_CAPACITY);
        }

        /**
         * Associates the specified Chinese character codepoint with the specified telegraph code in
         * this map. If the map previously contained a mapping for this codepoint or telegraph code,
         * the old value is replaced.
         * @param han a Chinese character Unicode codepoint
         * @param telegraph a Chinese telegraph code
         */
        public void put(int han, int telegraph) {
            mHanToTelegraph.put(han, telegraph);
            mTelegraphToHan.put(telegraph, han);
        }

        /**
         * Returns the telegraph code to which the specified Chinese character codepoint is mapped,
         * or null if this map contains no mapping for the codepoint.
         * @param han a Chinese character Unicode codepoint
         * @return the corresponding Chinese telegraph code
         */
        public Integer hanToTelegraph(int han) {
            return mHanToTelegraph.get(han);
        }

        /**
         * Returns the Chinese character codepoint to which the specified telegraph code is mapped,
         * or null if this map contains no mapping for the Chinese character codepoint.
         * @param telegraph a Chinese telegraph code
         * @return the corresponding Chinese character Unicode codepoint
         */
        public Integer telegraphToHan(int telegraph) {
            return mTelegraphToHan.get(telegraph);
        }

    }

}
