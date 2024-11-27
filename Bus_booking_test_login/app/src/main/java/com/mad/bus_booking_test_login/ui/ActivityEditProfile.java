package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

public class ActivityEditProfile extends AppCompatActivity {

    private String userId, name, telephoneNumber, email, dob, newName, newTelNo, newEmail, newDOB;
    private byte[] user_image;
    Bitmap profileImage;
    EditText et_name, et_email, et_telephone, et_dob;
    ImageView profile_image;
    UserDAO user;

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_edit_profile);
        user = new UserDAO(this);

        //get the values from intent to this class
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        //initialize the components
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_telephone = findViewById(R.id.et_telephone);
        et_dob = findViewById(R.id.et_dob);
        profile_image = findViewById(R.id.image_view);

        //get all the values to display to the edit profile page
        getUserById(userId);
    }

    public void getUserById(String userId){
        Cursor cursor = user.getUserById(userId);
        if(cursor.moveToFirst()){
            name = cursor.getString(1);
            telephoneNumber = cursor.getString(3);
            email = cursor.getString(5);
            dob = cursor.getString(6);
            user_image = cursor.getBlob(7);
        }
        et_name.setText(name);
        et_telephone.setText(telephoneNumber);
        et_email.setText(email);
        et_dob.setText(dob);
        if(user_image != null){
            profileImage = BitmapFactory.decodeByteArray(user_image, 0, user_image.length);
            profile_image.setImageBitmap(profileImage);
        }
    }

    public void onSignUpClicked(View view){
        newName = et_name.getText().toString();
        newTelNo = et_telephone.getText().toString();
        newEmail = et_email.getText().toString();
        newDOB = et_dob.getText().toString();

        boolean result = user.updateUserDetails(userId, newName, newTelNo, newEmail, newDOB);
        if (result) {
            Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT).show();

            Intent navigate_profile = new Intent(this, ActivityUserProfile.class);
            navigate_profile.putExtra("user_id", userId);
            navigate_profile.putExtra("name", newName);
            startActivity(navigate_profile);
        } else {
            Toast.makeText(this, "Update Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}
