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
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a2340project.R;
import com.example.a2340project.model.DiningReservation;
import com.example.a2340project.model.DiningReservationStorage;
import com.example.a2340project.model.TravelLog;
import com.example.a2340project.model.TravelLogStorage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import android.os.Bundle;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiningEstablishment extends AppCompatActivity {
    private DatabaseReference database;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText locationEditText;
    private EditText websiteEditText;
    private EditText timeEditText;
    //private LinearLayout FormLayout;
    private GridLayout reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining_establishment);

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
            if (!TextUtils.isEmpty(location) && !TextUtils.isEmpty(website) && !TextUtils.isEmpty(time)) {
                saveDiningToDatabase();
                Toast.makeText(this, "Reservation saved for " + time, Toast.LENGTH_SHORT).show();
                locationEditText.setText("");
                websiteEditText.setText("");
                timeEditText.setText("");
                reservationForm.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
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
    }
    private void loadReservations() {
        List<DiningReservation> reservations = DiningReservationStorage.getInstance().getDiningReservations();
        // Sort logs by start date
        Collections.sort(reservations, (res1, res2) -> res1.getTime().compareTo(res2.getTime()));
        reservationList.removeAllViews();
        for (int i = 0; i < reservations.size(); i++) {
            DiningReservation log = reservations.get(i);
            View resView = createResView(log);

            //if (isPastDate(log.getEndDate())) {
           //     logView.setBackgroundColor(ContextCompat.getColor(this, R.color.past_date_background));
           // }

            reservationList.addView(resView);
            // Add a divider line between entries
            if (i < reservations.size() - 1) { // Avoid adding a divider after the last item
                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2  // Height of the divider line
                ));
                divider.setBackgroundColor(ContextCompat.getColor(this, R.color.divider_color)); // Set divider color
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
                database.child("users").child(userId).child("diningReservations");

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
                    String formattedDateTime = String.format(Locale.US, "%02d/%02d/%d %02d:%02d",
                            month + 1, day, year, selectedHour, selectedMinute);
                    dateInput.setText(formattedDateTime);
                }, hour, minute, true); // 'true' for 24-hour format
        timePickerDialog.show();
    }

    private void showAlert(String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}