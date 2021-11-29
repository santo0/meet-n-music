package com.example.meet_n_music.api;

import com.example.meet_n_music.model.EventGeographicalLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoLocationServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static OpenStreetMapApi osmApi = retrofit.create(OpenStreetMapApi.class);

    public static OpenStreetMapApi getOsmApi() {
        return osmApi;
    }


}




