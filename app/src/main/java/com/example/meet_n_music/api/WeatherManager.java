package com.example.meet_n_music.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.meet_n_music.model.Daily;
import com.example.meet_n_music.model.Example;
import com.example.meet_n_music.model.GeoLocationWeather;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherManager {
    private static final String TAG = "WeatherManager";

    public static MutableLiveData<GeoLocationWeather> getWeatherByCoords(double lat, double lng){
        MutableLiveData<GeoLocationWeather> geoLocationWeatherMutableLiveData = new MutableLiveData<>();
        Log.d(TAG, "getting weather");

        OpenWeatherMapApi owmApi = WeatherServiceGenerator.getOwmApi();

        Log.d(TAG, "Sending weather api request");
        Call<Example> callApi = owmApi.getWeatherByCoords(lat, lng, "hourly,current,minutely", "metric", "0962e23c81a8c028a66ac1eeacf564a1");
        callApi.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d(TAG, "onResponse: " + response.toString());
                if(response.body() != null){
                    Log.d(TAG, "body not null!");
                    Log.d(TAG, response.body().toString());
                    Example weather =response.body();
                    try {
                        Log.d(TAG, Double.toString(weather.getLat()));
                    }catch (NullPointerException e){
                        Log.d(TAG, "SE JODIO LA WEAAAAAAAAAAAAAAAA");
                    }


                }else {
                    Log.d(TAG, "body iS NULL!!!!!!");
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onFailure");
                Log.d(TAG, t.toString());
                Log.d(TAG, t.getMessage());
                Log.d(TAG, t.getLocalizedMessage());
            }
        });


        return geoLocationWeatherMutableLiveData;



    }
}
