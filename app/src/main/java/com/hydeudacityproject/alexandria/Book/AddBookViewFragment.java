package com.hydeudacityproject.alexandria.Book;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hydeudacityproject.alexandria.R;
import com.hydeudacityproject.alexandria.ScannerView.ScannerView;

/**
 * Created by jhyde on 12/23/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class AddBookViewFragment extends Fragment {

    private static final int RESULT_SCAN = 1;

    private ImageButton mAddBookByEntryImageButton;
    private ImageButton mAddBookByScanImageButton;

    public static AddBookViewFragment newInstance() {
        AddBookViewFragment fragment = new AddBookViewFragment();

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_book_view_fragment, container, false);

        mAddBookByEntryImageButton = (ImageButton) view.findViewById(R.id.imageButton_add_book_by_entry);
        mAddBookByEntryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBookByISBNView.class);

                startActivity(intent);
            }
        });

        mAddBookByScanImageButton = (ImageButton) view.findViewById(R.id.imageButton_add_book_by_scan);
        mAddBookByScanImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScannerView.class);

                getActivity().startActivityForResult(intent, RESULT_SCAN);
            }
        });

        return view;
    }
}
