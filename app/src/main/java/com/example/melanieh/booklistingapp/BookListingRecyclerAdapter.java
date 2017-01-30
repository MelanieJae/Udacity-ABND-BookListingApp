package com.example.melanieh.booklistingapp;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by melanieh on 1/28/17.
 */

public class BookListingRecyclerAdapter extends RecyclerView.Adapter<BookListingRecyclerAdapter.BookViewHolder> {

    Context context;
    ArrayList<BookListing> bookListings;

    public BookListingRecyclerAdapter(Context context, ArrayList<BookListing> bookListings) {
        this.context = context;
        this.bookListings = bookListings;
    }

    @Override
    public int getItemCount() {
        if (bookListings != null) {
            return bookListings.size();
        } else {
            return 0;
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedview = LayoutInflater.from(context).inflate(R.layout.book_list_recyclerview_item, parent, false);
        return new BookViewHolder(inflatedview);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookListing itemBook = bookListings.get(position);
        holder.bind(itemBook);
    }

    /*** Created by melanieh on 1/28/17. */

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView mainImageView;
        TextView categoryView;
        TextView ratingView;
        TextView titleView;
        TextView authorsView;
        TextView resultDescriptionView;
        Context context;
        LinearLayoutManager bookLLManager;
        RecyclerView bookRecyclerView;

        public BookViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mainImageView = (ImageView) itemView.findViewById(R.id.main_image);
            categoryView = (TextView) itemView.findViewById(R.id.result_category);
            ratingView = (TextView) itemView.findViewById(R.id.result_rating);
            titleView = (TextView) itemView.findViewById(R.id.result_title);
            authorsView = (TextView) itemView.findViewById(R.id.result_authors);
            resultDescriptionView = (TextView) itemView.findViewById(R.id.result_description);

        }

        public void bind(final BookListing bookListing) {
            mainImageView.setImageResource(R.drawable.main_img_1);
            ArrayList<CharSequence> categoryArray = bookListing.getCategory();
            ArrayList<CharSequence> authorArray = bookListing.getAuthor();
            categoryView.setText("category: " + formatText(categoryArray));
            ratingView.setText("rating: " + bookListing.getRating().toString());
            titleView.setText("title: " + bookListing.getTitle());
            authorsView.setText("authors: " + formatText(authorArray));
            resultDescriptionView.setText("description: " + bookListing.getDescription());
        }
    }

    public static String formatText(ArrayList<CharSequence> inputArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < inputArray.size(); i++) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(inputArray);
        }
        return stringBuilder.toString().substring(1, stringBuilder.length()-1);
    }
}
