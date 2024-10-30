package com.example.a2340project.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// to access from other classes:
// DatabaseReference dbRef = FirebaseDatabaseInstance.getInstance().getDatabaseReference()

class FirebaseDatabaseSingleton {

    private static FirebaseDatabaseSingleton instance;
    private DatabaseReference databaseReference;

    private FirebaseDatabaseSingleton() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true); // Enables offline persistence if needed
        databaseReference = database.getReference();
    }

    static FirebaseDatabaseSingleton getInstance() {
        if (instance == null) {
            synchronized (FirebaseDatabaseSingleton.class) {
                if (instance == null) {
                    instance = new FirebaseDatabaseSingleton();
                }
            }
        }
        return instance;
    }

    DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}