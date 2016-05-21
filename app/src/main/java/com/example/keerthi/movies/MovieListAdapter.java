package com.example.keerthi.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieListAdapter extends ArrayAdapter<MovieData> {
    LayoutInflater mInflater;
    String mImageBasePath;

    public MovieListAdapter(Context context, List<MovieData> data) {
        super(context, 0, data);
        mInflater = LayoutInflater.from(getContext());
        mImageBasePath = MovieApiWrapper.getInstance().getImageBaseUrl() + "w154";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView movieName;
        TextView releaseDate;
        ImageView movieImage;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.movie_list_item, parent, false);
        } else {
            view = convertView;
        }

        movieName = (TextView) view.findViewById(R.id.movieName);
        releaseDate= (TextView) view.findViewById(R.id.movieDate);
        movieImage = (ImageView) view.findViewById(R.id.movieImage);

        MovieData item = getItem(position);
        movieName.setText(item.getMovieTitle());
        releaseDate.setText(item.getReleaseDate());

        Picasso.with(getContext())
                .load(mImageBasePath + item.getImagePath())
                .into(movieImage);

        view.setTag(item.getMovieId());

        return view;
    }
}

