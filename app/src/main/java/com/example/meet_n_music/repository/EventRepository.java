package com.example.meet_n_music.repository;

import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.meet_n_music.R;
import com.example.meet_n_music.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventRepository {
    private static EventRepository instance;
    private static MutableLiveData<ArrayList<Event>> eventsLiveData;


    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
            eventsLiveData = new MutableLiveData<>();
            eventsLiveData.setValue(new ArrayList<>());
        }
        return instance;
    }


    public void setEvent(MutableLiveData<Event> event) {
//        Event event = new Event(eventNameString, eventDescriptionString, locationString, startEventDateString, eventGenre, eventCovidRestrictionString);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Events");
        String eventId = dbRef.push().getKey();
        event.getValue().setId(eventId);
        Log.d("EventRepository", "Before: " + eventId);
        dbRef.child(eventId).setValue(event.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("EventRepository", "After: " + eventId);
                     event.setValue(event.getValue());//notify UI completed
                } else {
                    Log.d("EventRepository", "NULL!!!");
                    event.setValue(null);//notify UI not completed
                }
            }
        });
    }


    public MutableLiveData<ArrayList<Event>> getAllEvents() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Events");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Event> events = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Event event = snap.getValue(Event.class);
                    event.setId(snap.getKey());
                    Log.d("debugFS", snap.getKey());
                    Log.d("debugFS", event.getId());
                    Log.d("debugFS", event.getName());
                    Log.d("debugFS", event.getDescription());
                    Log.d("debugFS", event.getCovid());
                    Log.d("debugFS", event.getGenre());
                    events.add(event);
                }
                eventsLiveData.setValue(events);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("debugFS", "Cancelled");
                Log.d("debugFS", error.getMessage());
                Log.d("debugFS", error.getDetails());
            }
        };

        query.addValueEventListener(valueEventListener);

        return eventsLiveData;
    }

}
