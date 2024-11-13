package com.mad.bus_booking_test_login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class BookingDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public BookingDAO(Context context) {
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

    // Insert into tbl_booking and return the generated booking ID
    public String insertBooking(String passengerId, String busId, String bookingDate, int totalFee) {
        open();

        String bookingId = generateBookingId();
        ContentValues values = new ContentValues();
        values.put("booking_id", bookingId);
        values.put("user_id", passengerId);
        values.put("bus_id", busId);
        values.put("booking_date", bookingDate);
        values.put("total_fee", totalFee);
        db.insert("tbl_booking", null, values);
        return bookingId;
    }

    // Insert seats into tbl_booking_seats
    public void insertBookingSeats(String bookingId, List<String> seatIds, String bookingDate) {
        open();

        for (String seatId : seatIds) {
            ContentValues values = new ContentValues();
            values.put("booking_id", bookingId);
            values.put("seat_id", seatId);
            values.put("booking_date", bookingDate);
            values.put("seat_status", "booked");
            db.insert("tbl_booking_seats", null, values);
        }
    }

    // Method to generate a unique booking ID in the format BOOK1, BOOK2, etc.
    private String generateBookingId() {
        open();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tbl_booking", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return "BOOK" + (count + 1);
    }
}
