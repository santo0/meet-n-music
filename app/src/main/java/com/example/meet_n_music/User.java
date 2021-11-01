package com.example.meet_n_music;

import androidx.appcompat.app.AppCompatActivity;

public class User extends AppCompatActivity {
    public String username, email, password;

    public User(){}

    public User(String username, String email, String password){
        this.username=username;
        this.password=password;
        this.email=email;
    }
}