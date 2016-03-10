package com.hydeudacityproject.alexandria;

import com.lonewolfgames.framework.AbstractAppSettings;
import com.lonewolfgames.framework.AbstractMainApplication;

/**
 * Created by jhyde on 12/21/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AppSettings extends AbstractAppSettings {

    public AppSettings(AbstractMainApplication app, AppSettingsLoadedListener listener) {
        super(app, listener);
    }
}
