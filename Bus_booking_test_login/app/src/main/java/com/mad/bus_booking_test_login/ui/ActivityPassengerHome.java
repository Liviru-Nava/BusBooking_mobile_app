package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.RouteDAO;

import java.util.List;

public class ActivityPassengerHome extends AppCompatActivity {

    private TextView tv_name;
    private EditText et_booking_date;
    private Spinner spinner_starting_point, spinner_ending_point;
    RouteDAO route;
    private String name, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_home);
        tv_name = findViewById(R.id.tv_name);
        et_booking_date = findViewById(R.id.et_selected_date);
        spinner_starting_point = findViewById(R.id.spinner_starting_point);
        spinner_ending_point = findViewById(R.id.spinner_ending_point);
        route = new RouteDAO(this);

        //set the name received from Intent
        name = getIntent().getStringExtra("name");
        tv_name.setText(name);

        //initialize spinners with route values
        loadStartingPoints();
        loadEndingPoints();

    }
    //search bus logic
    public void onSearchBus(View view){
        String selected_date = et_booking_date.getText().toString();
        String starting_point = spinner_starting_point.getSelectedItem().toString();
        String ending_point = spinner_ending_point.getSelectedItem().toString();

        //get the user_id from the ActivityLogin.class
        String userId = getIntent().getStringExtra("user_id");

        Intent navigate_bus_list_intent = new Intent(this, ActivityBusList.class);
        navigate_bus_list_intent.putExtra("selected_date", selected_date);
        navigate_bus_list_intent.putExtra("starting_point", starting_point);
        navigate_bus_list_intent.putExtra("ending_point", ending_point);
        navigate_bus_list_intent.putExtra("user_id", userId);
        startActivity(navigate_bus_list_intent);
    }

    //navigate to booking list
    public void onNavigateBookingList(View view){
        //get the user_id from the ActivityLogin.class
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        Intent navigate_booking_list = new Intent(this, ActivityBookingList.class);
        navigate_booking_list.putExtra("user_id", userId);
        navigate_booking_list.putExtra("name", name);
        startActivity(navigate_booking_list);
    }

    private void loadStartingPoints() {
        List<String> startingPoints = route.getDistinctStartingPoints();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, startingPoints);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_starting_point.setAdapter(adapter);
    }

    private void loadEndingPoints() {
        List<String> endingPoints = route.getDistinctEndingPoints();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, endingPoints);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ending_point.setAdapter(adapter);
    }

}
