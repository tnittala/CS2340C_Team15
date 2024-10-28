package com.example.a2340project.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.a2340project.model.Trip;
import com.example.a2340project.model.User;
import com.example.a2340project.model.Note;
import com.example.a2340project.model.TripRepository;

public class TripViewModel extends ViewModel {

    private TripRepository tripRepository;
    private MutableLiveData<Trip> currentTrip = new MutableLiveData<>();

    public TripViewModel() {
        tripRepository = new TripRepository();
    }

    public void addContributor(String tripId, User user) {
        tripRepository.addContributor(tripId, user, isSuccessful -> {
            if (isSuccessful) {
                tripRepository.fetchTrip(tripId, trip -> {
                    // Handle the updated trip data here if needed
                });
            }
        });
    }

    public void addNoteToTrip(String tripId, Note note) {
        tripRepository.addNoteToTrip(tripId, note, isSuccessful -> {
            if (isSuccessful) {
                tripRepository.fetchTrip(tripId, trip -> {
                    // Handle the updated trip data here if needed
                });
            }
        });
    }
}
