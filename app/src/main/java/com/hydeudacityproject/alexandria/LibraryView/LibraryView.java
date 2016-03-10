package com.hydeudacityproject.alexandria.LibraryView;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydeudacityproject.alexandria.AboutView.AboutView;
import com.hydeudacityproject.alexandria.Book.AddBookView;
import com.hydeudacityproject.alexandria.Detail.BookDetailView;
import com.hydeudacityproject.alexandria.Detail.BookDetailViewFragment;
import com.hydeudacityproject.alexandria.R;
import com.hydeudacityproject.alexandria.Service.Framework.Book;

/**
 * Created by jhyde on 12/1/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class LibraryView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LibraryViewFragment.BookSelectedListener {


    private static final String TAG_LIBRARY_VIEW_FRAGMENT = "library_view_fragment";
    private static final String TAG_BOOK_DETAIL_VIEW_FRAGMENT = "book_detail_view_fragment";

    protected Toolbar mToolbar;
    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    protected NavigationView mNavigationView;

    private FrameLayout mLibraryViewFragmentLayout;
    private FrameLayout mBookDetailViewFragmentLayout;

    private LibraryViewFragment mLibraryViewFragment;
    private BookDetailViewFragment mBookDetailViewFragment;

    private boolean         mShowSearch = false;
    private LinearLayout    mSearchLinearLayout;
    private ImageButton     mSearchBackButton;
    private ImageButton     mSearchClearButton;
    private EditText        mSearchEditText;
    private String          mSearchTerm = "";



//    private CharSequence title;
//    public static boolean IS_TABLET = false;
//    private BroadcastReceiver messageReciever;
//
//    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
//    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.library_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);

        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(R.string.library_view_title);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });

            mNavigationView.setNavigationItemSelectedListener(this);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mSearchLinearLayout = (LinearLayout) findViewById(R.id.search_bar);
        mSearchBackButton = (ImageButton) findViewById(R.id.imageButton_back);
        mSearchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowSearch = false;
                mSearchLinearLayout.setVisibility(View.GONE);

                mSearchTerm = "";
                mSearchEditText.setText(mSearchTerm);

                if(mLibraryViewFragment != null) {
                    mLibraryViewFragment.performSearch(mSearchTerm);
                }

                // Close the soft keyboard
                View view = LibraryView.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                enableNavigationDrawer();
            }
        });

        mSearchClearButton = (ImageButton) findViewById(R.id.imageButton_clear);
        mSearchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTerm = "";
                mSearchEditText.setText(mSearchTerm);
                if(mLibraryViewFragment != null) {
                    mLibraryViewFragment.performSearch(mSearchTerm);
                }

                // Show the soft keyboard
                if(mSearchEditText != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(mSearchEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    mSearchEditText.requestFocus();
                }
            }
        });

        mSearchEditText = (EditText) findViewById(R.id.editText_search);
        mSearchEditText.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    mSearchTerm = mSearchEditText.getText().toString();

                    if(mLibraryViewFragment != null) {
                        mLibraryViewFragment.performSearch(mSearchTerm);
                    }

                    // Close the soft keyboard
                    View view = LibraryView.this.getCurrentFocus();
                    if(view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }

                return false;
            }
        });


        mLibraryViewFragmentLayout = (FrameLayout) findViewById(R.id.frameLayout_library_view);

        if(mLibraryViewFragment == null) {
            mLibraryViewFragment = new LibraryViewFragment().newInstance();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_library_view, mLibraryViewFragment, TAG_LIBRARY_VIEW_FRAGMENT).commit();
        }

        mBookDetailViewFragmentLayout = (FrameLayout) findViewById(R.id.frameLayout_book_detail_view);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.navigation_library_view:
                //intent = new Intent(mContext, DashboardView.class);
                if(mLibraryViewFragment == null) {
                    mLibraryViewFragment = LibraryViewFragment.newInstance();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout_library_view, mLibraryViewFragment, TAG_LIBRARY_VIEW_FRAGMENT).commit();
                } else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout_library_view, mLibraryViewFragment, TAG_LIBRARY_VIEW_FRAGMENT);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case R.id.add_book_view: {
                    gotoAddBookView();
                }
                break;
            case R.id.about:
                Intent intent = new Intent(this, AboutView.class);
                startActivity(intent);
                break;
            default:
                return true;
        }

        if(mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_dashboard_view, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }

        switch(item.getItemId()) {
            case R.id.dashboard_view_search:
                mShowSearch = !mShowSearch;

                if(mShowSearch) {
                    disableNavigationDrawer();
                    mSearchLinearLayout.setVisibility(View.VISIBLE);

                    // Show the soft keyboard
                    if(mSearchEditText != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInputFromWindow(mSearchEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        mSearchEditText.requestFocus();
                    }
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void disableNavigationDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void enableNavigationDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnBookSelected(View itemView, Book book) {
        if(mBookDetailViewFragmentLayout == null) {
            View image_view = itemView.findViewById(R.id.imageView_cover);

            Intent intent = new Intent(itemView.getContext(), BookDetailView.class);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) itemView.getContext(),
                    image_view,
                    image_view.getTransitionName()
            );

            Bundle bundle = new Bundle();
            bundle.putParcelable(BookDetailView.KEY_BOOK, book);

            intent.putExtras(bundle);
            ActivityCompat.startActivity((Activity) itemView.getContext(), intent, options.toBundle());
        } else {
            mBookDetailViewFragment = BookDetailViewFragment.newInstance(book);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_book_detail_view, mBookDetailViewFragment, TAG_BOOK_DETAIL_VIEW_FRAGMENT)
                    .commit();

        }
    }

    public boolean isTabletView() {
        return mBookDetailViewFragment != null;
    }


    public void gotoAddBookView() {
        Intent intent = new Intent(LibraryView.this, AddBookView.class);
        startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}