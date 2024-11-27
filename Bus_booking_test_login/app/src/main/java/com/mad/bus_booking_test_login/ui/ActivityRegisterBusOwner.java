package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

import java.io.ByteArrayOutputStream;

public class ActivityRegisterBusOwner extends AppCompatActivity {

    private EditText et_name, et_email, et_tel_no, et_dob, et_password;
    private UserDAO user;

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
        user = new UserDAO(this);
    }

    //method to direct to register the bus and the route
    public void onRegisterOwnerClicked(View view){
        //logic to register the bus owner
        registerUser();

        //move to the next UI to register the bus and the route
        Intent navigate_driver_intent = new Intent(this, ActivityRegisterDriver.class);
        startActivity(navigate_driver_intent);
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

        // Convert default image to byte array
        byte[] defaultProfilePicture = getDefaultProfilePicture();

        boolean isInserted = user.insertOwner(name, email, telNo, dob, password, defaultProfilePicture);
        if (isInserted) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Convert the default profile picture to byte array
    private byte[] getDefaultProfilePicture() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }

}
