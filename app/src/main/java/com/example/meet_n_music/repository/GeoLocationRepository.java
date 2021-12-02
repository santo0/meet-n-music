package com.example.meet_n_music.repository;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GeoLocationRepository {

    private static GeoLocationRepository instance;
    private static final String TAG = "GeoLocationRepository";

    public static GeoLocationRepository getInstance() {
        if (instance == null) {
            instance = new GeoLocationRepository();
        }
        return instance;
    }


    public MutableLiveData<Boolean> setGeoLocation(String geoId, MutableLiveData<EventGeographicalLocation> eventGeographicalLocationMutableLiveData) {
        MutableLiveData<Boolean> setGeoLocationState = new MutableLiveData<>();
        FirebaseDatabase.getInstance().getReference("EventGeographicalLocation").child(geoId).setValue(eventGeographicalLocationMutableLiveData.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "success on setting location " + geoId);
                    setGeoLocationState.setValue(true);
//                    eventGeographicalLocationMutableLiveData.setValue(eventGeographicalLocationMutableLiveData.getValue());//notify UI completed
                } else {
                    Log.d(TAG, "failed on setting location " + geoId);
                    setGeoLocationState.setValue(false);
  //                  eventGeographicalLocationMutableLiveData.setValue(null);//notify UI not completed
                }
            }
        });
        return setGeoLocationState;
    }

    public MutableLiveData<ArrayList<Pair<String, EventGeographicalLocation>>> getAllEventGeoLocations(){
        MutableLiveData<ArrayList<Pair<String, EventGeographicalLocation>>> geoLocMutableLiveData = new MutableLiveData<>();
        //Gets id of event and geolocation
        FirebaseDatabase.getInstance().getReference("EventGeographicalLocation").get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        ArrayList<Pair<String, EventGeographicalLocation>> geoLocations = new ArrayList<>();
                        for(DataSnapshot child: dataSnapshot.getChildren()){
                            EventGeographicalLocation geoLoc = child.getValue(EventGeographicalLocation.class);
                            geoLocations.add(new Pair<>(child.getKey(), geoLoc));
                        }

                        geoLocMutableLiveData.setValue(geoLocations);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        return geoLocMutableLiveData;
    }

}
