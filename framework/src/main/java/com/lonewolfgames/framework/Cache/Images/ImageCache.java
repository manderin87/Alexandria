package com.lonewolfgames.framework.Cache.Images;

import java.io.FileInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class ImageCache {

	private Context mContext;
	private BitmapCache mBitmapCache;
	private static ImageCache mInstance;
	
	public ImageCache(Context context) {
		mContext = context;
		mBitmapCache = new BitmapCache();
		mInstance = this;
	}

	public static ImageCache instance() { return mInstance; }
	
	/**
	 * Clears the cache
	 */
	public void clear() {
		mBitmapCache.clear();
	}


	/**
	 * Loads a bitmap from a file if the key is not in the cache
	 * @param key the key of the bitmap
	 * @param filepath the filepath of the bitmap
	 * @return the bitmap
	 */
	public Bitmap loadBitmapFromFile(String key, String filepath) {
		Bitmap bitmap = mBitmapCache.getBitmap(key);
		
		if(bitmap != null) {
			return bitmap;
		}
		
		// Bitmap is not in cache so load it from the file
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        FileInputStream input_stream = null;
		try {			
			input_stream = new FileInputStream(filepath);
			
			bitmap = BitmapFactory.decodeStream(input_stream, null, options);
			
			// Add the bitmap to the cache after the bitmap is loaded
			if(bitmap != null) {
				addToCache(key, bitmap);
			}

            input_stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public Bitmap loadBitmapFromResource(String key, int resource_id) {
		Bitmap bitmap = mBitmapCache.getBitmap(key);

		if(bitmap != null) {
			return bitmap;
		}

		// Bitmap is not in cache to load it from the file
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		try {
			bitmap = BitmapFactory.decodeResource(mContext.getResources(), resource_id);
			// Add the bitmap to the cache after the bitmap is loaded
			if(bitmap != null) {
				addToCache(key, bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}




	public Bitmap bitmap(String key) {
		return mBitmapCache.getBitmap(key);
	}
	
	public void addToCache(String key, Bitmap bitmap) {
		mBitmapCache.addToCache(key, bitmap);
	}


	private class BitmapCache {

		private LruCache<String, Bitmap> mCache;
		
		public BitmapCache() {
			init();
		}
		
		public void init() {
			final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
			
			final int cacheSize = maxMemory / 8;
			
			mCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getByteCount() / 1024;
				}
			};
		}
		
		public void addToCache(String key, Bitmap bitmap) {
			if(!key.isEmpty() && bitmap != null) {
				if(bitmap(key) == null) {
					mCache.put(key, bitmap);
				}
			}
		}
		
		private Bitmap bitmap(String key) {
			return mCache.get(key);
		}
		
		public Bitmap getBitmap(String id) {
			final Bitmap bitmap = bitmap(id);
			
			return bitmap;
		}
		
		public void clear() {
			init();
		}
	}
}
