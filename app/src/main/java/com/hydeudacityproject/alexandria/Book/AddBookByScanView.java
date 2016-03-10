package com.hydeudacityproject.alexandria.Book;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hydeudacityproject.alexandria.R;
import com.hydeudacityproject.alexandria.Service.GetBooks;
import com.hydeudacityproject.alexandria.Service.GetBooksResponse;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.PopupDialog;

/**
 * Created by jhyde on 12/22/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AddBookByScanView extends AppCompatActivity implements GetBooks.GetBooksListener {

    private static final String TAG_ADD_BOOK_BY_ENTRY_VIEW_FRAGMENT = "add_book_by_entry_view_fragment";
    private static final String KEY_ISBN = "isbn";

    protected Toolbar mToolbar;

    private String mISBN = "";

    private CoordinatorLayout mCoordinatorLayout;
    private FrameLayout mContainerLayout;
    private TextView mNoResultsTextView;

    private TextView mISBNTextView;
    private AddBookEntryViewFragment mAddBookEntryViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_book_by_scan_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(R.string.add_book_by_scan_view_title);
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mContainerLayout = (FrameLayout) findViewById(R.id.frameLayout_library_view);
        mNoResultsTextView = (TextView) findViewById(R.id.textView_no_results);
        mISBNTextView = (TextView) findViewById(R.id.textView_isbn);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ISBN)) {
            mISBN = savedInstanceState.getString(KEY_ISBN);
            mISBNTextView.setText(mISBN);
        }

        mAddBookEntryViewFragment = (AddBookEntryViewFragment) getFragmentManager().findFragmentByTag(TAG_ADD_BOOK_BY_ENTRY_VIEW_FRAGMENT);

        if(mAddBookEntryViewFragment == null) {
            if(getIntent().hasExtra(AddBookView.KEY_ISBN)) {
                mISBN = getIntent().getStringExtra(AddBookView.KEY_ISBN);

                if(mISBN != null && !mISBN.isEmpty()) {
                    retrieveResults(mISBN);
                    mISBNTextView.setText(mISBN);
                }
            } else {
                finish();
            }
        } else {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_library_view, mAddBookEntryViewFragment, TAG_ADD_BOOK_BY_ENTRY_VIEW_FRAGMENT).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(!mISBN.isEmpty()) {
            outState.putString(KEY_ISBN, mISBN);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PopupDialog.newInstance(
                        getResources().getString(R.string.add_book_cancel_dialog_title),
                        getResources().getString(R.string.add_book_cancel_dialog_message),
                        getResources().getString(R.string.add_book_cancel_dialog_yes),
                        getResources().getString(R.string.add_book_cancel_dialog_no),
                        new PopupDialog.PopupDialogListener() {
                            @Override
                            public void OnOkClicked() {
                                onBackPressed();
                            }

                            @Override
                            public void OnCancelClicked(DialogFragment dialog) {
                                dialog.dismiss();
                            }
                        }).show(getFragmentManager(), "Cancel Dialog");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnGetBooksFinished(GetBooksResponse response) {
        if(response != null && response.items().size() > 0) {
            hideNoResults();
            mAddBookEntryViewFragment = AddBookEntryViewFragment.newInstance(response.items());

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_library_view, mAddBookEntryViewFragment, TAG_ADD_BOOK_BY_ENTRY_VIEW_FRAGMENT).commit();
        } else {
            showNoResults();
        }
    }

    @Override
    public void OnGetBooksError(AbstractAppService.ServiceError error, String isbn) {
        if(error == AbstractAppService.ServiceError.NetworkConnectionError) {
            showNetworkConnectionError(isbn);
        }
    }

    private void showNoResults() {
        if(mNoResultsTextView != null) {
            mNoResultsTextView.setVisibility(View.VISIBLE);
        }

        if(mContainerLayout != null) {
            mContainerLayout.setVisibility(View.GONE);
        }
    }

    private void hideNoResults() {
        if(mNoResultsTextView != null) {
            mNoResultsTextView.setVisibility(View.GONE);
        }

        if(mContainerLayout != null) {
            mContainerLayout.setVisibility(View.VISIBLE);
        }
    }

    private void retrieveResults(String isbn) {
        new GetBooks(AddBookByScanView.this, AddBookByScanView.this, isbn).execute();
    }

    private void showNetworkConnectionError(final String isbn) {
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, getResources().getString(R.string.add_book_network_connection_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.add_book_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        retrieveResults(isbn);
                    }
                });

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
