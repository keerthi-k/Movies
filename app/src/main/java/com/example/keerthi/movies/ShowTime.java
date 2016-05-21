package com.example.keerthi.movies;

/**
 * Created by Selvanayagam on 4/20/2016.
 */
public class ShowTime {
    private String mTime;
    private String mBookingUrl;
    public ShowTime(String time, String bookingUrl){
        mTime = time;
        mBookingUrl = bookingUrl;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getBookingUrl() {
        return mBookingUrl;
    }

    public void setBookingUrl(String bookingUrl) {
        mBookingUrl = bookingUrl;
    }
}
