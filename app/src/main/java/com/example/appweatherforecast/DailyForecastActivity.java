package com.example.appweatherforecast;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appweatherforecast.Dailymodel.DailyWeatherAdapter;
import com.example.appweatherforecast.Hourlymodel.HourlyWeatherAdapter;
import com.example.appweatherforecast.Hourlymodel.HourlyWeatherApiService;
import com.example.appweatherforecast.Hourlymodel.HourlyWeatherItem;
import com.example.appweatherforecast.Hourlymodel.HourlyWeatherResponse;
import com.example.appweatherforecast.databinding.ActivityDailyForecastBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DailyForecastActivity extends MainActivity {

    private ActivityDailyForecastBinding binding;
    private RecyclerView recyclerViewDailyWeather;
    private DailyWeatherAdapter dailyWeatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyForecastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewDailyWeather = findViewById(R.id.recyclerViewDailyWeather);
        recyclerViewDailyWeather.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        String cityName = getIntent().getStringExtra("CITY_NAME");
        binding.cityname.setText(cityName);
        String Temp = getIntent().getStringExtra("TEMP");
        binding.temp.setText(Temp);
        String Condition = getIntent().getStringExtra("CONDITION");
        changeImagesWeather(Condition);

        fetchDailyWeatherData(cityName);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyForecastActivity.this, MainActivity.class);
                String CTname = binding.cityname.getText().toString();
                intent.putExtra("CT_NAME", CTname);
                startActivity(intent);
            }
        });
    }

    private void fetchDailyWeatherData(String cityname) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/") // Thay thế bằng URL của API thời tiết bạn đang sử dụng
                .build();

        HourlyWeatherApiService hourlyweatherApiService = retrofit.create(HourlyWeatherApiService.class);

        Call<HourlyWeatherResponse> hourlyWeatherResponseCall = hourlyweatherApiService.getHourlyWeatherData(cityname, "4e81285e1eca7bd5310a67ee1ed41986", "metric");

        hourlyWeatherResponseCall.enqueue(new Callback<HourlyWeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<HourlyWeatherResponse> call, @NonNull Response<HourlyWeatherResponse> response) {
                if (response.isSuccessful()) {
                    HourlyWeatherResponse hourlyWeatherResponse = response.body();
                    if (hourlyWeatherResponse != null && hourlyWeatherResponse.getHourlyWeatherItemList() != null) {
                        // Lấy danh sách thông tin thời tiết hàng giờ
                        List<HourlyWeatherItem> hourlyWeatherItemList = hourlyWeatherResponse.getHourlyWeatherItemList();

                        // Khởi tạo và thiết lập adapter cho RecyclerView
                        dailyWeatherAdapter = new DailyWeatherAdapter(hourlyWeatherItemList);
                        recyclerViewDailyWeather.setAdapter(dailyWeatherAdapter);
                    } else {
                        Toast.makeText(DailyForecastActivity.this, "Không tìm thấy thông tin thời tiết hàng giờ.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.e(TAG, "Error getting hourly weather data: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<HourlyWeatherResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error getting hourly weather data: " + t.getMessage());
            }
        });
    }

private void changeImagesWeather(String conditions) {
    if (conditions.equals("Partly Clouds") || conditions.equals("Foggy") || conditions.equals("Overcast")|| conditions.equals("Mist")|| conditions.equals("Clouds")) {
        binding.getRoot().setBackgroundResource(R.drawable.colud_background);
        binding.lottieAnimationView.setAnimation(R.raw.cloud);
    } else if (conditions.equals("Sunny")|| conditions.equals("Clear") || conditions.equals("Clear Sky")) {
        binding.getRoot().setBackgroundResource(R.drawable.sunny_background);
        binding.lottieAnimationView.setAnimation(R.raw.sun);
    } else if (conditions.equals("Light Snow")||conditions.equals("Moderate Snow")||conditions.equals("Heavy Snow")||conditions.equals("Blizzard")||conditions.equals("Snow")) {
        binding.getRoot().setBackgroundResource(R.drawable.snow_background);
        binding.lottieAnimationView.setAnimation(R.raw.snow);
    } else {
        binding.getRoot().setBackgroundResource(R.drawable.rain_background);
        binding.lottieAnimationView.setAnimation(R.raw.rain);
    }
    binding.lottieAnimationView.playAnimation();
}
}