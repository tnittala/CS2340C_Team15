package com.example.a2340project.model;

import java.util.ArrayList;
import java.util.List;

public class NotesStorage {
    private static NotesStorage instance;
    private final List<Note> notes;

    private NotesStorage() {
        notes = new ArrayList<>();
    }

    public static synchronized NotesStorage getInstance() {
        if (instance == null) {
            instance = new NotesStorage();
        }
        return instance;
    }

    public List<Note> getNotesList() {
        return notes;
    }

    public void addNote(Note r) {
        notes.add(0, r);
    }
}
