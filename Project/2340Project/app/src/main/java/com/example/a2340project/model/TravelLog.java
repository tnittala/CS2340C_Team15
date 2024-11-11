package com.example.a2340project.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TravelLog {
    private final String location;
    private final String checkInDate;
    private final String checkOutDate;
    private final String roomType;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");

    public TravelLog(String location, String checkInDate, String checkOutDate, String roomType) {
        this.location = location;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
    }

    public String getLocation() {
        return location;
    }

    // Rename these methods to match the expected names in Destination.java
    public String getStartDate() {
        return checkInDate;
    }

    public String getEndDate() {
        return checkOutDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public Date getParsedCheckInDate() {
        return parseDate(checkInDate);
    }

    public Date getParsedCheckOutDate() {
        return parseDate(checkOutDate);
    }

    private Date parseDate(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
