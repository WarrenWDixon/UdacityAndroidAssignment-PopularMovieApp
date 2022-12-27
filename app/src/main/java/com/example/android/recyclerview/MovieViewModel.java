package com.example.android.recyclerview;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;

    // private LiveData member variable to cache list of words
    private LiveData<List<Movie>> mAllMovies;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public MovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    // add a "getter" method that gets all the words, hides implementation from UI
    LiveData<List<Movie>> getAllMovies() { return mAllMovies;}

    // create wrapper insert() method that calls Repository's insert() method,
    // implementation of insert() is hidden from UI
    public void insert(Movie movie) {
        mRepository.insert(movie);
    }
}
