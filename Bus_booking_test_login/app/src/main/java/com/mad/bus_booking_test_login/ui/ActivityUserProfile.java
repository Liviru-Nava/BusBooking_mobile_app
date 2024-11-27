package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

public class ActivityUserProfile extends AppCompatActivity {

    private String userId, name;
    private byte[] user_image;
    Bitmap profile_image = null;
    TextView txt_username;
    ImageView userImageView;
    UserDAO user;

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_profile);

        //get values from intent and set it to variables
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        //initialise the layout components
        txt_username = findViewById(R.id.txt_user_name);
        userImageView = findViewById(R.id.image_view);
        txt_username.setText(name);
        user = new UserDAO(this);

        Cursor cursor = user.getUserById(userId);
        if(cursor.moveToFirst()){
            user_image = cursor.getBlob(7);
            if(user_image != null){
                profile_image = BitmapFactory.decodeByteArray(user_image, 0, user_image.length);
                userImageView.setImageBitmap(profile_image);
            }
        }
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
