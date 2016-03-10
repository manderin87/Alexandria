package com.lonewolfgames.framework.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lonewolfgames.framework.R;

import java.util.HashMap;

/**
 * Created by jhyde on 6/29/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class PinView extends LinearLayout {

    private int mBackgroundImage = 0;
    private int mForegroundImage = 0;

    private int mBackgroundImageSize = 0;
    private int mForegroundImageSize = 0;

    private int mPinLength = 1;

    private int mImageMargin = 15;

    private HashMap<Integer, ImageView> mImageViews = new HashMap<>();

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) return;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PinView, 0, 0);

        mForegroundImage = a.getResourceId(R.styleable.PinView_foregroundImage, 0);
        mBackgroundImage = a.getResourceId(R.styleable.PinView_backgroundImage, 0);

        mForegroundImageSize = a.getDimensionPixelSize(R.styleable.PinView_foregroundImageSize, mForegroundImageSize);
        mBackgroundImageSize = a.getDimensionPixelSize(R.styleable.PinView_backgroundImageSize, mBackgroundImageSize);

        mImageMargin =  a.getDimensionPixelSize(R.styleable.PinView_imageMargin, mImageMargin);

        mPinLength = a.getInteger(R.styleable.PinView_pinLength, mPinLength);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        initialize();
    }

    public PinView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PinView, defStyle, 0);

        mForegroundImage = a.getResourceId(R.styleable.PinView_foregroundImage, 0);
        mBackgroundImage = a.getResourceId(R.styleable.PinView_backgroundImage, 0);

        mForegroundImageSize = a.getDimensionPixelSize(R.styleable.PinView_foregroundImageSize, mForegroundImageSize);
        mBackgroundImageSize = a.getDimensionPixelSize(R.styleable.PinView_backgroundImageSize, mBackgroundImageSize);

        mImageMargin =  a.getDimensionPixelSize(R.styleable.PinView_imageMargin, mImageMargin);

        mPinLength = a.getInteger(R.styleable.PinView_pinLength, mPinLength);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        initialize();
    }

    public void setPinLength(int length) {
        mPinLength = length;
    }

    private void initialize() {
        for(int index = 1; index <= mPinLength; index++) {

            ImageView dot_image = new ImageView(getContext());

            LayoutParams layoutParams = new LayoutParams(mBackgroundImageSize, mBackgroundImageSize);
            layoutParams.leftMargin = mImageMargin;
            layoutParams.rightMargin = mImageMargin;
            dot_image.setLayoutParams(layoutParams);
            dot_image.setImageResource(mBackgroundImage);

            mImageViews.put(index, dot_image);
            addView(dot_image);
        }

        setWillNotDraw(false);
    }

    public void update(int filled) {
        if(filled < 1) {
            filled = 0;
        } else if(filled > mPinLength) {
            filled = mPinLength;
        }

        for(int index = 1; index <= mPinLength; index++) {

            ImageView dot_image = mImageViews.get(index);

            if(index <= filled) {
                dot_image.setImageResource(mForegroundImage);
            } else {
                dot_image.setImageResource(mBackgroundImage);
            }

            mImageViews.put(index, dot_image);
        }

        invalidate();
    }

    public void shake() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        update(0);
    }
}
