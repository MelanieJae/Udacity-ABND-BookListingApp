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

public class MainActivity extends AppCompatActivity
        {

    String QUERY_URL;
    ArrayList<BookListing> storedBookListings;
    BookListingRecyclerAdapter adapter;
    LinearLayoutManager bookLLManager;
    Bundle args;

    private String LOG_TAG=MainActivity.class.getSimpleName();
    // User types in keyword in this View that is incorporated as the query parameter
    // in the API query URL.

    @BindView(R.id.search_field) EditText keywordFieldView;
    @BindView(R.id.search_button) Button searchButton;
    @BindView(R.id.instructions) TextView instructText;
//    @BindView(R.id.book_recycler_view) RecyclerView bookRecyclerView;

    // loader ID
    private static final int BOOK_LOADER_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "testing: onCreate called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // preserves data upon screen rotation

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                // construct API query
                final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
                StringBuilder stringBuilder = new StringBuilder(BASE_URL);
                stringBuilder.append("q=" + keywordFieldView.getText().toString());
                stringBuilder.append("&api-key=" + BuildConfig.GOOGLE_BOOKS_API_KEY);
                stringBuilder.append("maxResults" + "3");
                QUERY_URL = stringBuilder.toString();
                Intent openSearchResult = new Intent(MainActivity.this, SearchResultActivity.class);
                openSearchResult.putExtra("queryUrl", QUERY_URL);
                Log.v(LOG_TAG, QUERY_URL);
                startActivity(openSearchResult);

                // dismiss keyboard after user hits the search button
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(keywordFieldView.getWindowToken(), 0);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


