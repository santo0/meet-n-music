package com.example.meet_n_music.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.EventRepository;
import com.example.meet_n_music.repository.GeoLocationRepository;
import com.example.meet_n_music.repository.ImageRepository;

public class EditEventViewModel extends ViewModel {


    public void modifyEventDetails(MutableLiveData<Event> event) {
        EventRepository.getInstance().modifyEvent(event);
    }


    public void modifyEventPicture(String imagePath, MutableLiveData<Uri> uriMutableLiveData) {
        ImageRepository.getInstance().changeImage(imagePath, uriMutableLiveData);

    }

    public void modifyEventGeoLocation(String geoLocationId, MutableLiveData<EventGeographicalLocation> eventGeographicalLocationMutableLiveData) {
        GeoLocationRepository.getInstance().setGeoLocation(geoLocationId, eventGeographicalLocationMutableLiveData);
    }

    public MutableLiveData<Event> getEventById(String eventId){
        return EventRepository.getInstance().getEventById(eventId);
    }
}
