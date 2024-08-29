package com.example.appweatherforecast.Hourlymodel;

public class WeatherHourly {
    private String Htime;
    private String Htemp;
    private String Icon;
    private String Hwindsped;

    public WeatherHourly(String htime, String htemp, String icon, String hwindsped) {
        Htime = htime;
        Htemp = htemp;
        Icon = icon;
        Hwindsped = hwindsped;
    }

    public String getHtime() {
        return Htime;
    }

    public void setHtime(String htime) {
        Htime = htime;
    }

    public String getHtemp() {
        return Htemp;
    }

    public void setHtemp(String htemp) {
        Htemp = htemp;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getHwindsped() {
        return Hwindsped;
    }

    public void setHwindsped(String hwindsped) {
        Hwindsped = hwindsped;
    }
}
