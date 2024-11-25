package com.example.a2340project.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a2340project.model.Note;
import com.example.a2340project.model.Trip;
import com.example.a2340project.model.TripRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TripViewModel extends ViewModel {

    private TripRepository tripRepository;
    private MutableLiveData<List<Note>> notes = new MutableLiveData<>();

    public TripViewModel() {
        tripRepository = new TripRepository();
    }

    // LiveData getter for notes
    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    // Method to add a note to a trip
    public void addNoteToTrip(String tripId, Note note) {
        tripRepository.addNoteToTrip(tripId, note, isSuccessful -> {
            if (isSuccessful) {
                fetchNotes(tripId); // Refresh notes after adding a new one
            }
        });
    }

    // Method to fetch all notes for a trip
    public void fetchNotes(String tripId) {
        tripRepository.fetchNotes(tripId, fetchedNotes -> {
            if (fetchedNotes != null) {
                notes.postValue(fetchedNotes);
            }
        });
    }

    // Method to check if the user has edit permissions
    public LiveData<Boolean> isUserCollaboratorWithEditPermissions(String tripId, String userId) {
        MutableLiveData<Boolean> canEdit = new MutableLiveData<>(false);
        FirebaseFirestore.getInstance().collection("trips").document(tripId)
                .collection("collaborators").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean permission = documentSnapshot.getBoolean("canEdit");
                        canEdit.setValue(permission != null && permission);
                    }
                });
        return canEdit;
    }

    // Method to get trip details based on trip ID
    public LiveData<Trip> getTripDetails(String tripId) {
        MutableLiveData<Trip> tripData = new MutableLiveData<>();

        FirebaseFirestore.getInstance().collection("trips").document(tripId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Trip trip = documentSnapshot.toObject(Trip.class);
                        tripData.setValue(trip);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure (e.g., log error)
                });

        return tripData;
    }

    public void updateTripDetails(String tripId, String destination,
                                  String startDate, String endDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tripRef = db.collection("trips").document(tripId);

        // Update fields in the trip document
        tripRef.update(
                        "destination", destination,
                        "startDate", startDate,
                        "endDate", endDate
                )
                .addOnSuccessListener(aVoid -> {
                    // Handle successful update, e.g., log success
                })
                .addOnFailureListener(e -> {
                    // Handle failure, e.g., log error
                });
    }
}
