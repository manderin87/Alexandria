package com.lonewolfgames.framework.Cache.Accessors;

import android.content.Context;

import com.lonewolfgames.framework.Cache.Files.AbstractFileHandle;
import com.lonewolfgames.framework.Cache.Files.FileCache;
import com.lonewolfgames.framework.Cache.Files.FileReadHandle;
import com.lonewolfgames.framework.Cache.Files.FileWriteHandle;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public abstract class AbstractCacheAccessor {

    protected static FileReadHandle mReadHandle;
    protected static FileWriteHandle mWriteHandle;

    protected static FileReadHandle getInternalReadHandle(Context context, String filename) {
        if(mReadHandle == null) {
            mReadHandle = FileCache.instance().readHandle();

            if(mReadHandle == null) {
                mReadHandle = new FileReadHandle.Builder(context)
                        .location(AbstractFileHandle.FileLocation.INTERNAL)
                        .build();
            }

            mReadHandle.setLocation(AbstractFileHandle.FileLocation.INTERNAL);
        }

        mReadHandle.setFileName(filename);
        
        return mReadHandle;
    }

    protected static FileWriteHandle getInternalWriteHandle(Context context, String filename) {
        if(mWriteHandle == null) {
            mWriteHandle = FileCache.instance().writeHandle();

            if(mWriteHandle == null) {
                mWriteHandle = new FileWriteHandle.Builder(context)
                        .location(AbstractFileHandle.FileLocation.INTERNAL)
                        .build();
            }

            mWriteHandle.setLocation(AbstractFileHandle.FileLocation.INTERNAL);
        }

        mWriteHandle.setFileName(filename);

        return mWriteHandle;
    }
}
