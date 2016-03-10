package com.hydeudacityproject.alexandria.Book;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.hydeudacityproject.alexandria.R;

/**
 * Created by jhyde on 12/23/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AddBookView extends AppCompatActivity {

    private static final String TAG_ADD_BOOK_VIEW_FRAGMENT = "add_book_view_fragment";
    public static final String KEY_ISBN = "isbn";

    private static final int RESULT_SCAN = 1;

    protected Toolbar mToolbar;

    private FrameLayout mFragmentLayout;
    private AddBookViewFragment mAddBookViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_book_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(R.string.add_book_view_title);
        }

        mFragmentLayout = (FrameLayout) findViewById(R.id.frameLayout_library_view);

        if(mAddBookViewFragment == null) {
            mAddBookViewFragment = new AddBookViewFragment().newInstance();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_library_view, mAddBookViewFragment, TAG_ADD_BOOK_VIEW_FRAGMENT).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_SCAN && resultCode == Activity.RESULT_OK) {
            String isbn = data.getExtras().getString(KEY_ISBN);

            Intent intent = new Intent(this, AddBookByScanView.class);

            Bundle bundle = new Bundle();
            bundle.putString(KEY_ISBN, isbn);

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


}
