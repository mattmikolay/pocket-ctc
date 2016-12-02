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
 * Defines valid translation modes.
 */
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
