package com.example.meet_n_music.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.repository.Repo;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    MutableLiveData<ArrayList<Event>> events;


    public void init() {
        if (events != null){
            return;
        }
        events = Repo.getInstance().getEvents();
    }

    public LiveData<ArrayList<Event>> getEvents() {
        return events;
    }
}
