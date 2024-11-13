package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ActivityPassengerHome extends AppCompatActivity {

    private TextView tv_name;
    private EditText et_booking_date;
    private Spinner spinner_starting_point, spinner_ending_point;
    RouteDAO route;

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
        String name = getIntent().getStringExtra("name");
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
