package com.example.a2340project.model;

import java.util.ArrayList;
import java.util.List;

public class TravelLogStorage {
 private static TravelLogStorage instance;
    private final List<TravelLog> travelLogs;

    private TravelLogStorage() {
        travelLogs = new ArrayList<>();
    }

    public static synchronized TravelLogStorage getInstance() {
        if (instance == null) {
            instance = new TravelLogStorage();
        }
        return instance;
    }

    public List<TravelLog> getTravelLogs() {
        return travelLogs;
    }

    public void addTravelLog(TravelLog log) {
        travelLogs.add(log);
    }
}
