package com.hydeudacityproject.alexandria.Service.Framework;

import android.database.Cursor;
import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import com.hydeudacityproject.alexandria.Service.ContentProvider.BookContract;
import com.lonewolfgames.framework.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jhyde on 12/21/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class Book implements Parcelable {
    private long mId = -1;
    private String mISBN = "";
    private String mTitle = "";
    private String mSubtitle = "";
    private String mPublisher = "";
    private String mPublishedDate = "";
    private String mDescription = "";
    private String mImageURL = "";
    private ArrayList<String> mAuthors = new ArrayList<>();
    private ArrayList<String> mCategories = new ArrayList<>();

    public long id() { return mId; }
    public String isbn() { return mISBN; }
    public String title() { return mTitle; }
    public String subtitle() { return mSubtitle; }
    public String publisher() { return mPublisher; }
    public String publishedDate() { return mPublishedDate; }
    public String description() { return mDescription; }
    public String imageURL() { return mImageURL; }

    public ArrayList<String> authors() { return mAuthors; }
    public ArrayList<String> genres() { return mCategories; }

    public String authorsString() {
        StringBuilder sb = new StringBuilder();

        for(int index = 0; index < mAuthors.size(); index++) {
            String author = mAuthors.get(index);
            if (index < mAuthors.size() - 1) {
                sb.append(author + ", ");
            } else {
                sb.append(author);
            }

        }

        return sb.toString();
    }

    public boolean hasAuthor(String constraint) {
        for(String author : mAuthors) {
            if(author.toLowerCase().contains(constraint.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public String genresString() {
        StringBuilder sb = new StringBuilder();

        for(int index = 0; index < mCategories.size(); index++) {
            String category = mCategories.get(index);
            if (index < mCategories.size() - 1) {
                sb.append(category + " | ");
            } else {
                sb.append(category);
            }

        }

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mISBN);
        dest.writeString(mTitle);
        dest.writeString(mSubtitle);
        dest.writeString(mPublisher);
        dest.writeString(mPublishedDate);
        dest.writeString(mDescription);
        dest.writeString(mImageURL);
        dest.writeList(mAuthors);
        dest.writeList(mCategories);
    }

    protected Book(Parcel in) {
        mId = in.readLong();
        mISBN = in.readString();
        mTitle = in.readString();
        mSubtitle = in.readString();
        mPublisher = in.readString();
        mPublishedDate = in.readString();
        mDescription = in.readString();
        mImageURL = in.readString();
        mAuthors = in.readArrayList(ArrayList.class.getClassLoader());
        mCategories = in.readArrayList(ArrayList.class.getClassLoader());
    }

    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {

        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };


    private Book(Builder build) {
        mId = build.mId;
        mISBN = build.mISBN;
        mTitle = build.mTitle;
        mSubtitle = build.mSubtitle;
        mPublisher = build.mPublisher;
        mPublishedDate = build.mPublishedDate;
        mDescription = build.mDescription;
        mImageURL = build.mImageURL;
        mAuthors = build.mAuthors;
        mCategories = build.mCategories;
    }

    public static class Builder {
        private long mId = -1;
        private String mISBN = "";
        private String mTitle = "";
        private String mSubtitle = "";
        private String mPublisher = "";
        private String mPublishedDate = "";
        private String mDescription = "";
        private String mImageURL = "";
        private ArrayList<String> mAuthors = new ArrayList<>();
        private ArrayList<String> mCategories = new ArrayList<>();

        public Builder() {

        }

        public Builder fromJSON(String isbn, String content) {
            mId = Utilities.parseLong(isbn);

            mISBN = isbn;

            try {
                JSONObject json_object = new JSONObject(content);

                JSONObject volume_info = json_object.getJSONObject("volumeInfo");

                if(volume_info.has("title")) {
                    mTitle = volume_info.getString("title");
                }

                if(volume_info.has("subtitle")) {
                    mSubtitle = volume_info.getString("subtitle");
                }

                if(volume_info.has("publisher")) {
                    mPublisher = volume_info.getString("publisher");
                }

                if(volume_info.has("publishedDate")) {
                    mPublishedDate = volume_info.getString("publishedDate");
                }

                if(volume_info.has("description")){
                    mDescription = volume_info.getString("description");
                }

                if(volume_info.has("imageLinks") && volume_info.getJSONObject("imageLinks").has("thumbnail")) {
                    mImageURL = volume_info.getJSONObject("imageLinks").getString("thumbnail");
                }

                if(volume_info.has("authors")) {
                    JSONArray authors_array = volume_info.getJSONArray("authors");
                    for (int index = 0; index < authors_array.length(); index++) {
                        mAuthors.add(authors_array.getString(index));
                    }
                }

                if(volume_info.has("categories")){
                    JSONArray categories_array = volume_info.getJSONArray("categories");
                    for (int index = 0; index < categories_array.length(); index++) {
                        mCategories.add(categories_array.getString(index));
                    }
                }

//                if(volume_info.has("industryIdentifiers")) {
//                    JSONArray indentifiers_array = volume_info.getJSONArray("industryIdentifiers");
//                    for(int index = 0; index < indentifiers_array.length(); index++) {
//
//                    }
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return this;
        }

        public Builder fromCursor(Cursor cursor) {
            mId = cursor.getLong(cursor.getColumnIndex(BookContract.BookEntry._ID));
            mISBN = String.valueOf(cursor.getLong(cursor.getColumnIndex(BookContract.BookEntry.ISBN)));
            mTitle = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.TITLE));
            mSubtitle = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.SUBTITLE));
            mPublisher = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.PUBLISHER));
            mPublishedDate = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.PUBLISHED_DATE));
            mImageURL = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.IMAGE_URL));
            mDescription = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.DESCRIPTION));

            String authors = cursor.getString(cursor.getColumnIndex(BookContract.AuthorEntry.AUTHOR));
            if(authors != null && !authors.isEmpty()) {
                String[] authors_array = authors.split(",");
                for(String author : authors_array) {
                    mAuthors.add(author);
                }
            }

            String categories = cursor.getString(cursor.getColumnIndex(BookContract.CategoryEntry.CATEGORY));
            if(categories != null && !categories.isEmpty()) {
                String[] categories_array = categories.split(",");
                for(String category : categories_array) {
                    mCategories.add(category);
                }
            }

            return this;
        }


        public Book build() {
            return new Book(this);
        }
    }
}
