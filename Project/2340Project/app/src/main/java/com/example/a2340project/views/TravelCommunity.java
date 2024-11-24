package com.example.a2340project.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2340project.R;
import com.example.a2340project.viewmodels.TravelCommunityViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelCommunity extends AppCompatActivity {

    private TravelCommunityViewModel travelCommunityViewModel;
    private TravelPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_community);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_travel_posts);
        adapter = new TravelPostAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        travelCommunityViewModel = new ViewModelProvider(this).get(TravelCommunityViewModel.class);

        travelCommunityViewModel.getTravelPosts().observe(this, posts -> adapter.submitList(posts));

        travelCommunityViewModel.listenToTravelPosts();

        Button addPostButton = findViewById(R.id.btn_create_post);
        addPostButton.setOnClickListener(v -> showAddPostDialog());
    }

    private void showAddPostDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);

        // Input fields
        EditText durationInput = new EditText(this);
        durationInput.setHint("Duration (e.g., 7 days)");
        durationInput.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(durationInput);

        EditText destinationsInput = new EditText(this);
        destinationsInput.setHint("Destinations (comma-separated)");
        destinationsInput.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(destinationsInput);

        EditText notesInput = new EditText(this);
        notesInput.setHint("Notes");
        notesInput.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(notesInput);

        // Show the dialog
        new AlertDialog.Builder(this)
                .setTitle("Add Travel Post")
                .setView(layout)
                .setPositiveButton("Submit", (dialog, which) -> {
                    // Extract user input
                    String duration = durationInput.getText().toString();
                    String[] destinationArray = destinationsInput.getText().toString().split(",");
                    List<Map<String, String>> destinations = new ArrayList<>();
                    for (String dest : destinationArray) {
                        Map<String, String> destinationMap = new HashMap<>();
                        destinationMap.put("destination", dest.trim());
                        destinations.add(destinationMap);
                    }
                    String notes = notesInput.getText().toString();

                    travelCommunityViewModel.addTravelPost(duration, destinations, notes);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
