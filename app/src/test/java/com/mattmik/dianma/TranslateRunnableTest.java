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

import android.os.Handler;
import android.os.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 23)
public class TranslateRunnableTest {

    @Mock
    private Handler mHandler;

    @Mock
    private CodeDictionary mDictionary;

    @Captor
    private ArgumentCaptor<String> mResultCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSimplifiedToTelegraph() {

        Message responseMessage = Message.obtain();
        when(mHandler.obtainMessage(anyInt(), any())).thenReturn(responseMessage);

        when(mDictionary.simplifiedToTelegraph(anyInt()))
                .thenReturn(3443)
                .thenReturn(2015)
                .thenReturn(6073)
                .thenReturn(3474)
                .thenReturn(826)
                .thenReturn(null)
                .thenReturn(null)
                .thenReturn(7001)
                .thenReturn(2105)
                .thenReturn(8638)
                .thenReturn(1)
                .thenReturn(4407)
                .thenReturn(null);

        new TranslateRunnable(mHandler, mDictionary, "Hello, world!",
                TranslateMode.HAN_TO_TELE, false).run();

        verify(mDictionary, times(13)).simplifiedToTelegraph(anyInt());
        verify(mHandler).obtainMessage(eq(TranslateActivity.MSG_TRANSLATE_SUCCESS),
                mResultCaptor.capture());
        verify(mHandler).sendMessage(responseMessage);
        assertEquals("3443 2015 6073 3474 0826 , 7001 2105 8638 0001 4407 !",
                mResultCaptor.getValue());

    }

    @Test
    public void testTraditionalToTelegraph() {

        Message responseMessage = Message.obtain();
        when(mHandler.obtainMessage(anyInt(), any())).thenReturn(responseMessage);

        when(mDictionary.traditionalToTelegraph(anyInt()))
                .thenReturn(6600)
                .thenReturn(3567)
                .thenReturn(1154)
                .thenReturn(6349)
                .thenReturn(6480)
                .thenReturn(null)
                .thenReturn(null)
                .thenReturn(5671)
                .thenReturn(3536)
                .thenReturn(9346)
                .thenReturn(3231)
                .thenReturn(22)
                .thenReturn(null);

        new TranslateRunnable(mHandler, mDictionary, "Hello, world!",
                TranslateMode.HAN_TO_TELE, true).run();

        verify(mDictionary, times(13)).traditionalToTelegraph(anyInt());
        verify(mHandler).obtainMessage(eq(TranslateActivity.MSG_TRANSLATE_SUCCESS),
                mResultCaptor.capture());
        verify(mHandler).sendMessage(responseMessage);
        assertEquals("6600 3567 1154 6349 6480 , 5671 3536 9346 3231 0022 !",
                mResultCaptor.getValue());

    }

    @Test
    public void testTelegraphToSimplified() {

        Message responseMessage = Message.obtain();
        when(mHandler.obtainMessage(anyInt(), any())).thenReturn(responseMessage);

        when(mDictionary.telegraphToSimplified(anyInt()))
                .thenReturn(72)
                .thenReturn(101)
                .thenReturn(108)
                .thenReturn(108)
                .thenReturn(111)
                .thenReturn(119)
                .thenReturn(111)
                .thenReturn(114)
                .thenReturn(108)
                .thenReturn(100);

        new TranslateRunnable(mHandler, mDictionary,
                "1109 3448 8419 6305 3414, 3447 0696 6153 7623 5822!",
                TranslateMode.TELE_TO_HAN, false).run();

        verify(mDictionary, times(10)).telegraphToSimplified(anyInt());
        verify(mHandler).obtainMessage(eq(TranslateActivity.MSG_TRANSLATE_SUCCESS),
                mResultCaptor.capture());
        verify(mHandler).sendMessage(responseMessage);
        assertEquals("H e l l o, w o r l d!", mResultCaptor.getValue());

    }

    @Test
    public void testTelegraphToTraditional() {

        Message responseMessage = Message.obtain();
        when(mHandler.obtainMessage(anyInt(), any())).thenReturn(responseMessage);

        when(mDictionary.telegraphToTraditional(anyInt()))
                .thenReturn(72)
                .thenReturn(101)
                .thenReturn(108)
                .thenReturn(108)
                .thenReturn(111)
                .thenReturn(119)
                .thenReturn(111)
                .thenReturn(114)
                .thenReturn(108)
                .thenReturn(100);

        new TranslateRunnable(mHandler, mDictionary,
                "2986 9255 6157 4187 0317, 5029 0001 8005 8766 3086!",
                TranslateMode.TELE_TO_HAN, true).run();

        verify(mDictionary, times(10)).telegraphToTraditional(anyInt());
        verify(mHandler).obtainMessage(eq(TranslateActivity.MSG_TRANSLATE_SUCCESS),
                mResultCaptor.capture());
        verify(mHandler).sendMessage(responseMessage);
        assertEquals("H e l l o, w o r l d!", mResultCaptor.getValue());

    }

}
