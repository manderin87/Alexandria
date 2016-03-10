package com.lonewolfgames.framework.Cache.Images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.Cache.Files.FileCache;
import com.lonewolfgames.framework.Cache.Files.FileReadHandle;
import com.lonewolfgames.framework.Cache.Files.FileWriteHandle;
import com.lonewolfgames.framework.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jhyde on 11/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

    private Context mContext;
    private ImageView mImageView;
    private String mUrl;
    private String mFilename;
    private boolean mSaveToDisk = false;
    private ImageLoaderListener mListener;
    private int mDefaultResourceId = -1;

    private AbstractAppService.ServiceError mErrorCode;

//    public ImageLoader(ImageView imageView, String url) {
//        mContext = imageView.getContext();
//        mImageView = imageView;
//        //mImageView.setImageResource(android.R.drawable.ic_menu_gallery);
//        mUrl = url;
//    }
//
//    public ImageLoader(ImageView imageView, String url, boolean saveToDisk) {
//        this(imageView, url);
//
//        mSaveToDisk = saveToDisk;
//    }
//
//    public ImageLoader(ImageLoaderListener listener, ImageView imageView, String url) {
//        mContext = (Context) listener;
//        mImageView = imageView;
//        mUrl = url;
//
//        mListener = listener;
//    }
//
//    public ImageLoader(Context context, ImageView imageView, String url) {
//        mContext = context;
//        mImageView = imageView;
//        mUrl = url;
//    }
//
//    public ImageLoader(Context context, String url) {
//        mContext = context;
//        mUrl = url;
//    }

//    public void setImageDefault() {
//        mImageView.setImageResource(android.R.drawable.ic_menu_gallery);
//    }
//    public void setListener(ImageLoaderListener listener) {
//        mListener = listener;
//    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap;

        if(mUrl.contains("null") || mUrl.isEmpty()) {
            return null;
        }

        // See if its in the cache
        bitmap = ImageCache.instance().bitmap(mFilename);

        if(bitmap != null) {
            mErrorCode = AbstractAppService.ServiceError.Success;
            return bitmap;
        }

        // See if its saved to disk
        if(mSaveToDisk) {
            FileReadHandle read_handle = FileCache.instance().readHandle();
            read_handle.setType(FileReadHandle.FileReadType.BITMAP);
            read_handle.setFileName(mFilename);

            if(read_handle.doesFileExist()) {
                try {
                    bitmap = (Bitmap) read_handle.read();
                } catch(Exception e) {
                    bitmap = null;
                }

                if(bitmap != null) {
                    mErrorCode = AbstractAppService.ServiceError.Success;
                    return bitmap;
                }
            }
        }

        if(Utilities.isNetworkAvailable(mContext)) {
            URL url;
            InputStream input_stream = null;

            try {
                url = new URL(mUrl);
                URLConnection connection = url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input_stream = connection.getInputStream();

                bitmap = BitmapFactory.decodeStream(input_stream);

                // Save to cache
                ImageCache.instance().addToCache(mFilename, bitmap);

                // Write to disk
                if(mSaveToDisk) {
                    FileWriteHandle write_handle = FileCache.instance().writeHandle();
                    write_handle.setFileName(mFilename);
                    write_handle.setData(bitmap, FileWriteHandle.FileWriteType.IMAGE);
                    write_handle.write();
                }

                mErrorCode = AbstractAppService.ServiceError.Success;

                return bitmap;
            } catch(Exception e) {
                e.printStackTrace();

            } finally {
                if(input_stream != null) {
                    try {
                        input_stream.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            mErrorCode = AbstractAppService.ServiceError.NetworkConnectionError;
        }


        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(mImageView != null && bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else if(mImageView != null && bitmap == null) {
            if(mDefaultResourceId != -1) {
                mImageView.setImageResource(mDefaultResourceId);
            } else {
                mImageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        if(mListener != null) {
            if(mErrorCode == AbstractAppService.ServiceError.Success) {
                mListener.OnImageLoaded(bitmap);
            } else {
                mListener.OnImageFailed(mErrorCode);
            }
        }
    }

    public interface ImageLoaderListener {
        void OnImageLoaded(Bitmap bitmap);
        void OnImageFailed(AbstractAppService.ServiceError error);
    }

    private ImageLoader(Builder build) {
        mContext = build.mContext;
        mImageView = build.mImageView;
        mUrl = build.mUrl;
        mSaveToDisk = build.mSaveToDisk;
        mListener = build.mListener;
        mFilename = build.mFilename;
        mDefaultResourceId = build.mDefaultResourceId;
    }

    public static class Builder {
        private Context mContext;
        private ImageView mImageView;
        private String mUrl;
        private String mFilename;
        private boolean mSaveToDisk = false;
        private ImageLoaderListener mListener;
        private int mDefaultResourceId = -1;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder imageView(ImageView imageView) {
            mImageView = imageView;
            return this;
        }

        public Builder url(String url) {
            mUrl = url;
            return this;
        }

        public Builder filename(String filename) {
            saveToDisk(true);
            mFilename = filename;
            return this;
        }

        public Builder saveToDisk(boolean save) {
            mSaveToDisk = save;
            return this;
        }

        public Builder listener(ImageLoaderListener listener) {
            mListener = listener;
            return this;
        }

        public Builder defaultResourceId(int resourceId) {
            mDefaultResourceId = resourceId;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }
    }
}
