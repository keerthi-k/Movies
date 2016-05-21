package com.example.keerthi.movies;

/**
 * Created by Selvanayagam on 3/11/2016.
 */
public class MovieData {
    private String mMovieTitle;
    private String mReleaseDate;
    private String mImagePath;
    private long mMovieId;

    public MovieData(String movieTitle, String releaseDate, String imagePath, long movieId){
        mMovieTitle = movieTitle;
        mReleaseDate = releaseDate;
        mImagePath = imagePath;
        mMovieId = movieId;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public long getMovieId() {
        return mMovieId;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }
}
