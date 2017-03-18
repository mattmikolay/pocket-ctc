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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * SwitcherView is a widget that allows the user to switch between translation modes.
 *
 * <p>
 *
 * Translate mode changes will be reported to the registered {@link OnTranslateModeSwitchListener}.
 */
public class SwitcherView extends RelativeLayout implements View.OnClickListener {

    private TextView mFromText;
    private TextView mToText;

    private OnTranslateModeSwitchListener mListener;
    private int mTranslateMode;

    /**
     * Constructs a new SwitcherView.
     * @param context context in which this view is running
     */
    public SwitcherView(@NonNull Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new SwitcherView.
     * @param context context in which this view is running
     * @param attrs attributes of inflating XML tag
     */
    public SwitcherView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new SwitcherView.
     * @param context context in which this view is running
     * @param attrs attributes of inflating XML tag
     * @param defStyleAttr style attribute
     */
    public SwitcherView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Constructs a new SwitcherView.
     * @param context context in which this view is running
     * @param attrs attributes of inflating XML tag
     * @param defStyleAttr style attribute
     * @param defStyleRes style resource
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitcherView(@NonNull Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Initializes this SwitcherView to be ready for user action. This method will inflate this
     * view's layout and set up any child views.
     * @param context context in which this view is running
     */
    private void init(@NonNull Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View parent = inflater.inflate(R.layout.toolbar_switcher, this, true);

        mFromText = (TextView) parent.findViewById(R.id.txt_from_language);
        mToText = (TextView) parent.findViewById(R.id.txt_to_language);

        // Add a click listener to the button that triggers a translate mode swap
        ImageButton swapButton = (ImageButton) parent.findViewById(R.id.btn_switcher);
        swapButton.setOnClickListener(this);

        // Default to conversion from Chinese to telegraphy code
        setTranslateMode(TranslateMode.HAN_TO_TELE);

    }

    /**
     * Updates the text displayed on the switcher to match the current translation mode.
     */
    private void refreshLabels() {

        if(mTranslateMode == TranslateMode.TELE_TO_HAN) {

            mFromText.setText(R.string.lang_telephony);
            mToText.setText(R.string.lang_chinese);

        } else {

            mFromText.setText(R.string.lang_chinese);
            mToText.setText(R.string.lang_telephony);

        }

    }

    /**
     * Sets the current translate mode.
     * @param mode a translate mode. Valid values are {@link TranslateMode#HAN_TO_TELE} and
     *             {@link TranslateMode#TELE_TO_HAN}.
     */
    public void setTranslateMode(int mode) {
        mTranslateMode = mode;
        refreshLabels();
    }

    /**
     * Returns the current translate mode.
     * @return the current translate mode. Valid values are {@link TranslateMode#HAN_TO_TELE} and
     *             {@link TranslateMode#TELE_TO_HAN}.
     */
    public int getTranslateMode() {
        return mTranslateMode;
    }

    /**
     * Sets the listener that will be notified when the translation mode changes.
     * @param listener an OnTranslateModeSwitchListener
     */
    public void setOnModeChangeListener(@Nullable OnTranslateModeSwitchListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(@Nullable View view) {

        // Switch to the opposite translate mode
        mTranslateMode = (mTranslateMode == TranslateMode.HAN_TO_TELE) ?
                TranslateMode.TELE_TO_HAN :
                TranslateMode.HAN_TO_TELE;

        refreshLabels();

        // Notify any registered listener
        if(mListener != null)
            mListener.onTranslateModeSwitched(mTranslateMode);

    }

    /**
     * Interface used to notify implementing classes when the app translation mode is changed.
     */
    public interface OnTranslateModeSwitchListener {

        /**
         * Invoked when a user switches app translation modes using a {@link SwitcherView}.
         * @param mode the new translation mode, either {@link TranslateMode#HAN_TO_TELE} or
         *             {@link TranslateMode#TELE_TO_HAN}.
         */
        void onTranslateModeSwitched(int mode);

    }

}
