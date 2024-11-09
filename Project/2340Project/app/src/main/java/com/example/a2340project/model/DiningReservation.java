package com.example.a2340project.model;

public class DiningReservation {
    private String location;
    private String website;
    private String time;

    public DiningReservation() {
        // Default constructor required for calls to DataSnapshot.getValue(VacationTime.class)
    }

    public DiningReservation(String location, String website, String time) {
        this.location = location;
        this.website = website;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getTime() {
        return time;
    }
}
