package com.lonewolfgames.framework.Cache.Preferences;

import android.content.Context;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class PreferencesHandle extends AbstractPreferencesHandle {
	
	public void write() {
		if(data().isEmpty()) {
			return;
		}
		
		writePreferences(this);
	}
	
	public void read() {
		readPreferences(this);
	}
	
	private PreferencesHandle(Builder build) {
		super(build);
	}
	
	public static class Builder extends AbstractPreferencesHandle.Builder {

		public Builder(Context context, String filename) {
			super(context, filename);
			
		}

		@Override
		public PreferencesHandle build() {
			return new PreferencesHandle(this);
		}
		
	}
}
