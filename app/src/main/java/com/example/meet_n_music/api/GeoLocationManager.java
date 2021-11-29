package com.example.meet_n_music.api;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.meet_n_music.BuildConfig;
import com.example.meet_n_music.R;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.google.maps.GeoApiContext;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoLocationManager {


    private static final String TAG = "GeoLocationManager";

    public static MutableLiveData<EventGeographicalLocation> geoLocate(String locationName) {
        MutableLiveData<EventGeographicalLocation> geographicalLocationLiveData = new MutableLiveData<>();
        Log.d(TAG, "geoLocate: geolocating");

        OpenStreetMapApi osmApi = GeoLocationServiceGenerator.getOsmApi();

     //   String searchString = locationInput.getText().toString();
        if (!locationName.isEmpty()) {
            Log.d(TAG, "Sending api request");
            Call<List<OpenStreetMapResponse>> callApi = osmApi.getLocationByName(locationName, "json", 1);
            Log.d(TAG, callApi.request().toString());
            callApi.enqueue(new Callback<List<OpenStreetMapResponse>>() {
                @Override
                public void onResponse(Call<List<OpenStreetMapResponse>> call, Response<List<OpenStreetMapResponse>> response) {
                    Log.d(TAG, "onResponse");
                    if (response.body() != null) {
                        if(  0 < response.body().size() ) {
                            EventGeographicalLocation eventGeographicalLocation = response.body().get(0).getLocationByName();
                            Log.d(TAG, eventGeographicalLocation.getName());
                            Log.d(TAG, Double.toString(eventGeographicalLocation.getLat()));
                            Log.d(TAG, Double.toString(eventGeographicalLocation.getLng()));
                            geographicalLocationLiveData.setValue(eventGeographicalLocation);
                            Log.d(TAG, "geoLocate: found a location: " + eventGeographicalLocation.getName());
                        }else{
                            geographicalLocationLiveData.setValue(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OpenStreetMapResponse>> call, Throwable t) {
                    Log.d(TAG, t.toString());
                    Log.d(TAG, t.getMessage());
                    Log.d(TAG, t.getLocalizedMessage());
                    Log.d(TAG, "geoLocate: did not found a location");
                    geographicalLocationLiveData.setValue(null);
                }
            });
        }
        return geographicalLocationLiveData;
    }

}
