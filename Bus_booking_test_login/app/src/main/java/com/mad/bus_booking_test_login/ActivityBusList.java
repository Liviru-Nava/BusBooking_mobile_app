package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

public class ActivityBusList extends AppCompatActivity {
    private TextView tv_starting_point, tv_ending_point, tv_booking_date;
    private RecyclerView recyclerViewBuses;
    private BusAdapter busAdapter;
    private BusDAO bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus_list);
        tv_starting_point = findViewById(R.id.tv_starting_point);
        tv_ending_point = findViewById(R.id.tv_ending_point);
        tv_booking_date = findViewById(R.id.tv_booking_date);
        recyclerViewBuses = findViewById(R.id.recyclerViewBuses);
        recyclerViewBuses.setLayoutManager(new LinearLayoutManager(this));
        bus = new BusDAO(this);

        Intent intent = getIntent();
        String booking_date = intent.getStringExtra("selected_date");
        String starting_point = intent.getStringExtra("starting_point");
        String ending_point = intent.getStringExtra("ending_point");

        tv_starting_point.setText(starting_point);
        tv_ending_point.setText(ending_point);

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(booking_date);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
            String formatted_date = outputFormat.format(date);
            tv_booking_date.setText(formatted_date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        loadBusesByRouteAndBookingDate(starting_point, ending_point, booking_date);
    }

    private void loadBusesByRouteAndBookingDate(String startingPoint, String endingPoint, String bookingDate) {
        Cursor cursor = bus.getBusesByRouteAndBookingDate(startingPoint, endingPoint, bookingDate);
        busAdapter = new BusAdapter(this, cursor);
        recyclerViewBuses.setAdapter(busAdapter);
    }
}
