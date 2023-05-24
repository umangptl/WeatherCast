package com.example.weathercast.Modal;

public class WeatherCast {
    private String city;
    private String dt;
    private int temp;
    private String icon;
    private String description;
    private int temp_max;
    private int temp_min;

    private String sunrise;
    private String sunset;
    private int wind_speed;
    private int wind_degree;
    private int humidity;
    private int visibility;

    public WeatherCast(String city, String dt, int temp, String icon, String description, int temp_min, int temp_max, String sunrise,
                       String sunset, int wind_speed, int wind_degree, int humidity, int visibility) {
        this.city = city;
        this.dt = dt;
        this.temp = temp;
        this.icon = icon;
        this.description = description;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.wind_speed = wind_speed;
        this.wind_degree = wind_degree;
        this.humidity = humidity;
        this.visibility = visibility;
    }
    public String getCity() {
        return city;
    }

    public String getDt() {
        return dt;
    }

    public int getTemp() {
        return temp;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public int getTemp_min() {
        return temp_min;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public int getWind_speed() {
        return wind_speed;
    }

    public int getWind_degree() {
        return wind_degree;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getVisibility() {
        return visibility;
    }

}
