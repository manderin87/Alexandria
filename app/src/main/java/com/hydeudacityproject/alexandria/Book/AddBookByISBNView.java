package com.hydeudacityproject.alexandria.Book;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hydeudacityproject.alexandria.R;
import com.hydeudacityproject.alexandria.Service.GetBooks;
import com.hydeudacityproject.alexandria.Service.GetBooksResponse;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.PopupDialog;
import com.lonewolfgames.framework.Utilities;

/**
 * Created by jhyde on 12/22/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AddBookByISBNView extends AppCompatActivity implements GetBooks.GetBooksListener {

    private static final String TAG = AddBookByISBNView.class.getSimpleName();

    private static final String TAG_ADD_BOOK_BY_ENTRY_VIEW_FRAGMENT = "add_book_by_entry_view_fragment";
    private static final String KEY_ISBN = "isbn";

    private Toolbar mToolbar;

    private String mISBN = "";

    private CoordinatorLayout mCoordinatorLayout;

    private FrameLayout mContainerLayout;
    private TextView mNoResultsTextView;
    private AddBookEntryViewFragment mAddBookEntryViewFragment;

    private ImageButton mSearchButton;
    private EditText mISBNEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_book_by_isbn_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(R.string.add_book_by_isbn_view_title);
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mContainerLayout = (FrameLayout) findViewById(R.id.frameLayout_library_view);
        mNoResultsTextView = (TextView) findViewById(R.id.textView_no_results);

        mISBNEditText = (EditText) findViewById(R.id.editText_isbn);
        mISBNEditText.requestFocus();
        mISBNEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    executeRetrieve();

                    return true;
                }

                return false;
            }
        });

        mSearchButton = (ImageButton) findViewById(R.id.imageButton_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeRetrieve();
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ISBN)) {
            mISBN = savedInstanceState.getString(KEY_ISBN);

            mISBNEditText.setText(mISBN);
        }

        mAddBookEntryViewFragment = (AddBookEntryViewFragment) getFragmentManager().findFragmentByTag(TAG_ADD_BOOK_BY_ENTRY_VIEW_FRAGMENT);

        if(mAddBookEntryViewFragment != null) {
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
                if(!mISBNEditText.getText().toString().isEmpty()) {
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
                } else {
                    onBackPressed();
                }

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

    /**
     * Clears the book detail
     */
    public void clear() {
        mISBNEditText.setText("");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(mAddBookEntryViewFragment).commit();
    }


    @Override
    public void OnGetBooksError(AbstractAppService.ServiceError error, String isbn) {
        if(error == AbstractAppService.ServiceError.NetworkConnectionError) {
            showNetworkConnectionError(isbn);
        }
    }

    private void executeRetrieve() {
        Utilities.hideSoftkeyboard(AddBookByISBNView.this, mISBNEditText);
        mISBN = mISBNEditText.getText().toString();

        if(mISBN.isEmpty()) {
            return;
        }

        if(Utilities.validateISBN10(mISBN) || Utilities.validateISBN13(mISBN)) {
            mISBN = mISBN.replace("-","");
            retrieveResults(mISBN);
        }
    }

    private void retrieveResults(String isbn) {
        new GetBooks(AddBookByISBNView.this, AddBookByISBNView.this, isbn).execute();
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
