package com.example.melanieh.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by melanieh on 9/6/16.
 */

public class BookListing implements Parcelable {

    // variables
    private Double rating;
    private ArrayList<CharSequence> mCategory;
    private CharSequence mTitle;
    private ArrayList<CharSequence> mAuthor;
    private CharSequence mDescription;
    String ratingParam;
    String categoryParam;
    String titleParam;
    String authorParam;
    String descriptionParam;
    private String[] bookParamStrings = {ratingParam, categoryParam, titleParam, authorParam, descriptionParam};

    /* constructor */

    public BookListing(Double rating, ArrayList<CharSequence> category, CharSequence mTitle,
                       ArrayList<CharSequence> mAuthor, CharSequence mDescription) {
            this.rating = rating;
            this.mCategory = category;
            this.mTitle = mTitle;
            this.mAuthor = mAuthor;
            this.mDescription = mDescription;
        }

   /* 'getter' methods */

    public Double getRating() { return rating; }

    public ArrayList<CharSequence> getCategory() { return mCategory; }

    public CharSequence getTitle() { return mTitle; }

    public ArrayList<CharSequence> getAuthor() { return mAuthor; }

    public CharSequence getDescription() { return mDescription; }


    /* 'setters' will not be included here because data is only being retrieved
    * and displayed from a JSON response, not edited.
     */

    @Override
    public String toString() {
        return "BookListing{" +
                "rating=" + rating +
                ", mCategory=" + mCategory +
                ", mTitle=" + mTitle +
                ", mAuthor=" + mAuthor +
                ", mDescription=" + mDescription +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(bookParamStrings);
    }

    private BookListing(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        in.readStringArray(bookParamStrings);
    }

    // parcelable constructor and getters
    public final Parcelable.Creator<BookListing> CREATOR = new Parcelable.Creator<BookListing>() {
        @Override
        public BookListing createFromParcel(Parcel parcel) {
            return new BookListing(parcel);
        }

        @Override
        public BookListing[] newArray(int i) {
            return new BookListing[0];
        }
    };
}
