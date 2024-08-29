package com.example.appweatherforecast;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.appweatherforecast.databinding.ActivityMainBinding;
import com.example.appweatherforecast.model.Main;
import com.example.appweatherforecast.model.Sys;
import com.example.appweatherforecast.model.Weather;
import com.example.appweatherforecast.model.WeatherApiService;
import com.example.appweatherforecast.model.WeatherResponse;
import com.example.appweatherforecast.model.Wind;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean errorDialogShown = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Người dùng đã nhấn nút Enter, query chứa nội dung tìm kiếm
                String cityName = query;

                FetchWeather(cityName);
                // Lưu tên thành phố vào SharedPreferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CITY_NAME_KEY", cityName);
                editor.apply();

                // Gửi broadcast thông báo cho tiện ích widget cập nhật với tên thành phố mới
                Intent updateIntent = new Intent(NewAppWidget.ACTION_UPDATE_WEATHER);
                updateIntent.putExtra("CITY_NAME", cityName);
                sendBroadcast(updateIntent);
                return true; // Trả về true để xác nhận rằng sự kiện đã được xử lý.
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Đây là nơi bạn có thể xử lý sự thay đổi văn bản trong searchView khi người dùng nhập
                return false;
            }
        });

        binding.temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityname = binding.searchView.getQuery().toString();
                String temp =binding.temp.getText().toString();
                String weather =binding.weather.getText().toString();
                String condition = binding.condition.getText().toString();
                Intent intent = new Intent(MainActivity.this, HourlyForecastActivity.class);
                intent.putExtra("CITY_NAME", cityname);
                intent.putExtra("TEMP", temp);
                intent.putExtra("WEATHER", weather);
                intent.putExtra("CONDITION", condition);
                startActivity(intent);
            }
        });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityListActivity.class);
                startActivity(intent);
            }
        });
        binding.cityname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý khi người dùng ấn vào cityname
                // Mở bản đồ và hiển thị vị trí được chọn
                openGoogleMapsAtSelectedLocation();
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String currentDate = format.format(new Date());
        binding.date.setText(currentDate);
        SimpleDateFormat fd = new SimpleDateFormat("EEEE", Locale.getDefault());
        String Day = fd.format(new Date());
        binding.day.setText(Day);

        String cityName = getIntent().getStringExtra("CT_NAME");
        binding.searchView.setQuery(cityName,true);
//        FetchWeather("london");
    }

    private void FetchWeather(String cityname) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/") // Thay thế bằng URL của API thời tiết bạn đang sử dụng
                .build();

        WeatherApiService weatherApiService = retrofit.create(WeatherApiService.class);

        Call<WeatherResponse> weatherResponseCall = getWeatherResponseCall(cityname, weatherApiService);

        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    Main to = weatherResponse.getMain();
                    Wind wi = weatherResponse.getWind();
                    Sys s = weatherResponse.getSys();
                    List<Weather> description = weatherResponse.getWeather();

                    binding.temp.setText(String.valueOf(to.getTemp())+"°C");
                    binding.max.setText(String.valueOf("Max :"+to.getTemp_max())+"°C");
                    binding.min.setText(String.valueOf("Min :"+to.getTemp_min())+"°C");
                    binding.humidity.setText(String.valueOf(to.getHumidity())+"%");
                    binding.sea.setText(String.valueOf(to.getPressure()+"hPa"));

                    binding.wind.setText(String.valueOf(wi.getSpeed())+"m/s");
                    
                    long sunrise = s.getSunrise(); // Lấy thời gian mặt trời mọc (dưới dạng timestamp)
                    long sunset = s.getSunset();
                    binding.sunrise.setText(time(sunrise));
                    binding.sunset.setText(time(sunset));
                    binding.cityname.setText(weatherResponse.getName());

                    for (Weather data : description ) {
                        binding.weather.setText(data.getDescription());
                        binding.condition.setText(data.getMain());
                    }
                    String conditionText = binding.condition.getText().toString();
                    changeImagesWeather(conditionText);
                } else {
                    if (!errorDialogShown) {
                        showErrorDialog();
                        errorDialogShown = true; // Đánh dấu rằng hộp thoại đã được hiển thị
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "Error getting weather data: " + t.getMessage());
            }
        });
    }

    private static Call<WeatherResponse> getWeatherResponseCall(String cityname, WeatherApiService weatherApiService) {
        Call<WeatherResponse> weatherResponseCall = weatherApiService.getWeatherData(cityname, "4e81285e1eca7bd5310a67ee1ed41986", "metric");
        return weatherResponseCall;
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
    private String time(Long timestamp){
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return ft.format(new Date(timestamp*1000));
    }
    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ERROR!!!");
        builder.setMessage("Không tìm thấy thành phố. Vui lòng nhập lại tên thành phố hợp lệ.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng hộp thoại
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                errorDialogShown = false;
                // Xóa văn bản nhập trong SearchView để người dùng có thể nhập tên thành phố mới
//                binding.searchView.requestFocus();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openGoogleMapsAtSelectedLocation() {
        // Create a Uri with the location coordinates
        Uri gmmIntentUri = Uri.parse("geo:12.345,67.890"); // Replace with the actual latitude and longitude

        // Create an Intent to open the Google Maps app
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps"); // Specify the Google Maps package

        // Check if the Google Maps app is installed
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Handle the case where Google Maps is not installed
            // You can open a web browser or prompt the user to install Google Maps
            // Example: Open Google Maps in a web browser
            Uri webUri = Uri.parse("https://www.google.com/maps?q=12.345,67.890"); // Replace with the actual coordinates
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
            startActivity(webIntent);
        }
    }
}