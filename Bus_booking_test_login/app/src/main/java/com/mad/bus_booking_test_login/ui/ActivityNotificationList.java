package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.adapter.BookingAdapter;
import com.mad.bus_booking_test_login.adapter.NotificationAdapter;
import com.mad.bus_booking_test_login.data_access_objects.NotificationDAO;

public class ActivityNotificationList extends AppCompatActivity {

    private NotificationAdapter notificationAdapter;
    private RecyclerView recyclerViewNotifications;
    private String userId, name;
    NotificationDAO notification;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));

        //handle intent content
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        //create notification database access object
        notification = new NotificationDAO(this);

        //method call to get notifications
        getNotificationsForUserId(userId);
    }

    public void onNavigateHome(View view){
        Intent navigate_home = new Intent(this, ActivityPassengerHome.class);
        navigate_home.putExtra("name", name);
        navigate_home.putExtra("user_id", userId);
        startActivity(navigate_home);
    }

    private void getNotificationsForUserId(String userId) {
        Cursor cursor = notification.getNotificationById(userId);
        notificationAdapter = new NotificationAdapter(this, cursor, userId);
        recyclerViewNotifications.setAdapter(notificationAdapter);
    }
}
