package com.example.keerthi.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by Selvanayagam on 5/4/2016.
 */
/*public class MovieDetailPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private List<MovieData> mMovieDatas;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_detail_movie);
        mViewPager = (ViewPager)findViewById(R.id.activity_pager_detail_movie_view_pager);
        mMovieDatas = MovieApiWrapper.getInstance().getMovieDataList(movieUrl);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                MovieData movieData = mMovieDatas.get(position);
                return MovieDetailsFragment.newInstance(movieData.getMovieId());
            }

            @Override
            public int getCount() {
                return mMovieDatas.size();
            }
        });
    }
}*/
