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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.a2340project.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.a2340project.model.User;
import com.example.a2340project.viewmodels.TripViewModel;

import java.util.ArrayList;

public class Logistics extends AppCompatActivity {

    public BarChart barChart;

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
    private TripViewModel tripViewModel;  // New ViewModel field for handling Firebase interactions
    private String tripId = "exampleTripId"; // Placeholder for the actual trip ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);

        // Setting up window insets as per original code
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
        // Initialize TripViewModel
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        // Set up the invite button listener
        findViewById(R.id.inviteButton).setOnClickListener(view -> openInviteDialog());

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

    // New method to open a dialog for inviting a collaborator
    private void openInviteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite Collaborator");

        // Set up the input field for email entry
        final EditText input = new EditText(this);
        input.setHint("Enter collaborator's email");
        builder.setView(input);

        // Configure "Invite" button in the dialog
        builder.setPositiveButton("Invite", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (!email.isEmpty()) {
                inviteCollaborator(email);
            } else {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });

        // Configure "Cancel" button in the dialog
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // New method to handle collaborator invitation using TripViewModel
    private void inviteCollaborator(String email) {
        // Creating a User object for the invited collaborator
        User user = new User("generatedUserId", email, "collaborator"); // Replace "generatedUserId" with actual user ID logic

        // Add the collaborator to the trip using ViewModel
        tripViewModel.addContributor(tripId, user);

        // Display a confirmation message
        Toast.makeText(this, "Invitation sent", Toast.LENGTH_SHORT).show();
    }
}
