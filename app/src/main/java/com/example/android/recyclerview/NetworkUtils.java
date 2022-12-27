package com.example.android.recyclerview;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

   final static String POPULAR_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=44bb9f3b21602f274a1127bb251ab87d&language=en-US&page=1";

    final static String TOPRATED_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=44bb9f3b21602f274a1127bb251ab87d&language=en-US&page=1";

    final static String GET_MOVIE_DETAILS_START = "https://api.themoviedb.org/3/movie/";
    final static String GET_MOVIE_DETAILS_END   = "?api_key=44bb9f3b21602f274a1127bb251ab87d&language=en-US&page=1&append_to_response=videos";
    final static String GET_REVIEW_END          = "/reviews?api_key=44bb9f3b21602f274a1127bb251ab87d&language=en-US&page=1";
    final static String YOUTUBE_URL             = "https://www.youtube.com/watch?v=";

    private static boolean networkConnected;

    public static URL buildPopularUrl() {
        Uri builtUri = Uri.parse(POPULAR_MOVIE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTopRatedUrl() {
        Uri builtUri = Uri.parse(TOPRATED_MOVIE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildGetVideoUrl(String detail) {
        String urlString = GET_MOVIE_DETAILS_START + detail + GET_MOVIE_DETAILS_END;
        Uri builtUri = Uri.parse(urlString).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildGetReviewsUrl(String id) {
        String urlString = GET_MOVIE_DETAILS_START + id + GET_REVIEW_END;
        Uri builtUri = Uri.parse(urlString).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }
        /**
         * This method returns the entire result from the HTTP response.
         *
         * @param url The URL to fetch the HTTP response from.
         * @return The contents of the HTTP response.
         * @throws IOException Related to network and stream reading
         */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
       // Log.d("WWD", "in getResponseFromHttpUrl url is " + url);
        try {
            InputStream in = urlConnection.getInputStream();
            if (in == null) {
                networkConnected = false;
            }
            else {
                networkConnected = true;
            }

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean getNetworkConnected() {
        return networkConnected;
    }
}
