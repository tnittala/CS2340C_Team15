package com.example.a2340project.views;

public class TravelLog {
    private final String location;
    private final String startDate;
    private final String endDate;

    public TravelLog(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
