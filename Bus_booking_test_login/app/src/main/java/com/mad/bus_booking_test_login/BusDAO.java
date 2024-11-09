package com.mad.bus_booking_test_login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BusDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public BusDAO(Context context) {
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

    // Insert new Bus
    public boolean insertBus(String route_id, String user_id, String bus_name, String bus_license, int no_of_seats, int bus_fee, String departure_time) {
        open();
        ContentValues values = new ContentValues();
        values.put("bus_id", getNextBusId());
        values.put("route_id", route_id);
        values.put("user_id", user_id);
        values.put("bus_name", bus_name);
        values.put("bus_license", bus_license);
        values.put("no_of_seats", no_of_seats);
        values.put("bus_fee", bus_fee);
        values.put("departure_time", departure_time);

        long result = db.insert("tbl_bus", null, values);
        return result != -1;
    }

    @SuppressLint("DefaultLocale")
    private String getNextBusId() {
        open();
        String next_id = "B0001";
        Cursor cursor = db.rawQuery("SELECT bus_id FROM tbl_bus WHERE bus_id LIKE 'B%' ORDER BY bus_id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String last_id = cursor.getString(0);
            int id = Integer.parseInt(last_id.substring(1)) + 1;
            next_id = String.format("B%04d", id);
        }
        cursor.close();
        return next_id;
    }
    //display all buses
    public Cursor getAllBuses() {
        open();
        return db.rawQuery("SELECT * FROM tbl_bus", null);
    }
}
