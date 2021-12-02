package com.example.meet_n_music.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OneCallOWS {

    @SerializedName("daily")
    @Expose
    private ArrayList<Daily> daily;


}
