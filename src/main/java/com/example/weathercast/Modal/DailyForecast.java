package com.example.weathercast.Modal;

public class DailyForecast {
    private String dt;
    private String icon;
    private int temp_max;
    private int temp_min;

    public DailyForecast(String dt, String icon, int temp_max, int temp_min) {
        this.dt = dt;
        this.icon = icon;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(int temp_max) {
        this.temp_max = temp_max;
    }
    public int getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(int temp_min) {
        this.temp_min = temp_min;
    }
}
