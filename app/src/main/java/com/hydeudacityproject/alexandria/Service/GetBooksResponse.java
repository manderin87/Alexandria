package com.hydeudacityproject.alexandria.Service;

import android.graphics.Movie;

import com.hydeudacityproject.alexandria.Service.Framework.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 12/21/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class GetBooksResponse {

    private ArrayList<Book> mItems;

    public ArrayList<Book> items() { return mItems; }

    private GetBooksResponse(Builder build) {
        mItems = build.mItems;
    }

    public static class Builder {
        private ArrayList<Book> mItems;

        public Builder() {
            mItems = new ArrayList<>();
        }

        public Builder fromJSON(String isbn, String content) {
            try {
                JSONObject json_object = new JSONObject(content);

                if(json_object.has("items")) {
                    JSONArray results_array = json_object.getJSONArray("items");

                    for(int index = 0; index < results_array.length(); index++) {
                        mItems.add(new Book.Builder().fromJSON(isbn, results_array.getString(index)).build());
                    }
                }

            } catch(Exception e) {

            }

            return this;
        }

        public GetBooksResponse build() {
            return new GetBooksResponse(this);
        }
    }

}
