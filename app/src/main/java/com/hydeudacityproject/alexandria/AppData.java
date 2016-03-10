package com.hydeudacityproject.alexandria;

import com.hydeudacityproject.alexandria.Service.ContentProvider.BookDBHelper;
import com.lonewolfgames.framework.AbstractAppData;
import com.lonewolfgames.framework.AbstractAppSettings;
import com.lonewolfgames.framework.AbstractMainApplication;

/**
 * Created by jhyde on 12/21/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AppData extends AbstractAppData implements AbstractAppSettings.AppSettingsLoadedListener {

    private BookDBHelper mHelper;

    public AppData(AbstractMainApplication app) {
        super(app);

        mHelper = new BookDBHelper(mApp);
    }

    public static AppData instance() {
        return (AppData) mInstance;
    }

    @Override
    public void OnAppSettingsLoadingFinished() {

    }

    public BookDBHelper database() { return mHelper; }
}
