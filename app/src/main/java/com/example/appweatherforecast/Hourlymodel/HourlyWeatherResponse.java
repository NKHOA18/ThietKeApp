package com.example.appweatherforecast.Hourlymodel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HourlyWeatherResponse {
    @SerializedName("list")
    private List<HourlyWeatherItem> hourlyWeatherItemList;

    public List<HourlyWeatherItem> getHourlyWeatherItemList() {
        return hourlyWeatherItemList;
    }


}
