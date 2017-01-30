package com.example.melanieh.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/*** Created by melanieh on 10/8/16. */

public class SearchResultActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<BookListing>> {

    String QUERY_URL;
    ArrayList<BookListing> storedBookListings;
    BookListingRecyclerAdapter adapter;
    LinearLayoutManager bookLLManager;
    Bundle args;
    String queryUrlString;

    private String LOG_TAG=SearchResultActivity.class.getSimpleName();
    // User types in keyword in this View that is incorporated as the query parameter
    // in the API query URL.

    @BindView(R.id.book_recycler_view) RecyclerView bookRecyclerView;

    // loader ID
    private static final int BOOK_LOADER_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "testing: onCreate called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search_result);
        ButterKnife.bind(this);
        Intent incoming = getIntent();
        queryUrlString = incoming.getStringExtra("queryUrl");
        Log.v(LOG_TAG, "queryUrlString " + queryUrlString);
        args = new Bundle();
        args.putString("query url", queryUrlString);
        Log.v(LOG_TAG, "args= " + args);
        getLoaderManager().initLoader(BOOK_LOADER_ID, args, SearchResultActivity.this).forceLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Loader<ArrayList<BookListing>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader:");
        return new BookLoader(this, bundle.getString("query url"));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BookListing>> loader, ArrayList<BookListing> bookListings) {
        // adapter initialization call to take in and display loader data
        bookLLManager = new LinearLayoutManager(SearchResultActivity.this);
        adapter = new BookListingRecyclerAdapter(SearchResultActivity.this, bookListings);
        bookRecyclerView.setLayoutManager(bookLLManager);
        bookRecyclerView.setAdapter(adapter);
        storedBookListings = bookListings;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BookListing>> loader) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}


