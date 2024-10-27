package com.example.a2340project.model;

import java.util.List;

public class Trip {
    private String tripId;
    private String destination;
    private List<User> contributors;
    private List<Note> notes;

    public Trip() {} // Default constructor for Firebase

    public Trip(String tripId, String destination) {
        this.tripId = tripId;
        this.destination = destination;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public List<User> getContributors() {
        return contributors;
    }

    public void setContributors(List<User> contributors) {
        this.contributors = contributors;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
