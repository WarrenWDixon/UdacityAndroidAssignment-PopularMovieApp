package com.example.android.recyclerview;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
        private ImageView mThumbnail;
        private TextView mTitle;
        private TextView mVoteAverage;
        private TextView mReleaseDate;
        private TextView mOverview;
        private ImageButton mFavIcon;
        public MovieViewModel mViewModel;

        final String BASE_URL = "http://image.tmdb.org/t/p/w185";

        public static final String VIDEO_KEY = "VIDEO_KEY";
        public static final String REVIEW_URL = "REVIEW_URL";
        private final String INDEX_KEY = "index";


        private String key = null;
        private int index;
        private Context context;
        private SharedPreferences mPreferences;
        private String sharedPrefFile = "com.example.android.recyclerview.moviesharedprefs";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            String mId;
            super.onCreate(savedInstanceState);
            context = this;
            setContentView(R.layout.movie_detail);
            String relativePath = new String();
            String fullPath = new String();
            Intent intent = getIntent();
            index = intent.getIntExtra("intIndex", -1);
            if (index != -1) {
                SaveIndex.StoreIndex(index);
            } else {
                index = SaveIndex.getStoredIndex();
            }
            SaveIndex.StoreIndex(index);
            mThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
            mTitle = (TextView) findViewById(R.id.tv_title);
            mVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
            mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
            mOverview = (TextView) findViewById(R.id.tv_overview);
            mFavIcon  = (ImageButton) findViewById(R.id.favIcon);

            mTitle.setText(JsonUtil.getTitle(index));
            mId = JsonUtil.getID(index);
            if (JsonUtil.isMovieFavorite(mId)) {
                mFavIcon.setImageResource(R.drawable.ic_star_black_48dp);
            }
            mVoteAverage.setText(JsonUtil.getPopularity(index));
            mReleaseDate.setText(JsonUtil.getReleaseDate(index));
            mOverview.setText(JsonUtil.getOverview(index));

            // Get a new or existing ViewModel from the ViewModelProvider.
            mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

            relativePath = JsonUtil.getPosterPath(index);
            fullPath = BASE_URL + relativePath;
            Picasso.get().load(fullPath).into(mThumbnail);

        }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void playTrailer(View view) {
            URL fetchMovieDetailsUrl;
            String ID = JsonUtil.getID(index);
            fetchMovieDetailsUrl = NetworkUtils.buildGetVideoUrl(JsonUtil.getID(index));
            new com.example.android.recyclerview.DetailActivity.MovieDetailTask().execute(fetchMovieDetailsUrl);
        }

        public void getReviews(View view) {
            URL fetchMovieReviewsUrl;
            String ID = JsonUtil.getID(index);
            fetchMovieReviewsUrl = NetworkUtils.buildGetReviewsUrl(JsonUtil.getID(index));
            new com.example.android.recyclerview.DetailActivity.MovieReviewTask().execute(fetchMovieReviewsUrl);
        }

        public void StoreMovieInDB() {
            String title = JsonUtil.getTitle(index);
            String popularity = JsonUtil.getPopularity(index);
            String overview   = JsonUtil.getOverview(index);
            String poster     = JsonUtil.getPosterPath(index);
            String releaseDate = JsonUtil.getReleaseDate(index);
            String movieID     = JsonUtil.getID(index);
            Movie mMovie       = new Movie(title, popularity,overview,poster, releaseDate, movieID);
            mViewModel.insert(mMovie);
        }


        public void addFavorite(View view) {
            final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    com.example.android.recyclerview.DetailActivity.this);

            // Setting Dialog Title
            alertDialog2.setTitle("Add Movie To Favorites");

            // Setting Dialog Message
            alertDialog2.setMessage("Add This Movie To Favorites?");

            // Setting Positive "Yes" Btn
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            mFavIcon.setImageResource(R.drawable.ic_star_black_48dp);
                            StoreMovieInDB();

                            Toast.makeText(getApplicationContext(),
                                    "You clicked on YES", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
            // Setting Negative "NO" Btn
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            Toast.makeText(getApplicationContext(),
                                    "You clicked on NO", Toast.LENGTH_SHORT)
                                    .show();
                            dialog.cancel();
                        }
                    });

            // Showing Alert Dialog
            alertDialog2.show();
        }

        public class MovieDetailTask extends AsyncTask<URL, Void, String> {
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

            @Override
            protected void onPostExecute(String movieSearchResults) {
                if (NetworkUtils.getNetworkConnected()) {
                    if (movieSearchResults != null && !movieSearchResults.equals("")) {
                        key = JsonUtil.parseDetailJson(movieSearchResults);
                        if (key != null) {
                            Intent intent = new Intent(context, WebviewActivity.class);
                            intent.putExtra(VIDEO_KEY, key);
                            startActivity(intent);
                        } else {
                            Log.d("WWD", "no trailer available");
                        }
                    }
                } else {
                    Log.d("WWD", "network error");
                }
            }

        }

        public class MovieReviewTask extends AsyncTask<URL, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(URL... params) {
                URL searchUrl = params[0];
                String movieReviews = null;
                try {
                    movieReviews = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return movieReviews;
            }

            @Override
            protected void onPostExecute(String movieReviews) {
                if (NetworkUtils.getNetworkConnected()) {
                    if (movieReviews != null && !movieReviews.equals("")) {
                        String url = JsonUtil.parseReviewJson(movieReviews);
                        if (url != null) {
                            Intent intent = new Intent(context, ReviewActivity.class);
                            intent.putExtra(REVIEW_URL, url);
                            startActivity(intent);
                        } else {
                            Log.d("WWD", "no reviews available");
                        }
                        return;
                    }
                } else {
                    Log.d("WWD", "network error");
                }
            }

        }
}
