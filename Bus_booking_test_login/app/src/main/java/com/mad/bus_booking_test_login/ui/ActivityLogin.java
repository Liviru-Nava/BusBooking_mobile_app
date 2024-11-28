package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

public class ActivityLogin extends AppCompatActivity {

    private EditText et_email, et_password;
    private UserDAO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        user = new UserDAO(this);
    }

    //button for login
    public void onLoginClicked(View view){
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        // Validate input fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email and password are correct
        boolean isValidUser = user.validateUser(email, password);
        if (isValidUser) {
            // Retrieve both user_id and name
            String[] userDetails = user.getUserDetails(email);
            if (userDetails != null && userDetails.length == 3) {
                String userId = userDetails[0];  // user_id
                String userName = userDetails[1]; // name
                String userType = userDetails[2];

                switch (userType) {
                    case "Passenger": {
                        // Show success message
                        Toast.makeText(this, "Logged in successfully as passenger!", Toast.LENGTH_SHORT).show();

                        // Pass user_id and name to the next activity
                        Intent navigate_passenger = new Intent(this, ActivityPassengerHome.class);
                        navigate_passenger.putExtra("user_id", userId); // Pass the user_id to the next activity
                        navigate_passenger.putExtra("name", userName);  // Pass the name to the next activity

                        startActivity(navigate_passenger);
                        finish();
                        break;
                    }
                    case "Owner": {
                        // Show success message
                        Toast.makeText(this, "Logged in successfully as owner!", Toast.LENGTH_SHORT).show();

                        // Pass user_id and name to the next activity
                        Intent navigate_owner = new Intent(this, ActivityOwnerHome.class);
                        navigate_owner.putExtra("user_id", userId); // Pass the user_id to the next activity
                        navigate_owner.putExtra("name", userName);  // Pass the name to the next activity

                        startActivity(navigate_owner);
                        finish();
                        break;
                    }
                    case "Driver": {
                        // Show success message
                        Toast.makeText(this, "Logged in successfully as driver!", Toast.LENGTH_SHORT).show();

                        // Pass user_id and name to the next activity
                        Intent navigate_driver = new Intent(this, ActivityDriverHome.class);
                        navigate_driver.putExtra("user_id", userId); // Pass the user_id to the next activity
                        navigate_driver.putExtra("name", userName);  // Pass the name to the next activity

                        startActivity(navigate_driver);
                        finish();
                        break;
                    }
                }
            }
        } else {
            // Unsuccessful login
            Toast.makeText(this, "Login unsuccessful. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    //button for signup
    public void onSignUpClicked(View view){
        Intent navigate_signup_intent = new Intent(this, ActivityRegisterSelect.class);
        startActivity(navigate_signup_intent);
    }
}