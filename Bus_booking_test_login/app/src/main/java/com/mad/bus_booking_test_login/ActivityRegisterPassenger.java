package com.mad.bus_booking_test_login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegisterPassenger extends AppCompatActivity {

    private EditText et_name, et_email, et_tel_no, et_dob, et_password;
    private UserDAO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_passenger);

        // Initialize views
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_tel_no = findViewById(R.id.et_telephone);
        et_dob = findViewById(R.id.et_dob);
        et_password = findViewById(R.id.et_password);
        user = new UserDAO(this);

        displayAllUsers();
    }

    public void onSignUpClicked(View view) {
        registerUser();
        Intent navigate_login_intent = new Intent(this, ActivityLogin.class);
        startActivity(navigate_login_intent);
    }

    private void registerUser() {
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String telNo = et_tel_no.getText().toString().trim();
        String dob = et_dob.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || telNo.isEmpty() || dob.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user.emailOrTelExists(email, telNo)) {
            Toast.makeText(this, "Email or phone number already in use", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = user.insertPassenger(name, email, telNo, dob, password);
        if (isInserted) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayAllUsers() {
        Cursor cursor = user.getAllUsers();
        StringBuilder data = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String userType = cursor.getString(cursor.getColumnIndex("user_type"));
                @SuppressLint("Range") String telNo = cursor.getString(cursor.getColumnIndex("tel_no"));
                @SuppressLint("Range") String dob = cursor.getString(cursor.getColumnIndex("dob"));

                data.append("User ID: ").append(userId)
                        .append("\nName: ").append(name)
                        .append("\nEmail: ").append(email)
                        .append("\nUser Type: ").append(userType)
                        .append("\nTelephone: ").append(telNo)
                        .append("\nDOB: ").append(dob)
                        .append("\n\n");
            } while (cursor.moveToNext());
        }
        cursor.close();

        TextView tvData = findViewById(R.id.tv_data);  // Ensure this TextView exists in your XML layout
        tvData.setText(data.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        user.close(); // Close the database connection when the activity is destroyed
    }
}
