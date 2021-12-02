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


    public MutableLiveData<Boolean> modifyEventDetails(MutableLiveData<Event> event) {
        return EventRepository.getInstance().modifyEvent(event);
    }


    public MutableLiveData<Boolean> modifyEventPicture(String imagePath, MutableLiveData<Uri> uriMutableLiveData) {
        return ImageRepository.getInstance().changeImage(imagePath, uriMutableLiveData);

    }

    public MutableLiveData<Boolean> modifyEventGeoLocation(String geoLocationId, MutableLiveData<EventGeographicalLocation> eventGeographicalLocationMutableLiveData) {
        return GeoLocationRepository.getInstance().setGeoLocation(geoLocationId, eventGeographicalLocationMutableLiveData);
    }

    public MutableLiveData<Event> getEventById(String eventId){
        return EventRepository.getInstance().getEventById(eventId);
    }
}
