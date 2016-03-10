package com.hydeudacityproject.alexandria.LibraryView;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.hydeudacityproject.alexandria.AppData;
import com.hydeudacityproject.alexandria.R;
import com.hydeudacityproject.alexandria.Service.ContentProvider.BookDBHelper;
import com.hydeudacityproject.alexandria.Service.Framework.Book;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.AbstractFilter;
import com.lonewolfgames.framework.AbstractViewAdapter;
import com.lonewolfgames.framework.AbstractViewData;
import com.lonewolfgames.framework.AbstractViewHolder;
import com.lonewolfgames.framework.Cache.Images.ImageLoader;
import com.lonewolfgames.framework.MarginDecoration;

import java.util.ArrayList;

/**
 * Created by jhyde on 12/1/2015.
 * Copyright (C) 2016 Jesse Hyde Lone Wolf Games
 */
public class LibraryViewFragment extends Fragment implements BookDBHelper.LibraryChangedListener {

    public static final String KEY_ADAPTER_DATA = "adapter_data";

    private static LibraryViewFragment mInstance;

    private RecyclerView mListView;
    private GridLayoutManager mListLayoutManager;
    private ViewAdapter mListAdapter;

    private FloatingActionButton mFloatingActionButton;

    public static LibraryViewFragment newInstance() {
        LibraryViewFragment fragment = new LibraryViewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayList<Book> books = new ArrayList<>();

        View view = inflater.inflate(R.layout.library_view_fragment, container, false);

        mListView = (RecyclerView) view.findViewById(R.id.recyclerView_library);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mListLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            mListLayoutManager = new GridLayoutManager(getActivity(), 4);
        }

        mListView.addItemDecoration(new MarginDecoration(0));
        mListView.setLayoutManager(mListLayoutManager);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ADAPTER_DATA)) {
            books = savedInstanceState.getParcelableArrayList(KEY_ADAPTER_DATA);
        } else {
            books = AppData.instance().database().getLibrary();
        }

        mListAdapter = new ViewAdapter(books);
        mListView.setAdapter(mListAdapter);

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((LibraryView) getActivity()).gotoAddBookView();
            }
        });

        AppData.instance().database().addListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save library data to reload
        if(mListAdapter != null && !mListAdapter.data().isEmpty()) {
            outState.putParcelableArrayList(KEY_ADAPTER_DATA, mListAdapter.data());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        AppData.instance().database().removeListener(this);
    }

    @Override
    public void OnLibraryChanged(ArrayList<Book> books) {
        mListAdapter = new ViewAdapter(books);
        mListView.setAdapter(mListAdapter);
    }

    public void performSearch(String searchConstraints) {
        if(mListAdapter != null) {
            mListAdapter.getFilter().filter(searchConstraints);
        }
    }

    private static class BookSearchFilter extends AbstractFilter<Book, ViewAdapter> {

        public BookSearchFilter(ViewAdapter adapter) {
            super(adapter);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraintSequence) {
            String constraint = constraintSequence.toString();

            mFilteredData.clear();

            if(constraintSequence.length() == 0) {
                mFilteredData.addAll(mData);
            } else {
                for(final Book book : mData) {
                    if(book.title().toLowerCase().contains(constraint.toLowerCase())
                            || book.hasAuthor(constraint)) {
                        mFilteredData.add(book);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = mFilteredData;
            results.count = mFilteredData.size();
            return results;
        }
    }

    private static class ViewAdapter extends AbstractViewAdapter<ViewAdapter.Item, com.hydeudacityproject.alexandria.Service.Framework.Book, AbstractViewHolder>
        implements Filterable {

        private Filter mSearchFilter;

        public ViewAdapter(ArrayList<Book> items) {
            super();

            addAll(items);

            mSearchFilter = new BookSearchFilter(this);
        }

        @Override
        public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder.Builder(parent, R.layout.library_view_list_item).build();
        }

        @Override
        public void addAll(ArrayList<Book> items) {
            // Add the content items
            for(Book item : items) {
                addItem(new Item(item, 0));
            }

            if(items.size() == 0) {
                notifyDataSetChanged();
            }
        }

        @Override
        public Filter getFilter() {
            return mSearchFilter;
        }

        public static class Item extends AbstractViewData<Book> {
            public Item(Book item, int type) {
                super(item, type);
            }
        }


        public static class ItemViewHolder extends AbstractViewHolder<Item, ViewAdapter> {

            private ImageView mCoverImageView;
            private ImageLoader mCoverImageLoader;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                mCoverImageView = (ImageView) itemView.findViewById(R.id.imageView_cover);

                if(mCoverImageLoader != null) {
                    if(mCoverImageLoader.getStatus() == AsyncTask.Status.RUNNING) {
                        mCoverImageLoader.cancel(true);
                        mCoverImageLoader = null;
                    }
                }
            }


            @Override
            public void initialize(final Item data, final int position) {
                final Book book = data.item();

                mCoverImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(book != null) {
                            showMovieDetails(book);
                        }
                    }
                });

                mCoverImageLoader = new ImageLoader.Builder(itemView.getContext())
                        .imageView(mCoverImageView)
                        .url(book.imageURL())
                        .filename(book.isbn())
                        .defaultResourceId(R.drawable.btn_add_book)
                        .listener(new ImageLoader.ImageLoaderListener() {
                            @Override
                            public void OnImageLoaded(Bitmap bitmap) {

                            }

                            @Override
                            public void OnImageFailed(AbstractAppService.ServiceError error) {
                            }
                        })
                        .build();
                mCoverImageLoader.execute();
            }

            private void showMovieDetails(Book book) {
                if(mInstance != null) {
                    ((BookSelectedListener) mInstance.getActivity()).OnBookSelected(itemView, book);
                }
            }

            public static class Builder extends AbstractViewHolder.Builder {

                public Builder(ViewGroup parent, int layoutResourceId) {
                    super(parent, layoutResourceId);
                }

                @Override
                public ItemViewHolder build() {
                    View view = LayoutInflater.from(mParent.getContext()).inflate(mLayoutResourceId, mParent, false);

                    return new ItemViewHolder(view);
                }
            }
        }
    }

    public interface BookSelectedListener {
        void OnBookSelected(View itemView, Book book);
    }

}
