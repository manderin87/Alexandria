package com.hydeudacityproject.alexandria.Service.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by jhyde on 12/1/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class BookContentProvider extends ContentProvider {

    private static final int BOOK_ID = 100;
    private static final int BOOK = 101;

    private static final int AUTHOR_ID = 200;
    private static final int AUTHOR = 201;

    private static final int CATEGORY_ID = 300;
    private static final int CATEGORY = 301;

    private static final int BOOK_FULL = 500;
    private static final int BOOK_FULLDETAIL = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private BookDBHelper mHelper;

    private static final SQLiteQueryBuilder bookFull;

    static {
        bookFull = new SQLiteQueryBuilder();
        bookFull.setTables(
                BookContract.BookEntry.TABLE_NAME + " LEFT OUTER JOIN " +
                BookContract.AuthorEntry.TABLE_NAME + " USING (" + BookContract.BookEntry._ID + ")" +
                " LEFT OUTER JOIN " +  BookContract.CategoryEntry.TABLE_NAME + " USING (" + BookContract.BookEntry._ID + ")");
    }


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BookContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BookContract.PATH_BOOKS+"/#", BOOK_ID);
        matcher.addURI(authority, BookContract.PATH_AUTHORS+"/#", AUTHOR_ID);
        matcher.addURI(authority, BookContract.PATH_CATEGORIES+"/#", CATEGORY_ID);

        matcher.addURI(authority, BookContract.PATH_BOOKS, BOOK);
        matcher.addURI(authority, BookContract.PATH_AUTHORS, AUTHOR);
        matcher.addURI(authority, BookContract.PATH_CATEGORIES, CATEGORY);

        matcher.addURI(authority, BookContract.PATH_FULLBOOK +"/#", BOOK_FULLDETAIL);
        matcher.addURI(authority, BookContract.PATH_FULLBOOK, BOOK_FULL);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new BookDBHelper(getContext());

        return (mHelper == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final Cursor cursor;
        final SQLiteDatabase db = mHelper.getReadableDatabase();

        switch(sUriMatcher.match(uri)) {
            case BOOK:
                cursor = db.query(
                        BookContract.BookEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection==null? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case AUTHOR:
                cursor = db.query(
                        BookContract.AuthorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY:
                cursor = db.query(
                        BookContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BOOK_ID:
                cursor = db.query(
                        BookContract.BookEntry.TABLE_NAME,
                        projection,
                        BookContract.BookEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case AUTHOR_ID:
                cursor = db.query(
                        BookContract.AuthorEntry.TABLE_NAME,
                        projection,
                        BookContract.AuthorEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY_ID:
                cursor = db.query(
                        BookContract.CategoryEntry.TABLE_NAME,
                        projection,
                        BookContract.CategoryEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BOOK_FULLDETAIL:
                String[] bfd_projection ={
                    BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.TITLE,
                    BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.SUBTITLE,
                    BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.IMAGE_URL,
                    BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.DESCRIPTION,
                    "group_concat(DISTINCT " + BookContract.AuthorEntry.TABLE_NAME+ "."+ BookContract.AuthorEntry.AUTHOR +") as " + BookContract.AuthorEntry.AUTHOR,
                    "group_concat(DISTINCT " + BookContract.CategoryEntry.TABLE_NAME+ "."+ BookContract.CategoryEntry.CATEGORY +") as " + BookContract.CategoryEntry.CATEGORY
                };
                cursor = bookFull.query(db,
                        bfd_projection,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry._ID,
                        null,
                        sortOrder);
                break;
            case BOOK_FULL:
                String[] bf_projection = {
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry._ID,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.ISBN,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.TITLE,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.SUBTITLE,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.PUBLISHER,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.PUBLISHED_DATE,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.IMAGE_URL,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry.DESCRIPTION,
                        "group_concat(DISTINCT " + BookContract.AuthorEntry.TABLE_NAME+ "."+ BookContract.AuthorEntry.AUTHOR + ") as " + BookContract.AuthorEntry.AUTHOR,
                        "group_concat(DISTINCT " + BookContract.CategoryEntry.TABLE_NAME+ "."+ BookContract.CategoryEntry.CATEGORY +") as " + BookContract.CategoryEntry.CATEGORY
                };
                cursor = bookFull.query(db,
                        bf_projection,
                        null,
                        selectionArgs,
                        BookContract.BookEntry.TABLE_NAME + "." + BookContract.BookEntry._ID,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }



    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case BOOK_FULLDETAIL:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            case AUTHOR_ID:
                return BookContract.AuthorEntry.CONTENT_ITEM_TYPE;
            case CATEGORY_ID:
                return BookContract.CategoryEntry.CONTENT_ITEM_TYPE;
            case BOOK:
                return BookContract.BookEntry.CONTENT_TYPE;
            case AUTHOR:
                return BookContract.AuthorEntry.CONTENT_TYPE;
            case CATEGORY:
                return BookContract.CategoryEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        Uri return_uri;

        switch (match) {
            case BOOK: {
                long _id = db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
                if ( _id > 0 ){
                    return_uri = BookContract.BookEntry.buildBookUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                getContext().getContentResolver().notifyChange(BookContract.BookEntry.buildFullBookUri(_id), null);
                break;
            }
            case AUTHOR:{
                long _id = db.insert(BookContract.AuthorEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    return_uri = BookContract.AuthorEntry.buildAuthorUri(values.getAsLong("_id"));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CATEGORY: {
                long _id = db.insert(BookContract.CategoryEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    return_uri = BookContract.CategoryEntry.buildCategoryUri(values.getAsLong("_id"));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return return_uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rows_deleted = 0;

        switch (match) {
            case BOOK:
                rows_deleted = db.delete(
                        BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case AUTHOR:
                rows_deleted = db.delete(
                        BookContract.AuthorEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY:
                rows_deleted = db.delete(
                        BookContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                rows_deleted = db.delete(
                        BookContract.BookEntry.TABLE_NAME,
                        BookContract.BookEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null deletes all rows
        if (selection == null || rows_deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows_deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int uri_type = sUriMatcher.match(uri);
        int rows_updated = 0;

        switch(uri_type) {
            case BOOK:
                rows_updated = db.update(BookContract.BookEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case AUTHOR:
                rows_updated = db.update(BookContract.AuthorEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CATEGORY:
                rows_updated = db.update(BookContract.CategoryEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows_updated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows_updated;
    }
}