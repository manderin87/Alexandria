package com.lonewolfgames.framework.Cache.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.content.Context;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class FileReadHandle extends AbstractFileHandle {

	private FileReadType		mType			= FileReadType.DATA;
	private FileLocation 		mLocation		= FileLocation.INTERNAL;
	private String				mDirectory		= "";
	protected long				mExpireTime		= 0;
	
	public enum FileReadType {
		DATA,
		TEXT_ARRAY,
		TEXT_STRING,
		BITMAP;
	}
	
	// Methods --------------------------------------------------------------------------------------
	public FileReadType type() { return mType; }
	
	/**
	 * Return the location the data will be read
	 * @return the location the data will be read
	 */
	public FileLocation location() { return mLocation; }
	public String directory() { return mDirectory; }
	
	public String path() { 
		StringBuilder sb = new StringBuilder();
		
		sb.append(path(mLocation));
		sb.append(mDirectory);
		sb.append(File.separator);
		
		return sb.toString(); 
	}
	
	public long expireTime() { return mExpireTime; }
	public void setType(FileReadType type) { mType = type; }
	public void setLocation(FileLocation location) { mLocation = location; }
	public void setDirectory(String directory) { mDirectory = directory; }
	
	public void setData(Object data, FileReadType type) { 
		setData(data);
		
		mType = type;
	}
	
	public void setExpireTime(long time) { mExpireTime = time; }
	public void setExpireTime(int days, int hours) {
		mExpireTime += days * FileCache.DAY;
		mExpireTime += hours * FileCache.HOUR;
	}
	// ----------------------------------------------------------------------------------------------
	
	
	public boolean valid() {
		return !mFileName.isEmpty() && mLocation != FileLocation.NONE;
	}
	
	public boolean validExpire() {
		return valid() && mExpireTime != 0;
	}
	
	private File file(String fileName) {
		return new File(path(), fileName);
	}

	/**
	 * Returns a file where the data will be read
	 * @return the file
	 */
	public File file() { 
		return !mFileName.isEmpty() ? file(mFileName) : file(mLocation, mDirectory); 
	}
	
	public boolean doesFileExist() {
		if(mFileName.isEmpty()) {
			return false;
		}
		
		return doesFileExist(file());
	}
	
	public boolean delete() {
		if(mLocation != FileLocation.NONE && doesFileExist()) {
			return super.delete(file());
		}
		
		return false;
	}
	
	public boolean isExpired() {
		if(validExpire()) {
			return (Calendar.getInstance().getTimeInMillis() - file().lastModified() > expireTime()) ? true : false;
		} else {
			return false;
		}	
	}
	
	public FileInputStream inputStream() throws FileNotFoundException {
		return new FileInputStream(file());
	}
	
	public InputStreamReader inputStreamReader() throws FileNotFoundException {
		return new InputStreamReader(inputStream());
	}
	
	/**
	 * Reads the data and returns
	 * @return the data
	 * @throws Exception
	 */
	public Object read() throws Exception {
		switch(mType) {
			case DATA: return FileCache.readFile(this);
			case TEXT_ARRAY: return FileCache.readTextToArray(this);
			case TEXT_STRING: return FileCache.readTextToString(this);
			case BITMAP: return FileCache.readImage(this);
			default: return FileCache.readFile(this);
		}
	}
	
	// Constructors ----------------------------------------------------------------------------------
	
	protected FileReadHandle(Builder build) {
		super(build);
		
		mType 		= build.mType;
		mLocation 	= build.mLocation;
		mDirectory 	= build.mDirectory;
	}
	// ------------------------------------------------------------------------------------------------
	
	// Builder ----------------------------------------------------------------------------------------
	public static class Builder extends AbstractFileHandle.Builder {
		
		private FileReadType		mType			= FileReadType.DATA;
		private FileLocation 		mLocation		= FileLocation.INTERNAL;
		private String				mDirectory		= "";
		
		public Builder(Context context) {
			super(context);
		}
		
		public Builder type(FileReadType type) { mType = type; return this; }
		public Builder directory(String directory) { mDirectory = directory; return this; }

		@Override
		public Builder location(FileLocation location) { mLocation = location; return this; }
		
		@Override
		public FileReadHandle build() {
			return new FileReadHandle(this);
		}
	}
}
