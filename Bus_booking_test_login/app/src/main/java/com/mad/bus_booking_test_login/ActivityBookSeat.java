package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityBookSeat extends AppCompatActivity {

    TextView tv_bus_name, tv_license, tv_seats_available, tv_fee, tv_route, tv_departure_time,tv_starting_point, tv_ending_point, tv_driver_name, tv_selected_seats;

    private List<Button> seatButtons = new ArrayList<>();
    private Set<Integer> bookedSeats = new HashSet<>();
    private Set<Integer> selectedSeats = new HashSet<>();

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
        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_selected_seats = findViewById(R.id.tv_selected_seats);

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
        String driverName = intent.getStringExtra("name");

        tv_bus_name.setText(busName);
        tv_license.setText(licenseNumber);
        tv_seats_available.setText(String.valueOf(seatsAvailable));
        tv_fee.setText(String.valueOf(busFee));
        tv_departure_time.setText(departureTime);
        tv_route.setText(routeName);
        tv_starting_point.setText(startingPoint);
        tv_ending_point.setText(endingPoint);
        tv_driver_name.setText(driverName);
    }

    private void initializeSeats() {
        GridLayout gridLayout = findViewById(R.id.grid_seats);

        // Set up 16 buttons for seats
        for (int i = 0; i < 16; i++) {
            Button seatButton = (Button) gridLayout.getChildAt(i);
            seatButtons.add(seatButton);

            final int seatIndex = i;
            seatButton.setBackgroundColor(Color.WHITE); // Initially all seats are available (white)

            // Set a click listener for each seat to handle selection
            seatButton.setOnClickListener(view -> toggleSeatSelection(seatButton, seatIndex));
        }
    }

    private void loadBookedSeats() {
        // Example: Adding booked seats to bookedSeats set (in a real scenario, get these from the server)
        bookedSeats.add(2); // Seat 3 is booked
        bookedSeats.add(7); // Seat 8 is booked
        bookedSeats.add(12); // Seat 13 is booked

        // Update seat buttons to show booked seats in red
        for (int index : bookedSeats) {
            Button seatButton = seatButtons.get(index);
            seatButton.setBackgroundColor(Color.RED);
            seatButton.setEnabled(false); // Disable selection for booked seats
        }
    }

    private void toggleSeatSelection(Button seatButton, int seatIndex) {
        if (selectedSeats.contains(seatIndex)) {
            // Deselect seat
            selectedSeats.remove(seatIndex);
            seatButton.setBackgroundColor(Color.WHITE);
        } else {
            // Select seat
            selectedSeats.add(seatIndex);
            seatButton.setBackgroundColor(Color.GREEN);
        }

        // Update TextView to display selected seats
        tv_selected_seats.setText("Selected Seats: " + selectedSeats.toString());
    }
}
