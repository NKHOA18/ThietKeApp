package com.example.appweatherforecast.Dailymodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweatherforecast.Hourlymodel.HourlyWeatherItem;
import com.example.appweatherforecast.Hourlymodel.WeatherHour;
import com.example.appweatherforecast.R; // Đảm bảo bạn sử dụng đúng ID của ứng dụng của bạn
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder> {
    private List<HourlyWeatherItem> dailyWeatherItemList;
    private List<HourlyWeatherItem> displayedItems ;

    public DailyWeatherAdapter(List<HourlyWeatherItem> dailyWeatherItemList) {
        this.dailyWeatherItemList = dailyWeatherItemList;
        // Khởi tạo danh sách hiển thị và chọn các phần tử để hiển thị (ví dụ: mỗi 7 phần tử)
        displayedItems = new ArrayList<>();
        for (int i = 0; i < dailyWeatherItemList.size(); i += 8) {
            displayedItems.add(dailyWeatherItemList.get(i));
        }
    }

    @NonNull
    @Override
    public DailyWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_weather, parent, false);
        return new DailyWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherViewHolder holder, int position) {
            HourlyWeatherItem dailyWeatherItem = displayedItems.get(position);

            // Format và hiển thị ngày tháng
            long timestamp = dailyWeatherItem.getTimestamp(); // Chuyển đổi từ giây thành mili giây
            String formattedDate = formatDate(timestamp);
            String dayOfWeek = getDayOfWeek(timestamp);

            holder.tvDay.setText(dayOfWeek);
            holder.tvDateTime.setText(formattedDate);
            holder.tvTempMax.setText(String.format(Locale.getDefault(), "%.1f°C", dailyWeatherItem.getMainHour().getTempmax()));
            holder.tvTempMin.setText(String.format(Locale.getDefault(), "%.1f°C", dailyWeatherItem.getMainHour().getTempmin()));

            // Lấy danh sách các mô tả thời tiết từ danh sách weatherHourList của hourlyWeatherItem
            List<WeatherHour> weatherDaiList = dailyWeatherItem.getWeatherHourList();

            if (weatherDaiList != null && !weatherDaiList.isEmpty()) {
                // Lấy mô tả thời tiết đầu tiên từ danh sách (thường chỉ có một mô tả)
                WeatherHour firstWeatherHour = weatherDaiList.get(0);

                // Lấy mô tả thời tiết từ firstWeatherHour
                String iconCode = firstWeatherHour.getIcon();

                Picasso.get().load("https://openweathermap.org/img/wn/" + getWeatherIconName(iconCode)).into(holder.tvIcon);
                // Sử dụng description ở đây nếu cần
            }
    }

    @Override
    public int getItemCount() {
        return displayedItems.size();
    }

    public class DailyWeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvDateTime, tvTempMax, tvTempMin;
        ImageView tvIcon;

        public DailyWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.ITDay);
            tvDateTime = itemView.findViewById(R.id.ITdatetime);
            tvTempMax = itemView.findViewById(R.id.ITtempmax);
            tvTempMin = itemView.findViewById(R.id.ITtempmin);
            tvIcon = itemView.findViewById(R.id.ITIcon);
        }
    }
    public String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000)); // Chuyển đổi từ giây thành mili giây
    }

    public String getDayOfWeek(long timestamp) {
        SimpleDateFormat ssdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return ssdf.format(new Date(timestamp * 1000)); // Chuyển đổi từ giây thành mili giây
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

