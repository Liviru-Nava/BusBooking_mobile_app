package com.mad.bus_booking_test_login.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.RouteDAO;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityPassengerHome extends AppCompatActivity {

    private TextView tv_name, tv_booking_date;
    private Spinner spinner_starting_point, spinner_ending_point;
    private ImageView profile_image;
    private Bitmap profileImage;
    Calendar calendar;
    RouteDAO route;
    UserDAO user;
    private String name, userId;
    private byte[] user_image;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_home);
        tv_name = findViewById(R.id.tv_name);
        tv_booking_date = findViewById(R.id.tv_selected_date);
        spinner_starting_point = findViewById(R.id.spinner_starting_point);
        spinner_ending_point = findViewById(R.id.spinner_ending_point);
        profile_image = findViewById(R.id.img_profile);
        route = new RouteDAO(this);
        user = new UserDAO(this);

        calendar = Calendar.getInstance();

        //get the user_id from the ActivityLogin.class
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");


        tv_name.setText("Welcome " + name);

        Cursor cursor = user.getUserById(userId);
        if(cursor.moveToFirst()){
            user_image = cursor.getBlob(7);
            if(user_image != null){
                profileImage = BitmapFactory.decodeByteArray(user_image, 0, user_image.length);
                profile_image.setImageBitmap(profileImage);
            }
        }

        //initialize spinners with route values
        loadStartingPoints();
        loadEndingPoints();

        Log.e("User Id", "User_id is " + userId );
    }

    //method to open the calendar and any selected date should be added to the tv_selected_date text view
    public void onOpenCalendar(View view){
        // Open DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ActivityPassengerHome.this,
                (datePicker,year, month, dayOfMonth) -> {
                    // Format and display the selected date
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    tv_booking_date.setText(dateFormat.format(selectedDate.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    //search bus logic
    public void onSearchBus(View view){
        String selected_date = tv_booking_date.getText().toString();
        String starting_point = spinner_starting_point.getSelectedItem().toString();
        String ending_point = spinner_ending_point.getSelectedItem().toString();

        if(selected_date.isEmpty()){
            Toast.makeText(this, "Please select a booking date", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent navigate_bus_list_intent = new Intent(this, ActivityBusList.class);
            navigate_bus_list_intent.putExtra("selected_date", selected_date);
            navigate_bus_list_intent.putExtra("starting_point", starting_point);
            navigate_bus_list_intent.putExtra("ending_point", ending_point);
            navigate_bus_list_intent.putExtra("user_id", userId);
            navigate_bus_list_intent.putExtra("name", name);
//        Log.e("Passed User_id", userId);
            startActivity(navigate_bus_list_intent);
        }
    }

    //navigate to booking list
    public void onNavigateBookingList(View view){
        Intent navigate_booking_list = new Intent(this, ActivityBookingList.class);
        navigate_booking_list.putExtra("user_id", userId);
        navigate_booking_list.putExtra("name", name);
        startActivity(navigate_booking_list);
    }
    //navigate to the notifications list
    public void onNavigateNotificationList(View view){
        Intent navigate_notification = new Intent(this, ActivityNotificationList.class);
        navigate_notification.putExtra("user_id", userId);
        navigate_notification.putExtra("name", name);
        startActivity(navigate_notification);
    }
    //navigate to the user profile
    public void onNavigateUserProfile(View view){
        Intent navigate_user_profile = new Intent(this, ActivityUserProfile.class);
        navigate_user_profile.putExtra("user_id", userId);
        navigate_user_profile.putExtra("name", name);
        Log.e("Passed User_id", userId);
        startActivity(navigate_user_profile);
    }

    private void loadStartingPoints() {
        List<String> startingPoints = route.getDistinctStartingPoints();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, startingPoints);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_starting_point.setAdapter(adapter);
    }

    private void loadEndingPoints() {
        List<String> endingPoints = route.getDistinctEndingPoints();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, endingPoints);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ending_point.setAdapter(adapter);
    }

}
