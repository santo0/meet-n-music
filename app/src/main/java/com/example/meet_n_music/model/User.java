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

}