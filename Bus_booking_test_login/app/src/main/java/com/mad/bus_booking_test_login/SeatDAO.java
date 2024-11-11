package com.mad.bus_booking_test_login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SeatDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public SeatDAO(Context context) {
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

    //insert seat
    public boolean insertSeat(String bus_id, int no_of_seats) {
        open();
        String seatPrefix = "S";
        int startingSeatNumber = getNextSeatId();  // Get the current max seat ID in the format Sxxxx

        for (int i = 0; i < no_of_seats; i++) {
            @SuppressLint("DefaultLocale") String seat_id = seatPrefix + String.format("%04d", startingSeatNumber + i);
            ContentValues seatValues = new ContentValues();
            seatValues.put("seat_id", seat_id);
            seatValues.put("bus_id", bus_id);
            seatValues.put("seat_number", i + 1);

            long result = db.insert("tbl_seat", null, seatValues);
            if (result == -1) {
                close();
                return false;  // Return false if any insert fails
            }
        }
        close();
        return true;  // Return true if all seats are inserted successfully
    }

    // Helper method to get the next seat ID in sequence
    private int getNextSeatId() {
        Cursor cursor = db.rawQuery("SELECT MAX(CAST(SUBSTR(seat_id, 2) AS INTEGER)) FROM tbl_seat", null);
        int nextId = cursor.moveToFirst() ? cursor.getInt(0) + 1 : 1;
        cursor.close();
        return nextId;
    }


    //getAvailableSeats


}
