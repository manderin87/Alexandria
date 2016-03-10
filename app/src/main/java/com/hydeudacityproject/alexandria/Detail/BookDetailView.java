package com.hydeudacityproject.alexandria.Detail;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hydeudacityproject.alexandria.R;
import com.hydeudacityproject.alexandria.Service.Framework.Book;

/**
 * Created by jhyde on 12/23/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class BookDetailView extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "fragment";
    public static final String KEY_BOOK = "book";

    private BookDetailViewFragment mBookDetailFragment;
    private Book mBook = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.book_detail_view);

        if(getIntent().hasExtra(KEY_BOOK)) {
            mBook = getIntent().getParcelableExtra(KEY_BOOK);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_BOOK)) {
            mBook = savedInstanceState.getParcelable(KEY_BOOK);
        }

        if(savedInstanceState == null) {
            mBookDetailFragment = BookDetailViewFragment.newInstance(mBook);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_library_view, mBookDetailFragment, TAG_FRAGMENT).commit();
        } else {
            mBookDetailFragment = (BookDetailViewFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_BOOK, mBook);
    }

}
