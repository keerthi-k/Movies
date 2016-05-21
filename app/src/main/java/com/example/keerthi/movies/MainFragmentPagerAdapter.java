package com.example.keerthi.movies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private static final int NOW_PLAYING_MOVIE_PAGE = 0;
    private static final int UPCOMING_MOVIE_PAGE = 1;
    private static String mTabTitles[] = new String[] { "Now Playing", "UpComing" };

    private final Context mContext;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case NOW_PLAYING_MOVIE_PAGE:
                return new NowPlayingMovieFragment();
            case UPCOMING_MOVIE_PAGE:
                return new UpComingMovieFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return mTabTitles[position];
    }
}
