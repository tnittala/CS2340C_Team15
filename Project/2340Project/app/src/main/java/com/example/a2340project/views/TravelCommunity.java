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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.a2340project.model.TravelLog;

import com.example.a2340project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import androidx.core.content.ContextCompat;

public class TravelCommunity extends AppCompatActivity {
    private EditText startDate;
    private EditText endDate;
    private EditText destinationInput;
    private EditText accommodationInput;
    private EditText dinInput;
    private EditText notesInput;

    private LinearLayout travelFormLayout;
    private GridLayout postsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_community);

        startDate = findViewById(R.id.startDateInput);
        endDate = findViewById(R.id.endDateInput);
        destinationInput = findViewById(R.id.locationInput);
        accommodationInput = findViewById(R.id.accomInput);
        dinInput = findViewById(R.id.diningInput);
        notesInput = findViewById(R.id.notes);
        travelFormLayout = findViewById(R.id.travelCommFormLayout);
        postsList = findViewById(R.id.postsList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startDate.setOnClickListener(v -> showDatePicker(startDate));
        endDate.setOnClickListener(v -> showDatePicker(endDate));
        findViewById(R.id.addTravelPost).setOnClickListener(v -> toggleFormVisibility());
        findViewById(R.id.saveLogButton).setOnClickListener(v -> savePost());
        findViewById(R.id.cancelLogButton).setOnClickListener(v -> {
            travelFormLayout.setVisibility(View.GONE);
            clearForm();
        });

        // home button
        ImageButton homeBtn = findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelCommunity.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        // destinations button
        ImageButton destinationsBtn = findViewById(R.id.destinationsButton);
        destinationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelCommunity.this, Destination.class);
                startActivity(intent);
            }
        });

        // logistics button
        ImageButton logisticsBtn = findViewById(R.id.logisticsButton);
        logisticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelCommunity.this, Logistics.class);
                startActivity(intent);
            }
        });

        // dining button
        ImageButton diningBtn = findViewById(R.id.diningButton);
        diningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelCommunity.this, DiningEstablishment.class);
                startActivity(intent);
            }
        });

        // accommodations button
        ImageButton accommodationsBtn = findViewById(R.id.accommodationsButton);
        accommodationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelCommunity.this, Accommodations.class);
                startActivity(intent);
            }
        });

        // community button
        ImageButton communityBtn = findViewById(R.id.communityButton);
        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelCommunity.this, TravelCommunity.class);
                startActivity(intent);
            }
        });
    }

    private void toggleFormVisibility() {
        if (travelFormLayout.getVisibility() == View.GONE) {
            travelFormLayout.setVisibility(View.VISIBLE);
        } else {
            travelFormLayout.setVisibility(View.GONE);
        }
    }

    private void toggleResultVisibility(boolean showResult) {
        if (showResult) {
            findViewById(R.id.addTravelPost).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.addTravelPost).setVisibility(View.VISIBLE);
        }
    }

    private void clearForm() {
        destinationInput.setText("");
        startDate.setText("");
        endDate.setText("");
    }

    private void savePost() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        String location = destinationInput.getText().toString();
        String startDateInput = startDate.getText().toString();
        String endDateInput = endDate.getText().toString();
        String dining = dinInput.getText().toString();
        String accomodation = accommodationInput.getText().toString();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location.isEmpty() || dining.isEmpty() || accomodation.isEmpty() || startDateInput.isEmpty() || endDateInput.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startDateInput.compareTo(endDateInput) > 0) {
            Toast.makeText(this, "Start date must be before end date",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidDate(startDateInput) || !isValidDate(endDateInput)) {
            Toast.makeText(this, "Invalid date format! Use MM/DD/YYYY",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        clearForm();
        travelFormLayout.setVisibility(View.GONE);
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

    private void resetView() {
        // Show main buttons and views
        findViewById(R.id.addTravelPost).setVisibility(View.VISIBLE);
    }
}