package com.example.a2340project.model;

import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    private SortStrategy sortStrategy;
    private List<ReservationObserver> observers = new ArrayList<>();

    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }

    public List<DiningReservation> sortReservations(List<DiningReservation> reservations) {
        if (sortStrategy != null) {
            return sortStrategy.sort(reservations);
        }
        return reservations;
    }

    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    private void notifyReservationAdded(DiningReservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationAdded(reservation);
        }
    }

    // Adding reservation and notifying observers
    public void addReservation(DiningReservation reservation) {
        // Add reservation to Firebase or local database
        // Notify observers
        notifyReservationAdded(reservation);
    }
}

/*Example usage in an Activity
ReservationManager reservationManager = new ReservationManager();
reservationManager.setSortStrategy(new SortByDate());
List<Reservation> sortedReservations = reservationManager.sortReservations(reservations);
*/
