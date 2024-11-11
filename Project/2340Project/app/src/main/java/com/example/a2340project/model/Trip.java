package com.example.a2340project.model;

import java.util.List;

public class Trip {
    private String destination;
    private String startDate;
    private String endDate;
    private String tripId;
    private String createdBy;
    private boolean isCollaborative;

    public Trip() {}

    public Trip(String destination, String startDate, String endDate, String tripId, String createdBy, boolean isCollaborative) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripId = tripId;
        this.createdBy = createdBy;
        this.isCollaborative = isCollaborative;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    //getting the end date of travel plan
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isCollaborative() {
        return isCollaborative;
    }

    public void setCollaborative(boolean collaborative) {
        isCollaborative = collaborative;
    }
}