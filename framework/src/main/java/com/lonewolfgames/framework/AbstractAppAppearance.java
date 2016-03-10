package com.lonewolfgames.framework;

import com.lonewolfgames.framework.AbstractMainApplication;

/**
 * Created by jhyde on 8/13/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractAppAppearance {

    protected AbstractMainApplication mApp;
    protected static AbstractAppAppearance mInstance;

    protected AbstractAppAppearance(AbstractMainApplication app) {
        mApp = app;

        mInstance = this;
    }
}
