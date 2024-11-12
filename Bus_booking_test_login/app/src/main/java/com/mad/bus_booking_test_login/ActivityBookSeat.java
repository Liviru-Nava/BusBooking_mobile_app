package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityBookSeat extends AppCompatActivity {

    TextView tv_bus_name, tv_license, tv_seats_available, tv_fee, tv_route, tv_departure_time,tv_starting_point, tv_ending_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_seat);
        tv_bus_name = findViewById(R.id.tv_bus_name);
        tv_license = findViewById(R.id.tv_license);
        tv_seats_available = findViewById(R.id.tv_seats_available);
        tv_fee = findViewById(R.id.tv_fee);
        tv_route = findViewById(R.id.tv_route);
        tv_departure_time = findViewById(R.id.tv_departure_time);
        tv_starting_point = findViewById(R.id.tv_starting_point);
        tv_ending_point = findViewById(R.id.tv_ending_point);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String busName = intent.getStringExtra("bus_name");
        String licenseNumber = intent.getStringExtra("bus_license");
        int seatsAvailable = intent.getIntExtra("no_of_seats_available", 0);
        int busFee = intent.getIntExtra("bus_fee", 0);
        String departureTime = intent.getStringExtra("departure_time");
        String routeName = intent.getStringExtra("route_name");
        String startingPoint = intent.getStringExtra("starting_point");
        String endingPoint = intent.getStringExtra("ending_point");

        tv_bus_name.setText(busName);
        tv_license.setText(licenseNumber);
        tv_seats_available.setText(String.valueOf(seatsAvailable));
        tv_fee.setText(String.valueOf(busFee));
        tv_departure_time.setText(departureTime);
        tv_route.setText(routeName);
        tv_starting_point.setText(startingPoint);
        tv_ending_point.setText(endingPoint);
    }
}
