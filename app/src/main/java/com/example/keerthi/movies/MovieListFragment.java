package com.example.keerthi.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public abstract class MovieListFragment extends Fragment{

    private static final String TAG = "MovieListFragment";

    private ListView mMovieListView;
    private MovieListAdapter mMovieListAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        mMovieListView = (ListView)v.findViewById(R.id.itemsList);

        MoviesTask moviesTask = new MoviesTask();
        moviesTask.execute();

        mMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick called. position: " + position);
                Intent movieIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                movieIntent.putExtra(MovieDetailsFragment.EXTRA_MOVIE_ID, (long)view.getTag());
                startActivity(movieIntent);
            }
        });

        return v;
    }

    abstract protected List<MovieData> getMoviesData();

    class MoviesTask extends AsyncTask<Void, Void, List<MovieData>> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Loading...");
            mProgressDialog.setMessage("Please wait.");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        @Override
        protected List<MovieData> doInBackground(Void... params) {
            try {
                return getMoviesData();
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieData> moviesList) {
            if (moviesList != null && !moviesList.isEmpty()) {
                mMovieListAdapter = new MovieListAdapter(getActivity(), moviesList);
                mMovieListView.setAdapter(mMovieListAdapter);
            } else {
                Toast.makeText(getActivity(), "Please connect to the Internet and try again", Toast.LENGTH_LONG).show();
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }
}
