package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.adapter.DriverBookingAdapter;
import com.mad.bus_booking_test_login.data_access_objects.BookingDAO;

public class ActivityDriverBookingList extends AppCompatActivity {

    private DriverBookingAdapter driverBookingAdapter;
    private RecyclerView recyclerViewDriverBookings;
    private BookingDAO booking;
    private String userId, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_booking_list);
        recyclerViewDriverBookings = findViewById(R.id.recyclerViewDriverBookings);
        recyclerViewDriverBookings.setLayoutManager(new LinearLayoutManager(this));
        booking = new BookingDAO(this);
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        getBookingsFromUserId(userId);
    }

    public void onNavigateHome(View view){
        Intent navigate_home = new Intent(this, ActivityDriverHome.class);
        navigate_home.putExtra("name", name);
        navigate_home.putExtra("user_id", userId);
        startActivity(navigate_home);
    }

    private void getBookingsFromUserId(String userId) {
        Cursor cursor = booking.getUpcomingBookings();
        driverBookingAdapter = new DriverBookingAdapter(this, cursor);
        recyclerViewDriverBookings.setAdapter(driverBookingAdapter);
    }
}
