package com.hydeudacityproject.alexandria.Detail;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hydeudacityproject.alexandria.AppData;
import com.hydeudacityproject.alexandria.R;
import com.hydeudacityproject.alexandria.Service.Framework.Book;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.Cache.Images.ImageLoader;
import com.lonewolfgames.framework.PopupDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhyde on 12/23/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class BookDetailViewFragment extends Fragment {

    private static final String KEY_BOOK = "book";

    private Book mBook;

    private Toolbar mToolbar;

    private ImageView mCoverImageView;
    private Bitmap mCoverImage;

    private LinearLayout mInfoLayout;
    private TextView mTitleTitleTextView;
    private TextView mTitleTextView;
    private TextView mAuthorTitleTextView;
    private TextView mAuthorTextView;
    private TextView mPublisherTitleTextView;
    private TextView mPublisherTextView;
    private TextView mGenreTitleTextView;
    private TextView mGenreTextView;

    private LinearLayout mDescriptionLayout;
    private TextView mDescriptionTitleTextView;
    private TextView mDescriptionTextView;

    private ShareActionProvider mShareActionProvider;


    public static BookDetailViewFragment newInstance(Book book) {
        BookDetailViewFragment fragment = new BookDetailViewFragment();

        fragment.setBook(book);

        return fragment;
    }

    private void setBook(Book book) {
        mBook = book;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_detail_view_fragment, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_BOOK)) {
            mBook = savedInstanceState.getParcelable(KEY_BOOK);
        }

        if(mToolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(mToolbar);
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setTitle(mBook.title());
        }

        // if the book data is not available close the detail
        // activity
        if(mBook == null) {
            getActivity().finish();
        }

        mCoverImageView = (ImageView) view.findViewById(R.id.imageView_cover);

        if(mCoverImage == null) {
            ImageLoader poster_image_loader = new ImageLoader.Builder(getActivity())
                    .imageView(mCoverImageView)
                    .url(mBook.imageURL())
                    .filename(mBook.isbn())
                    .defaultResourceId(R.drawable.btn_add_book)
                    .listener(new ImageLoader.ImageLoaderListener() {
                        @Override
                        public void OnImageLoaded(Bitmap bitmap) {

                            mCoverImage = bitmap;

                            updateViewColors(bitmap);
                        }

                        @Override
                        public void OnImageFailed(AbstractAppService.ServiceError error) { }
                    })
                    .build();
            poster_image_loader.execute();
        } else {
            mCoverImageView.setImageBitmap(mCoverImage);
        }

        mInfoLayout = (LinearLayout) view.findViewById(R.id.linearLayout_info);

        mTitleTitleTextView = (TextView) view.findViewById(R.id.textView_title_title);

        mTitleTextView = (TextView) view.findViewById(R.id.textView_title);
        mTitleTextView.setText(mBook.title());

        mAuthorTitleTextView = (TextView) view.findViewById(R.id.textView_author_title);

        mAuthorTextView = (TextView) view.findViewById(R.id.textView_author);
        mAuthorTextView.setText(mBook.authorsString());

        mPublisherTitleTextView = (TextView) view.findViewById(R.id.textView_publisher_title);

        mPublisherTextView = (TextView) view.findViewById(R.id.textView_publisher);
        mPublisherTextView.setText(mBook.publisher() + " (" + mBook.publishedDate() + ")");

        mGenreTitleTextView = (TextView) view.findViewById(R.id.textView_genre_title);

        mGenreTextView = (TextView) view.findViewById(R.id.textView_genre);
        mGenreTextView.setText(mBook.genresString());

        mDescriptionLayout = (LinearLayout) view.findViewById(R.id.linearLayout_description);

        mDescriptionTitleTextView = (TextView) view.findViewById(R.id.textView_description_title);

        mDescriptionTextView = (TextView) view.findViewById(R.id.textView_description);
        mDescriptionTextView.setText(mBook.description());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_book_detail_view_fragment, menu);

        MenuItem share_menu_item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share_menu_item);
        mShareActionProvider.setShareIntent(createShareIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete: {
                PopupDialog.newInstance(
                        getResources().getString(R.string.book_detail_view_fragment_delete_book_dialog_title),
                        getResources().getString(R.string.book_detail_view_fragment_delete_book_dialog_message),
                        getResources().getString(R.string.book_detail_view_fragment_delete_book_dialog_yes),
                        getResources().getString(R.string.book_detail_view_fragment_delete_book_dialog_no),
                        new PopupDialog.PopupDialogListener() {
                            @Override
                            public void OnOkClicked() {
                                AppData.instance().database().deleteBook(mBook);
                                ((BookDetailView) getActivity()).finish();
                            }

                            @Override
                            public void OnCancelClicked(DialogFragment dialog) {
                                dialog.dismiss();
                            }
                        }).show(getFragmentManager(), "Cancel Dialog");
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent() {
        Intent share_intent = new Intent(Intent.ACTION_SEND);
        share_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + mBook.title());
        return share_intent;
    }

    /**
     * Update the view colors
     */
    private void updateViewColors(Bitmap bitmap) {
        if(bitmap != null) {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {

                    List<Palette.Swatch> swatches = palette.getSwatches();

                    for(int index = 0; index < swatches.size(); index++) {
                        Palette.Swatch swatch = swatches.get(index);

                        switch(index) {
                            case 0: {
                                mTitleTitleTextView.setTextColor(swatch.getTitleTextColor());
                                mTitleTextView.setTextColor(swatch.getTitleTextColor());
                                mAuthorTitleTextView.setTextColor(swatch.getTitleTextColor());
                                mAuthorTextView.setTextColor(swatch.getTitleTextColor());
                                mPublisherTitleTextView.setTextColor(swatch.getTitleTextColor());
                                mPublisherTextView.setTextColor(swatch.getTitleTextColor());
                                mGenreTitleTextView.setTextColor(swatch.getTitleTextColor());
                                mGenreTextView.setTextColor(swatch.getTitleTextColor());
                                mInfoLayout.setBackgroundColor(swatch.getRgb());
                            }
                            break;

                            case 2: {
                                mDescriptionTitleTextView.setTextColor(swatch.getTitleTextColor());
                                mDescriptionTextView.setTextColor(swatch.getTitleTextColor());
                                mDescriptionLayout.setBackgroundColor(swatch.getRgb());
                            }
                            break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mBook != null) {
            outState.putParcelable(KEY_BOOK, mBook);
        }
    }
}