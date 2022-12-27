/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.recyclerview;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 20;

    private MovieAdapter mAdapter;
    private RecyclerView mMoviesList;
    private TextView mErrorMessage;
    private MovieViewModel mViewModel2;

    enum SearchType {
        PopularMovies,
        TopRatedMovies
    }

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeMovieSearchQuery(SearchType.PopularMovies);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);


        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(NUM_LIST_ITEMS, this);
        mMoviesList.setAdapter(mAdapter);
        mAdapter.SetNumberItems(NUM_LIST_ITEMS);
        MovieViewModel mViewModel2 = ViewModelProviders.of(this).get(MovieViewModel.class);
        mViewModel2.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                int numFavorites = movies.size();
                int i;
                JsonUtil.updateFavoriteMovies(movies);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_popular:
                mAdapter.SetNumberItems(NUM_LIST_ITEMS);
                makeMovieSearchQuery(SearchType.PopularMovies);
                return true;
            case R.id.action_top_rated:
                mAdapter.SetNumberItems(NUM_LIST_ITEMS);
                makeMovieSearchQuery(SearchType.TopRatedMovies);
                return true;
            case R.id.action_favorites:
                loadMoviesFromDatabase();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadMoviesFromDatabase() {
        if (JsonUtil.checkForZeroFavorites()) {
            Intent intent = new Intent(MainActivity.this, EmptyFavorites.class);
            startActivity(intent);
            return;
        }
       JsonUtil.copyFavMovies();
       int numItems = JsonUtil.getFavArraySize();
       mAdapter.SetNumberItems(numItems);
       mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        android.util.Log.d("WWD7", "the clicked item index is " + clickedItemIndex);
        if (NetworkUtils.getNetworkConnected()) {
            Intent myIntent = new Intent(this, DetailActivity.class);
            myIntent.putExtra("intIndex", clickedItemIndex);
            startActivity(myIntent);
        }
    }

    private void makeMovieSearchQuery(SearchType type) {
        URL fetchMovieUrl;
        if (type == SearchType.PopularMovies)
            fetchMovieUrl = NetworkUtils.buildPopularUrl();
        else
            fetchMovieUrl = NetworkUtils.buildTopRatedUrl();
        new MovieQueryTask().execute(fetchMovieUrl);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResults = null;
            try {
                movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieResults;
        }

        public void showRecyclerView() {
            mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
            mMoviesList.setVisibility(View.VISIBLE);

            mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
            mErrorMessage.setVisibility(View.INVISIBLE);
        }

        public void showErrorMessage() {
            mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
            mMoviesList.setVisibility(View.INVISIBLE);

            mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
            mErrorMessage.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String movieSearchResults) {
            if (NetworkUtils.getNetworkConnected()) {
                showRecyclerView();
                if (movieSearchResults != null && !movieSearchResults.equals("")) {
                    Log.d("WWD", "got movie results");
                    JsonUtil.parseMovieJson(movieSearchResults);
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                showErrorMessage();
            }
        }

    }

}
