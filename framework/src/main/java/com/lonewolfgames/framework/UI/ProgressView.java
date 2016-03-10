package com.lonewolfgames.framework.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lonewolfgames.framework.R;

/**
 * Created by jhyde on 8/27/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class ProgressView extends LinearLayout {

    private LayoutInflater mInflater;



    public ProgressView(Context context) {
        super(context);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public void initialize() {
        hideProgress();
        View view = mInflater.inflate(R.layout.progress_view, this, true);


    }

    public void hideProgress() {
        setVisibility(View.GONE);
    }

    public void showProgress() { setVisibility(View.VISIBLE); }
}
