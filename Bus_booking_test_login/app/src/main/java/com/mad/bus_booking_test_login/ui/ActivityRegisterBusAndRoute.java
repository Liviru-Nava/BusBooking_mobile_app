package com.mad.bus_booking_test_login.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.BusDAO;
import com.mad.bus_booking_test_login.data_access_objects.RouteDAO;
import com.mad.bus_booking_test_login.data_access_objects.SeatDAO;

public class ActivityRegisterBusAndRoute extends AppCompatActivity {

    private EditText et_route_name, et_starting_point, et_ending_point;
    private RouteDAO route;

    private EditText et_bus_name, et_license, et_no_of_seats, et_bus_fee, et_departure_time;
    private BusDAO bus;

    private SeatDAO seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_bus_and_route);

        //initialize the route
        et_route_name = findViewById(R.id.et_route_name);
        et_starting_point = findViewById(R.id.et_starting_point);
        et_ending_point = findViewById(R.id.et_ending_point);
        route = new RouteDAO(this);

        //initialize the bus
        et_bus_name = findViewById(R.id.et_bus_name);
        et_license = findViewById(R.id.et_license);
        et_no_of_seats = findViewById(R.id.et_no_of_seats);
        et_bus_fee = findViewById(R.id.et_bus_fee);
        et_departure_time = findViewById(R.id.et_departure_time);
        bus = new BusDAO(this);

        seat = new SeatDAO(this);
    }

    //method called when register driver button is clicked
    public void onRegisterBusAndRouteClicked(View view){
        //logic to insert the bus and route details to the database
        registerRoute();

        //move to the next UI to register the Driver
        Intent navigate_login_intent = new Intent(this, ActivityLogin.class);
        startActivity(navigate_login_intent);
    }

    private void registerRoute() {
        String route_name = et_route_name.getText().toString().trim();
        String route_starting_point = et_starting_point.getText().toString().trim();
        String route_ending_point = et_ending_point.getText().toString().trim();

        String registered_route_id = route.insertRoute(route_name, route_starting_point, route_ending_point);
        if(registered_route_id != null){
            Toast.makeText(this, "Route Registration successful!", Toast.LENGTH_SHORT).show();

            //logic to register the bus with the obtained registered route_id
            String driver_id = getIntent().getStringExtra("driver_id");
            registerBus(registered_route_id, driver_id);
        }else{
            Toast.makeText(this, "Route registration failed, try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerBus(String registered_route_id, String user_id) {
        String bus_name = et_bus_name.getText().toString().trim();
        String bus_license = et_license.getText().toString().trim();
        int no_of_seats = Integer.parseInt(et_no_of_seats.getText().toString().trim());
        int bus_fee = Integer.parseInt(et_bus_fee.getText().toString().trim());
        String departure_time = et_departure_time.getText().toString().trim();

        String bus_id = bus.insertBus(registered_route_id, user_id, bus_name, bus_license, no_of_seats, bus_fee, departure_time);
        if (bus_id != null) {
            boolean seatsRegistered = seat.insertSeat(bus_id, no_of_seats);
            if (seatsRegistered) {
                Toast.makeText(this, "Bus registration and seat allocation successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bus registration successful, but seat allocation failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Bus registration failed, try again later!", Toast.LENGTH_SHORT).show();
        }
    }
}
