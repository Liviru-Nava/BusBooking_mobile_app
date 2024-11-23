package com.mad.bus_booking_test_login.data_access_objects;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mad.bus_booking_test_login.database.DatabaseHelper;

public class UserDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        this.db_helper = new DatabaseHelper(context);
        open();
    }
    // Open the database
    private void open() {
        if (db == null || !db.isOpen()) {
            db = db_helper.getWritableDatabase();
        }
    }
    // Close the database
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Get the next Passenger ID
    @SuppressLint("DefaultLocale")
    public String getNextPassengerId() {
        open();
        String next_id = "P0001";
        Cursor cursor = db.rawQuery("SELECT user_id FROM tbl_user WHERE user_id LIKE 'P%' ORDER BY user_id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String last_id = cursor.getString(0);
            int id = Integer.parseInt(last_id.substring(1)) + 1;
            next_id = String.format("P%04d", id);
        }
        cursor.close();
        return next_id;
    }

    // Get the next Bus Owner ID
    @SuppressLint("DefaultLocale")
    public String getNextOwnerId() {
        open();
        String next_id = "O0001";
        Cursor cursor = db.rawQuery("SELECT user_id FROM tbl_user WHERE user_id LIKE 'O%' ORDER BY user_id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String last_id = cursor.getString(0);
            int id = Integer.parseInt(last_id.substring(1)) + 1;
            next_id = String.format("O%04d", id);
        }
        cursor.close();
        return next_id;
    }

    // Get the next Driver ID
    @SuppressLint("DefaultLocale")
    public String getNextDriverId() {
        open();
        String next_id = "D0001";
        Cursor cursor = db.rawQuery("SELECT user_id FROM tbl_user WHERE user_id LIKE 'D%' ORDER BY user_id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String last_id = cursor.getString(0);
            int id = Integer.parseInt(last_id.substring(1)) + 1;
            next_id = String.format("D%04d", id);
        }
        cursor.close();
        return next_id;
    }

    // Insert new Passenger
    public boolean insertPassenger(String name, String email, String tel_no, String dob, String password) {
        open();
        ContentValues values = new ContentValues();
        values.put("user_id", getNextPassengerId());
        values.put("name", name);
        values.put("user_type", "Passenger");
        values.put("tel_no", tel_no);
        values.put("password", password);
        values.put("email", email);
        values.put("dob", dob);
        values.put("profile_picture", "");

        long result = db.insert("tbl_user", null, values);
        return result != -1;
    }

    // Insert new Owner
    public boolean insertOwner(String name, String email, String tel_no, String dob, String password) {
        open();
        ContentValues values = new ContentValues();
        values.put("user_id", getNextOwnerId());
        values.put("name", name);
        values.put("user_type", "Owner");
        values.put("tel_no", tel_no);
        values.put("password", password);
        values.put("email", email);
        values.put("dob", dob);
        values.put("profile_picture", "");

        long result = db.insert("tbl_user", null, values);
        return result != -1;
    }

    // Insert new Driver
    public String insertDriver(String name, String email, String tel_no, String dob, String password) {
        open();
        String driver_id = getNextDriverId();
        ContentValues values = new ContentValues();
        values.put("user_id", driver_id);
        values.put("name", name);
        values.put("user_type", "Driver");
        values.put("tel_no", tel_no);
        values.put("password", password);
        values.put("email", email);
        values.put("dob", dob);
        values.put("profile_picture", "");

        long result = db.insert("tbl_user", null, values);
        return result != -1 ? driver_id : null;
    }

    // Check if email or phone number exists
    public boolean emailOrTelExists(String email, String tel_no) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_user WHERE email = ? OR tel_no = ?", new String[]{email, tel_no});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    //display all the users registered
    public Cursor getAllUsers() {
        open();
        return db.rawQuery("SELECT * FROM tbl_user", null);
    }

    //validate the login credentials
    public boolean validateUser(String email, String password) {
        open();
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbl_user WHERE email = ? AND password = ?", new String[]{email, password});
        boolean isValidUser = cursor.getCount() > 0;
        cursor.close();
        return isValidUser;
    }

    // Get both user_id and name
    public String[] getUserDetails(String email){
        open(); // Ensure this method actually opens the database connection if needed
        SQLiteDatabase db = db_helper.getReadableDatabase();
        String[] userDetails = new String[2]; // Array to hold both user_id and name
        Cursor cursor = db.rawQuery("SELECT user_id, name FROM tbl_user WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) { // Check if the cursor has at least one row
            // Get user_id and name
            userDetails[0] = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
            userDetails[1] = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        cursor.close(); // Close the cursor to release resources
        return userDetails; // Return the user_id and name as an array
    }
}
