package com.mad.bus_booking_test_login.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

public class ActivityDriverHome extends AppCompatActivity {
    private TextView tv_total_earnings, tv_no_of_trips, tv_current_bus;
    private String userId, name, totalEarnings, totalTrips, currentBus;
    UserDAO user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_home);
        tv_total_earnings = findViewById(R.id.tv_total_earnings);
        tv_no_of_trips = findViewById(R.id.tv_no_of_trips);
        tv_current_bus = findViewById(R.id.tv_current_bus);

        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        user = new UserDAO(this);

        //auto load content relevant to the logged user
        getDriverStatistics(userId);
    }

    public void onNavigateBookingList(View view){
        Intent navigate_booking_list = new Intent(this, ActivityDriverBookingList.class);
        navigate_booking_list.putExtra("user_id", userId);
        navigate_booking_list.putExtra("name", name);
        startActivity(navigate_booking_list);
    }

    public void onNavigateUserProfile(View view){
        Intent navigate_user_profile = new Intent(this, ActivityDriverProfile.class);
        navigate_user_profile.putExtra("user_id", userId);
        navigate_user_profile.putExtra("name", name);
        Log.e("Passed User_id", userId);
        startActivity(navigate_user_profile);
    }

    private void getDriverStatistics(String userID){
        Cursor cursor = user.getDriverStatistics(userID);
        if(cursor.moveToFirst()){
            totalEarnings = cursor.getString(2);
            totalTrips = cursor.getString(1);
            currentBus = cursor.getString(0);
        }

        tv_total_earnings.setText(totalEarnings);
        tv_no_of_trips.setText(totalTrips);
        tv_current_bus.setText(currentBus);
    }
}
