package com.example.android.quakereport;

/**
 * Created by Demo on 2017-07-21.
 */

public class Earthquake {
    private double mag;
    private String place;
    private long date;
    private String url;

    public Earthquake(double mag, String place, long date,String url) {
        this.mag = mag;
        this.place = place;
        this.date = date;
        this.url=url;
    }

    public double getMag() {
        return mag;
    }

    public String getPlace() {
        return place;
    }

    public long getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
