package com.lonewolfgames.framework;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int mMargin;

    public MarginDecoration(int margin) {
        mMargin = margin;
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(mMargin, mMargin, mMargin, mMargin);
    }
}
