package com.example.android.recyclerview;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName="movie")
public class Movie {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="movieID")
    private String mMovieID;

    @ColumnInfo(name="title")
    private String mTitle;

    @ColumnInfo(name="popularity")
    private String mPopularity;

    @ColumnInfo(name="overview")
    private String mOverview;

    @ColumnInfo(name="poster")
    private String mPoster;

    @ColumnInfo(name="releaseDate")
    private String mReleaseDate;


    public Movie(String title, String popularity, String overview, String poster,
                 String releaseDate, String movieID) {
        mTitle       = title;
        mPopularity  = popularity;
        mOverview    = overview;
        mPoster      = poster;
        mReleaseDate = releaseDate;
        mMovieID    = movieID;
    }

    public String getMovieID() {
        return mMovieID;
    }
    public void setMovieID(String movieID) {
        mMovieID = movieID;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPopularity() {
        return mPopularity;
    }
    public void setPopularity(String popularity) {
        mPopularity = popularity;
    }

    public String getOverview() {
        return mOverview;
    }
    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getPoster() {
        return mPoster;
    }
    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

}
