package com.mad.bus_booking_test_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
            String userName = user.getName(email); // Retrieve the user's name
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ActivityPassengerHome.class);
            intent.putExtra("name", userName); // Pass the name to the next activity
            startActivity(intent);
            finish();
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