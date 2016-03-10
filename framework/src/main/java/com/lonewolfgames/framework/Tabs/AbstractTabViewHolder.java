package com.lonewolfgames.framework.Tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonewolfgames.framework.AbstractMainApplication;


/**
 * The class is used to create a view holder when not using a recycler view
 * Created by jhyde on 2/4/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractTabViewHolder<I extends Object, F extends AbstractPagerFragment> {

    private AbstractMainApplication mApp;
    private Context                 mContext;
    private View                    mView;
    private F                       mFragment;

//    protected AbstractViewHolder(View itemView) {
//        mView = itemView;
//        mContext = itemView.getContext();
//    }

    public View view() { return mView; }
    public Context context() { return mContext; }
    public AbstractMainApplication app() { return mApp; }

    protected abstract void setAnimation(View viewToAnimate, int position);
    public abstract void initialize(F fragment, I item, int position);

    protected AbstractTabViewHolder(Builder build) {
        mContext = build.mParent.getContext();
        mApp = (AbstractMainApplication) mContext.getApplicationContext();
        mView = LayoutInflater.from(build.mParent.getContext()).inflate(build.mResource, build.mParent, build.mAttachToRoot);
    }



    public abstract static class Builder {

        public View         mView;
        public int          mResource;
        public ViewGroup    mParent;
        public boolean      mAttachToRoot = false;

        public Builder() { }

        public Builder parent(ViewGroup parent) { mParent = parent; return this; }
        public Builder resource(int resource) { mResource = resource; return this; }
        public Builder attachToRoot(boolean attach) { mAttachToRoot = attach; return this; }

        public abstract AbstractTabViewHolder build();
    }
}
