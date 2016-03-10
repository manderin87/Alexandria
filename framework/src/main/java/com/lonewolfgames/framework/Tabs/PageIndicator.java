package com.lonewolfgames.framework.Tabs;

import android.support.v4.view.ViewPager;

/**
 * Created by jhyde on 6/29/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public interface PageIndicator extends ViewPager.OnPageChangeListener {
    void setViewPager(ViewPager view);
    void setViewPager(ViewPager view, int initialPosition);
    void setCurrentItem(int item);
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);
    void notifyDataSetChanged();
}
