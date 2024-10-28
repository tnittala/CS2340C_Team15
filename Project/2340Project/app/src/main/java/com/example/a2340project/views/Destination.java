package com.example.a2340project.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Destination extends AppCompatActivity {

    private EditText locationInput, startDateInput, endDateInput;
    private LinearLayout formLayout;
    private GridLayout destinationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);

        locationInput = findViewById(R.id.locationInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        formLayout = findViewById(R.id.formLayout);
        destinationList = findViewById(R.id.destinationList);  // Get reference to the GridLayout

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startDateInput.setOnClickListener(v -> showDatePicker(startDateInput));
        endDateInput.setOnClickListener(v -> showDatePicker(endDateInput));
        findViewById(R.id.logTravelButton).setOnClickListener(v -> toggleFormVisibility());
        findViewById(R.id.saveLogButton).setOnClickListener(v -> saveTravelLog());
        findViewById(R.id.cancelLogButton).setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            clearForm();
        });
        displayTravelLogs();
        setupNavigationButtons();
    }

    private void addLogToGrid(TravelLog log) {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout.LayoutParams locationParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        locationParams.setMargins(0, 0, 16, 16);

        LinearLayout.LayoutParams daysParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );

        TextView locationView = new TextView(this);
        locationView.setText(log.getLocation());
        locationView.setPadding(8, 8, 8, 8);
        locationView.setTextSize(16);
        locationView.setGravity(Gravity.START);
        locationView.setLayoutParams(locationParams);

        TextView daysView = new TextView(this);
        long days = calculateDaysBetween(log.getStartDate(), log.getEndDate(), new SimpleDateFormat("MM/dd/yy"));
        daysView.setText(days + " days planned");
        daysView.setPadding(8, 8, 8, 8);
        daysView.setTextSize(16);
        daysView.setGravity(Gravity.END);
        daysView.setLayoutParams(daysParams);

        rowLayout.addView(locationView);
        rowLayout.addView(daysView);

        destinationList.addView(rowLayout);
    }


    private void toggleFormVisibility() {
        if (formLayout.getVisibility() == View.GONE) {
            formLayout.setVisibility(View.VISIBLE);
        } else {
            formLayout.setVisibility(View.GONE);
        }
    }

    private void displayTravelLogs() {
        destinationList.removeAllViews();  // Clear previous views

        List<TravelLog> logs = TravelLogStorage.getInstance().getTravelLogs();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

        for (TravelLog log : logs) {
            String location = log.getLocation();
            long days = calculateDaysBetween(log.getStartDate(), log.getEndDate(), sdf);

            TextView locationView = new TextView(this);
            locationView.setText(location);
            locationView.setPadding(8, 8, 8, 8);

            TextView daysView = new TextView(this);
            daysView.setText(days + " days");
            daysView.setPadding(8, 8, 8, 8);

            destinationList.addView(locationView);
            destinationList.addView(daysView);
        }
    }

    private long calculateDaysBetween(String startDate, String endDate, SimpleDateFormat sdf) {
        try {
            long startMillis = sdf.parse(startDate).getTime();
            long endMillis = sdf.parse(endDate).getTime();
            long diffInMillis = endMillis - startMillis;
            return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
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
        addLogToGrid(log);
        Toast.makeText(this, "Travel log saved!", Toast.LENGTH_SHORT).show();
        clearForm();
        formLayout.setVisibility(View.GONE);
    }

    private void clearForm() {
        locationInput.setText("");
        startDateInput.setText("");
        endDateInput.setText("");
    }

    private void setupNavigationButtons() {
        findViewById(R.id.homeButton).setOnClickListener(view -> startActivity(new Intent(this, HomeScreen.class)));
        findViewById(R.id.destinationsButton).setOnClickListener(view -> startActivity(new Intent(this, Destination.class)));
        findViewById(R.id.logisticsButton).setOnClickListener(view -> startActivity(new Intent(this, Logistics.class)));
        findViewById(R.id.diningButton).setOnClickListener(view -> startActivity(new Intent(this, DiningEstablishment.class)));
        findViewById(R.id.accommodationsButton).setOnClickListener(view -> startActivity(new Intent(this, Accommodations.class)));
        findViewById(R.id.communityButton).setOnClickListener(view -> startActivity(new Intent(this, TravelCommunity.class)));
    }

    private void showDatePicker(EditText dateInput) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date to MM/DD/YY
                    String formattedDate = String.format("%d/%d/%02d",
                            selectedMonth + 1, selectedDay, selectedYear % 100);
                    dateInput.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

}

