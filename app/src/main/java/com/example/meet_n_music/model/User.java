package com.example.meet_n_music.model;


public class User {
    public String id, username, email, interestedIn;

    public User(){}

    public User(String username, String email, String interestedIn){
        this.username=username;
        this.email=email;
        this.interestedIn = interestedIn;
    }
}