package com.example.appweatherforecast.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("weather?")
    Call<WeatherResponse> getWeatherData(
            @Query("q") String q,
            @Query("appid") String appid,
            @Query("units") String units
    );
}
