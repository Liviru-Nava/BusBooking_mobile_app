package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;

public class ActivityRegisterSelect extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_select);
    }
    //login button clicked method
    public void onLoginClicked(View view){
        Intent navigate_login_intent = new Intent(this, ActivityLogin.class);
        startActivity(navigate_login_intent);
    }
    //passenger button clicked method
    public void onPassengerClicked(View view){
        Intent navigate_passenger_intent = new Intent(this, ActivityRegisterPassenger.class);
        startActivity(navigate_passenger_intent);
    }
    //owner button clicked method
    public void onOwnerClicked(View view){
        Intent navigate_owner_intent = new Intent(this, ActivityRegisterBusOwner.class);
        startActivity(navigate_owner_intent);
    }
}
