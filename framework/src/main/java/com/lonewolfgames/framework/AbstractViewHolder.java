package com.lonewolfgames.framework;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jhyde on 8/27/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractViewHolder<T, A extends AbstractViewAdapter> extends RecyclerView.ViewHolder {

    protected A mAdapter;

    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    public AbstractViewHolder(A adapter, View itemView) {
        super(itemView);

        mAdapter = adapter;
    }

    public abstract void initialize(T data, int position);

    public A adapter() { return mAdapter; }

    public abstract static class Builder<A extends AbstractViewAdapter> {
        protected A         mAdapter;
        protected ViewGroup mParent;
        protected int       mLayoutResourceId;

        public Builder(ViewGroup parent, int layoutResourceId) {
            mParent = parent;
            mLayoutResourceId = layoutResourceId;
        }

        public Builder(A adapter, ViewGroup parent, int layoutResourceId) {
            mAdapter = adapter;
            mParent = parent;
            mLayoutResourceId = layoutResourceId;
        }

        public abstract AbstractViewHolder build();
    }
}
