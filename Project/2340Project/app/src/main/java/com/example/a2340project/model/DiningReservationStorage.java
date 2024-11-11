package com.example.a2340project.model;

import java.util.ArrayList;
import java.util.List;


public class DiningReservationStorage {
    private static DiningReservationStorage instance;
    private final List<DiningReservation> reservations;

    private DiningReservationStorage() {
        reservations = new ArrayList<>();
    }

    public static synchronized DiningReservationStorage getInstance() {
        if (instance == null) {
            instance = new DiningReservationStorage();
        }
        return instance;
    }

    public List<DiningReservation> getDiningReservations() {
        return reservations;
    }

    public void addDiningReservation(DiningReservation r) {
        reservations.add(r);
    }
}
