package com.example.appweatherforecast.Hourlymodel;

import com.google.gson.annotations.SerializedName;

public class WeatherHour {
    @SerializedName("main")
    private String mainHour;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    // Các getter và setter cho các trường này

    public String getMainHour() {
        return mainHour;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
