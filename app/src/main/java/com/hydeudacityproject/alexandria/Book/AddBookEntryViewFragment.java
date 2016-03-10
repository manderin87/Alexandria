package com.hydeudacityproject.alexandria.Book;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by jhyde on 12/22/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AddBookEntryViewFragment extends Fragment {

    private static final String KEY_BOOK = "book";

    private ArrayList<Book> mBooks;
    private Book mBook;

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

    private LinearLayout mCancelLinearLayout;
    private LinearLayout mAddBookLinearLayout;

    public static AddBookEntryViewFragment newInstance(ArrayList<Book> books) {
        AddBookEntryViewFragment fragment = new AddBookEntryViewFragment();

        fragment.setBooks(books);

        return fragment;
    }

    private void setBooks(ArrayList<Book> books) { mBooks = books; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_book_entry_view_fragment, container, false);


        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_BOOK)) {
            mBook = savedInstanceState.getParcelable(KEY_BOOK);
        } else if(mBooks != null && mBooks.size() > 0) {
            mBook = mBooks.get(0);
        }

        if(mBook == null) {
            getActivity().finish();
        }

        ImageView cover_imageview = (ImageView) view.findViewById(R.id.imageView_cover);

            ImageLoader poster_image_loader = new ImageLoader.Builder(getActivity())
                    .imageView(cover_imageview)
                    .url(mBook.imageURL())
                    .filename(mBook.isbn())
                    .defaultResourceId(R.drawable.btn_add_book)
                    .listener(new ImageLoader.ImageLoaderListener() {
                        @Override
                        public void OnImageLoaded(Bitmap bitmap) {
                            updateViewColors(bitmap);
                        }

                        @Override
                        public void OnImageFailed(AbstractAppService.ServiceError error) {
                        }
                    })
                    .build();
            poster_image_loader.execute();


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

        mCancelLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_cancel);
        mCancelLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialog.newInstance(
                        getResources().getString(R.string.add_book_cancel_dialog_title),
                        getResources().getString(R.string.add_book_cancel_dialog_message),
                        getResources().getString(R.string.add_book_cancel_dialog_yes),
                        getResources().getString(R.string.add_book_cancel_dialog_no),
                        new PopupDialog.PopupDialogListener() {
                            @Override
                            public void OnOkClicked() {

                                if(getActivity() instanceof AddBookByISBNView) {
                                    ((AddBookByISBNView) getActivity()).clear();
                                } else {
                                    getActivity().finish();
                                }
                            }

                            @Override
                            public void OnCancelClicked(DialogFragment dialog) {
                                dialog.dismiss();
                            }
                        }).show(getFragmentManager(), "Cancel Dialog");
            }
        });

        mAddBookLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_add);
        mAddBookLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialog.newInstance(
                        getResources().getString(R.string.add_book_add_dialog_title),
                        getResources().getString(R.string.add_book_add_dialog_message),
                        getResources().getString(R.string.add_book_add_dialog_yes),
                        getResources().getString(R.string.add_book_add_dialog_no),
                        new PopupDialog.PopupDialogListener() {
                            @Override
                            public void OnOkClicked() {
                                AppData.instance().database().addBook(mBook);

                                if(getActivity() instanceof AddBookByISBNView) {
                                    ((AddBookByISBNView) getActivity()).clear();
                                } else {
                                    getActivity().finish();
                                }
                            }

                            @Override
                            public void OnCancelClicked(DialogFragment dialog) {
                                dialog.dismiss();
                            }
                        }).show(getFragmentManager(), "Cancel Dialog");
            }
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mBook != null) {
            outState.putParcelable(KEY_BOOK, mBook);
        }
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

                            case 3: {
                                //((AddBookByISBNView) getActivity()).setBackgroundColor(swatch.getRgb());
                                mCancelLinearLayout.setBackgroundColor(swatch.getRgb());
                                mAddBookLinearLayout.setBackgroundColor(swatch.getRgb());
                            }
                        }
                    }
                }
            });
        }
    }

}
