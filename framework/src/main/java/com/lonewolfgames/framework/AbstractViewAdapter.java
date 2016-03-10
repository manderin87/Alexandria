package com.lonewolfgames.framework;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by jhyde on 8/28/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractViewAdapter<D extends AbstractViewData, I, T extends AbstractViewHolder> extends RecyclerView.Adapter<T> {

    protected ArrayList<D> mItems = new ArrayList<>();
    private boolean mHasHeader = false;

    public AbstractViewAdapter() {
        mItems = new ArrayList<>();
    }

    public AbstractViewAdapter(ArrayList<D> data) {
        this();

        mItems.addAll(data);
    }

    public abstract void addAll(ArrayList<I> items);

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void addHeader(D header) {
        mItems.add(0, header);
        mHasHeader = true;
    }

    public void insertAt(int position, D item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void removeAt(int position, int amount) {
        for(int index = position; index < position + amount; index++) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addItem(D item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public int size() {

        if(mHasHeader) {
            return mItems.size() - 1;
        }

        return mItems.size();
    }

    public void setItems(ArrayList<I> items) {
        mItems.clear();
        addAll(items);
    }

    public ArrayList<D> items() { return mItems; }

    public ArrayList<I> data() {
        ArrayList<I> data = new ArrayList<>();

        for(D item : mItems) {
            data.add((I) item.item());
        }

        return data;
    }

    public D item(int position) {
        return mItems.get(position);
    }

    public D lastItem() { return mItems.get(mItems.size() - 1); }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        D data = mItems.get(position);

        holder.initialize(data, position);
    }

    @Override
    public int getItemCount() {
        if(mItems != null) {
            return mItems.size();
        }

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type();
    }

}
