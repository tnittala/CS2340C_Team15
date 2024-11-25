package com.example.a2340project.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a2340project.R;
import com.example.a2340project.model.DiningReservation;

import com.example.a2340project.model.ReservationManager;
import com.example.a2340project.model.ReservationObserver;
import com.example.a2340project.model.SortStrategy;

import com.example.a2340project.model.DiningReservationStorage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

import java.util.Collections;
import java.util.Locale;
import com.example.a2340project.model.SortByDate;


import android.widget.TextView;

public class DiningEstablishment extends AppCompatActivity implements ReservationObserver {
  

    private DatabaseReference database;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText locationEditText;
    private EditText websiteEditText;
    private EditText timeEditText;

    private ReservationManager reservationManager;
    private List<DiningReservation> reservations; // this is the sorted list to display

    //private LinearLayout FormLayout;
    private GridLayout reservationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining_establishment);

        reservationManager = new ReservationManager();
        //reservationManager.addObserver(this);

        // Set default sorting strategy
        reservationManager.setSortStrategy(new SortByDate());

        Button addReservationButton = findViewById(R.id.addReservationButton);
        LinearLayout reservationForm = findViewById(R.id.addReservationOverlay);
        locationEditText = findViewById(R.id.locationInput);
        websiteEditText = findViewById(R.id.websiteInput);
        timeEditText = findViewById(R.id.reservationTimeInput);
        Button saveButton = findViewById(R.id.saveButton);
        reservationList = findViewById(R.id.reservationList);

        database = FirebaseDatabase.getInstance().getReference();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addReservationButton.setOnClickListener(view -> {
            reservationForm.setVisibility(View.VISIBLE);
        });
        timeEditText.setOnClickListener(v -> showDatePicker(timeEditText));

        saveButton.setOnClickListener(view -> {
            String location = locationEditText.getText().toString().trim();
            String website = websiteEditText.getText().toString().trim();
            String time = timeEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(location) && !TextUtils.isEmpty(website)
                    && !TextUtils.isEmpty(time)) {
                saveDiningToDatabase();
                Toast.makeText(this, "Reservation saved for " + time,
                        Toast.LENGTH_SHORT).show();
                locationEditText.setText("");
                websiteEditText.setText("");
                timeEditText.setText("");
                reservationForm.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Please fill out all fields",
                        Toast.LENGTH_SHORT).show();
            }
        });
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
        } else {
            showAlert("Authentication Required", "Please log in to continue.");
            startActivity(new Intent(DiningEstablishment.this, LoginActivity.class));
            finish();
            return;
        }
        setupNavigationButtons();

        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(view -> showSortDialog());

    }

    private void showSortDialog() {
        String[] options = {"Sort by Date", "Sort by Time"};

        new AlertDialog.Builder(this)
                .setTitle("Sort Reservations")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        sortByDate();
                    } else {
                        sortByTime();
                    }
                    // Refresh the UI
                    loadReservations();
                })
                .show();
    }

    private void sortByDate() {
        Collections.sort(reservations, (r1, r2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
                Date date1 = sdf.parse(r1.getTime());
                Date date2 = sdf.parse(r2.getTime());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    private void sortByTime() {
        Collections.sort(reservations, (r1, r2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
                String time1 = r1.getTime().split(" ")[1]; // Extract time portion
                String time2 = r2.getTime().split(" ")[1]; // Extract time portion
                Date t1 = sdf.parse(time1);
                Date t2 = sdf.parse(time2);
                return t1.compareTo(t2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    private void loadReservations() {
        List<DiningReservation> reservations = DiningReservationStorage.getInstance().
                getDiningReservations();
        // Sort logs by start date
        Collections.sort(reservations, (res1, res2) -> res1.getTime().compareTo(res2.getTime()));
        reservationList.removeAllViews();
        for (int i = 0; i < reservations.size(); i++) {
            DiningReservation log = reservations.get(i);
            View resView = createResView(log);

            if (isPastDate(log.getTime())) {
                resView.setBackgroundColor(ContextCompat.getColor(this,
                        R.color.past_date_background));
            }

            reservationList.addView(resView);
            // Add a divider line between entries
            if (i < reservations.size() - 1) { // Avoid adding a divider after the last item
                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2  // Height of the divider line
                ));
                divider.setBackgroundColor(ContextCompat.getColor(this,
                        R.color.divider_color)); // Set divider color
                reservationList.addView(divider);
            }
        }
    }
    private View createResView(DiningReservation reservation) {
        LinearLayout resLayout = new LinearLayout(this);
        resLayout.setOrientation(LinearLayout.VERTICAL);
        resLayout.setPadding(10, 10, 10, 10);

        TextView locationText = new TextView(this);
        locationText.setText(reservation.getLocation());
        resLayout.addView(locationText);

        TextView websiteText = new TextView(this);
        websiteText.setText("Website: " + reservation.getWebsite());
        resLayout.addView(websiteText);


        TextView timeText = new TextView(this);
        timeText.setText(reservation.getTime());
        resLayout.addView(timeText);
        return resLayout;
    }

    private void applySort(SortStrategy sortStrategy) {
        reservationManager.setSortStrategy(sortStrategy);
        reservations = reservationManager.sortReservations(reservations);
        // Refresh the UI with sorted data
    }

    // Called when a new reservation is added
    @Override
    public void onReservationAdded(DiningReservation reservation) {
        reservations.add(reservation);
        applySort(reservationManager.getSortStrategy());  // Apply current sorting strategy
        // Update UI
    }

    private void saveDiningToDatabase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        String location = locationEditText.getText().toString();
        String website = websiteEditText.getText().toString();
        String date = timeEditText.getText().toString();

        DiningReservation diningPlace = new DiningReservation(location, website, date);
        DiningReservationStorage.getInstance().addDiningReservation(diningPlace);
        DatabaseReference diningRef =
                database.child("users").child(userId).child(
                        "diningReservations");

        diningRef.push().setValue(diningPlace)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Dining reservation saved successfully",
                            Toast.LENGTH_SHORT).show();
                    loadReservations();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save dining reservation",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });

        // also adding to dining database
        diningRef =
                database.child("Dining");
        diningRef.push().setValue(diningPlace)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Dining reservation saved successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save dining reservation",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
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

    private void showDatePicker(final EditText dateInput) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    showTimePicker(dateInput, selectedYear, selectedMonth, selectedDay);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(final EditText dateInput, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    String formattedDateTime = String.format(Locale.US,
                            "%02d/%02d/%d %02d:%02d",
                            month + 1, day, year, selectedHour, selectedMinute);
                    dateInput.setText(formattedDateTime);
                }, hour, minute, true); // 'true' for 24-hour format
        timePickerDialog.show();
    }

    private void showAlert(String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.
                app.AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private boolean isPastDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            Date checkOutDate = sdf.parse(date);
            return checkOutDate.before(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }



}