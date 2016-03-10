package com.lonewolfgames.framework.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lonewolfgames.framework.R;

/**
 * Created by jhyde on 8/13/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class ImageToggleButton extends LinearLayout {

    private boolean mChecked = false;

    private int mCheckedImageResource = -1;
    private int mUncheckedImageResource = -1;
    private int mCheckedBackgroundImageResource = -1;
    private int mUncheckedBackgroundImageResource = -1;

    private LayoutInflater mInflater;

    private LinearLayout mLayout;
    private ImageButton mButton;

    public ImageToggleButton(Context context) {
        super(context, null);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ImageToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) return;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageToggleButton, 0, 0);

        mCheckedImageResource = a.getResourceId(R.styleable.ImageToggleButton_checkedImage, 0);
        mUncheckedImageResource = a.getResourceId(R.styleable.ImageToggleButton_uncheckedImage, 0);
        mCheckedBackgroundImageResource = a.getResourceId(R.styleable.ImageToggleButton_checkedBackgroundImage, 0);
        mUncheckedBackgroundImageResource = a.getResourceId(R.styleable.ImageToggleButton_uncheckedBackgroundImage, 0);

        a.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public ImageToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageToggleButton, defStyleAttr, 0);

        mCheckedImageResource = a.getResourceId(R.styleable.ImageToggleButton_checkedImage, 0);
        mUncheckedImageResource = a.getResourceId(R.styleable.ImageToggleButton_uncheckedImage, 0);
        mCheckedBackgroundImageResource = a.getResourceId(R.styleable.ImageToggleButton_checkedBackgroundImage, 0);
        mUncheckedBackgroundImageResource = a.getResourceId(R.styleable.ImageToggleButton_uncheckedBackgroundImage, 0);

        a.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    private void initialize() {

        setClickable(true);

        View view = mInflater.inflate(R.layout.image_toggle_button, this, true);

        setBackgroundResource(mUncheckedBackgroundImageResource);

        mButton = (ImageButton) view.findViewById(R.id.imageButton_button);
        mButton.setImageResource(mUncheckedImageResource);
    }

    public void setChecked(boolean checked) {
        mChecked = checked;

        update();
    }

    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        mButton.setOnClickListener(listener);
    }

    private void update() {
        if(mChecked) {
            setBackgroundResource(mCheckedBackgroundImageResource);
            mButton.setImageResource(mCheckedImageResource);
        } else {
            setBackgroundResource(mUncheckedBackgroundImageResource);
            mButton.setImageResource(mUncheckedImageResource);
        }

        invalidate();
    }

    public void onClick(View view) {
        mChecked = !mChecked;

        update();
    }
}
