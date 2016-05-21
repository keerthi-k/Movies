package com.example.keerthi.movies;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Selvanayagam on 3/11/2016.
 */
public class MovieApiWrapper {

    private static MovieApiWrapper instance;

    private MovieApiWrapper() {

    }

    public static MovieApiWrapper getInstance() {
        if (instance == null) {
            instance = new MovieApiWrapper();
        }
        return instance;
    }

    private static final String API_KEY = "1518d0e428e58aac84dd44c2ecded9f1";
    private static final String TAG = "MovieApiWrapper";
    private String mImageBaseUrl;


    public List<MovieData> getNowPlaying(int page, String language) {
        String movieUrl = "http://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&page=" + page + "&language=" + language;
        return getMovieDataList(movieUrl);
    }

    public List<MovieData> getUpcoming(int page, String language) {
        String movieUrl = "http://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY + "&page=" + page + "&language=" + language;
        return getMovieDataList(movieUrl);
    }

    private List<MovieData> getMovieDataList(String url) {
        getImageBaseUrl();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        List<MovieData> moviesList = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray results = root.optJSONArray("results");

            String movieName;
            String releaseDate;
            String imagePath;
            long movieId;
            JSONObject movieObject;

            for (int i = 0; i < results.length(); i++) {
                movieObject = results.getJSONObject(i);
                movieName = movieObject.optString("original_title");
                releaseDate = movieObject.optString("release_date");
                imagePath = movieObject.optString("poster_path");
                movieId = movieObject.optLong("id");
                MovieData movieData = new MovieData(movieName, releaseDate, imagePath, movieId);
                moviesList.add(movieData);
            }

        } catch (IOException | JSONException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return moviesList;
    }

    public String getImageBaseUrl() {
        if (mImageBaseUrl == null) {
            String url = "http://api.themoviedb.org/3/configuration?api_key=" + API_KEY;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String jsonResponse = response.body().string();
                JSONObject root = new JSONObject(jsonResponse);
                JSONObject images = root.optJSONObject("images");
                mImageBaseUrl = images.optString("base_url");
            } catch (IOException | JSONException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
        return mImageBaseUrl;
    }


    public List<String> getMovieImages(long movieId) {
        String imageApiUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/images?api_key=" + API_KEY;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(imageApiUrl)
                .build();

        List<String> imageUrls = new ArrayList<>();

        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray backdrops = root.optJSONArray("backdrops");

            if (backdrops != null) {
                String url;
                JSONObject imageObject;

                for (int i = 0; i < backdrops.length(); i++) {
                    imageObject = backdrops.getJSONObject(i);
                    url = imageObject.optString("file_path");
                    if (url != null) {
                        imageUrls.add(url);
                    }
                }
            }

        } catch (IOException | JSONException ex) {
            Log.e(TAG, "Error getting data from the web. ex" + ex.getMessage());
        }

        return imageUrls;
    }

    public List<String> getTrailerUrls(long movieId) {
        String videosUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + API_KEY;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(videosUrl)
                .build();

        List<String> trailerUrls = new ArrayList<>();
        //Map<String, String> genreMap = new HashMap<>();
        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray results = root.optJSONArray("results");

            String key;
            String site;
            String type;
            JSONObject videoObject;

            for (int i = 0; i < results.length(); i++) {
                videoObject = results.optJSONObject(i);
                key = videoObject.optString("key");
                site = videoObject.optString("site");
                type = videoObject.optString("type");
                if ("YouTube".equalsIgnoreCase(site) && "Trailer".equalsIgnoreCase(type)) {
                    String url = "https://www.youtube.com/watch?v=" + key;
                    trailerUrls.add(url);
                }
            }

        } catch (IOException | JSONException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return trailerUrls;
    }

    public Map<Integer, String> getGenre(long movieId) {

        String genreUrl = "http://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(genreUrl)
                .build();
        Map<Integer, String> genreInfo = new HashMap<>();
        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray genre = root.optJSONArray("genres");

            int genreId;
            String genreName;
            JSONObject genreObject;

            for (int i = 0; i < genre.length(); i++) {
                genreObject = genre.optJSONObject(i);
                genreId = genreObject.optInt("id");
                genreName = genreObject.optString("name");
                genreInfo.put(genreId, genreName);
            }
        } catch (IOException | JSONException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return genreInfo;
    }

    public Map<String, String> getCast(long movieId) {
        String castUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(castUrl).build();
        Map<String, String> castInfo = new HashMap<>();

        JSONObject castObject;
        String castName;
        String castProfilePath;

        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray cast = root.optJSONArray("cast");
            for (int i = 0; i < cast.length(); i++) {
                castObject = cast.optJSONObject(i);
                castName = castObject.optString("name");
                castProfilePath = castObject.optString("profile_path");
                castInfo.put(castName, castProfilePath);
            }
        } catch (IOException | JSONException ex) {
            Log.d(TAG, "");
        }
        return castInfo;
    }


    public MovieDetailsData getMovieDetails(long movieId) {
        String movieDetialUrl = "http://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(movieDetialUrl).build();

        String status = null;
        String movieName = null;
        String releaseDate = null;
        String overView = null;
        String movieImagePath = null;
        String genre  = null;
        String joinedGenre = null;
        int runTime = 0;
        String genreName = null;

        JSONObject genreObject;

        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            status = jsonObject.optString("status");
            movieName = jsonObject.optString("original_title");
            releaseDate = jsonObject.optString("release_date");
            movieImagePath = jsonObject.optString("poster_path");
            overView = jsonObject.optString("overview");
            runTime = jsonObject.optInt("runtime");
            JSONArray genres = jsonObject.optJSONArray("genres");

            List<String> genreList = new ArrayList<>();

            for (int i = 0; i < genres.length(); i++) {
                genreObject = genres.optJSONObject(i);
                genreName = genreObject.optString("name");
                genreList.add(genreName);
            }
            joinedGenre = TextUtils.join(", ", genreList);

        } catch (IOException | JSONException ex) {
            Log.d(TAG, " ");
        }
        Map<String, String> cast = getCast(movieId);
        List<String> videoUrls = getTrailerUrls(movieId);

        return new MovieDetailsData(movieName, releaseDate, movieImagePath, movieId, status, cast, joinedGenre, runTime, overView, videoUrls);
    }

    public Map<String, List<ShowTime>> getTheaters(String originalTitle, String zipCode) {
        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("data.tmsapi.com")
                .appendPath("v1.1")
                .appendPath("movies")
                .appendPath("showings")
                .appendQueryParameter("startDate", startDate)
                .appendQueryParameter("zip", zipCode)
                .appendQueryParameter("api_key", "nknv6s2y8nuadn3epcgmmetw");
        String theaterUrl = builder.build().toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(theaterUrl).build();
        Map<String, List<ShowTime>> theaterObject = null;
        try {
            Response response = client.newCall(request).execute();
            String jsonResponse = response.body().string();
            JSONArray root = new JSONArray(jsonResponse);
            JSONObject movie;
            String title;
            String plainTitle;
            String plainOriginalTitle;
            JSONArray showTime = null;
            for (int i = 0; i < root.length(); i++) {
                movie = root.getJSONObject(i);
                title = movie.optString("title");
                plainTitle = title.replace("^[A-Za-z]+$\n", "");
                plainOriginalTitle = originalTitle.replace("^[A-Za-z]+$\n", "");
                if (plainOriginalTitle.equalsIgnoreCase(plainTitle)) {
                    showTime = movie.optJSONArray("showtimes");
                    break;
                }
            }
            String time;
            String bookingUrl;
            String name;
            JSONObject showTimeInfo;
            JSONObject theater;

            theaterObject = new HashMap<>();
            if (showTime != null) {
                for (int i = 0; i < showTime.length(); i++) {
                    showTimeInfo = showTime.optJSONObject(i);
                    time = showTimeInfo.optString("dateTime");
                    bookingUrl = showTimeInfo.optString("ticketURI");
                    theater = showTimeInfo.optJSONObject("theatre");
                    name = theater.optString("name");
                    ShowTime showTimeObject = new ShowTime(time, bookingUrl);
                    if (!theaterObject.containsKey(name)) {
                        theaterObject.put(name, new ArrayList<ShowTime>());
                    }
                    theaterObject.get(name).add(showTimeObject);
                }
            }


        } catch (IOException | JSONException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return theaterObject;
    }

}
