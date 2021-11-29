package com.example.meet_n_music.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherServiceGenerator {
    private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofit = retrofitBuilder.build();

    private static OpenWeatherMapApi owmApi = retrofit.create(OpenWeatherMapApi.class);

    public static OpenWeatherMapApi getOwmApi() {
        return owmApi;
    }

}
