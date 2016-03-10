package com.lonewolfgames.framework.Cache.Files;

import java.io.File;

import android.content.Context;
import android.os.Environment;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractFileHandle {
	
	private Context				mContext			= null;
	protected String			mFileName			= "";
	//private String				mUrlFrom			= "";
	protected Object			mData				= null;
	
	public enum FileLocation {
		NONE,
		INTERNAL,
		EXTERNAL,
		EXTERNAL_SD_CARD,
		INTERNAL_DATABASE,
		CACHE,
		URL,
		SHARED_PREFERENCES,
		ABSOLUTE;			// path must be set
	}
	
	// Methods --------------------------------------------------------------------------------------
	public Context context() { return mContext; }
	public String fileName() { return mFileName; }
	public Object data() { return mData; }
	public boolean hasFileName() { return !mFileName.isEmpty(); }
	
	//public void setContext(Context context) { mContext = context; }
	public void setFileName(String fileName) { mFileName = fileName; }
	public void setData(Object data) { mData = data; }
	// -----------------------------------------------------------------------------------------------
	
	protected String path(FileLocation location) {
		if(location == null) {
			return "";
		}
		
		return infoLocation(location).getAbsolutePath();
	}
	
	/**
	 * Returns information about files in location
	 * @param location the location
	 * @return the file containing the location information
	 */
	private File infoLocation(FileLocation location) {
		switch(location) {
			case INTERNAL: return mContext.getFilesDir();
			case EXTERNAL: return mContext.getExternalFilesDir(null);
			case EXTERNAL_SD_CARD: {
				if(hasSDCard()) {
					return Environment.getExternalStorageDirectory();
				} else {
					return mContext.getExternalFilesDir(null);
				}
			}
			case INTERNAL_DATABASE: {
				if(!mFileName.isEmpty()) {
					return mContext.getDatabasePath(mFileName);
				} else {
					return mContext.getFilesDir();
				}
			}
			case CACHE: return mContext.getCacheDir();
			default: return mContext.getFilesDir();
		}
	}
	
	public String[] files(FileLocation location) {
		String[] files = infoLocation(location).list();
		
		if(files == null) {
			return new String[] {};
		}
		
		return files;
	}
	
//	public String pathFromURI(Uri contentUri) {
//		if(contentUri == null) {
//			return "";
//		}
//		
//		Cursor cursor = null;
//  
//		try { 
//			String[] proj = { MediaStore.Images.Media.DATA };
//			cursor = mContext.getContentResolver().query(contentUri,  proj, null, null, null);
//			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			cursor.moveToFirst();
//			return cursor.getString(column_index);
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//	}

	
//	public String pathUri(Uri contentUri) { return pathFromURI(contentUri); }
	

	
	protected File file(FileLocation location, String directory) {
		return new File(path(location) + directory);
	}
	
	
//	public File FileFromUri(Uri contentUri) {
//		return new File(pathFromURI(contentUri), mFileName);
//	}
	
	
	
	private boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	
	
	protected boolean doesFileExist(File file) {
		return file.exists();
	}
	
	protected boolean delete(File file) {
		return file.delete();
	}
	
	// Constructors ----------------------------------------------------------------------------------
	public AbstractFileHandle(Context context) {
		mContext = context;
	}
	
	protected AbstractFileHandle(Builder build) {
		mContext 	= build.mContext;
		mFileName 	= build.mFileName;
		mData 		= build.mData;
	}
	// ------------------------------------------------------------------------------------------------
	
	// Builder ----------------------------------------------------------------------------------------
	public abstract static class Builder {
		
		private Context mContext;
		private String mFileName;
		private Object mData;
		
		public Builder(Context context) {
			mContext = context;
		}
		
		public abstract Builder location(FileLocation location);
		public abstract AbstractFileHandle build();
		
		public Builder fileName(String fileName) { mFileName = fileName; return this; }
		public Builder data(Object data) { mData = data; return this; }
	}
}
