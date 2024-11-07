package com.example.a2340project.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a2340project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

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
        EdgeToEdge.enable(this);
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accomRoomTypes, android.R.layout.simple_spinner_item);
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
                Intent intent = new Intent(Accommodations.this, DiningEstablishment.class);
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

    private void saveTravelLog() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        String location = accomLocInput.getText().toString();
        String checkIn = checkInInput.getText().toString();
        String checkOut = checkOutInput.getText().toString();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkIn.compareTo(checkOut) < 0) {
            Toast.makeText(this, "Check in date must be before check out date",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidDate(checkIn) || !isValidDate(checkOut)) {
            Toast.makeText(this, "Invalid date format! Use MM/DD/YYYY",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        TravelLog log = new TravelLog(location, checkIn, checkOut);
        DatabaseReference accommodation = database.child("users").child(userId).child("accommodation");
        TravelLogStorage.getInstance().addTravelLog(log);
        addLogToGrid(log);
        accommodation.push().setValue(log)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Accommodation saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save accommodation", Toast.LENGTH_SHORT).show();
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
        daysView.setPadding(8, 8, 8, 8);
        daysView.setTextSize(16);
        daysView.setGravity(Gravity.END);
        daysView.setLayoutParams(daysParams);

        rowLayout.addView(locationView);
        rowLayout.addView(daysView);

        accommodationList.addView(rowLayout);
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
}