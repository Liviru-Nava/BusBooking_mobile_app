package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.adapter.BookingAdapter;
import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.BookingDAO;

public class ActivityBookingList extends AppCompatActivity {

    private BookingAdapter bookingAdapter;
    private RecyclerView recyclerViewBookings;
    private BookingDAO booking;
    private String userId, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_list);
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        booking = new BookingDAO(this);
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        getBookingsFromUserId(userId);
    }

    public void onNavigateHome(View view){
        Intent navigate_home = new Intent(this, ActivityPassengerHome.class);
        navigate_home.putExtra("name", name);
        navigate_home.putExtra("user_id", userId);
        startActivity(navigate_home);
    }
    private void getBookingsFromUserId(String userId) {
        Cursor cursor = booking.getBookingsFromUserId(userId);
        bookingAdapter = new BookingAdapter(this, cursor, userId);
        recyclerViewBookings.setAdapter(bookingAdapter);
    }
}
