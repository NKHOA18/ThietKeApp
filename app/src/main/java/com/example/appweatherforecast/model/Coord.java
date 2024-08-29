
package com.example.appweatherforecast.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {


    private Double lon;

    private Double lat;

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

}
