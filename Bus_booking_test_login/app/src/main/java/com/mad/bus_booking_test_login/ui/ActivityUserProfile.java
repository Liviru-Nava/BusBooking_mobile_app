package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;

public class ActivityUserProfile extends AppCompatActivity {

    private String userId, name;
    TextView txt_username;

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_profile);

        //get values from intent and set it to variables
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        //initialise the layout components
        txt_username = findViewById(R.id.txt_user_name);
        txt_username.setText(name);
    }

    //method to navigate to edit profile
    public void onEditButton(View view){
        Intent navigate_edit_profile = new Intent(this, ActivityEditProfile.class);
        navigate_edit_profile.putExtra("user_id", userId);
        navigate_edit_profile.putExtra("name", name);
        startActivity(navigate_edit_profile);
    }

    //method to navigate the reset password

    //method to invoke the logout function
}
