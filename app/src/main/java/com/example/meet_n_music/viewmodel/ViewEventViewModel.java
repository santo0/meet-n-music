package com.example.meet_n_music.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_n_music.repository.AuthRepository;
import com.example.meet_n_music.repository.EventRepository;
import com.example.meet_n_music.repository.GeoLocationRepository;
import com.example.meet_n_music.repository.ImageRepository;

public class ViewEventViewModel extends ViewModel {


    public MutableLiveData<Boolean> deleteEvent(String eventId) {
        return EventRepository.getInstance().deleteEvent(eventId);
    }

    public MutableLiveData<Boolean> deleteEventImage(String imgPath) {
        return ImageRepository.getInstance().deleteImage(imgPath);
    }

    public MutableLiveData<Boolean> deleteEventGeoLocation(String eventId) {
        return GeoLocationRepository.getInstance().deleteGeoLocation(eventId);
    }

    public MutableLiveData<Boolean> deleteEventAttendees(String eventId) {
        return AuthRepository.getAuthRepository().deleteEventAttendees(eventId);
    }

    public MutableLiveData<Boolean> deleteEventOwnership(String eventId, String ownerId) {
        return AuthRepository.getAuthRepository().deleteEventOwnership(eventId, ownerId);
    }
}
