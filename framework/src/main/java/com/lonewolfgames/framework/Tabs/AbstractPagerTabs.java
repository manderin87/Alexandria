package com.lonewolfgames.framework.Tabs;

import com.lonewolfgames.framework.AbstractMainApplication;
import java.util.ArrayList;

/**
 * Created by jhyde on 2/4/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class AbstractPagerTabs<T extends AbstractPagerTab> {

    protected AbstractMainApplication mApp;
    protected ArrayList<T> mTabs;

    public AbstractPagerTabs(AbstractMainApplication main) {
        mApp = main;
    }

    public int size() {
        return mTabs.size();
    }

    public String title(int position) {
        if(position >= 0 && position < mTabs.size()) {
            AbstractPagerTab tab = mTabs.get(position);

            return tab.title();
        } else {
            return "";
        }
    }

    public AbstractMainApplication context() { return mApp; }


//	public int tabIndicatorColor(int position) {
//		DashboardPagerTab tab = mTabs.getAll(position);
//
//		return tab.indicatorColor();
//	}
//
//	public int tabDividerColor(int position) {
//		DashboardPagerTab tab = mTabs.getAll(position);
//
//		return tab.dividerColor();
//	}

    public AbstractPagerFragment tab(int index) {
        T tab = mTabs.get(index);

        return tab.fragment();
    }
}
