package com.example.a2340project.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TripRepository {

    private DatabaseReference database;

    public TripRepository() {
        database = FirebaseDatabase.getInstance().getReference("Trips");
    }

    public void addContributor(String tripId, User user, FirebaseCallback<Boolean> callback) {
        DatabaseReference contributorRef = database.child(tripId).child("contributors").child(user.getUserId());
        contributorRef.setValue(user)
                .addOnSuccessListener(aVoid -> callback.onCallback(true))
                .addOnFailureListener(e -> callback.onCallback(false));
    }

    public void addNoteToTrip(String tripId, Note note, FirebaseCallback<Boolean> callback) {
        DatabaseReference notesRef = database.child(tripId).child("notes").push();
        notesRef.setValue(note)
                .addOnSuccessListener(aVoid -> callback.onCallback(true))
                .addOnFailureListener(e -> callback.onCallback(false));
    }
    public void fetchTrip(String tripId, FirebaseCallback<Trip> callback) {
        DatabaseReference tripRef = database.child(tripId);
        tripRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Trip trip = task.getResult().getValue(Trip.class);
                callback.onCallback(trip); // Pass the Trip object back on success
            } else {
                callback.onCallback(null); // Pass null if there's an error
            }
        });
    }
}

