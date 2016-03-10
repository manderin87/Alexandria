package com.lonewolfgames.framework;

import com.lonewolfgames.framework.Cache.Preferences.PreferencesHandle;

/**
 * Created by jhyde on 8/13/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractAppSettings {

    public static boolean DEBUG_MESSAGES = true;
    public static boolean DEBUG_SOAP_MESSAGES = true;

    protected AbstractMainApplication mApp;
    protected static AbstractAppSettings mInstance = null;

    protected AppSettingsLoadedListener mListener;

    protected PreferencesHandle mPreferences;

    protected AbstractAppSettings(AbstractMainApplication app) {
        this(app, null);
    }

    protected AbstractAppSettings(AbstractMainApplication app, AppSettingsLoadedListener listener) {
        mApp = app;
        mListener = listener;

        mInstance = this;

        mPreferences = loadPreferences();
        mPreferences.read();
    }

    private PreferencesHandle loadPreferences() {
        return new PreferencesHandle.Builder(mApp, "mainapplication").build();
    }


    public interface AppSettingsLoadedListener {
        void OnAppSettingsLoadingFinished();
    }
}
