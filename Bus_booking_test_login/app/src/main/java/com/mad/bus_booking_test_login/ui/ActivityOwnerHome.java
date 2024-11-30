package com.mad.bus_booking_test_login.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.BusDAO;


public class ActivityOwnerHome extends AppCompatActivity {

    private TextView tv_name, tv_bus_name, tv_bus_license, tv_starting_point, tv_ending_point, tv_route_name, tv_bus_fee, tv_departure_time, tv_no_of_seats, tv_driver_name, tv_total_earnings, tv_total_owner_earnings, tv_rating_value;
    private String userId, name, busName, busLicense, starting, ending, routeName, busFee, departureTime, noSeats, driverName, totalEarnings, totalOwnerEarnings, ratingValue, ratingCount;
    BusDAO bus;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_home);

        tv_name = findViewById(R.id.tv_name);
        tv_bus_name = findViewById(R.id.tv_bus_name);
        tv_bus_license = findViewById(R.id.tv_bus_license);
        tv_starting_point = findViewById(R.id.tv_starting_point);
        tv_ending_point = findViewById(R.id.tv_ending_point);
        tv_route_name = findViewById(R.id.tv_route_name);
        tv_bus_fee = findViewById(R.id.tv_bus_fee);
        tv_departure_time = findViewById(R.id.tv_departure_time);
        tv_no_of_seats = findViewById(R.id.tv_no_of_seats);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_total_earnings = findViewById(R.id.tv_total_earnings);
        tv_total_owner_earnings = findViewById(R.id.tv_total_owner_earnings);
        tv_rating_value = findViewById(R.id.tv_rating_value);

        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        tv_name.setText("Welcome " + name);

        bus = new BusDAO(this);

        //auto load content relevant to the logged user
        getOwnerStatistics(userId);
    }

    @SuppressLint("SetTextI18n")
    public void getOwnerStatistics(String userId){
        Cursor cursor = bus.getBusDetailsForOwner(userId);
        if(cursor.moveToFirst()){
            busName = cursor.getString(0);
            busLicense = cursor.getString(1);
            starting = cursor.getString(2);
            ending = cursor.getString(3);
            routeName = cursor.getString(4);
            driverName = cursor.getString(5);
            busFee = cursor.getString(6);
            departureTime = cursor.getString(7);
            noSeats = cursor.getString(8);
            totalEarnings = cursor.getString(9);
            totalOwnerEarnings = cursor.getString(10);
            ratingValue = cursor.getString(11);
            ratingCount = cursor.getString(12);
        }

        tv_bus_name.setText(busName);
        tv_bus_license.setText("License No: " + busLicense);
        tv_starting_point.setText("Start: " + starting);
        tv_ending_point.setText("End: " + ending);
        tv_route_name.setText("Route: " + routeName);
        tv_bus_fee.setText("Bus fee: " + busFee);
        tv_driver_name.setText("Driver name: " + driverName);
        tv_departure_time.setText("Departure time: " + departureTime);
        tv_no_of_seats.setText("Seats: " + noSeats);
        tv_total_earnings.setText("Total earnings from bus: " + totalEarnings);
        tv_total_owner_earnings.setText("Total you earned: " + totalOwnerEarnings);
        tv_rating_value.setText("Rating: " + ratingValue + " (" + ratingCount + ")");
    }

    public void onNavigateUserProfile(View view){
        Intent navigate_user_profile = new Intent(this, ActivityUserProfile.class);
        navigate_user_profile.putExtra("user_id", userId);
        navigate_user_profile.putExtra("name", name);
        Log.e("Passed User_id", userId);
        startActivity(navigate_user_profile);
    }

}
