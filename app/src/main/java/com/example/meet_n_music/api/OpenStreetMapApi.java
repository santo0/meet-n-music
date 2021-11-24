package com.example.meet_n_music.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenStreetMapApi {

    @GET("/search")
    Call<List<OpenStreetMapResponse>> getLocationByName(@Query("q") String locationName, @Query("format") String responseFormat, @Query("limit") int responseLimit);
}
