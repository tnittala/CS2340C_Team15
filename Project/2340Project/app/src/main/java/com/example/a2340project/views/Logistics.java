package com.example.a2340project.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a2340project.R;
import com.example.a2340project.model.Note;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.a2340project.model.User;
import com.example.a2340project.viewmodels.TripViewModel;
import com.example.a2340project.views.NotesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class Logistics extends AppCompatActivity {

    private TripViewModel tripViewModel;
    private NotesAdapter notesAdapter;
    private String tripId = "exampleTripId"; // Replace this with the actual trip ID if available
    private BarChart barChart;

    private void graphTrips() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 4));
        entries.add(new BarEntry(1, 3));

        BarDataSet dataSet = new BarDataSet(entries, "Trip Days");
        dataSet.setColors(Color.RED, Color.BLUE);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); //this refreshes the chart

    }
    private TripViewModel tripViewModel;  // New ViewModel field for handling Firebase interactions
    private String tripId = "exampleTripId"; // Placeholder for the actual trip ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);

        // Set up window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button graphButton = findViewById(R.id.button_tripgraph);
        barChart = findViewById(R.id.barChart);

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphTrips();
            }
        });

        // home button

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        // Set up RecyclerView for displaying notes
        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(new ArrayList<>()); // Start with an empty list
        notesRecyclerView.setAdapter(notesAdapter);

        // Observe notes LiveData from ViewModel and update the adapter
        tripViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);
            }
        });

        // Fetch notes for the specified trip
        tripViewModel.fetchNotes(tripId);

        // Set up Add Note button listener
        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(view -> openAddNoteDialog());

        // Original button listeners remain unchanged
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        ImageButton destinationsBtn = findViewById(R.id.destinationsButton);
        destinationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics.this, Destination.class);
                startActivity(intent);
            }
        });

        ImageButton logisticsBtn = findViewById(R.id.logisticsButton);
        logisticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics.this, Logistics.class);
                startActivity(intent);
            }
        });

        ImageButton diningBtn = findViewById(R.id.diningButton);
        diningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics.this, DiningEstablishment.class);
                startActivity(intent);
            }
        });

        ImageButton accommodationsBtn = findViewById(R.id.accommodationsButton);
        accommodationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics.this, Accommodations.class);
                startActivity(intent);
            }
        });

        ImageButton communityBtn = findViewById(R.id.communityButton);
        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logistics.this, TravelCommunity.class);
                startActivity(intent);
            }
        });
    }

    // Method to open the Add Note dialog
    private void openAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        final EditText input = new EditText(this);
        input.setHint("Enter your note");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String content = input.getText().toString().trim();
            if (!content.isEmpty()) {
                // Retrieve the current user ID (ensure user is authenticated)
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Create a new Note object
                Note note = new Note(null, userId, content, System.currentTimeMillis());
                // Add the note to the trip
                tripViewModel.addNoteToTrip(tripId, note);
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
