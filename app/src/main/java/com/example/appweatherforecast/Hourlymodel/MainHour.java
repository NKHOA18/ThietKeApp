package com.example.appweatherforecast.Hourlymodel;

import com.google.gson.annotations.SerializedName;

public class MainHour {
    @SerializedName("temp")
    private double temphour;

    @SerializedName("temp_min")
    private double tempmin;
    @SerializedName("temp_max")
    private double tempmax;
    @SerializedName("humidity")
    private int humidity;


    public double getTemphour() {
        return temphour;
    }

    public double getTempmin() {
        return tempmin;
    }

    public double getTempmax() {
        return tempmax;
    }

    public int getHumidity() {
        return humidity;
    }
}
