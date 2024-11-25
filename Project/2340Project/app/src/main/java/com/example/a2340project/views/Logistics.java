package com.example.a2340project.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a2340project.R;
import com.example.a2340project.model.DiningReservation;
import com.example.a2340project.model.DiningReservationStorage;
import com.example.a2340project.model.Note;
import com.example.a2340project.model.NotesStorage;
import com.example.a2340project.model.CollaboratorAdapter;
import com.example.a2340project.model.Note;
import com.example.a2340project.viewmodels.TripViewModel;
import com.example.a2340project.views.NotesAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Logistics extends AppCompatActivity {

    // creating private vars
    // grid
    public BarChart barChart;
    private DatabaseReference database;
    private List<Note> notes;
    private GridLayout notesList; // might need to create this in XML

    public void graphTrips() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 4));
        entries.add(new BarEntry(1, 3));

        BarDataSet dataSet = new BarDataSet(entries, "Trip Days");
        dataSet.setColors(Color.RED, Color.BLUE);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); //this refreshes the chart

    }
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

        database = FirebaseDatabase.getInstance().getReference();
        Button graphButton = findViewById(R.id.button_tripgraph);
        barChart = findViewById(R.id.barChart);
        notesList = findViewById(R.id.notesList);

    private void setupGraphButton() {
        barChart = findViewById(R.id.barChart);
        Button graphButton = findViewById(R.id.button_tripgraph);
        graphButton.setOnClickListener(v -> graphTrips());
    }


    public void graphTrips() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 4));
        entries.add(new BarEntry(1, 3));


        //tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        // Set up RecyclerView for displaying notes
        /*RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotesAdapter notesAdapter = new NotesAdapter(notes);
        notesRecyclerView.setAdapter(notesAdapter);*/

        /*tripViewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);
            }
        });*/

        //tripViewModel.fetchNotes(tripId);


        BarDataSet dataSet = new BarDataSet(entries, "Trip Days");
        dataSet.setColors(Color.RED, Color.BLUE);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Refresh the chart
    }

    /*private void setupRecyclerView() {
        if (tripViewModel == null) {
            Log.e("Logistics", "TripViewModel is null during setupRecyclerView");
            return;
        }
        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        NotesAdapter notesAdapter = new NotesAdapter(new ArrayList<>());
        notesRecyclerView.setAdapter(notesAdapter);

        tripViewModel.getNotes().observe(this, notesAdapter::setNotes);
    }*/


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

    private void loadNotes() {
        List<Note> n = NotesStorage.getInstance().getNotesList();
        // Sort logs by start date
        //Collections.sort(n, (res1, res2) -> res1.getTimestamp().compareTo(res2.getTimestamp()));
        notesList.removeAllViews();
        for (int i = 0; i < n.size(); i++) {
            Note log = n.get(i);
            View resView = createResView(log);

            //if (isPastDate(log.getEndDate())) {
            //     logView.setBackgroundColor(ContextCompat.getColor(this, R.color.past_date_background));
            // }

            notesList.addView(resView);
            // Add a divider line between entries
            if (i < n.size() - 1) { // Avoid adding a divider after the last item
                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2  // Height of the divider line
                ));
                divider.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color)); // Set divider color
                notesList.addView(divider);
            }
        }
    }

    private View createResView(Note log) {
        LinearLayout resLayout = new LinearLayout(this);
        resLayout.setOrientation(LinearLayout.VERTICAL);
        resLayout.setPadding(10, 10, 10, 10);

        TextView contentText = new TextView(this);
        contentText.setText(log.getContent());
        resLayout.addView(contentText);

        TextView authorText = new TextView(this);
        authorText.setText("Author: " + log.getAuthorId());
        resLayout.addView(authorText);


        TextView timeText = new TextView(this);
        timeText.setText(String.format("%f", log.getTimestamp()));
        resLayout.addView(timeText);
        return resLayout;
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
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Note newNote = new Note(null, userId, content, System.currentTimeMillis());
                NotesStorage.getInstance().addNote(newNote);
                DatabaseReference notesRef = database.child("Notes");

                notesRef.push().setValue(newNote);
                //tripViewModel.addNoteToTrip(tripId, note);
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
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