package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegisterDriver extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_driver);
    }

    public void onRegisterDriverClicked(View view){
        //logic to register the driver to the database


        //logic to move to the Login UI
        Intent navigate_to_login_intent = new Intent(this, ActivityLogin.class);
        startActivity(navigate_to_login_intent);
    }
}
