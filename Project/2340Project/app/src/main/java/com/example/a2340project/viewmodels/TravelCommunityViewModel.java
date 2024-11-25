package com.example.a2340project.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a2340project.model.TravelPost;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravelCommunityViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<TravelPost>> travelPosts = new MutableLiveData<>();

    public MutableLiveData<List<TravelPost>> getTravelPosts() {
        return travelPosts;
    }

    public void addTravelPost(String duration, List<Map<String, String>> destinations, String notes) {
        TravelPost post = new TravelPost(duration, destinations, notes, null);
        db.collection("travel_posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    // Optional: Log the successful addition of a post
                })
                .addOnFailureListener(e -> {
                    // Optional: Log the error
                });
    }

    public void listenToTravelPosts() {
        db.collection("travel_posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) return;

                    List<TravelPost> posts = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        TravelPost post = doc.toObject(TravelPost.class);
                        posts.add(post);
                    }
                    travelPosts.setValue(posts);
                });
    }
}
