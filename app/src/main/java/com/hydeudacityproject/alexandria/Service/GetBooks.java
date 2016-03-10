package com.hydeudacityproject.alexandria.Service;

import android.app.IntentService;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.hydeudacityproject.alexandria.AppData;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.Utilities;


/**
 * Created by jhyde on 12/21/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class GetBooks extends AsyncTask<Void, Void, GetBooksResponse> {

    private static final String TAG = GetBooks.class.getSimpleName();

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q=";
    private static final String QUERY_ISBN_PARAM = "isbn:";
    private static final String QUERY_AUTHOR_PARAM = "inauthor:";
    private static final String QUERY_TITLE_PARAM = "intitle:";

    private String mUrl = BASE_URL + QUERY_PARAM;

    private Context mContext;
    private GetBooksListener mListener;
    private AbstractAppService.ServiceError mErrorCode;
    private String mISBN = "";

    public GetBooks(Context context, GetBooksListener listener, String isbn) {
        mContext = context;
        mListener = listener;
        mISBN = isbn;

        mUrl += QUERY_ISBN_PARAM + mISBN;
    }


    @Override
    protected GetBooksResponse doInBackground(Void... params) {

        if(AppData.instance().database().hasBook(Utilities.parseLong(mISBN))) {
            return null;
        }


        if(Utilities.isNetworkAvailable(mContext)) {
            StringBuilder content = new StringBuilder();

            URL url;
            BufferedReader buffered_reader = null;

            try {
                url = new URL(mUrl);

                Log.i(TAG, "Retrieving Book: " + url);

                URLConnection connection = url.openConnection();

                buffered_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String read_line;

                while((read_line = buffered_reader.readLine()) != null) {
                    content.append(read_line);
                }

                mErrorCode = AbstractAppService.ServiceError.Success;

            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(buffered_reader != null) {
                    try {
                        buffered_reader.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return new GetBooksResponse.Builder().fromJSON(mISBN, content.toString()).build();
        } else {
            mErrorCode = AbstractAppService.ServiceError.NetworkConnectionError;
        }

        return null;
    }

    @Override
    protected void onPostExecute(GetBooksResponse result) {
        super.onPostExecute(result);

        if(mListener != null) {
            if(mErrorCode == AbstractAppService.ServiceError.Success) {
                mListener.OnGetBooksFinished(result);
            } else {
                mListener.OnGetBooksError(mErrorCode, mISBN);
            }
        }
    }


    public interface GetBooksListener {
        void OnGetBooksFinished(GetBooksResponse response);
        void OnGetBooksError(AbstractAppService.ServiceError error, String isbn);
    }



}