package com.example.keerthi.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class NowPlayingMovieFragment extends MovieListFragment {

    @WorkerThread
    @Override
    protected List<MovieData> getMoviesData() {
        return MovieApiWrapper.getInstance().getNowPlaying(1, "en");


        /*TmdbApi tmdbApi = new TmdbApi("1518d0e428e58aac84dd44c2ecded9f1");
        MovieConfiguration movieConfiguration = MovieConfiguration.getInstance();
        movieConfiguration.getConfiguration();
        TmdbMovies movies = tmdbApi.getMovies();

        MovieResultsPage movieResultsPage = movies.getNowPlayingMovies("en", 1);
        return movieResultsPage.getResults();*/
    }
}
