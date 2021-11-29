package com.example.meet_n_music.api;

import com.example.meet_n_music.model.EventGeographicalLocation;

public class OpenStreetMapResponse {
    private String display_name;
    double lat, lon;

    public EventGeographicalLocation getLocationByName(){
        return new EventGeographicalLocation(display_name, lat, lon);
    }
}
