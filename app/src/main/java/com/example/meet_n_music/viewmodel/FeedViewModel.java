package com.example.meet_n_music.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.repository.EventRepository;

import java.util.ArrayList;

public class FeedViewModel extends ViewModel {

    EventRepository eventRepository = EventRepository.getInstance();
   // MutableLiveData<ArrayList<Event>> eventMutableLiveData;

    public MutableLiveData<ArrayList<Event>> getEventMutableLiveData() {
        return EventRepository.getInstance().getAllEvents();
    }


    //public MutableLiveData<ArrayList<Event>> getEvents() {
    //    return eventRepository.getAllEvents();
    //}

    public MutableLiveData<ArrayList<Event>> getEventsWithGenre(String genre) {
        Log.d("ASK!", genre);
        MutableLiveData<ArrayList<Event>> eventGenreLiveData = new MutableLiveData<>();
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : EventRepository.getInstance().getAllEvents().getValue()) {
            Log.d("ASK", event.getGenre());
            Log.d("ASK", event.getName());
            if (event.getGenre().equals(genre)) {
                Log.d("YES", event.getName());
                events.add(event);
            }
        }
        eventGenreLiveData.setValue(events);
        return eventGenreLiveData;

    }

}
