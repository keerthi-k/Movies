package com.example.keerthi.movies;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Selvanayagam on 2/3/2016.
 */
public class MovieDetailsFragment extends Fragment {
    public static final String TAG = "MovieDetailsFragment";

    private MovieDetailsData mMovie;
    private String mZipCode;

    private TextView mReleaseDate;
    private TextView mRunTime;
    private TextView mOverView;
    private TextView mStatus;
    private TextView mCast;
    private TextView mGenre;
    //private EditText mUserZipCode;
    private RecyclerView mRecyclerView;
    private RecyclerView mTheaterRecyclerView;

    private MovieImageListAdapter mMovieImageListAdapter;
    private TheaterListAdapter mTheaterListAdapter;

    public static final String EXTRA_MOVIE_ID = "com.example.keerthi.entertainment.movie.EXTRA_ID";
    private VideoEnabledWebView mWebView;
    private VideoEnabledWebChromeClient webChromeClient;

    public static MovieDetailsFragment newInstance(long movieId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MOVIE_ID, movieId);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);

        Bundle args = getArguments();
        final long movieId = args.getLong(EXTRA_MOVIE_ID, -1);
        if (movieId == -1){
            return view;
        }

        mReleaseDate = (TextView)view.findViewById(R.id.releaseDate);
        mRunTime = (TextView)view.findViewById(R.id.runningTime);
        mOverView = (TextView)view.findViewById(R.id.overView);
        mStatus = (TextView)view.findViewById(R.id.status);
        mCast = (TextView)view.findViewById(R.id.cast);
        mGenre = (TextView)view.findViewById(R.id.genre);
        mWebView = (VideoEnabledWebView)view.findViewById(R.id.trailer);
        View nonVideoLayout = view.findViewById(R.id.movie_detail_scroll); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup)view.findViewById(R.id.videoLayout); // Your own view, read class comments
        //View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, null, mWebView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String js = "javascript:";
                js += "var _ytrp_html5_video = document.getElementsByTagName('video')[0];";
                js += "if (!document.webkitFullScreen && _ytrp_html5_video.webkitEnterFullscreen) {_ytrp_html5_video.webkitEnterFullscreen();}";
                mWebView.loadUrl(js);
            }
        });

        //mUserZipCode = (EditText) view.findViewById(R.id.user_zipCode);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.movieImageList);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mTheaterRecyclerView = (RecyclerView)view.findViewById(R.id.theaterList);
        mTheaterRecyclerView.setHasFixedSize(true);
        LinearLayoutManager theaterLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mTheaterRecyclerView.setLayoutManager(theaterLayoutManager);

        mMovieImageListAdapter = new MovieImageListAdapter(getContext());
        mRecyclerView.setAdapter(mMovieImageListAdapter);

        mTheaterListAdapter = new TheaterListAdapter(getContext());
        mTheaterRecyclerView.setAdapter(mTheaterListAdapter);



        //You tube need JavaScript.
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        // disable scroll on touch
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        MovieDetailsTask movieDetailsTask = new MovieDetailsTask();
        movieDetailsTask.execute((int)movieId);

        MovieImageFetcher movieImageFetcher = new MovieImageFetcher();
        movieImageFetcher.execute(movieId);

        ZipCodeFetcher zipCodeFetcher = new ZipCodeFetcher();
        zipCodeFetcher.execute();

        return view;

    }

    private void fetchShowTimes() {
        if (mMovie == null || mMovie.getMovieTitle() == null || mZipCode == null) {
            return;
        }
        ShowTimeFetcher showTimeFetcher = new ShowTimeFetcher();
        showTimeFetcher.execute(mMovie.getMovieTitle(), mZipCode);
    }



    @Override
    public void onPause() {
        mWebView.onPause();
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    class MovieDetailsTask extends AsyncTask<Integer, Void, MovieDetailsData> {
        @Override
        protected MovieDetailsData doInBackground(Integer... params) {
            return MovieApiWrapper.getInstance().getMovieDetails(params[0]);
        }

        @Override
        protected void onPostExecute(MovieDetailsData movie) {
            AppCompatActivity appCompatActivity =(AppCompatActivity)getActivity();
            try {
                ActionBar actionBar = appCompatActivity.getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(movie.getMovieTitle());
                }
            }catch (Exception ex){
                Log.e(TAG, "unable to get action bar. e:" + ex.getMessage());
            }


            mReleaseDate.setText(movie.getReleaseDate());
            mRunTime.setText(Integer.toString(movie.getMovieRunTime())+ " minutes");
            mStatus.setText(movie.getMovieStatus());
            mOverView.setText(movie.getMovieOverview());
            mGenre.setText(movie.getMovieGenre());

            Map<String, String> cast = movie.getMovieCast();
            String castNames = TextUtils.join(", ", cast.keySet());
            mCast.setText(castNames);
            mMovie = movie;

            List<String> videoList =movie.getTrailerUrls();
            if (videoList != null && videoList.size() > 0){
                mWebView.loadUrl(videoList.get(0));
                mWebView.setVisibility(View.VISIBLE);
            }
            fetchShowTimes();
        }
    }

    public class ZipCodeFetcher extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            try {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException ex) {
                Log.e(TAG, "e:" + ex.getMessage());
            }
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            List<Address> addresses = null;

            Address address = null;
            String zipCode = null;


            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size()> 0){
                    address = addresses.get(0);
                    zipCode = address.getPostalCode();
                }

            } catch (IOException ex){
                Log.e(TAG, "e:" + ex.getMessage());
            }
            return zipCode;
        }
        @Override
        protected void onPostExecute(String zipCode) {
            mZipCode = zipCode;
            //mUserZipCode.setText(mZipCode);
            fetchShowTimes();
        }
    }


    public class MovieImageFetcher extends AsyncTask<Long, Void, List<String>>{

        @Override
        protected List<String> doInBackground(Long... params) {
            return MovieApiWrapper.getInstance().getMovieImages(params[0]);
        }

        @Override
        protected void onPostExecute(List<String> imageUrls) {
            mMovieImageListAdapter.setImageUrls(imageUrls);
        }
    }

    public class ShowTimeFetcher extends AsyncTask<String, Void, Map<String, List<ShowTime>>>{

        @Override
        protected Map<String, List<ShowTime>> doInBackground(String... params) {
            return MovieApiWrapper.getInstance().getTheaters(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(Map<String, List<ShowTime>> showTimes) {
            mTheaterListAdapter.setTheaters(showTimes);
        }
    }



}
