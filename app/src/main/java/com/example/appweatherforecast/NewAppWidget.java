package com.example.appweatherforecast;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;

import com.example.appweatherforecast.model.Main;
import com.example.appweatherforecast.model.Weather;
import com.example.appweatherforecast.model.WeatherApiService;
import com.example.appweatherforecast.model.WeatherResponse;
import com.example.appweatherforecast.model.Wind;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    public static final String ACTION_UPDATE_WEATHER = "com.example.appweatherforecast.ACTION_UPDATE_WEATHER";
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String cityName = preferences.getString("CITY_NAME_KEY", "DefaultCity");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService service = retrofit.create(WeatherApiService.class);

        Call<WeatherResponse> call = service.getWeatherData(cityName,"4e81285e1eca7bd5310a67ee1ed41986", "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherData = response.body();
                    // Lấy thông tin thời tiết từ weatherData và cập nhật giao diện tiện ích
                    Main to = weatherData.getMain();
                    Wind wi = weatherData.getWind();
                    List<Weather> description = weatherData.getWeather();
                    String location = cityName;
                    String temperature = (String.valueOf(to.getTemp())+"°C");
                    String wind = (String.valueOf(wi.getSpeed())+"m/s");
                    StringBuilder weatherDescription = new StringBuilder();
                    StringBuilder weatherIcon = new StringBuilder();
                    for (Weather data : description) {
                        weatherDescription.append(data.getDescription()).append("");
                        weatherIcon.append(data.getMain()).append("");
                    }
                    String combinedDescription = weatherDescription.toString();
                    String iconCode = weatherIcon.toString();

                    views.setTextViewText(R.id.widgetLocation, location);
                    views.setTextViewText(R.id.widgetTemperature, temperature );
                    views.setTextViewText(R.id.weather, combinedDescription);
                    getWeatherIconName(views,iconCode);
                    views.setTextViewText(R.id.wind, wind);

                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Xử lý lỗi khi không thể kết nối hoặc không có dữ liệu
            }
        });

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Thực hiện cập nhật widget
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    public void getWeatherIconName(RemoteViews views,String iconCode) {
        switch (iconCode) {
            case "Clear":
                views.setImageViewResource(R.id.Icon,R.drawable.sunny);
                break;
            case "Clouds":
                views.setImageViewResource(R.id.Icon,R.drawable.cloud_black);
                break;
            case "Haze":
            case "Rain":
                views.setImageViewResource(R.id.Icon,R.drawable.rain);
                break;
            case "Snow":
                views.setImageViewResource(R.id.Icon,R.drawable.snow);
                break;
            default:
                views.setImageViewResource(R.id.Icon,R.drawable.conditions);
                break;
        }
    }
}