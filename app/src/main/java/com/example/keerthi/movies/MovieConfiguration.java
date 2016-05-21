package com.example.keerthi.movies;

import android.support.annotation.WorkerThread;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by Selvanayagam on 2/1/2016.
 */
public class MovieConfiguration {
    //1518d0e428e58aac84dd44c2ecded9f1
    //https://api.themoviedb.org/3/movie/550?api_key=1518d0e428e58aac84dd44c2ecded9f1

    private static MovieConfiguration instance;

    private String mImageBaseUrl;

    private MovieConfiguration(){

    }

    public static synchronized MovieConfiguration getInstance() {
        if (instance == null){
            instance = new MovieConfiguration();
        }
        return instance;
    }

    public String getImageBaseUrl() {
        return mImageBaseUrl;
    }

    @WorkerThread
    public synchronized void getConfiguration(){
        TmdbApi tmdbApi = new TmdbApi("1518d0e428e58aac84dd44c2ecded9f1");

        if (mImageBaseUrl == null) {
            // Get image base url from configuration
            TmdbConfiguration configuration = tmdbApi.getConfiguration();
            mImageBaseUrl = configuration.getBaseUrl();
        }
    }
}
