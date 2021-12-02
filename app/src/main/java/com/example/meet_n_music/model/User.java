package com.example.meet_n_music.model;


import java.util.ArrayList;
import java.util.List;

public class User {
    public String id, username, email, interestedIn;
   // public List<String> ownedEventsIds, attendingEventsIds;

    public User(){}

    public User(String username, String email, String interestedIn){
        this.username=username;
        this.email=email;
        this.interestedIn = interestedIn;
    }

    public User(String id, String username, String email, String interestedIn){
        this.id = id;
        this.username=username;
        this.email=email;
        this.interestedIn = interestedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInterestedIn() {
        return interestedIn;
    }

    public void setInterestedIn(String interestedIn) {
        this.interestedIn = interestedIn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}