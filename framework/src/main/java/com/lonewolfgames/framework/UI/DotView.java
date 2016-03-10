package com.lonewolfgames.framework.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lonewolfgames.framework.R;
import com.lonewolfgames.framework.Tabs.PageIndicator;

import java.util.HashMap;

/**
 * Created by jhyde on 6/29/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class DotView extends LinearLayout implements PageIndicator {


    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private int mSelectedIndex;
    private int mBackgroundImage = 0;
    private int mForegroundImage = 0;

    private int mBackgroundImageSize = 0;
    private int mForegroundImageSize = 0;

    private int mImageMargin = 15;

    private HashMap<Integer, ImageView> mImageViews = new HashMap<>();

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) return;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotView, 0, 0);

        mForegroundImage = a.getResourceId(R.styleable.DotView_foregroundImage, 0);
        mBackgroundImage = a.getResourceId(R.styleable.DotView_backgroundImage, 0);

        mForegroundImageSize = a.getDimensionPixelSize(R.styleable.DotView_foregroundImageSize, mForegroundImageSize);
        mBackgroundImageSize = a.getDimensionPixelSize(R.styleable.DotView_backgroundImageSize, mBackgroundImageSize);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    public DotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotView, defStyle, 0);

        mForegroundImage = a.getResourceId(R.styleable.DotView_foregroundImage, 0);
        mBackgroundImage = a.getResourceId(R.styleable.DotView_backgroundImage, 0);

        mForegroundImageSize = a.getDimensionPixelSize(R.styleable.DotView_foregroundImageSize, mForegroundImageSize);
        mBackgroundImageSize = a.getDimensionPixelSize(R.styleable.DotView_backgroundImageSize, mBackgroundImageSize);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }


//    public boolean onTouchEvent(android.view.MotionEvent ev) {
//        if (super.onTouchEvent(ev)) {
//            return true;
//        }
//        if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) {
//            return false;
//        }
//
//        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//                mLastMotionX = ev.getX();
//                break;
//
//            case MotionEvent.ACTION_MOVE: {
//                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
//                final float x = MotionEventCompat.getX(ev, activePointerIndex);
//                final float deltaX = x - mLastMotionX;
//
//                if (!mIsDragging) {
//                    if (Math.abs(deltaX) > mTouchSlop) {
//                        mIsDragging = true;
//                    }
//                }
//
//                if (mIsDragging) {
//                    mLastMotionX = x;
//                    if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
//                        mViewPager.fakeDragBy(deltaX);
//                    }
//                }
//
//                break;
//            }
//
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (!mIsDragging) {
//                    final int count = mViewPager.getAdapter().getCount();
//                    final int width = getWidth();
//                    final float halfWidth = width / 2f;
//                    final float sixthWidth = width / 6f;
//
//                    if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
//                        if (action != MotionEvent.ACTION_CANCEL) {
//                            mViewPager.setCurrentItem(mCurrentPage - 1);
//                        }
//                        return true;
//                    } else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth)) {
//                        if (action != MotionEvent.ACTION_CANCEL) {
//                            mViewPager.setCurrentItem(mCurrentPage + 1);
//                        }
//                        return true;
//                    }
//                }
//
//                mIsDragging = false;
//                mActivePointerId = INVALID_POINTER;
//                if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
//                break;
//
//            case MotionEventCompat.ACTION_POINTER_DOWN: {
//                final int index = MotionEventCompat.getActionIndex(ev);
//                mLastMotionX = MotionEventCompat.getX(ev, index);
//                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
//                break;
//            }
//
//            case MotionEventCompat.ACTION_POINTER_UP:
//                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
//                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
//                if (pointerId == mActivePointerId) {
//                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
//                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
//                }
//                mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
//                break;
//        }
//
//        return true;
//    }


    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(null);
        }

        PagerAdapter adapter = view.getAdapter();
        if(adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        adapter.getCount();



        for(int index = 0; index < adapter.getCount(); index++) {

            ImageView dot_image = new ImageView(getContext());


            LinearLayout.LayoutParams layoutParams;

            if(index == mSelectedIndex) {
                layoutParams = new LinearLayout.LayoutParams(mForegroundImageSize, mForegroundImageSize);
                layoutParams.rightMargin = mImageMargin;
                dot_image.setLayoutParams(layoutParams);
                dot_image.setImageResource(mForegroundImage);

            } else {
                layoutParams = new LinearLayout.LayoutParams(mBackgroundImageSize, mBackgroundImageSize);
                layoutParams.rightMargin = mImageMargin;
                dot_image.setLayoutParams(layoutParams);
                dot_image.setImageResource(mBackgroundImage);
            }

            mImageViews.put(index, dot_image);
            addView(dot_image);
        }

        mViewPager = view;
        view.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }

        mSelectedIndex = item;
        mViewPager.setCurrentItem(item);

        for(int index = 0; index < mViewPager.getAdapter().getCount(); index++) {
            ImageView dot_image = mImageViews.get(index);

            LinearLayout.LayoutParams layoutParams;

            if(index == mSelectedIndex) {
                layoutParams = new LinearLayout.LayoutParams(mForegroundImageSize, mForegroundImageSize);
                layoutParams.rightMargin = mImageMargin;
                dot_image.setLayoutParams(layoutParams);
                dot_image.setImageResource(mForegroundImage);
            } else {
                layoutParams = new LinearLayout.LayoutParams(mBackgroundImageSize, mBackgroundImageSize);
                layoutParams.rightMargin = mImageMargin;
                dot_image.setLayoutParams(layoutParams);
                dot_image.setImageResource(mBackgroundImage);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        mImageViews.clear();
        removeAllViews();

        PagerAdapter adapter = mViewPager.getAdapter();

        for(int index = 0; index < adapter.getCount(); index++) {
            ImageView dot_image = new ImageView(getContext());

            LinearLayout.LayoutParams layoutParams;

            if(index == mSelectedIndex) {
                layoutParams = new LinearLayout.LayoutParams(mForegroundImageSize, mForegroundImageSize);
                layoutParams.rightMargin = mImageMargin;
                dot_image.setLayoutParams(layoutParams);
                dot_image.setImageResource(mForegroundImage);
            } else {
                layoutParams = new LinearLayout.LayoutParams(mBackgroundImageSize, mBackgroundImageSize);
                layoutParams.rightMargin = mImageMargin;
                dot_image.setLayoutParams(layoutParams);
                dot_image.setImageResource(mBackgroundImage);
            }

            mImageViews.put(index, dot_image);
            addView(dot_image);
        }

        if(mSelectedIndex > adapter.getCount()) {
            mSelectedIndex = adapter.getCount() - 1;
        }

        setCurrentItem(mSelectedIndex);
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }
}
