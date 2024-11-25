package com.example.a2340project.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TravelPost {
    private String duration;
    private List<Map<String, String>> destinations; // Each map represents a destination's data
    private String notes;
    private Date timestamp;

    // Default constructor (required for Firebase Firestore)
    public TravelPost() {}

    // Parameterized constructor
    public TravelPost(String duration, List<Map<String, String>> destinations, String notes, Date timestamp) {
        this.duration = duration;
        this.destinations = destinations;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<Map<String, String>> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Map<String, String>> destinations) {
        this.destinations = destinations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}