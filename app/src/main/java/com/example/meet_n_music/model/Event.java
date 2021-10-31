package com.example.meet_n_music.model;

public class Event {
    private String name;
    private String imgPath;
    private String description;

    public Event(){
        this.name = "ERROR_NAME";
        this.imgPath = "ERORR_PATH";
        this.description = "ERROR_DESCRIPTION";
    }

    public Event(String name, String imgPath, String description) {
        this.name = name;
        this.imgPath = imgPath;
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
