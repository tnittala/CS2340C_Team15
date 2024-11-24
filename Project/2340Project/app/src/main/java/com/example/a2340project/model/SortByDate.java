package com.example.a2340project.model;

import java.util.Comparator;
import java.util.List;

public class SortByDate implements SortStrategy {
    @Override
    public List<DiningReservation> sort(List<DiningReservation> reservations) {
        reservations.sort(Comparator.comparing(DiningReservation::getTime));
        return reservations;
    }
}
