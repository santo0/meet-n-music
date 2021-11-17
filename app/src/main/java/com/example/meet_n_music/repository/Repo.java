package com.example.meet_n_music.repository;

import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_n_music.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repo {
    private static Repo instance;
    private ArrayList<Event> arrayListEvents = new ArrayList<>();
    private MutableLiveData<ArrayList<Event>> mutableLiveDataEvent = new MutableLiveData<>();

    public static Repo getInstance() {
        if (instance == null) {
            instance = new Repo();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Event>> getEvents() {
        /**
         * BE AWARE OF THIS CONDITION
         * */
        if (arrayListEvents.size() == 0){
            mutableLiveDataEvent.setValue(arrayListEvents);
        }

        loadEvents();

        Log.d("debugGetEvents", "Going to NY");
        Log.d("debugGetEvents", String.valueOf(arrayListEvents.size()));
        for (Event event : arrayListEvents) {
            Log.d("debugGetEvents", event.getName());
        }
        return mutableLiveDataEvent;
    }

    private void loadEvents() {
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<Event> list) {
                Log.d("callbackDebug", list.toString());
                mutableLiveDataEvent.postValue(list);
            }
        });

    }

    private void readData(FirebaseCallback firebaseCallback) {
        Log.d("debugFS", "Before references");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Log.d("debugFS", "Got references");
        Query query = reference.child("Events");
        Log.d("debugFS", "Got events");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListEvents.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Event event = snap.getValue(Event.class);
                    event.setId(snap.getKey());
                    Log.d("debugFS", snap.getKey());
                    arrayListEvents.add(event);
                    Log.d("debugFS", event.getId());
                    Log.d("debugFS", event.getName());
                    Log.d("debugFS", event.getDescription());
                    Log.d("debugFS", event.getCovid());
                    Log.d("debugFS", event.getGenre());

                }
                firebaseCallback.onCallback(arrayListEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("debugFS", "Cancelled");
                Log.d("debugFS", error.getMessage());
                Log.d("debugFS", error.getDetails());
            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);
    }

    private interface FirebaseCallback {
        void onCallback(ArrayList<Event> list);

    }
}
