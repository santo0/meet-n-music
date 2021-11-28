package com.example.meet_n_music;

public class SliderItem {
    private String description;
    private String imageUrl;

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public SliderItem(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
