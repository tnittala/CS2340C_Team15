package com.example.a2340project.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a2340project.model.Note;
import com.example.a2340project.model.TripRepository;
import com.example.a2340project.model.FirebaseCallback;

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
}
