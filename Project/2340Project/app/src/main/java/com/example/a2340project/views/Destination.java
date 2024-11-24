package com.example.a2340project.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.example.a2340project.R;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.a2340project.model.TravelLog;
import com.example.a2340project.model.TravelLogStorage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AlertDialog;
import com.google.firebase.auth.FirebaseUser;

public class Destination extends AppCompatActivity {

    private EditText locationInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private LinearLayout formLayout;
    private GridLayout destinationList;

    private EditText startDateCalc;
    private EditText endDateCalc;
    private EditText durationCalc;
    private TextView resultText;
    private LinearLayout formLayout2;
    private LinearLayout formLayout3;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userId;
    private Spinner dropdownRoomType;


    private DatabaseReference database;

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

        startDateCalc = findViewById(R.id.startDate);
        endDateCalc = findViewById(R.id.endDate);
        formLayout2 = findViewById(R.id.formLayout2);
        durationCalc = findViewById(R.id.duration);
        formLayout3 = findViewById(R.id.formLayout3);
        resultText = findViewById(R.id.resultText);

        dropdownRoomType = findViewById(R.id.dropdown_roomTypes);

        database = FirebaseDatabase.getInstance().getReference();

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

        startDateCalc.setOnClickListener(v -> showDatePicker(startDateCalc));
        endDateCalc.setOnClickListener(v -> showDatePicker(endDateCalc));
        findViewById(R.id.calculateVacationTime).setOnClickListener(v -> toggleForm2Visibility());
        findViewById(R.id.resetButton).setOnClickListener(v -> resetView());
        findViewById(R.id.calculate).setOnClickListener(v -> calculateVacationTime());
        // Initialize FirebaseAuth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            // Handle user not logged in
            showAlert("Authentication Required", "Please log in to continue.");
            startActivity(new Intent(Destination.this, LoginActivity.class));
            finish();
            return;
        }
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
        long days = calculateDaysBetween(log.getStartDate(), log.getEndDate(),
                new SimpleDateFormat("MM/dd/yy"));
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
    private void toggleForm2Visibility() {
        if (formLayout2.getVisibility() == View.GONE) {
            formLayout2.setVisibility(View.VISIBLE);
        } else {
            formLayout2.setVisibility(View.GONE);
        }
    }
    private void toggleForm3Visibility() {
        if (formLayout3.getVisibility() == View.GONE) {
            formLayout3.setVisibility(View.VISIBLE);
        } else {
            formLayout3.setVisibility(View.GONE);
        }
    }

    public long calculateDaysBetween(String startDate, String endDate, SimpleDateFormat sdf) {
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

    public boolean isValidDate(String date) {
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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        String location = locationInput.getText().toString();
        String startDate = startDateInput.getText().toString();
        String endDate = endDateInput.getText().toString();
        String roomType = dropdownRoomType.getSelectedItem().toString();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startDate.compareTo(endDate) > 0) {
            Toast.makeText(this, "Start date must be before end date",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            Toast.makeText(this, "Invalid date format! Use MM/DD/YYYY",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        TravelLog log = new TravelLog(location, startDate, endDate, roomType);
        DatabaseReference travelLog = database.child("users").child(userId).child(
                "logTravel");
        TravelLogStorage.getInstance().addTravelLog(log);
        addLogToGrid(log);
        travelLog.push().setValue(log)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Travel log saved successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save travel log",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
        clearForm();
        formLayout.setVisibility(View.GONE);
    }

    private void clearForm() {
        locationInput.setText("");
        startDateInput.setText("");
        endDateInput.setText("");
    }

    private void setupNavigationButtons() {
        findViewById(R.id.homeButton).setOnClickListener(view ->
                startActivity(new Intent(this, HomeScreen.class)));
        findViewById(R.id.destinationsButton).setOnClickListener(view ->
                startActivity(new Intent(this, Destination.class)));
        findViewById(R.id.logisticsButton).setOnClickListener(view ->
                startActivity(new Intent(this, Logistics.class)));
        findViewById(R.id.diningButton).setOnClickListener(view ->
                startActivity(new Intent(this, DiningEstablishment.class)));
        findViewById(R.id.accommodationsButton).setOnClickListener(view ->
                startActivity(new Intent(this, Accommodations.class)));
        findViewById(R.id.communityButton).setOnClickListener(view ->
                startActivity(new Intent(this, TravelCommunity.class)));
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

    public void calculateVacationTime() {
        // Get the user input from EditTexts
        String startDateStr = startDateCalc.getText().toString();
        String endDateStr = endDateCalc.getText().toString();
        String durationStr = durationCalc.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        sdf.setLenient(false);

        int filledFields = 0;
        if (!startDateStr.isEmpty()) {
            filledFields++;
        }
        if (!endDateStr.isEmpty()) {
            filledFields++;
        }
        if (!durationStr.isEmpty()) {
            filledFields++;
        }

        // Ensure that at least two fields are filled
        if (filledFields < 2) {
            Toast.makeText(this, "Please enter at least two fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (startDateStr.isEmpty()) {
                // Calculate start date based on end date and duration
                if (!isValidDate(endDateStr)) {
                    Toast.makeText(this, "Invalid end date format. Use MM/dd/yy",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Date endDate = sdf.parse(endDateStr);
                int duration = Integer.parseInt(durationStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(endDate);
                cal.add(Calendar.DAY_OF_YEAR, -duration);
                startDateStr = sdf.format(cal.getTime());
                startDateCalc.setText(startDateStr);

            } else if (endDateStr.isEmpty()) {
                // Calculate end date based on start date and duration
                if (!isValidDate(startDateStr)) {
                    Toast.makeText(this, "Invalid start date format. Use MM/dd/yy",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Date startDate = sdf.parse(startDateStr);
                int duration = Integer.parseInt(durationStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DAY_OF_YEAR, duration);
                endDateStr = sdf.format(cal.getTime());
                endDateCalc.setText(endDateStr);

            } else if (durationStr.isEmpty()) {
                // Calculate duration based on start and end dates
                if (!isValidDate(startDateStr) || !isValidDate(endDateStr)) {
                    Toast.makeText(this, "Invalid date format. Use MM/dd/yy",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Date startDate = sdf.parse(startDateStr);
                Date endDate = sdf.parse(endDateStr);
                long diffInMillis = endDate.getTime() - startDate.getTime();
                if (diffInMillis < 0) {
                    Toast.makeText(this, "End date must be after start date",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                long duration = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                durationStr = String.valueOf(duration);
                durationCalc.setText(durationStr);
            }

            // After calculation, update the resultText with the calculated days
            resultText.setText(durationStr + " days");

            saveVacationToDatabase();

            // Display the result container and hide other UI elements if needed
            toggleResultVisibility(true);

            // Optionally, show any other layout if necessary (like formLayout3)
            formLayout3.setVisibility(View.VISIBLE);

        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid input. Please check the dates and duration",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleResultVisibility(boolean showResult) {
        if (showResult) {
            findViewById(R.id.logTravelButton).setVisibility(View.VISIBLE);
            findViewById(R.id.calculateVacationTime).setVisibility(View.VISIBLE);
            destinationList.setVisibility(View.VISIBLE);
            formLayout3.setVisibility(View.VISIBLE);
            formLayout2.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.logTravelButton).setVisibility(View.VISIBLE);
            findViewById(R.id.calculateVacationTime).setVisibility(View.VISIBLE);
            destinationList.setVisibility(View.VISIBLE);
            formLayout3.setVisibility(View.GONE);
        }
    }

    private void saveVacationToDatabase() {
        // Get the current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        // Retrieve input values
        String startDate = startDateCalc.getText().toString();
        String endDate = endDateCalc.getText().toString();
        String duration = durationCalc.getText().toString();

        // Create a VacationTime object
        VacationTime vacationTime = new VacationTime(startDate, endDate, duration);

        // Define the database path where this entry will be saved
        DatabaseReference vacationRef =
                database.child("users").child(userId).child("vacationTimes");

        // Push the vacation time data to the database
        vacationRef.push().setValue(vacationTime)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Vacation time saved successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save vacation time",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetView() {
        // Show main buttons and views
        findViewById(R.id.logTravelButton).setVisibility(View.VISIBLE);
        findViewById(R.id.calculateVacationTime).setVisibility(View.VISIBLE);
        destinationList.setVisibility(View.VISIBLE);

        // Hide form layouts
        formLayout3.setVisibility(View.GONE);
        formLayout2.setVisibility(View.VISIBLE);

        // Clear the input fields
        startDateCalc.setText("");
        endDateCalc.setText("");
        durationCalc.setText("");

        // Clear the result text
        resultText.setText("XX days");
    }

}

