package com.example.appweatherforecast.Hourlymodel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HourlyWeatherApiService {
    @GET("forecast?")
    Call<HourlyWeatherResponse> getHourlyWeatherData(
            @Query("q") String q,
            @Query("appid") String appid,
            @Query("units") String units
    );
}
