package com.mad.bus_booking_test_login;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityPassengerHome extends AppCompatActivity {

    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_home);
        tv_name = findViewById(R.id.tv_name);

        //set the name received from Intent
        String name = getIntent().getStringExtra("name");
        tv_name.setText(name);
    }

}
