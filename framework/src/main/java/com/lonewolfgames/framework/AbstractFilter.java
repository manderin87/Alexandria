package com.lonewolfgames.framework;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by jhyde on 12/23/2015.
 */
public abstract class AbstractFilter<T,A extends AbstractViewAdapter> extends Filter {

    protected ArrayList<T> mData;
    protected ArrayList<T> mFilteredData;
    private A mAdapter;

    public AbstractFilter(A adapter) {
        mAdapter = adapter;

        mData = new ArrayList<>();
        mData.addAll(mAdapter.data());

        mFilteredData = new ArrayList<>();
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mAdapter.clear();
        mAdapter.addAll((ArrayList<T>) results.values);
        mAdapter.notifyDataSetChanged();
    }
}
