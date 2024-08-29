package com.example.appweatherforecast;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appweatherforecast.R;

import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends AppCompatActivity {
    private SearchView searchView;
    private ListView cityListView;
    private CityAdapter cityAdapter;
    private List<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_city);
        // Liên kết các thành phần giao diện với mã nguồn Java
        searchView = findViewById(R.id.search);
        cityListView = findViewById(R.id.listcity);
        // Khởi tạo danh sách thành phố và adapter
        cityList = new ArrayList<>();
        cityAdapter = new CityAdapter(this, cityList);
        // Thiết lập adapter cho ListView
        cityListView.setAdapter(cityAdapter);
        // Thiết lập sự kiện tìm kiếm cho SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn "Enter" trên bàn phím
                String cityName = query.trim();
                if (!cityName.isEmpty()) {
                    cityAdapter.addCity(cityName);
                }
                // Xóa nội dung trong SearchView sau khi thêm thành phố
                searchView.setQuery("", false);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng thay đổi nội dung tìm kiếm (không cần thiết cho việc thêm thành phố)
                return false;
            }
        });
    }
}
