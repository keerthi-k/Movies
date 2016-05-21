package com.example.keerthi.movies;

import java.util.List;
import java.util.Map;

/**
 * Created by Selvanayagam on 3/21/2016.
 */
public class MovieDetailsData extends MovieData {
    private String mMovieStatus;
    private Map<String, String> mMovieCast;
    private String mMovieGenre;
    private int mMovieRunTime;
    private String mMovieOverview;
    private List<String> mTrailerUrl;

    public MovieDetailsData(String movieTitle, String releaseDate, String imagePath, long movieId, String movieStatus,
                            Map<String, String> movieCast, String movieGenre, int movieRunTime, String movieOverview, List<String> trailerUrl) {
        super(movieTitle, releaseDate, imagePath, movieId);
        mMovieStatus = movieStatus;
        mMovieCast = movieCast;
        mMovieGenre = movieGenre;
        mMovieRunTime = movieRunTime;
        mTrailerUrl = trailerUrl;
        mMovieOverview = movieOverview;
    }

    public Map<String, String> getMovieCast() {
        return mMovieCast;
    }

    public String getMovieGenre() {
        return mMovieGenre;
    }

    public int getMovieRunTime() {
        return mMovieRunTime;
    }

    public String getMovieStatus() {
        return mMovieStatus;
    }

    public List<String> getTrailerUrls() {
        return mTrailerUrl;
    }

    public String getMovieOverview() {
        return mMovieOverview;
    }
}
