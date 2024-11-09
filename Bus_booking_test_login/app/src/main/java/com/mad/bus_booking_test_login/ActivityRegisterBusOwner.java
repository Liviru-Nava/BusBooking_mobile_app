package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegisterBusOwner extends AppCompatActivity {

    private EditText et_name, et_email, et_tel_no, et_dob, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_bus_owner);

        // Initialize views
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_tel_no = findViewById(R.id.et_telephone);
        et_dob = findViewById(R.id.et_dob);
        et_password = findViewById(R.id.et_password);
    }

    //method to direct to register the bus and the route
    public void onRegisterOwnerClicked(View view){
        //logic to register the bus owner

        //move to the next UI to register the bus and the route
        Intent navigate_register_bus_and_route_intent = new Intent(this, ActivityRegisterBusAndRoute.class);
        startActivity(navigate_register_bus_and_route_intent);
    }
}
