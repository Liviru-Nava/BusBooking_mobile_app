package com.mad.bus_booking_test_login.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.NotificationDAO;
import com.mad.bus_booking_test_login.data_access_objects.SeatDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivityChangeSeat extends AppCompatActivity {

    TextView tv_bus_name, tv_route_name, tv_booking_date, tv_starting_point, tv_ending_point, tv_driver_name, tv_selected_seats;
    String userId, bookingId, busId, bookingDate;
    SeatDAO seat;
    NotificationDAO notification;
    private List<Button> seatButtons = new ArrayList<>();
    private Set<Integer> bookedSeats = new HashSet<>();
    private Set<Integer> selectedSeats = new HashSet<>();
    private Set<Integer> alreadyBookedSeats = new HashSet<>();
    private int seatsToSwap = 0; // Default mode is swapping

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


        seat = new SeatDAO(this);
        notification = new NotificationDAO(this);

        busId = getIntent().getStringExtra("bus_id");
        String busName = getIntent().getStringExtra("bus_name");
        String routeName = getIntent().getStringExtra("route_name");
        String startingPoint = getIntent().getStringExtra("starting_point");
        String endingPoint = getIntent().getStringExtra("ending_point");
        String driverName = getIntent().getStringExtra("driver_name");
        userId = getIntent().getStringExtra("user_id");
        bookingId = getIntent().getStringExtra("booking_id");
        bookingDate = getIntent().getStringExtra("booking_date");


        tv_bus_name.setText(busName);
        tv_route_name.setText(routeName);
        tv_booking_date.setText(bookingDate);
        tv_starting_point.setText(startingPoint);
        tv_ending_point.setText(endingPoint);
        tv_driver_name.setText(driverName);

        // Load initially booked seats and determine how many seats to swap
        seatsToSwap = seat.getBookedSeatsForBooking(bookingId).size();

        //initialise the seats
        initializeSeats();
        loadBookedSeats(busId, bookingDate);

        // Handle seat change confirmation
        findViewById(R.id.btn_confirm_seat_change).setOnClickListener(view -> confirmSeatChange());

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
                bookedSeats.add(index); // Add to the bookedSeats set

                if (seat.isSeatBookedByUser(index + 1, bookingId, busId)) {
                    seatButton.setEnabled(false);
                } else {
                    alreadyBookedSeats.add(index);
                }
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
            if (selectedSeats.size() >= seatsToSwap) {
                Toast.makeText(this, "You can only select " + seatsToSwap + " seats.", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedSeats.add(seatNumber);
            if (alreadyBookedSeats.contains(seatIndex)) {
                seatButton.setBackgroundColor(Color.parseColor("#FFA500")); // Orange for already booked seats
            } else {
                seatButton.setBackgroundColor(Color.GREEN);
            }
            seatButton.setTextColor(Color.WHITE);
        }

        // Update TextView to display selected seat numbers
        tv_selected_seats.setText("Selected Seats: " + selectedSeats.toString().replaceAll("[\\[\\]]", ""));
    }

    private void confirmSeatChange() {
        if (selectedSeats.size() != seatsToSwap) {
            Toast.makeText(this, "Please select exactly " + seatsToSwap + " seats.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (alreadyBookedSeats.stream().anyMatch(index -> selectedSeats.contains(index + 1))) {
            // Notify user for a seat swap request
            seat.sendSeatSwapRequest(userId, bookingId, new ArrayList<>(selectedSeats), busId, bookingDate, notification);
            Toast.makeText(this, "Swap request sent to the other passenger.", Toast.LENGTH_LONG).show();
        } else {
            // Perform direct seat swap
            boolean success = seat.swapSeats(bookingId, new ArrayList<>(selectedSeats), busId, bookingDate);
            if (success) {
                Toast.makeText(this, "Seats successfully changed.", Toast.LENGTH_LONG).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(this, "Failed to update seats. Try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
