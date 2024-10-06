package com.example.a2340project.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a2340project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Accommodations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(Accommodations.this, HomeScreen.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Logistics) {
                    startActivity(new Intent(Accommodations.this, Logistics.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Destination) {
                    startActivity(new Intent(Accommodations.this, Logistics.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Diningestablishment) {
                    startActivity(new Intent(Accommodations.this, DiningEstablishment.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Accommodations) {
                    return true;
                }
                else if (item.getItemId() == R.id.Travelcommunity) {
                    startActivity(new Intent(Accommodations.this, TravelCommunity.class));
                    return true;
                }
                return true;
            }
        });
    }

}