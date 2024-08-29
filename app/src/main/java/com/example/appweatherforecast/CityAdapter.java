package com.example.appweatherforecast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.appweatherforecast.model.WeatherApiService;
import com.example.appweatherforecast.model.WeatherResponse;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> cityList;
    private boolean errorDialogShown = false;
    public CityAdapter(Context context, List<String> cityList) {
        super(context, R.layout.item_list_city, cityList);
        this.context = context;
        this.cityList = cityList;
    }

    static class ViewHolder {
        TextView locationTextView;
        TextView datetimeTextView;
        ImageView iconImageView;
        TextView tempMaxTextView;
        TextView tempMinTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_list_city, parent, false);

            holder = new ViewHolder();
            holder.locationTextView = convertView.findViewById(R.id.LClocation);
            holder.datetimeTextView = convertView.findViewById(R.id.LCdatetime);
            holder.iconImageView = convertView.findViewById(R.id.LCIcon);
            holder.tempMaxTextView = convertView.findViewById(R.id.LCtempmax);
            holder.tempMinTextView = convertView.findViewById(R.id.LCtempmin);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String cityName = cityList.get(position);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/") // Thay thế bằng URL của API OpenWeatherMap
                .build();

        WeatherApiService weatherApiService = retrofit.create(WeatherApiService.class);

        Call<WeatherResponse> weatherResponseCall = weatherApiService.getWeatherData(cityName, "4e81285e1eca7bd5310a67ee1ed41986", "metric");

        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        updateViewHolder(holder, cityName, weatherResponse);
                    }
                } else {
                    if (!errorDialogShown) {
                        showErrorDialog();
                        errorDialogShown = true; // Đánh dấu rằng hộp thoại đã được hiển thị
                        removeInvalidCity(cityName);
                    }
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                handleApiError();
            }
        });
        return convertView;
    }

    private void updateViewHolder(ViewHolder holder, String cityName, WeatherResponse weatherResponse) {
        long timestamp = weatherResponse.getDt();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedTime = sdf.format(new Date(timestamp*1000));

        holder.locationTextView.setText(cityName);
        holder.datetimeTextView.setText(formattedTime);
        holder.tempMaxTextView.setText(weatherResponse.getMain().getTemp_max() + "°C");
        holder.tempMinTextView.setText(weatherResponse.getMain().getTemp_min() + "°C");

        String iconCode = weatherResponse.getWeather().get(0).getIcon();
        Picasso.get().load("https://openweathermap.org/img/wn/" + getWeatherIconName(iconCode)).into(holder.iconImageView);
    }

    private void handleApiError() {
    }
    public void addCity(String cityName) {
        cityList.add(cityName);
        notifyDataSetChanged();
    }
    public void removeInvalidCity(String cityName) {
        cityList.remove(cityName);
        notifyDataSetChanged();
    }
    public String getWeatherIconName(String iconCode) {
        switch (iconCode) {
            case "01d":
                return "01d@2x.png"; // Ví dụ, bạn có thể sử dụng tên tương ứng với icon.
            case "01n":
                return "01n@2x.png";
            case "02d":
                return "02d@2x.png";
            case "02n":
                return "02n@2x.png";
            case "03d":
            case "03n":
                return "03d@2x.png";
            case "04d":
            case "04n":
                return "04d@2x.png";
            case "09d":
            case "09n":
                return "09d@2x.png";
            case "10d":
            case "10n":
                return "10d@2x.png";
            case "11d":
            case "11n":
                return "11d@2x.png";
            case "13d":
            case "13n":
                return "13d@2x.png";
            case "50d":
            case "50n":
                return "50d@2x.png";
            default:
                return "10d@2x.png"; // Trường hợp mặc định nếu không tìm thấy mã icon.
        }
    }
    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
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

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
