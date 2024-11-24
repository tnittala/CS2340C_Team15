package com.example.a2340project.viewmodels;

import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a2340project.model.Note;
import com.example.a2340project.model.Trip;
import com.example.a2340project.model.TripRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class TripViewModel extends ViewModel {

    private final TripRepository tripRepository;
    private MutableLiveData<List<Note>> notes = new MutableLiveData<>();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public TripViewModel() {
        tripRepository = new TripRepository();
    }

    // LiveData getter for notes
    public LiveData<List<Note>> getNotes() {
        if (notes == null) {
            notes = new MutableLiveData<>();
        }
        return notes;
    }

    // Method to add a note to a trip
    public LiveData<Boolean> addNoteToTrip(String tripId, Note note) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        tripRepository.addNoteToTrip(tripId, note, isSuccessful -> {
            result.setValue(isSuccessful); // Notify about success or failure
            if (isSuccessful) {
                fetchNotes(tripId); // Refresh notes after a successful addition
            }
        });
        return result; // Return LiveData to observe in the Activity/Fragment
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
        databaseReference.child("trips").child(tripId).child("collaborators").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        canEdit.setValue(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        canEdit.setValue(false);
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

    // Method to update trip details
    public void updateTripDetails(String tripId, String destination, String startDate, String endDate) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("destination", destination);
        updates.put("startDate", startDate);
        updates.put("endDate", endDate);

        databaseReference.child("trips").child(tripId).updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Notify collaborators about the update
            }
        });
    }

    // Method to invite a collaborator to a trip
    public LiveData<Boolean> inviteCollaboratorToTrip(String tripId, String collaboratorUsername) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        databaseReference.child("users").orderByChild("username").equalTo(collaboratorUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String collaboratorId = snapshot.getChildren().iterator().next().getKey();
                            databaseReference.child("trips").child(tripId).child("collaborators").child(collaboratorId)
                                    .setValue(true).addOnCompleteListener(task -> {
                                        result.setValue(task.isSuccessful());
                                    });
                        } else {
                            result.setValue(false); // Collaborator username not found
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        result.setValue(false);
                    }
                });

        return result;
    }
}
