package com.example.weathercast.Modal;
public class HourlyForecast {
    private String dt;
    private String icon;
    private int temp;

    public HourlyForecast(String dt, String icon, int temp) {
        this.dt = dt;
        this.icon = icon;
        this.temp = temp;
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

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

}
