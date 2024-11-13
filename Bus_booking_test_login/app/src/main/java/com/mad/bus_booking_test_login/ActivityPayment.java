package com.mad.bus_booking_test_login;

import android.os.Bundle;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityPayment extends AppCompatActivity {

    private TextView tv_from, tv_to, tv_booking_date, tv_route_name, tv_bus_fee, tv_no_seats_booked, tv_total_fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        //initialize components from activity_payment
        tv_from = findViewById(R.id.tv_from);
        tv_to = findViewById(R.id.tv_to);
        tv_booking_date = findViewById(R.id.tv_booking_date);
        tv_route_name = findViewById(R.id.tv_route_name);
        tv_bus_fee = findViewById(R.id.tv_bus_fare);
        tv_no_seats_booked = findViewById(R.id.tv_number_seats_booked);
        tv_total_fee = findViewById(R.id.tv_total_fee);

        //get values from intents

    }
}
