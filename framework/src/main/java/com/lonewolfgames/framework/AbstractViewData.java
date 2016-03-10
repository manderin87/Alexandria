package com.lonewolfgames.framework;

import java.util.ArrayList;

/**
 * Created by jhyde on 10/30/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractViewData<D> {

    private D mItem;
    private int mType = 0;

    protected AbstractViewData(D item) {
        mItem = item;
    }

    protected AbstractViewData(D item, int type) {
        mItem = item;
        mType = type;
    }

    public int type() { return mType; }
    public D item() { return mItem; }


}
