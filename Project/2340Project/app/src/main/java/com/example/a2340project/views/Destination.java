package com.example.a2340project.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.example.a2340project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class Destination extends AppCompatActivity {

    private EditText locationInput, startDateInput, endDateInput;
    private LinearLayout formLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);

        locationInput = findViewById(R.id.locationInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        formLayout = findViewById(R.id.formLayout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.logTravelButton).setOnClickListener(v -> toggleFormVisibility());
        findViewById(R.id.saveLogButton).setOnClickListener(v -> saveTravelLog());
        findViewById(R.id.cancelLogButton).setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            clearForm();
        });
        setupNavigationButtons();
        displayTravelLogs();
    }

    private void toggleFormVisibility() {
        if (formLayout.getVisibility() == View.GONE) {
            formLayout.setVisibility(View.VISIBLE);
        } else {
            formLayout.setVisibility(View.GONE);
        }
    }

    private void setupNavigationButtons() {
        findViewById(R.id.homeButton).setOnClickListener(view -> startActivity(new Intent(this, HomeScreen.class)));
        findViewById(R.id.destinationsButton).setOnClickListener(view -> startActivity(new Intent(this, Destination.class)));
        findViewById(R.id.logisticsButton).setOnClickListener(view -> startActivity(new Intent(this, Logistics.class)));
        findViewById(R.id.diningButton).setOnClickListener(view -> startActivity(new Intent(this, DiningEstablishment.class)));
        findViewById(R.id.accommodationsButton).setOnClickListener(view -> startActivity(new Intent(this, Accommodations.class)));
        findViewById(R.id.communityButton).setOnClickListener(view -> startActivity(new Intent(this, TravelCommunity.class)));
    }

    private void displayTravelLogs() {
        List<TravelLog> logs = TravelLogStorage.getInstance().getTravelLogs();
        StringBuilder logDisplay = new StringBuilder();

        for (TravelLog log : logs) {
            logDisplay.append("Location: ").append(log.getLocation())
                    .append("\nStart Date: ").append(log.getStartDate())
                    .append("\nEnd Date: ").append(log.getEndDate())
                    .append("\n\n");
        }
    }

    private void clearForm() {
        locationInput.setText("");
        startDateInput.setText("");
        endDateInput.setText("");
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/YYYY");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void saveTravelLog() {
        String location = locationInput.getText().toString();
        String startDate = startDateInput.getText().toString();
        String endDate = endDateInput.getText().toString();

        if (location.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate.compareTo(endDate) > 0) {
            Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            Toast.makeText(this, "Invalid date format! Use MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            return;
        }
        TravelLog log = new TravelLog(location, startDate, endDate);
        TravelLogStorage.getInstance().addTravelLog(log);

        Toast.makeText(this, "Travel log saved!", Toast.LENGTH_SHORT).show();
        displayTravelLogs();
    }
}

