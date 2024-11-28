package com.mad.bus_booking_test_login.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.adapter.NotificationAdapter;

public class ActivityNotificationList extends AppCompatActivity {

    private NotificationAdapter boAdapter;
    private NotificationRecycler notificationRecycler;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        recyclerViewBookings = findViewById(R.id.recyclerViewNotifications);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
    }
}
