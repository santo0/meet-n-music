package com.example.meet_n_music.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_n_music.model.Event;
import com.example.meet_n_music.model.EventGeographicalLocation;
import com.example.meet_n_music.model.User;
import com.example.meet_n_music.repository.EventRepository;
import com.example.meet_n_music.repository.GeoLocationRepository;
import com.example.meet_n_music.repository.ImageRepository;


public class CreateEventViewModel extends ViewModel {


    public void setEvent(MutableLiveData<Event> event, MutableLiveData<User> user) {
        EventRepository.getInstance().setEvent(event, user);
    }

    public void setEventImage(String imagePath, MutableLiveData<Uri> uriMutableLiveData) {
        ImageRepository.getInstance().uploadImage(imagePath, uriMutableLiveData);

    }

    public void setGeoLocation(String geoLocationId, MutableLiveData<EventGeographicalLocation> eventGeographicalLocationMutableLiveData) {
        GeoLocationRepository.getInstance().setGeoLocation(geoLocationId, eventGeographicalLocationMutableLiveData);
    }

}
