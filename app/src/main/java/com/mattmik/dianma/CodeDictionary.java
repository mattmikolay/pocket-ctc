package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

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

    // Codepoint is key, CTC code is value
    private Map<Integer, Integer> mSimplifiedToTelegraph;
    private Map<Integer, Integer> mTraditionalToTelegraph;

    public CodeDictionary(Resources resources) {

        mResources = resources;

        mIsSimplifiedLoaded = false;
        mIsTraditionalLoaded = false;

        mSimplifiedToTelegraph = new HashMap<>(9000);
        mTraditionalToTelegraph = new HashMap<>(9000);

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
     * Converts the given Chinese character codepoint into the corresponding telegraph code. For
     * example, an input of 0x56FD will yield 948.
     * @param code a valid Chinese character codepoint
     * @return the corresponding telegraph code
     */
    public int hanToTelegraph(int code) {
        // TODO Code me!
        return 0;
    }

    /**
     * Converts the given telegraph code into the corresponding simplified Chinese character
     * codepoint. For example, an input of 948 will yield 0x56FD.
     * @param code a valid Chinese telegraph code
     * @return the corresponding simplified Chinese character codepoint
     */
    public int telegraphToSimplified(int code) {
        // TODO Code me!
        return 0;
    }

    /**
     * Converts the given telegraph code into the corresponding traditional Chinese character
     * codepoint. For example, an input of 948 will yield 0x570B.
     * @param code a valid Chinese telegraph code
     * @return the corresponding traditional Chinese character codepoint
     */
    public int telegraphToTraditional(int code) {
        // TODO Code me!
        return 0;
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

                mSimplifiedToTelegraph.put(codePoint, ctcCode);

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

                mTraditionalToTelegraph.put(codePoint, ctcCode);

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

}
