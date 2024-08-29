
package com.example.appweatherforecast.model;




public class Main {
    public double temp;
    public double fells_like;
    public double temp_min;
    public double temp_max;

    public int pressure;
    public int humidity;

    public Main(double temp, double fells_like, double temp_min, double temp_max, int pressure, int humidity) {
        this.temp = temp;
        this.fells_like = fells_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFells_like() {
        return fells_like;
    }

    public void setFells_like(double fells_like) {
        this.fells_like = fells_like;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
