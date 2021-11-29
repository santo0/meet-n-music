package com.example.meet_n_music.api;

import com.example.meet_n_music.model.Daily;
import com.example.meet_n_music.model.Example;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapApi {

//https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=hourly,current,minutely&units=metric

    @GET("/data/2.5/onecall")
    Call<Example> getWeatherByCoords(@Query("lat") double lat,
                                     @Query("lon") double lng,
                                     @Query("exclude") String exclude,
                                     @Query("units") String units,
                                     @Query("appid") String apiKey);

}
