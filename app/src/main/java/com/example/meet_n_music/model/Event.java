package com.example.meet_n_music.model;

public class Event {
    private String id, name, description, location, startDate, genre, covid;
    private int totalAttendants = 0;


    public Event(){
        this.name = "ERROR_NAME";
        this.description = "ERROR_DESCRIPTION";
    }

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Event(String id, String name, String description, String location, String startDate, String genre, String covid, int totalAttendants){
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.genre = genre;
        this.covid = covid;
        this.totalAttendants = totalAttendants;
    }

    public Event(String id, String name, String description, String location, String startDate, String genre, String covid){
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.genre = genre;
        this.covid = covid;
        this.totalAttendants = 0;
    }

    public Event(String name, String description, String location, String startDate, String genre, String covid){
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.genre = genre;
        this.covid = covid;
        this.totalAttendants = 0;
    }

    //setId
    public void setId(String id) { this.id = id;}
    public String getId() {return this.id;}
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getGenre() {
        return genre;
    }

    public String getCovid() { return covid;
    }

    public String getImagePath() {
        return getId() + "/" + getId() + ".jpg";
    }


    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startDate='" + startDate + '\'' +
                ", genre='" + genre + '\'' +
                ", covid='" + covid + '\'' +
                '}';
    }

    public int getTotalAttendants() {
        return totalAttendants;
    }
}
