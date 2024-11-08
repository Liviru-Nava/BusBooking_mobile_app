package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegisterSelect extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_select);
    }

    public void onLoginClicked(View view){
        Intent navigate_login_intent = new Intent(this, ActivityLogin.class);
        startActivity(navigate_login_intent);
    }

    public void onPassengerClicked(View view){
        Intent navigate_passenger_intent = new Intent(this, ActivityRegisterPassenger.class);
        startActivity(navigate_passenger_intent);
    }
}
