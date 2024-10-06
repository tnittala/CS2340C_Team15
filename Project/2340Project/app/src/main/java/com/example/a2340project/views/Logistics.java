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

public class Logistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);
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
                    startActivity(new Intent(Logistics.this, HomeScreen.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Logistics) {
                    return true;
                }
                else if (item.getItemId() == R.id.Destination) {
                    startActivity(new Intent(Logistics.this, Destination.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Diningestablishment) {
                    startActivity(new Intent(Logistics.this, DiningEstablishment.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Accommodations) {
                    startActivity(new Intent(Logistics.this, Accommodations.class));
                    return true;
                }
                else if (item.getItemId() == R.id.Travelcommunity) {
                    startActivity(new Intent(Logistics.this, TravelCommunity.class));
                    return true;
                }
                return true;
            }
        });
    }
}