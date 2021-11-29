package com.example.meet_n_music.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_n_music.model.EventGeographicalLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeoLocationRepository {

    private static GeoLocationRepository instance;
    private static final String TAG = "GeoLocationRepository";

    public static GeoLocationRepository getInstance() {
        if (instance == null) {
            instance = new GeoLocationRepository();
        }
        return instance;
    }


    public void setGeoLocation(String geoId, MutableLiveData<EventGeographicalLocation> eventGeographicalLocationMutableLiveData) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("EventGeographicalLocation");
        dbRef.child(geoId).setValue(eventGeographicalLocationMutableLiveData.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    eventGeographicalLocationMutableLiveData.setValue(eventGeographicalLocationMutableLiveData.getValue());//notify UI completed
                } else {
                    Log.d(TAG, "NULL!!!");
                    eventGeographicalLocationMutableLiveData.setValue(null);//notify UI not completed
                }
            }
        });

    }

}
