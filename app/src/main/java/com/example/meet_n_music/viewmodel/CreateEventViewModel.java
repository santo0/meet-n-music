package com.example.meet_n_music.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.repository.EventRepository;


public class CreateEventViewModel extends ViewModel {

    public void setEvent(MutableLiveData<Event> event, MutableLiveData<Uri> image) {
        EventRepository.getInstance().setEvent(event);
    }

}
