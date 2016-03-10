package com.lonewolfgames.framework.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lonewolfgames.framework.R;

/**
 * Created by jhyde on 7/28/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class PincodeView extends RelativeLayout {

    // Data Member Variables -----------------------------------------------------------------------

    private String  mPincode = "";
    private String  mCurrentPincode = "";
    private int     mCurrentErrors = 0;

    private int mPinLength = -1;
    private int mMaxErrors = -1;

    private PincodeStatus mStatus = PincodeStatus.STATUS_VERIFY;

    private boolean mShowAlternate = false;

    private PincodeViewListener mListener;
    // ---------------------------------------------------------------------------------------------

    // Layout Member Variables ---------------------------------------------------------------------
    private LayoutInflater mInflater;

    private TextView mErrorTextView;

    private PinView mPinView;

    private TextView mTitleTextView;

    private Button mButton0;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;

    private Button mDelete;
    private Button mAlternate;
    // ---------------------------------------------------------------------------------------------


    public PincodeView(Context context) {
        super(context, null);

        if (isInEditMode()) return;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public PincodeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) return;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PincodeView, 0, 0);

        mPinLength = a.getInteger(R.styleable.PincodeView_pinLength, mPinLength);
        mMaxErrors = a.getInteger(R.styleable.PincodeView_maxErrors, mMaxErrors);

        mShowAlternate = a.getBoolean(R.styleable.PincodeView_showAlternate, mShowAlternate);

        a.recycle();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public PincodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) return;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PincodeView, defStyleAttr, 0);

        mPinLength = a.getInteger(R.styleable.PincodeView_pinLength, mPinLength);
        mMaxErrors = a.getInteger(R.styleable.PincodeView_maxErrors, mMaxErrors);

        mShowAlternate = a.getBoolean(R.styleable.PincodeView_showAlternate, mShowAlternate);

        a.recycle();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    private void initialize() {

        View view = mInflater.inflate(R.layout.pincode_view, this, true);

        mPinView = (PinView) view.findViewById(R.id.pinView);
        mPinView.setPinLength(mPinLength);

        mTitleTextView = (TextView) view.findViewById(R.id.textView_title);

        mErrorTextView = (TextView) view.findViewById(R.id.textView_error);
        updateError();

        mButton0 = (Button) view.findViewById(R.id.button_0);
        mButton0.setOnClickListener(new DigitClickListener("0"));

        mButton1 = (Button) view.findViewById(R.id.button_1);
        mButton1.setOnClickListener(new DigitClickListener("1"));

        mButton2 = (Button) view.findViewById(R.id.button_2);
        mButton2.setOnClickListener(new DigitClickListener("2"));

        mButton3 = (Button) view.findViewById(R.id.button_3);
        mButton3.setOnClickListener(new DigitClickListener("3"));

        mButton4 = (Button) view.findViewById(R.id.button_4);
        mButton4.setOnClickListener(new DigitClickListener("4"));

        mButton5 = (Button) view.findViewById(R.id.button_5);
        mButton5.setOnClickListener(new DigitClickListener("5"));

        mButton6 = (Button) view.findViewById(R.id.button_6);
        mButton6.setOnClickListener(new DigitClickListener("6"));

        mButton7 = (Button) view.findViewById(R.id.button_7);
        mButton7.setOnClickListener(new DigitClickListener("7"));

        mButton8 = (Button) view.findViewById(R.id.button_8);
        mButton8.setOnClickListener(new DigitClickListener("8"));

        mButton9 = (Button) view.findViewById(R.id.button_9);
        mButton9.setOnClickListener(new DigitClickListener("9"));

        mDelete = (Button) view.findViewById(R.id.button_delete);
        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mCurrentPincode.isEmpty()) {
                    mCurrentPincode = mCurrentPincode.substring(0, mCurrentPincode.length() - 1);
                    mPinView.update(mCurrentPincode.length());
                }
            }
        });

        if(mShowAlternate) {
            mAlternate = (Button) view.findViewById(R.id.button_alt);
            mAlternate.setVisibility(VISIBLE);
            mAlternate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        mListener.OnAlternateClick();
                    }
                }
            });
        }


//        addView(view);
    }

    private void updateError() {
        if(mCurrentErrors > 0 && mCurrentErrors < mMaxErrors) {
            mErrorTextView.setText(String.valueOf(mMaxErrors - mCurrentErrors) + " chances remaining");
            mErrorTextView.setVisibility(View.VISIBLE);
        } else {
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    private void updateDelete() {
        if(mCurrentPincode.isEmpty()) {
            mDelete.setVisibility(GONE);
        } else {
            mDelete.setVisibility(VISIBLE);
        }
    }

    private void disable() {
        mButton0.setEnabled(false);
        mButton1.setEnabled(false);
        mButton2.setEnabled(false);
        mButton3.setEnabled(false);
        mButton4.setEnabled(false);
        mButton5.setEnabled(false);
        mButton6.setEnabled(false);
        mButton7.setEnabled(false);
        mButton8.setEnabled(false);
        mButton9.setEnabled(false);

        mDelete.setEnabled(false);
    }

    public void setVerifyPincode(String pincode) {
        mStatus = PincodeStatus.STATUS_VERIFY;
        mPincode = pincode;
        mCurrentPincode = "";
        mPinView.update(mCurrentPincode.length());
        setTitle("Enter Pincode");
    }

    public void setSetPincode() {
        mStatus = PincodeStatus.STATUS_SET;
        mPincode = "";
        mCurrentPincode = "";
        mPinView.update(mCurrentPincode.length());
        setTitle("Enter Pincode");
    }

    public void setSetReverifyPincode(String pincode) {
        mStatus = PincodeStatus.STATUS_SET_REVERIFY;
        mPincode = pincode;
        mCurrentPincode = "";
        mPinView.update(mCurrentPincode.length());
        setTitle("Re-Enter Pincode");
    }

    public void setPincodeViewListener(PincodeViewListener listener) {
        mListener = listener;
    }

    private void setTitle(String title) { mTitleTextView.setText(title); }

    private void handleButton(String digit) {

        if(mStatus == PincodeStatus.STATUS_VERIFY || mStatus == PincodeStatus.STATUS_SET_REVERIFY) {
            if(mCurrentPincode.length() < mPincode.length()) {
                mCurrentPincode += digit;
                mPinView.update(mCurrentPincode.length());
            }
        }

        if(mStatus == PincodeStatus.STATUS_SET) {
            if(mCurrentPincode.length() < mPinLength) {
                mCurrentPincode += digit;
                mPinView.update(mCurrentPincode.length());
            }
        }

        updateDelete();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mStatus == PincodeStatus.STATUS_VERIFY || mStatus == PincodeStatus.STATUS_SET_REVERIFY) {
                    if(mCurrentPincode.length() == mPincode.length()) {
                        if(!mPincode.equalsIgnoreCase(mCurrentPincode)) {
                            mPinView.shake();
                            mCurrentPincode = "";
                            mDelete.setVisibility(GONE);
                            mCurrentErrors++;
                            updateError();

                            if(mCurrentErrors >= mMaxErrors) {
                                mCurrentErrors = 0;
                                disable();
                                updateError();
                                if(mListener != null) {
                                    mListener.OnPincodeInvalid(mCurrentPincode);
                                }
                            }

                            return;
                        } else {
                            if(mListener != null) {
                                mPincode = "";
                                mListener.OnPincodeValid(mStatus, mCurrentPincode);
                            }
                        }
                    }
                }

                if(mStatus == PincodeStatus.STATUS_SET) {
                    if(mCurrentPincode.length() == mPinLength) {
                        if(mListener != null) {
                            mListener.OnPincodeSet(mCurrentPincode);
                        }
                    }
                }


            }
        }, 1000);


    }



    private class DigitClickListener implements View.OnClickListener {

        private String mDigit = "";

        public DigitClickListener(String digit) {
            mDigit = digit;
        }

        @Override
        public void onClick(View view) {
            handleButton(mDigit);
        }
    }

    public enum PincodeStatus {
        STATUS_VERIFY,
        STATUS_INVALID,
        STATUS_SET,
        STATUS_SET_REVERIFY,
    }

    public interface PincodeViewListener {
        void OnPincodeValid(PincodeStatus status, String pincode);
        void OnPincodeInvalid(String pincode);
        void OnPincodeSet(String pincode);
        void OnAlternateClick();
    }
}
