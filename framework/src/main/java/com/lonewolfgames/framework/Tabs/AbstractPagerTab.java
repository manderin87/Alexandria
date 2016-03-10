package com.lonewolfgames.framework.Tabs;

import com.lonewolfgames.framework.AbstractMainApplication;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractPagerTab<F extends AbstractPagerFragment, T>  {
	
	private static final String TAG = AbstractPagerTab.class.getSimpleName();
	
	private AbstractMainApplication mApp;
	private T 					mTab;
	private String 				mTitle 			= "";
	private F 					mFragment 		= null;
//	private int					mIndicatorColor = Color.BLUE;
//	private int					mDividerColor = Color.GRAY;
	
	public T tab() { return mTab; }
	
//	public void setShowCount(boolean b) {
//		mShowCount = b;
//	}
	
	public String title() { 
		return mTitle;
	}
	
	public F fragment() { return mFragment; }
//	public int indicatorColor() { return mIndicatorColor; }
//	public int dividerColor() { return mDividerColor; }
	
	protected AbstractPagerTab(Builder<F, T> build) {
		mApp			= build.mApp;
		mTab			= build.mTab;
		mTitle 			= build.mTitle;
		mFragment 		= build.mFragment;
//		mIndicatorColor = build.mIndicatorColor;
//		mDividerColor 	= build.mDividerColor;
	}
	
	public abstract static class Builder<F extends AbstractPagerFragment, T> {
		private AbstractMainApplication		mApp;
		private T 					mTab;
		private boolean				mShowCount = false;
		private String 				mTitle 		= "";
		private F 					mFragment 	= null;
//		private int					mIndicatorColor = Color.BLUE;
//		private int					mDividerColor = Color.GRAY;
		
		public Builder(AbstractMainApplication app) {
			mApp = app;
		}
		
		public Builder<F, T> title(String title) { mTitle = title; return this; }
		public Builder<F, T> fragment(F fragment) { mFragment = fragment; return this; }
		public Builder<F, T> tab(T tab) { mTab = tab; return this; }
		public Builder<F, T> showCount(boolean count) { mShowCount = count; return this; }
//		public Builder<F, T> indicatorColor(int color) { mIndicatorColor = color; return this; }
//		public Builder<F, T> dividerColor(int color) { mDividerColor = color; return this; }
//		public Builder<F, T> indicatorColor(String color) { mIndicatorColor = Color.parseColor(color); return this; }
//		public Builder<F, T> dividerColor(String color) { mDividerColor = Color.parseColor(color); return this; }
		
		public abstract AbstractPagerTab<F, T> build();
	}
}
