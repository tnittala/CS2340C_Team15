package com.example.a2340project.model;

import java.util.List;

public interface SortStrategy {
    List<DiningReservation> sort(List<DiningReservation> reservations);
}
