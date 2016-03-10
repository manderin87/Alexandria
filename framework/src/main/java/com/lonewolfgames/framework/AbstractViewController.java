package com.lonewolfgames.framework;

import android.app.Activity;

/**
 * Created by jhyde on 8/24/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class AbstractViewController<T extends Activity> {

    protected AbstractMainApplication mApp;
    protected static AbstractViewController mInstance = null;
    protected T mParentView;

    protected AbstractViewController(AbstractMainApplication app, T parentView) {
        mApp = app;

        mInstance = this;
        mParentView = parentView;
    }

    public T parent() { return mParentView; }
}
