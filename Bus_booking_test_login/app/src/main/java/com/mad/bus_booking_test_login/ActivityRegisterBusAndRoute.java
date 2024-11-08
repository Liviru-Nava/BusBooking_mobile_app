package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegisterBusAndRoute extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_bus_and_route);
    }

    //method called when register driver button is clicked
    public void onRegisterDriverClicked(View view){
        Intent navigate_register_driver_intent = new Intent(this, ActivityRegisterDriver.class);
        startActivity(navigate_register_driver_intent);
    }
}
