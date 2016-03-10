package com.hydeudacityproject.alexandria.Service.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.ParcelFormatException;

import java.util.ArrayList;

import com.hydeudacityproject.alexandria.Service.Framework.Book;
import com.lonewolfgames.framework.Utilities;

/**
 * Created by jhyde on 12/21/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class BookDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "alexandria.db";
    private static final int DATABASE_VERSION = 1;

    private Context mContext;
    private ContentResolver mContentResolver;

    private ArrayList<LibraryChangedListener> mListeners;

    public BookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("+
                BookContract.BookEntry._ID + " INTEGER PRIMARY KEY," +
                BookContract.BookEntry.ISBN + " TEXT NOT NULL," +
                BookContract.BookEntry.TITLE + " TEXT NOT NULL," +
                BookContract.BookEntry.SUBTITLE + " TEXT ," +
                BookContract.BookEntry.PUBLISHER + " TEXT, " +
                BookContract.BookEntry.PUBLISHED_DATE + " TEXT, " +
                BookContract.BookEntry.DESCRIPTION + " TEXT ," +
                BookContract.BookEntry.IMAGE_URL + " TEXT, " +
                "UNIQUE ("+ BookContract.BookEntry._ID +") ON CONFLICT IGNORE)";

        final String SQL_CREATE_AUTHOR_TABLE = "CREATE TABLE " + BookContract.AuthorEntry.TABLE_NAME + " ("+
                BookContract.AuthorEntry._ID + " INTEGER," +
                BookContract.AuthorEntry.ISBN + " TEXT NOT NULL," +
                BookContract.AuthorEntry.AUTHOR + " TEXT," +
                " FOREIGN KEY (" + BookContract.AuthorEntry._ID + ") REFERENCES " +
                BookContract.BookEntry.TABLE_NAME + " (" + BookContract.BookEntry._ID + "))";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + BookContract.CategoryEntry.TABLE_NAME + " ("+
                BookContract.CategoryEntry._ID + " INTEGER," +
                BookContract.CategoryEntry.ISBN + " TEXT NOT NULL," +
                BookContract.CategoryEntry.CATEGORY + " TEXT," +
                " FOREIGN KEY (" + BookContract.CategoryEntry._ID + ") REFERENCES " +
                BookContract.BookEntry.TABLE_NAME + " (" + BookContract.BookEntry._ID + "))";

        db.execSQL(SQL_CREATE_BOOK_TABLE);
        db.execSQL(SQL_CREATE_AUTHOR_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addListener(LibraryChangedListener listener) {
        if(mListeners == null) {
            mListeners = new ArrayList<>();
        }

        if(!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeListener(LibraryChangedListener listener) {
        if(mListeners == null) {
            return;
        }

        if(mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public void notifyListeners() {
        ArrayList<Book> library = getLibrary();
        for(LibraryChangedListener listener : mListeners) {
            listener.OnLibraryChanged(library);
        }
    }



    public ArrayList<Book> getLibrary() {
        ArrayList<Book> books = new ArrayList<>();

        Cursor cursor = mContentResolver.query(
                BookContract.BookEntry.FULL_CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        if(cursor != null && cursor.moveToFirst()) {
            do {
                books.add(new Book.Builder().fromCursor(cursor).build());
            } while(cursor.moveToNext());

            if(!cursor.isClosed()) {
                cursor.close();
            }
        }

        return books;
    }

    public Book book(long id) {
        Book book = null;
        Cursor cursor = mContentResolver.query(
                BookContract.BookEntry.buildFullBookUri(id),
                null,
                null,
                null,
                null
        );

        if(cursor != null && cursor.moveToFirst()) {
            do {
                book = new Book.Builder().fromCursor(cursor).build();
            } while(cursor.moveToNext());

            if(!cursor.isClosed()) {
                cursor.close();
            }
        }

        return book;
    }

    public void addBook(Book book) {
        ContentValues values = new ContentValues();

        values.put(BookContract.BookEntry._ID, book.id());
        values.put(BookContract.BookEntry.ISBN, book.isbn());
        values.put(BookContract.BookEntry.TITLE, book.title());
        values.put(BookContract.BookEntry.SUBTITLE, book.subtitle());
        values.put(BookContract.BookEntry.PUBLISHER, book.publisher());
        values.put(BookContract.BookEntry.PUBLISHED_DATE, book.publishedDate());
        values.put(BookContract.BookEntry.DESCRIPTION, book.description());
        values.put(BookContract.BookEntry.IMAGE_URL, book.imageURL());

        mContentResolver.insert(BookContract.BookEntry.CONTENT_URI, values);

        addAuthors(book);
        addCategories(book);

        notifyListeners();
        Utilities.displayToastShort(mContext, book.title() + " added to library...");
    }

    private void addAuthors(Book book) {
        ArrayList<String> authors = book.authors();

        for(String author : authors) {
            ContentValues values = new ContentValues();
            values.put(BookContract.AuthorEntry._ID, book.id());
            values.put(BookContract.AuthorEntry.ISBN, book.isbn());
            values.put(BookContract.AuthorEntry.AUTHOR, author);
            mContentResolver.insert(BookContract.AuthorEntry.CONTENT_URI, values);
        }
    }

    private void addCategories(Book book) {
        ArrayList<String> categories = book.genres();

        for(String category : categories) {
            ContentValues values = new ContentValues();
            values.put(BookContract.CategoryEntry._ID, book.id());
            values.put(BookContract.CategoryEntry.ISBN, book.isbn());
            values.put(BookContract.CategoryEntry.CATEGORY, category);
            mContentResolver.insert(BookContract.CategoryEntry.CONTENT_URI, values);
        }
    }

    public void deleteBook(long id) {
        if(id > 0) {
            mContentResolver.delete(BookContract.BookEntry.buildBookUri(id), null, null);
            notifyListeners();
        }
    }

    public void deleteBook(Book book) {
        deleteBook(book.id());
    }

    public boolean hasBook(long id) {
        Cursor book_entry = null;

        try {
            book_entry = mContentResolver.query(
                    BookContract.BookEntry.buildBookUri(id),
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null  // sort order
            );
        } catch(ParcelFormatException ex) {
            return false;
        }

        if(book_entry.getCount() > 0) {
            book_entry.close();
            return true;
        }

        if(book_entry != null) {
            book_entry.close();
        }
        return false;
    }

    public static interface LibraryChangedListener {
        public void OnLibraryChanged(ArrayList<Book> books);
    }
}
