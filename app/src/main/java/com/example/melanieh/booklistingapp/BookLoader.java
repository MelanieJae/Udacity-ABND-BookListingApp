package com.example.melanieh.booklistingapp;

import android.content.AsyncTaskLoader;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


/*** Created by melanieh on 10/8/16. */

public class BookLoader extends AsyncTaskLoader {

    ArrayList<BookListing> bookData;
    String urlString;

    private static final String LOG_TAG = BookLoader.class.getName();

    public BookLoader(Context context, String urlString) {
        super(context);
        this.urlString = urlString;
    }

    @Override
    public ArrayList<BookListing> loadInBackground() {
        Log.v(LOG_TAG, "testing: loadInBackground called...");

        // Perform the HTTP request for earthquake data and process the response.
        bookData = obtainBookData(urlString);
        return bookData;
    }
    /** Query the Google Books dataset and return a list of {@link BookListing} objects. */
    public ArrayList<BookListing> obtainBookData(String requestUrlString) {
        Log.v(LOG_TAG, "testing: obtainBookData called...");
//
////         uncomment for testing purposes only to simulate slow network response to test the progress indicator
////        try {
////            Thread.sleep(2000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        Log.v(LOG_TAG, "obtainBookData: requestUrlString= " + requestUrlString);
        try {
            URL url = new URL(requestUrlString);
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Earthquake} object
        ArrayList<BookListing> bookListData = extractFromJson(jsonResponse);
        // Return a list of {@link Earthquake} events.
        return bookListData;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link BookListing} object by parsing out information
     * about the first book from the inputstring.
     */

    private ArrayList<BookListing> extractFromJson(String jSONResponse) {

        CharSequence category = "";
        CharSequence author = "";
        CharSequence bookTitle;
        CharSequence bookDesc;

        ArrayList<BookListing> bookListings = new ArrayList<BookListing>();

        try {
            Double rating = 0.0;
            // Extracts the JSONObject mapped by "items" from the base response.
            JSONObject baseJsonResponse = new JSONObject(jSONResponse);
            JSONArray itemsJSONArray = baseJsonResponse.getJSONArray("items");
            for (int itemIndex = 0; itemIndex < itemsJSONArray.length(); itemIndex++) {
                ArrayList<CharSequence> categoryArray = new ArrayList<CharSequence>();
                ArrayList<CharSequence> authorArray = new ArrayList<CharSequence>();
                JSONObject itemsObject = itemsJSONArray.getJSONObject(itemIndex);
                JSONObject volumeInfoValue = itemsObject.getJSONObject("volumeInfo");

                JSONArray categoriesJSONArray = volumeInfoValue.getJSONArray("categories");
                JSONArray authorsJSONArray = volumeInfoValue.getJSONArray("authors");

                // check for empty authors array
                for (int authIndex = 0; authIndex < authorsJSONArray.length(); authIndex++) {
                    if (authorsJSONArray.length() == 0) {
                        Log.i("extractFeature", "No author information is available");
                    } else {
                        // Extract the formatted contents from the objects in the authors array
                        author = authorsJSONArray.getString(authIndex);
                        Log.i("extractFeature", "authorArray: " + authorArray);
                        authorArray.add(author);
                    }
                }

                // validation/null checks
                if (volumeInfoValue.has("averageRating") == false) {
                    Log.i(LOG_TAG, "average rating is null");
                } else {
                    rating = volumeInfoValue.getDouble("averageRating");
                }

                // check for empty categories array
                if (categoriesJSONArray.length() == 0) {
                    Log.i("extractFeature", "No category information available");
                } else {
                    for (int catIndex = 0; catIndex < categoriesJSONArray.length(); catIndex++) {
                        // Extract the formatted contents from the objects in the categories array
                        category = categoriesJSONArray.getString(catIndex);
                        Log.i("extractFeature", "categoryArray: " + categoryArray);
                        categoryArray.add(category);
                    }
                }

                bookTitle = volumeInfoValue.getString("title");
                bookDesc = volumeInfoValue.getString("description");
                Log.i("extractFeature", "ItemIndex= " + itemIndex +
                        "; categoriesArray= {" + categoryArray + "}"
                        + "; rating= " + rating
                        + "; title= " + bookTitle
                        + "; authorsArray= {" + authorArray + "}"
                        + "; bookDesc= " + bookDesc);

                BookListing currentListing = new BookListing(rating, categoryArray, bookTitle,
                        authorArray, bookDesc);
                bookListings.add(currentListing);

            }
        } catch (JSONException e) {
            Log.e("extractFeature", "" + e);
        }
        return bookListings;
    }
}
