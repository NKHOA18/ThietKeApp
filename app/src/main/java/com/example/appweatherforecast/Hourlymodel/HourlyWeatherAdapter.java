package com.example.appweatherforecast.Hourlymodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweatherforecast.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<com.example.appweatherforecast.Hourlymodel.HourlyWeatherAdapter.HourlyWeatherViewHolder>{

    private List<HourlyWeatherItem> hourlyWeatherItemList;

    public HourlyWeatherAdapter(List<HourlyWeatherItem> hourlyWeatherItemList) {
        this.hourlyWeatherItemList = hourlyWeatherItemList;
    }

    @NonNull
    @Override
    public HourlyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_hf_item, parent, false);
        return new HourlyWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherViewHolder holder, int position) {
        HourlyWeatherItem hourlyWeatherItem = hourlyWeatherItemList.get(position);

        // Format và hiển thị thời gian (dt) dưới dạng giờ và phút
        long timestamp = hourlyWeatherItem.getTimestamp() * 1000; // Chuyển đổi từ giây thành mili giây
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTime = sdf.format(new Date(timestamp));

        holder.tvTime.setText(formattedTime);
        holder.tvTemperature.setText(String.format(Locale.getDefault(), "%.1f°C", hourlyWeatherItem.getMainHour().getTemphour()));
        holder.tvWindspeed.setText(String.valueOf(hourlyWeatherItem.getWindHour().getSpeed()) + "m/s");

        // Lấy danh sách các mô tả thời tiết từ danh sách weatherHourList của hourlyWeatherItem
        List<WeatherHour> weatherHourList = hourlyWeatherItem.getWeatherHourList();

        if (weatherHourList != null && !weatherHourList.isEmpty()) {
            // Lấy mô tả thời tiết đầu tiên từ danh sách (thường chỉ có một mô tả)
            WeatherHour firstWeatherHour = weatherHourList.get(0);

            // Lấy mô tả thời tiết từ firstWeatherHour
            String iconCode = firstWeatherHour.getIcon();

            Picasso.get().load("https://openweathermap.org/img/wn/" + getWeatherIconName(iconCode)).into(holder.tvCondition);
            // Sử dụng description ở đây nếu cần
        }
    }


    @Override
    public int getItemCount() {
        return hourlyWeatherItemList.size();
    }

    public class HourlyWeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTemperature, tvWindspeed;
        ImageView tvCondition;

        public HourlyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.TVtime);
            tvTemperature = itemView.findViewById(R.id.TVtemp);
            tvCondition = itemView.findViewById(R.id.TVcondition);
            tvWindspeed = itemView.findViewById(R.id.TVwind);
        }
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

}



