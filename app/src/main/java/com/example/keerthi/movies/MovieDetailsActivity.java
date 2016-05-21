package com.example.keerthi.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.detailFragmentContainer);
        if (fragment == null) {
            fragment = new MovieDetailsFragment();
            Bundle args = new Bundle();
            Intent intent = getIntent();
            long movieId = intent.getLongExtra(MovieDetailsFragment.EXTRA_MOVIE_ID, -1);
            args.putLong(MovieDetailsFragment.EXTRA_MOVIE_ID, movieId);
            fragment.setArguments(args);
            fm.beginTransaction().add(R.id.detailFragmentContainer, fragment)
                    .commit();
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
