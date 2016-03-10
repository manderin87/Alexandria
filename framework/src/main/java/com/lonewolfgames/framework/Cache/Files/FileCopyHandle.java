package com.lonewolfgames.framework.Cache.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class FileCopyHandle extends AbstractFileHandle {

	private FileWriteHandle mWriteHandle;
	private FileReadHandle mReadHandle;
	
	public boolean validCopyFile() {
		return !mFileName.isEmpty() && 
				mWriteHandle.location() != FileLocation.NONE && 
				mReadHandle.location() != FileLocation.NONE;
	}

	public FileCopyHandle(Context context) {
		super(context);
		
	}
	
	public File readFile() { return mReadHandle.file(); }
	
	public String path() { return mWriteHandle.path(); }
	
	public FileOutputStream outputStream(boolean makeDirectory) throws IOException, FileNotFoundException {
		return mWriteHandle.outputStream(makeDirectory);
	}
}
