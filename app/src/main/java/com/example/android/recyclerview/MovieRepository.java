package com.example.android.recyclerview;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class MovieRepository {
    private MovieDAO mMovieDAO;
    private LiveData<List<Movie>> mAllMovies;

    //constructor that gets a handle to the database and initializes the member variables
    MovieRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mMovieDAO = db.mMovieDAO();
        mAllMovies = mMovieDAO.getAllMovies();
    }

    // wrapper method called getAllMovies() that returns the cached words as LiveData
    // Room executes on a separate thread. Observed LiveData notifies the observer when data changes
    LiveData<List<Movie>> getAllMovies() { return mAllMovies;}

    // wrapper for insert method, must use AsyncTask to call insert() on non-UI thread or app will crash
    // Room ensures no long-running operations on the main thread which would block UI
    public void insert (Movie movie) {
        new insertAsyncTask(mMovieDAO).execute(movie);
    }

    // create insertAsyncTask as an inner class
    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDAO mAsyncTaskDao;

        insertAsyncTask(MovieDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }
}
