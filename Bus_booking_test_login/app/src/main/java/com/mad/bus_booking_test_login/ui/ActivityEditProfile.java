package com.mad.bus_booking_test_login.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ActivityEditProfile extends AppCompatActivity {

    private String userId, name, telephoneNumber, email, dob, newName, newTelNo, newEmail, newDOB;
    private byte[] user_image;
    private Bitmap profileImage;
    private Bitmap selectedBitmap;
    EditText et_name, et_email, et_telephone, et_dob;
    ImageView profile_image;
    UserDAO user;
    private byte[] newImage;

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;

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

    public void onSaveDetailsClicked(View view){
        newName = et_name.getText().toString();
        newTelNo = et_telephone.getText().toString();
        newEmail = et_email.getText().toString();
        newDOB = et_dob.getText().toString();

        // Get the Bitmap from the ImageView
        Bitmap bitmap = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, outputStream); // You can change the format and quality
        newImage = outputStream.toByteArray();


        boolean result = user.updateUserDetails(userId, newName, newTelNo, newEmail, newDOB, newImage);
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

    //method to change profile image
    public void onChangeProfileImage(View view){
        showImageSelectionDialog();
    }

    // Show dialog for camera or gallery selection
    private void showImageSelectionDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Open camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                // Open gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                // Handle camera result
                selectedBitmap = (Bitmap) data.getExtras().get("data");
                profile_image.setImageBitmap(selectedBitmap);
            } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                // Handle gallery result
                Uri selectedImageUri = data.getData();
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    profile_image.setImageBitmap(selectedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
