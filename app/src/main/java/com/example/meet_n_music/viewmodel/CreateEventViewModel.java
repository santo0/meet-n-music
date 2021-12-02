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


    public MutableLiveData<Boolean> setEvent(MutableLiveData<Event> event, MutableLiveData<User> user) {
        return EventRepository.getInstance().setEvent(event, user);
    }

    public MutableLiveData<Boolean> setEventImage(String imagePath, MutableLiveData<Uri> uriMutableLiveData) {
        return ImageRepository.getInstance().uploadImage(imagePath, uriMutableLiveData);

    }

    public MutableLiveData<Boolean> setGeoLocation(String geoLocationId, MutableLiveData<EventGeographicalLocation> eventGeographicalLocationMutableLiveData) {
        return GeoLocationRepository.getInstance().setGeoLocation(geoLocationId, eventGeographicalLocationMutableLiveData);
    }

}
