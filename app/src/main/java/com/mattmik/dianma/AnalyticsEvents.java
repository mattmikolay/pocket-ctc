/*
 * Copyright (c) 2016-2017 Matthew Mikolay
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
 * Defines event names used for logging via Firebase Analytics.
 */
public final class AnalyticsEvents {

    /**
     * Event recorded when the text to be translated was received in an Intent. For example, when
     * the user shares text into the app.
     */
    public static final String TEXT_FROM_INTENT = "text_from_intent";

    /**
     * Event recorded when the user clicks a link to view Pocket CTC's GitHub page.
     */
    public static final String VIEW_GITHUB_PAGE = "view_github_page";

}
