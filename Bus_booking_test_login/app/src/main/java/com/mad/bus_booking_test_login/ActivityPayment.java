package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityPayment extends AppCompatActivity {

    private TextView tv_bus_name, tv_from, tv_to, tv_booking_date, tv_route_name, tv_bus_fee, tv_no_seats_booked, tv_total_fee;
    String busName, startingPoint, endingPoint, bookingDate, routeName, passengerId, busId;
    int busFee, seatBookedCount, totalFee;
    BookingDAO booking;
    PaymentDAO payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        //initialize the DAOs
        booking = new BookingDAO(this);
        payment = new PaymentDAO(this);

        //initialize components from activity_payment
        tv_bus_name = findViewById(R.id.tv_bus_name);
        tv_from = findViewById(R.id.tv_from);
        tv_to = findViewById(R.id.tv_to);
        tv_booking_date = findViewById(R.id.tv_booking_date);
        tv_route_name = findViewById(R.id.tv_route_name);
        tv_bus_fee = findViewById(R.id.tv_bus_fare);
        tv_no_seats_booked = findViewById(R.id.tv_number_seats_booked);
        tv_total_fee = findViewById(R.id.tv_total_fee);

        //get values from intents
        busName = getIntent().getStringExtra("bus_name");
        startingPoint = getIntent().getStringExtra("starting_point");
        endingPoint = getIntent().getStringExtra("ending_point");
        bookingDate = getIntent().getStringExtra("booking_date");
        routeName = getIntent().getStringExtra("route_name");

        passengerId = getIntent().getStringExtra("passenger_id");
        busId = getIntent().getStringExtra("bus_id");
        busFee = getIntent().getIntExtra("bus_fee", 0);
        seatBookedCount = getIntent().getIntExtra("seats_booked_count", 0);
        totalFee = busFee * seatBookedCount;

        //set the values to the TextViews
        tv_bus_name.setText(busName);
        tv_from.setText("From: " + startingPoint);
        tv_to.setText("To: " + endingPoint);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(bookingDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
            String formatted_date = outputFormat.format(date);
            tv_booking_date.setText(formatted_date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        tv_route_name.setText("Route: " + routeName);
        tv_bus_fee.setText("Rs. " + busFee);
        tv_no_seats_booked.setText(String.valueOf(seatBookedCount));
        tv_total_fee.setText("Rs. " + totalFee);
    }

    //pay now method
    public void onPayNowClicked(View view){
        //Insert booking and retrieve booking ID
        String bookingId = booking.insertBooking(passengerId, busId, bookingDate, totalFee);

        //Insert seat details for the booking
        ArrayList<String> seatIds = getIntent().getStringArrayListExtra("seat_ids");
        booking.insertBookingSeats(bookingId, seatIds, bookingDate);

        //Insert payment details
        String paymentMethod = "Card"; // or retrieve dynamically based on user's choice
        payment.insertPayment(bookingId, passengerId, paymentMethod, totalFee);

        //Display message to mention booking successful
        Toast.makeText(this, "Booking successful!", Toast.LENGTH_LONG).show();

        //Navigate to the passenger's home page
        Intent navigate_passenger_home_intent = new Intent(this, ActivityPassengerHome.class);
        startActivity(navigate_passenger_home_intent);
    }
}
