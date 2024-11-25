package com.example.a2340project.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Logistics extends AppCompatActivity {


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
    private TripViewModel tripViewModel;
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

        database = FirebaseDatabase.getInstance().getReference();
        Button graphButton = findViewById(R.id.button_tripgraph);
        barChart = findViewById(R.id.barChart);
        notesList = findViewById(R.id.notesList);

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphTrips();
            }
        });

        // home button

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


        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(view -> openAddNoteDialog());

        Button addCollabButton = findViewById(R.id.inviteButton);
        addCollabButton.setOnClickListener((view -> openAddCollabDialog()));

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

                Toast.makeText(this, "Collaborator invited", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
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
            } else {
                Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    //use to allow collaborators to modify plans
    private void openModifyTripDialog() {
        tripViewModel.isUserCollaboratorWithEditPermissions(tripId, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .observe(this, canEdit -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Trip Details");

                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_modify_trip, null);
                    builder.setView(dialogView);

                    EditText destinationField = dialogView.findViewById(R.id.editDestination);
                    EditText startDateField = dialogView.findViewById(R.id.editStartDate);
                    EditText endDateField = dialogView.findViewById(R.id.editEndDate);

                    tripViewModel.getTripDetails(tripId).observe(this, trip -> {
                        if (trip != null) {
                            destinationField.setText(trip.getDestination());
                            startDateField.setText(trip.getStartDate());
                            endDateField.setText(trip.getEndDate());
                        }
                    });

                    destinationField.setEnabled(canEdit);
                    startDateField.setEnabled(canEdit);
                    endDateField.setEnabled(canEdit);

                    builder.setPositiveButton("Save", (dialog, which) -> {
                        if (canEdit) {
                            String newDestination = destinationField.getText().toString().trim();
                            String newStartDate = startDateField.getText().toString().trim();
                            String newEndDate = endDateField.getText().toString().trim();

                            if (validateDates(newStartDate, newEndDate)) {
                                tripViewModel.updateTripDetails(tripId, newDestination, newStartDate, newEndDate);
                                Toast.makeText(this, "Trip details updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Invalid date range", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "You do not have permission to edit this trip.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.show();
                });
    }
    private boolean validateDates(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format if necessary
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            if (start != null && end != null) {
                return start.before(end);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


}
