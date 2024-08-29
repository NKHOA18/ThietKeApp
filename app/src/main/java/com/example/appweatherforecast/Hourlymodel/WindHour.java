package com.example.appweatherforecast.Hourlymodel;

import com.google.gson.annotations.SerializedName;

public class WindHour {
    @SerializedName("speed")
    private double speed;

    @SerializedName("deg")
    private int deg;

    @SerializedName("gust")
    private double gust;


    public double getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

    public double getGust() {
        return gust;
    }
}
