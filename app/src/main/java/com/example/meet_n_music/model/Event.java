package com.example.meet_n_music.model;

public class Event {
    private String name, description, location, startDate, genre, covid;

    public Event(){
        this.name = "ERROR_NAME";
        this.description = "ERROR_DESCRIPTION";
    }

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Event(String name, String description, String location, String startDate, String genre, String covid){

    }
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
