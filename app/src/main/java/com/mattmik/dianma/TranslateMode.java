package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

public final class TranslateMode {

    /** Translation mode for conversion from Chinese characters to telegraph code. */
    public static final int HAN_TO_TELE = 0;

    /** Translation mode for conversion from telegraph code to Chinese characters.  */
    public static final int TELE_TO_HAN = 1;

    // Private constructor to prevent instantiation
    private TranslateMode() {
        // Empty!
    }

}
