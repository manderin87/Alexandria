package com.lonewolfgames.framework;

/**
 * Created by jhyde on 8/14/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractAppController {

    protected AbstractMainApplication mApp;
    protected static AbstractAppController mInstance = null;

    protected AbstractAppController(AbstractMainApplication app) {
        mApp = app;

        mInstance = this;
    }
}
