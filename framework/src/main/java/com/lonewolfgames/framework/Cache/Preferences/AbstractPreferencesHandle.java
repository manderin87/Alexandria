package com.lonewolfgames.framework.Cache.Preferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractPreferencesHandle {
	
	private Context					mContext;
	private String					mFilename;
	private HashMap<String, Object> mData;
	private int 					mMode = Context.MODE_PRIVATE;

	public abstract void write();
	public abstract void read();
	
	public int mode() { return mMode; }

	public Context context() { return mContext; }

	public String filename() { return mFilename; }
	
	public void add(String key, Object value) {
		if(mData == null) {
			mData = new HashMap<>();
		}
		
		mData.put(key, value);
		
		write();
	}
	
	public void set(String key, Object value) {
		if(mData == null) {
			mData = new HashMap<>();
		}
		
		mData.remove(key);

		mData.put(key, value);
		
		write();
	}
	
	public String getString(String key, String defaultValue) {
		if(key == null || key.isEmpty() || defaultValue == null) {
			return defaultValue;
		}
		
		String object = (String) mData.get(key);
		
		if(object == null) {
			return defaultValue;
		}
		
		return object;
	}
	
	public Boolean getBoolean(String key, Boolean defaultValue) {
		if(key.isEmpty() || defaultValue == null) {
			return defaultValue;
		}
		
		Boolean object = (Boolean) mData.get(key);
		
		if(object == null) {
			return defaultValue;
		}
		
		return object;
	}
	
    public boolean contains(String key) {
    	if(key == null || key.isEmpty()) {
    		return false;
    	}
    	
        return mData.containsKey(key);
    }
	
	public Object remove(String key) {
		if(key == null || key.isEmpty()) {
			return null;
		}
		
		if(mData == null) {
			return null;
		}
		
		Object object = mData.remove(key);
		
		write();
		
		return object;
	}
	
	protected void writePreferences(AbstractPreferencesHandle handle) {
		SharedPreferences prefs = handle.context().getApplicationContext().getSharedPreferences(handle.filename(), handle.mode());
		
		Editor editor = prefs.edit();
		
		editor.clear();
	
		Iterator<Entry<String, Object>> iter = handle.data().entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry<String, Object> pairs = iter.next();
	
			if(pairs.getValue() instanceof String) {
				editor.putString(pairs.getKey(), (String) pairs.getValue());
			} else if(pairs.getValue() instanceof Integer) {
				editor.putInt(pairs.getKey(), (Integer) pairs.getValue());
			} else if(pairs.getValue() instanceof Long) {
				editor.putLong(pairs.getKey(), (Long) pairs.getValue());
			} else if(pairs.getValue() instanceof Boolean) {
				editor.putBoolean(pairs.getKey(), (Boolean) pairs.getValue());
			} else if(pairs.getValue() instanceof Float) {
				editor.putFloat(pairs.getKey(), (Float) pairs.getValue());
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		editor.commit();
	}
	
	protected void readPreferences(AbstractPreferencesHandle handle) {
		SharedPreferences prefs = handle.context().getApplicationContext().getSharedPreferences(handle.filename(), handle.mode());
		
		Map<String, Object> keys = (Map<String, Object>) prefs.getAll();
		
		for(Map.Entry<String, Object> entry : keys.entrySet()) {
			handle.add(entry.getKey(), entry.getValue());
		}
	}

	public HashMap<String, Object> data() {
		return mData;
	}

	protected AbstractPreferencesHandle(Builder build) {
		mContext = build.mContext;
		
		mData = new HashMap<>();
	}
	
	public abstract static class Builder  {
		
		protected Context mContext;
		protected String mFileName;
		
		public Builder(Context context, String filename) {
			mContext = context;
			mFileName = filename;
		}
		
		public abstract AbstractPreferencesHandle build();
		
		
	}

}
