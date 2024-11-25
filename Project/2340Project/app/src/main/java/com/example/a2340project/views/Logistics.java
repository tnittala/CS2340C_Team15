package com.example.a2340project.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
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
import com.example.a2340project.model.CollaboratorAdapter;
import com.example.a2340project.model.Note;
import com.example.a2340project.viewmodels.TripViewModel;
import com.example.a2340project.views.NotesAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logistics extends AppCompatActivity {

    private BarChart barChart;
    private TripViewModel tripViewModel;
    private String tripId = "exampleTripId"; // Placeholder for the actual trip ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);

        setupEdgeToEdge();
        setupGraphButton();
        setupRecyclerView();
        setupNoteButtons();
        setupNavigationButtons();

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.fetchNotes(tripId);
        if (tripViewModel == null) {
            Log.e("Logistics", "TripViewModel is null");
            return;
        }

        RecyclerView collaboratorsRecyclerView = findViewById(R.id.collaboratorsRecyclerView);
        collaboratorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

// Example list of collaborators
        List<String> collaborators = new ArrayList<>();

// Set the adapter
        CollaboratorAdapter adapter = new CollaboratorAdapter(this, collaborators);
        collaboratorsRecyclerView.setAdapter(adapter);

        setupRecyclerView();
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupGraphButton() {
        barChart = findViewById(R.id.barChart);
        Button graphButton = findViewById(R.id.button_tripgraph);
        graphButton.setOnClickListener(v -> graphTrips());
    }

    public void graphTrips() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 4));
        entries.add(new BarEntry(1, 3));

        BarDataSet dataSet = new BarDataSet(entries, "Trip Days");
        dataSet.setColors(Color.RED, Color.BLUE);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Refresh the chart
    }

    private void setupRecyclerView() {
        if (tripViewModel == null) {
            Log.e("Logistics", "TripViewModel is null during setupRecyclerView");
            return;
        }
        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        NotesAdapter notesAdapter = new NotesAdapter(new ArrayList<>());
        notesRecyclerView.setAdapter(notesAdapter);

        tripViewModel.getNotes().observe(this, notesAdapter::setNotes);
    }

    private void setupNoteButtons() {
        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(view -> openAddNoteDialog());

        Button addCollabButton = findViewById(R.id.inviteButton);
        addCollabButton.setOnClickListener(view -> openAddCollabDialog());
    }

    private void setupNavigationButtons() {
        setupNavigationButton(R.id.homeButton, HomeScreen.class);
        setupNavigationButton(R.id.destinationsButton, Destination.class);
        setupNavigationButton(R.id.logisticsButton, Logistics.class);
        setupNavigationButton(R.id.diningButton, DiningEstablishment.class);
        setupNavigationButton(R.id.accommodationsButton, Accommodations.class);
        setupNavigationButton(R.id.communityButton, TravelCommunity.class);
    }

    private void setupNavigationButton(int buttonId, Class<?> targetActivity) {
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Logistics.this, targetActivity);
            startActivity(intent);
        });
    }

    private void openAddCollabDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite Collaborators");

        final EditText input = new EditText(this);
        input.setHint("Enter collaborator username");
        builder.setView(input);

        builder.setPositiveButton("Invite", (dialog, which) -> {
            String content = input.getText().toString().trim();
            if (!content.isEmpty()) {
                inviteCollaborator(content);
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void inviteCollaborator(String username) {
        tripViewModel.inviteCollaboratorToTrip(tripId, username).observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Collaborator invited", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to invite collaborator", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        final EditText input = new EditText(this);
        input.setHint("Enter your note");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String content = input.getText().toString().trim();
            if (!content.isEmpty()) {
                addNoteToTrip(content);
            } else {
                Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addNoteToTrip(String content) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Note note = new Note(null, userId, content, System.currentTimeMillis());

        tripViewModel.addNoteToTrip(tripId, note).observe(this, isSuccessful -> {
            if (Boolean.TRUE.equals(isSuccessful)) {
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show();
            }
        });
    }

}