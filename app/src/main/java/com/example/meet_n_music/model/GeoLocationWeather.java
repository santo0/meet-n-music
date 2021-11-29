package com.example.meet_n_music.model;

public class GeoLocationWeather {
    float avgTemp;
    String weatherDescription;

    public GeoLocationWeather(float avgTemp, String weatherDescription) {
        this.avgTemp = avgTemp;
        this.weatherDescription = weatherDescription;
    }
}
