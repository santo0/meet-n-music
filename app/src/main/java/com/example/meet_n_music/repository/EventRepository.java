package com.example.meet_n_music.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventRepository {
    private static final String TAG = "EventRepository";
    private static EventRepository instance;
    private static MutableLiveData<ArrayList<Event>> eventsLiveData;


    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
            eventsLiveData = new MutableLiveData<>(null);
        }
        return instance;
    }


    public MutableLiveData<Event> getEventById(String eventId){
        MutableLiveData<Event> eventMutableLiveData = new MutableLiveData<>();
        Log.d("EventRepository", "getting event with id " + eventId);
        FirebaseDatabase.getInstance().getReference().child("Events").child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Event event = task.getResult().getValue(Event.class);
                    if (event != null) {
                        Log.d("debugFS", event.getName());
                        Log.d("debugFS", event.getDescription());
                        Log.d("debugFS", event.getStartDate());
                        Log.d("debugFS", event.getLocation());
                        eventMutableLiveData.setValue(event);
                    } else {
                        Log.d("EventRepository", "event with id " + eventId + " appears to be null!");
                        eventMutableLiveData.setValue(null);
                    }
                } else {
                    Log.d("EventRepository", "task was not successful when getting event with id " + eventId);
                    eventMutableLiveData.setValue(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventMutableLiveData.setValue(null);
            }
        });
        return eventMutableLiveData;
    }


    public MutableLiveData<Boolean> modifyEvent(MutableLiveData<Event> event) {
        MutableLiveData<Boolean> modifyEventState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Events");
        Log.d("EventRepository", "Before: " + event.getValue().getId());
        dbRef.child(event.getValue().getId()).setValue(event.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("EventRepository", "Modified event " + event.getValue().getId());
                    modifyEventState.setValue(true);
                    //event.setValue(event.getValue());//notify UI completed
                } else {
                    Log.d("EventRepository", "Can't modify event!!!");
                    //event.setValue(null);//notify UI not completed
                    modifyEventState.setValue(false);
                }
            }
        });
        return modifyEventState;
    }


    public MutableLiveData<Boolean> setEvent(MutableLiveData<Event> event, MutableLiveData<User> user) {
        MutableLiveData<Boolean> setEventState = new MutableLiveData<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Events");
        String eventId = dbRef.push().getKey();
        event.getValue().setId(eventId);
        Log.d(TAG, "Set event with id " + eventId);
        dbRef.child(eventId).setValue(event.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Crated event with id " + eventId);
                    FirebaseDatabase.
                            getInstance()
                            .getReference("Users")
                            .child(user.getValue().id)
                            .child("ownedEventsIds")
                            .child(eventId)
                            .setValue(eventId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "User " + user.getValue().id + " is owner of " + eventId);
                                setEventState.setValue(true);
                            }   else{
                                Log.e(TAG, "User "+ user.getValue().id +" is not owner of " + eventId);
                                setEventState.setValue(false);
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Can't create event!!!");
                    setEventState.setValue(false);
                }
            }
        });
        return setEventState;
    }


    public MutableLiveData<ArrayList<Event>> getAllEvents() {
        Log.i(TAG, "getting all events");
        if(eventsLiveData.getValue() == null) {
            Log.i(TAG, "set value event listener to firebase-Event");
            FirebaseDatabase.getInstance().getReference("Events").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Event> events = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Event event = snap.getValue(Event.class);
                        if (event != null) {
                            event.setId(snap.getKey());
                            Log.d(TAG, "child with id " + snap.getKey());
                            Log.d(TAG, event.toString());
                            events.add(event);
                        } else {
                            Log.e(TAG, "Event with id " + snap.getKey() + " is not valid");
                        }
                    }
                    eventsLiveData.setValue(events);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "getting all events is cancelled");
                    Log.e(TAG, error.getMessage());
                    Log.e(TAG, error.getDetails());
                }
            });
        }
        return eventsLiveData;
    }

}
