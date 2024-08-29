package com.example.appweatherforecast.Hourlymodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HourlyWeatherItem {
    @SerializedName("dt")
    private long timestamp;

    @SerializedName("main")
    private MainHour mainHour;

    @SerializedName("weather")
    private List<WeatherHour> weatherHourList;

    @SerializedName("wind")
    private WindHour windHour;

    public HourlyWeatherItem(long timestamp, MainHour mainHour, List<WeatherHour> weatherHourList, WindHour windHour) {
        this.timestamp = timestamp;
        this.mainHour = mainHour;
        this.weatherHourList = weatherHourList;
        this.windHour = windHour;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MainHour getMainHour() {
        return mainHour;
    }

    public void setMainHour(MainHour mainHour) {
        this.mainHour = mainHour;
    }

    public List<WeatherHour> getWeatherHourList() {
        return weatherHourList;
    }

    public void setWeatherHourList(List<WeatherHour> weatherHourList) {
        this.weatherHourList = weatherHourList;
    }

    public WindHour getWindHour() {
        return windHour;
    }

    public void setWindHour(WindHour windHour) {
        this.windHour = windHour;
    }
}
