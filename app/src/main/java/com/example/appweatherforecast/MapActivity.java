package com.example.appweatherforecast;

//import android.os.Bundle;
//import androidx.fragment.app.FragmentActivity;
//
//import com.example.appweatherforecast.Mapmodel.MapApiService;
//import com.example.appweatherforecast.Mapmodel.MapWeatherResponse;
//import com.example.appweatherforecast.R;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    // ... (các khai báo khác)
//    private SupportMapFragment mMapFragment;
//    private String apiKey = "4e81285e1eca7bd5310a67ee1ed41986"; // Thay thế bằng API key của bạn
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//
//        // Initialize mMapFragment
//        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
//
//        // ... (other code)
//    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        GoogleMap mMap = googleMap;
//
//        // Lấy latitude và longitude của vị trí từ mMap (ví dụ: latitude = 37.7749, longitude = -122.4194)
//
//        double latitude = 37.7749;
//        double longitude = -122.4194;
//        // Customize your map (e.g., add markers, set initial location, etc.)
//        LatLng location = new LatLng(37.7749, -122.4194);
//        mMap.addMarker(new MarkerOptions().position(location).title("Marker Title"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
//        // Thực hiện cuộc gọi API OpenWeatherMap để lấy thông tin thời tiết
//        MapApiService apiService = createApiService();
//        Call<MapWeatherResponse> call = apiService.getWeatherData(latitude, longitude, apiKey, "metric");
//
//        call.enqueue(new Callback<MapWeatherResponse>() {
//            @Override
//            public void onResponse(Call<MapWeatherResponse> call, Response<MapWeatherResponse> response) {
//                if (response.isSuccessful()) {
//                    MapWeatherResponse mapweatherResponse = response.body();
//
//                    // Xử lý dữ liệu thời tiết và cập nhật giao diện người dùng
//                    updateUIWithWeatherData(mapweatherResponse);
//                } else {
//                    // Xử lý lỗi
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MapWeatherResponse> call, Throwable t) {
//                // Xử lý lỗi
//            }
//        });
//    }
//
//    private MapApiService createApiService() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.openweathermap.org/data/2.5/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        return retrofit.create(MapApiService.class);
//    }
//
//    private void updateUIWithWeatherData(MapWeatherResponse weatherResponse) {
//        // Cập nhật giao diện người dùng với thông tin thời tiết từ weatherResponse
//        // Ví dụ: Hiển thị nhiệt độ, tình trạng thời tiết, v.v.
//    }
//}

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // You can add markers, configure map settings, and handle map events here.
    }
}


