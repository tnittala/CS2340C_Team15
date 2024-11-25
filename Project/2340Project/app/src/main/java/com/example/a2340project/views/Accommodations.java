package com.example.a2340project.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.a2340project.model.TravelLogStorage;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.a2340project.model.TravelLog;

import com.example.a2340project.R;
import com.example.a2340project.model.TravelLog;
import com.example.a2340project.model.TravelLogStorage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;


public class Accommodations extends AppCompatActivity {

    private EditText checkInInput;
    private EditText checkOutInput;
    private EditText accomLocInput;
    private EditText numRoomsInput;
    private TextView roomTypeInput;
    private Spinner dropdownRoomType;
    private LinearLayout accomFormLayout;
    private GridLayout accommodationList;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations);

        checkInInput = findViewById(R.id.checkIn);
        checkOutInput = findViewById(R.id.checkOut);
        accomLocInput = findViewById(R.id.accommodationLocation);
        numRoomsInput = findViewById(R.id.numRooms);
        roomTypeInput = findViewById(R.id.roomType);
        dropdownRoomType = findViewById(R.id.dropdown_roomTypes);
        accomFormLayout = findViewById(R.id.accommodationFormLayout);
        accommodationList = findViewById(R.id.accommodationList);

        database = FirebaseDatabase.getInstance().getReference();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkInInput.setOnClickListener(v -> showDatePicker(checkInInput));
        checkOutInput.setOnClickListener(v -> showDatePicker(checkOutInput));
        findViewById(R.id.addNewAccommodation).setOnClickListener(v -> toggleAccomFormVisibility());
        findViewById(R.id.saveAccommodation).setOnClickListener(v -> saveTravelLog());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.accomRoomTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dropdownRoomType.setAdapter(adapter);

        // home button
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accommodations.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        // destinations button
        ImageButton destinationsBtn = findViewById(R.id.destinationsButton);
        destinationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accommodations.this, Destination.class);
                startActivity(intent);
            }
        });

        // logistics button
        ImageButton logisticsBtn = findViewById(R.id.logisticsButton);
        logisticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accommodations.this, Logistics.class);
                startActivity(intent);
            }
        });

        // dining button
        ImageButton diningBtn = findViewById(R.id.diningButton);
        diningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accommodations.this,
                        DiningEstablishment.class);
                startActivity(intent);
            }
        });

        // accommodations button
        ImageButton accommodationsBtn = findViewById(R.id.accommodationsButton);
        accommodationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accommodations.this, Accommodations.class);
                startActivity(intent);
            }
        });

        // community button
        ImageButton communityBtn = findViewById(R.id.communityButton);
        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Accommodations.this, TravelCommunity.class);
                startActivity(intent);
            }
        });
    }

    private void toggleAccomFormVisibility() {
        if (accomFormLayout.getVisibility() == View.GONE) {
            accomFormLayout.setVisibility(View.VISIBLE);
        } else {
            accomFormLayout.setVisibility(View.GONE);
        }
    }

    private void loadReservations() {
        List<TravelLog> logs = TravelLogStorage.getInstance().getTravelLogs();

        // Sort logs by start date
        Collections.sort(logs, (log1, log2) -> log1.getStartDate().compareTo(log2.getStartDate()));
        accommodationList.removeAllViews();

        for (int i = 0; i < logs.size(); i++) {
            TravelLog log = logs.get(i);
            View logView = createLogView(log);

            if (isPastDate(log.getEndDate())) {
                logView.setBackgroundColor(ContextCompat.getColor(this,
                        R.color.past_date_background));
            }

            accommodationList.addView(logView);

            // Add a divider line between entries
            if (i < logs.size() - 1) { // Avoid adding a divider after the last item
                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2  // Height of the divider line
                ));
                divider.setBackgroundColor(ContextCompat.getColor(this,
                        R.color.divider_color));
                accommodationList.addView(divider);
            }
        }
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
        List<TravelLog> logs = TravelLogStorage.getInstance().getTravelLogs(); // Fetch logs
        Collections.sort(logs, (log1, log2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                Date date1 = sdf.parse(log1.getStartDate());
                Date date2 = sdf.parse(log2.getStartDate());
                return date1.compareTo(date2); // Ascending order
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
        loadReservations(); // Refresh UI
    }

    private void sortByTime() {
        List<TravelLog> logs = TravelLogStorage.getInstance().getTravelLogs(); // Fetch logs
        Collections.sort(logs, (log1, log2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                Date date1 = sdf.parse(log1.getEndDate());
                Date date2 = sdf.parse(log2.getEndDate());
                return date1.compareTo(date2); // Ascending order
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
        loadReservations(); // Refresh UI
    }

    private View createLogView(TravelLog log) {
        LinearLayout logLayout = new LinearLayout(this);
        logLayout.setOrientation(LinearLayout.VERTICAL);
        logLayout.setPadding(10, 10, 10, 10);

        TextView locationText = new TextView(this);
        locationText.setText(log.getLocation());
        logLayout.addView(locationText);

        TextView checkInText = new TextView(this);
        checkInText.setText("Check-in: " + log.getStartDate());
        logLayout.addView(checkInText);

        TextView checkOutText = new TextView(this);
        checkOutText.setText("Check-out: " + log.getEndDate());
        logLayout.addView(checkOutText);

        TextView roomTypeText = new TextView(this);
        roomTypeText.setText(log.getRoomType());
        logLayout.addView(roomTypeText);

        return logLayout;
    }

    public static boolean isPastDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            Date checkOutDate = sdf.parse(date);
            return checkOutDate.before(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showDatePicker(EditText dateInput) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date to MM/DD/YY
                    String formattedDate = String.format("%d/%02d/%02d",
                            selectedMonth + 1, selectedDay, selectedYear % 100);
                    dateInput.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void saveTravelLog() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();
        String location = accomLocInput.getText().toString();
        String checkIn = checkInInput.getText().toString();
        String checkOut = checkOutInput.getText().toString();
        String roomType = dropdownRoomType.getSelectedItem().toString();

        if (location.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkIn.compareTo(checkOut) > 0) {
            Toast.makeText(this, "Check-in date must be before check-out date",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidDate(checkIn) || !isValidDate(checkOut)) {
            Toast.makeText(this, "Invalid date format! Use MM/DD/YY",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        TravelLog log = new TravelLog(location, checkIn, checkOut, roomType);
        DatabaseReference accommodation = database.child("users").child(userId).child(
                "accommodation");

        TravelLogStorage.getInstance().addTravelLog(log);
        accommodation.push().setValue(log)
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(this, "Accommodation saved successfully",
                            Toast.LENGTH_SHORT).show();
                    loadReservations();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save accommodation",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });

        clearForm();
        accomFormLayout.setVisibility(View.GONE);
    }


    private void clearForm() {
        checkInInput.setText("");
        checkOutInput.setText("");
        accomLocInput.setText("");
        numRoomsInput.setText("");
        roomTypeInput.setText("");
    }

    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}