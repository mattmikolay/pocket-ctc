package com.mattmik.dianma;

// Copyright 2016 Matthew Mikolay. All rights reserved.

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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

    public SwitcherView(Context context) {
        super(context);
        init(context);
    }

    public SwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwitcherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitcherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

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
    public void setOnModeChangeListener(OnTranslateModeSwitchListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {

        // Switch to the opposite translate mode
        mTranslateMode = (mTranslateMode == TranslateMode.HAN_TO_TELE) ?
                TranslateMode.TELE_TO_HAN :
                TranslateMode.HAN_TO_TELE;

        refreshLabels();

        // Notify any registered listener
        if(mListener != null)
            mListener.onTranslateModeSwitched(mTranslateMode);

    }

    public interface OnTranslateModeSwitchListener {

        void onTranslateModeSwitched(int mode);

    }

}
