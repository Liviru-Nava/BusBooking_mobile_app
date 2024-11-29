package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.GridLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.SeatDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityBookSeat extends AppCompatActivity {

    TextView tv_bus_name, tv_license, tv_seats_available, tv_fee, tv_route, tv_departure_time,tv_starting_point, tv_ending_point, tv_driver_name, tv_selected_seats;
    SeatDAO seat;
    String startingPoint, endingPoint, driverId, driverName;

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
        seat = new SeatDAO(this);

        // Retrieve data from Intent
        String busId = getIntent().getStringExtra("bus_id");
        String busName = getIntent().getStringExtra("bus_name");
        String licenseNumber = getIntent().getStringExtra("bus_license");
        int seatsAvailable = getIntent().getIntExtra("no_of_seats_available", 0);
        int busFee = getIntent().getIntExtra("bus_fee", 0);
        String departureTime = getIntent().getStringExtra("departure_time");
        String routeName = getIntent().getStringExtra("route_name");
        startingPoint = getIntent().getStringExtra("starting_point");
        endingPoint = getIntent().getStringExtra("ending_point");
        driverName = getIntent().getStringExtra("driver_name");
        String bookingDate = getIntent().getStringExtra("booking_date");
        driverId = getIntent().getStringExtra("driver_id");

        tv_bus_name.setText(busName);
        tv_license.setText(licenseNumber);
        tv_seats_available.setText(String.valueOf(seatsAvailable));
        tv_fee.setText(String.valueOf(busFee));
        tv_departure_time.setText(departureTime);
        tv_route.setText(routeName);
        tv_starting_point.setText(startingPoint);
        tv_ending_point.setText(endingPoint);
        tv_driver_name.setText(driverName);

        // Initialize the seats
        initializeSeats();
        loadBookedSeats(busId, bookingDate);
    }

    //method to confirm booking
    public void onConfirmBooking(View view){
        String busName = getIntent().getStringExtra("bus_name");
        String startingPoint = getIntent().getStringExtra("starting_point");
        String endingPoint = getIntent().getStringExtra("ending_point");
        String bookingDate = getIntent().getStringExtra("booking_date");
        String routeName = getIntent().getStringExtra("route_name");
        int busFee = getIntent().getIntExtra("bus_fee", 0);
        String passengerId = getIntent().getStringExtra("user_id");
        String passengerName = getIntent().getStringExtra("name");
        String busId = getIntent().getStringExtra("bus_id");

        // Step 1: Get the number of seats selected
        int seatsBookedCount = selectedSeats.size();

        // Step 2: Retrieve seat IDs for the selected seats
        List<String> seatIds = new ArrayList<>();
        for (int seatNumber : selectedSeats) {
            String seatId = seat.getSeatId(busId, seatNumber); // Retrieve seat_id for the bus and seat number
            seatIds.add(seatId);
        }

        // Step 3: Pass values to ActivityPayment
        Intent intent = new Intent(this, ActivityPayment.class);
        intent.putExtra("bus_name", busName);
        intent.putExtra("starting_point", startingPoint);
        intent.putExtra("ending_point", endingPoint);
        intent.putExtra("booking_date", bookingDate);
        intent.putExtra("route_name", routeName);
        intent.putExtra("bus_fee", busFee);
        intent.putExtra("user_id", passengerId);
        intent.putExtra("name", passengerName);
        intent.putExtra("bus_id", busId);
        intent.putExtra("seats_booked_count", seatsBookedCount);
        intent.putStringArrayListExtra("seat_ids", new ArrayList<>(seatIds));

//        Log.e("Passed User_id", passengerId);

        startActivity(intent);
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

    private void loadBookedSeats(String busId, String bookingDate) {
        // Fetch booked seats from SeatDAO
        List<Integer> bookedSeatIndices = seat.getBookedSeats(busId, bookingDate);

        // If the list is empty, all seats are available, otherwise update booked seats
        if (!bookedSeatIndices.isEmpty()) {
            for (int index : bookedSeatIndices) {
                Button seatButton = seatButtons.get(index);
                seatButton.setBackgroundColor(Color.RED);
                seatButton.setEnabled(false); // Disable selection for booked seats
                bookedSeats.add(index); // Add to the bookedSeats set
            }
        }
    }

    private void toggleSeatSelection(Button seatButton, int seatIndex) {
        // Seat numbers are 1-based, so add 1 to the index
        int seatNumber = seatIndex + 1;

        if (selectedSeats.contains(seatNumber)) {
            // Deselect seat
            selectedSeats.remove(seatNumber);
            seatButton.setBackgroundColor(Color.WHITE);
            seatButton.setTextColor(Color.parseColor("#1149EB"));
        } else {
            // Select seat
            selectedSeats.add(seatNumber);
            seatButton.setBackgroundColor(Color.GREEN);
            seatButton.setTextColor(Color.WHITE);
        }

        // Update TextView to display selected seat numbers
        tv_selected_seats.setText("Selected Seats: " + selectedSeats.toString().replaceAll("[\\[\\]]", ""));
    }

    public void onShowMap(View view){
        Intent navigate_map_intent = new Intent(this, ActivityMaps.class);
        navigate_map_intent.putExtra("starting_point", startingPoint);
        navigate_map_intent.putExtra("ending_point", endingPoint);
        startActivity(navigate_map_intent);
    }

}
