package com.mad.bus_booking_test_login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        this.db_helper = new DatabaseHelper(context);
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
}
