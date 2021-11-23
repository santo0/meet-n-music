package com.example.meet_n_music.model;

public class EventGeographicalLocation {
    String eventId, name;
    double lat, lng;

    public EventGeographicalLocation(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
