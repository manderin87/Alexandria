package com.lonewolfgames.framework.Cache.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class FileWriteHandle extends AbstractFileHandle {

	private FileWriteType			mType			= FileWriteType.DATA;
	private FileLocation			mLocation		= FileLocation.INTERNAL;
	private String					mDirectory		= "";
	
	public enum FileWriteType {
		DATA,
		DATA_APPEND,
		TEXT,
		TEXT_APPEND,
		PREFERENCE,
		IMAGE,
		JSON;
	}
	
	// Methods --------------------------------------------------------------------------------------
	public FileWriteType type() { return mType; }
	
	/**
	 * Return the location the data will be written
	 * @return the location the data will be written
	 */
	public FileLocation location() { return mLocation; }
	
	public String directory() { return mDirectory; }
	
	public String path() { return path(mLocation) + mDirectory; }
	
	public void setType(FileWriteType type) { mType = type; }
	public void setLocation(FileLocation location) { mLocation = location; } 
	public void setDirectory(String directory) { mDirectory = directory; }
	
	public void setData(Object data, FileWriteType type) { 
		setData(data);
		
		mType = type;
	}
	// ----------------------------------------------------------------------------------------------
	
	public void makeDirectory() throws IOException {
		if(!path().isEmpty()) {
			File destinationDirectory = new File(path());
			
			if (destinationDirectory.exists() && !destinationDirectory.isDirectory()) {
                throw new IOException("Can't create directory, a file is in the way");
			}
		
			if(!destinationDirectory.exists()) {
				destinationDirectory.mkdirs();
				
				if (!destinationDirectory.isDirectory()) {
                    throw new IOException("Unable to create directory");
				}
			}
		}
	}
	
	public boolean valid() {
		return !mFileName.isEmpty() && mLocation != FileLocation.NONE;
	}
	
	private File file(String fileName) {
		return new File(path(), fileName);
	}
	
	/**
	 * Returns a file where the data will be written
	 * @return the file
	 */
	public File file() { 
		return !mFileName.isEmpty() ? file(mFileName) : file(mLocation, mDirectory); 
	}
	
	public boolean doesFileExist() {
		return doesFileExist(file());
	}
	

	
	public boolean delete() {
		if(mLocation != FileLocation.NONE && doesFileExist()) {
			return super.delete(file());
		}
		
		return false;
	}
	
	public FileOutputStream outputStream(boolean makeDirectory) throws IOException, FileNotFoundException {
		if(makeDirectory) {
			makeDirectory();
		}
		
		return outputStream();
	}
	
	public FileOutputStream outputStream() throws FileNotFoundException {
		return new FileOutputStream(file(), false);
	}
	
	public FileOutputStream appendOutputStream(boolean makeDirectory) throws IOException, FileNotFoundException {
		if(makeDirectory) {
			makeDirectory();
		}
		
		return appendOutputStream();
	}
	
	public FileOutputStream appendOutputStream() throws FileNotFoundException {
		return new FileOutputStream(file(), true);
	}
	
	public OutputStreamWriter outputStreamWriter() throws FileNotFoundException {
		return new OutputStreamWriter(outputStream());
	}
	
	/**
	 * Write the data of the file handle to the cache
	 * @throws Exception
	 */
	public void write() throws Exception {
		switch(mType) {
			case DATA: FileCache.writeFile(this); break;
			case TEXT: FileCache.writeText(this); break;
//			case JSON: Cache.writeJSON(this); break;
			case DATA_APPEND: FileCache.appendFile(this); break;
			case TEXT_APPEND: FileCache.appendText(this); break;
			case IMAGE: FileCache.writeImage(this); break;
			default: FileCache.writeFile(this); break;
		}
	}
	
	public void writeStream(FileOutputStream stream, Object data) throws Exception {
		FileCache.writeTextStream(stream, data);
	}
	
	
//	/**
//	 * Write the data to the cache
//	 * @param data the data to be cached
//	 * @throws Exception
//	 */
//	public void write(Object data) throws Exception {
//		mData = data;
//
//		switch(mType) {
//			case DATA: FileCache.writeFile(this); break;
//			case TEXT: FileCache.writeText(this); break;
////			case JSON: Cache.writeJSON(this); break;
//			case DATA_APPEND: FileCache.appendFile(this); break;
//			case TEXT_APPEND: FileCache.appendText(this); break;
//			default: FileCache.writeFile(this); break;
//		}
//	}
	
	
	// Constructors ----------------------------------------------------------------------------------
//	protected FileWriteHandle(Context context) { super(context); }
	
	protected FileWriteHandle(Builder build) {
		super(build);
		
		mType 		= build.mType;
		mLocation 	= build.mLocation;
		mDirectory 	= build.mDirectory;
	}
	// ------------------------------------------------------------------------------------------------
	
	// Builder ----------------------------------------------------------------------------------------
	public static class Builder extends AbstractFileHandle.Builder {
		
		private FileWriteType	mType			= FileWriteType.DATA;
		private FileLocation	mLocation		= FileLocation.INTERNAL;
		private String			mDirectory		= "";
		
		public Builder(Context context) {
			super(context);
		}
		
		public Builder type(FileWriteType type) { mType = type; return this; }
		public Builder directory(String directory) { mDirectory = directory; return this; }

		@Override
		public Builder location(FileLocation location) { mLocation = location; return this; }

		@Override
		public FileWriteHandle build() {
			return new FileWriteHandle(this);
		}
	}
}
