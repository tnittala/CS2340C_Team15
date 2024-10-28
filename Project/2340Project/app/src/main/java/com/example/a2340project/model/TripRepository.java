package com.example.a2340project.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripRepository {

    private DatabaseReference database;

    public TripRepository() {
        // Initialize the database reference to the "Trips" path in Firebase
        database = FirebaseDatabase.getInstance().getReference("Trips");
    }

    // Method to add a note to a specific trip
    public void addNoteToTrip(String tripId, Note note, FirebaseCallback<Boolean> callback) {
        DatabaseReference notesRef = database.child(tripId).child("notes").push();
        String noteId = notesRef.getKey();
        note.setNoteId(noteId);
        notesRef.setValue(note)
                .addOnSuccessListener(aVoid -> callback.onCallback(true))
                .addOnFailureListener(e -> {
                    Log.e("TripRepository", "Failed to add note", e);
                    callback.onCallback(false);
                });
    }

    // Method to fetch all notes for a specific trip
    public void fetchNotes(String tripId, FirebaseCallback<List<Note>> callback) {
        DatabaseReference notesRef = database.child(tripId).child("notes");
        notesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Note> notesList = new ArrayList<>();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    notesList.add(note);
                }
                callback.onCallback(notesList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("TripRepository", "Failed to fetch notes", error.toException());
                callback.onCallback(null);
            }
        });
    }
}
