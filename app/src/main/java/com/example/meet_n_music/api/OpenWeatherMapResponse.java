package com.example.meet_n_music.api;

import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.model.GeoLocationWeather;

public class OpenWeatherMapResponse {
    float avgTemp;
    String weatherDescription;

        public GeoLocationWeather getWeatherByCoords(){
            return new GeoLocationWeather(avgTemp, weatherDescription);
        }

}
