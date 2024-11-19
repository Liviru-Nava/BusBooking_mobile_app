package com.mad.bus_booking_test_login;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityChangeSeat extends AppCompatActivity {

    TextView tv_bus_name, tv_route_name, tv_booking_date, tv_starting_point, tv_ending_point, tv_driver_name, tv_selected_seats;
    String userId, bookingId;
    Button btn_confirm_swap, btn_request_swap;
    SeatDAO seat;
    private List<Button> seatButtons = new ArrayList<>();
    private Set<Integer> bookedSeats = new HashSet<>();
    private Set<Integer> selectedSeats = new HashSet<>();
    private boolean isSwapMode = true; // Default mode is swapping

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_seat);
        tv_bus_name = findViewById(R.id.tv_bus_name);
        tv_route_name = findViewById(R.id.tv_route_name);
        tv_booking_date = findViewById(R.id.tv_booking_date);
        tv_starting_point = findViewById(R.id.tv_starting_point);
        tv_ending_point = findViewById(R.id.tv_ending_point);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_selected_seats = findViewById(R.id.tv_selected_seats);

        btn_confirm_swap = findViewById(R.id.btn_confirm_swap);
        btn_request_swap = findViewById(R.id.btn_request_swap);

        seat = new SeatDAO(this);

        String busId = getIntent().getStringExtra("bus_id");
        String busName = getIntent().getStringExtra("bus_name");
        String routeName = getIntent().getStringExtra("route_name");
        String startingPoint = getIntent().getStringExtra("starting_point");
        String endingPoint = getIntent().getStringExtra("ending_point");
        String driverName = getIntent().getStringExtra("driver_name");
        userId = getIntent().getStringExtra("user_id");
        bookingId = getIntent().getStringExtra("booking_id");
        String bookingDate = getIntent().getStringExtra("booking_date");


        tv_bus_name.setText(busName);
        tv_route_name.setText(routeName);
        tv_booking_date.setText(bookingDate);
        tv_starting_point.setText(startingPoint);
        tv_ending_point.setText(endingPoint);
        tv_driver_name.setText(driverName);

        //initialise the seats
        initializeSeats();
        loadBookedSeats(busId, bookingDate);


        // Toggle modes
        btn_confirm_swap.setOnClickListener(view -> {
            isSwapMode = true;
            Toast.makeText(this, "Swap Mode Enabled", Toast.LENGTH_SHORT).show();
        });

        btn_request_swap.setOnClickListener(view -> {
            isSwapMode = false;
            Toast.makeText(this, "Request Swap Mode Enabled", Toast.LENGTH_SHORT).show();
        });

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
            seatButton.setOnClickListener(view -> handleSeatSelection(seatButton, seatIndex));
        }
    }
    private void loadBookedSeats(String busId, String bookingDate) {
        // Fetch booked seats from SeatDAO
        List<Integer> bookedSeatIndices = seat.getBookedSeats(busId, bookingDate);

        // If the list is empty, all seats are available, otherwise update booked seats
//        if (!bookedSeatIndices.isEmpty()) {
//            for (int index : bookedSeatIndices) {
//                Button seatButton = seatButtons.get(index);
//                seatButton.setBackgroundColor(Color.RED);
//                seatButton.setEnabled(false); // Disable selection for booked seats
//                bookedSeats.add(index); // Add to the bookedSeats set
//            }
//        }
        if (!bookedSeatIndices.isEmpty()) {
            for (int index : bookedSeatIndices) {
                Button seatButton = seatButtons.get(index);
                seatButton.setBackgroundColor(Color.RED);
                bookedSeats.add(index);
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






    //-----------swapping part for testing------------------

    private void handleSeatSelection(Button seatButton, int seatIndex) {
        if (isSwapMode) {
            handleSwapMode(seatButton, seatIndex);
        } else {
            handleRequestSwap(seatIndex);
        }
    }

    private void handleSwapMode(Button seatButton, int seatIndex) {
        int seatNumber = seatIndex + 1;

        if (selectedSeats.contains(seatNumber)) {
            // Deselect seat
            selectedSeats.remove(seatNumber);
            seatButton.setBackgroundColor(Color.WHITE);
        } else {
            selectedSeats.add(seatNumber);
            seatButton.setBackgroundColor(Color.GREEN);
        }

        // Update TextView
        tv_selected_seats.setText("Selected Seats: " + selectedSeats.toString());
    }

    private void handleRequestSwap(int seatIndex) {
        if (bookedSeats.contains(seatIndex)) {
            int seatNumber = seatIndex + 1;
            Toast.makeText(this, "Request sent for Seat " + seatNumber, Toast.LENGTH_SHORT).show();
            // Simulate sending a request
            sendSwapRequest(seatNumber);
        } else {
            Toast.makeText(this, "Select a booked seat to send a swap request.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSwapRequest(int seatNumber) {
        // Logic to send a notification or request (can be expanded with server-side implementation)
        System.out.println("Request sent for seat swap to seat: " + seatNumber);
    }
}
