package com.mad.bus_booking_test_login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
        return true;  // Return true if all seats are inserted successfully
    }

    // Helper method to get the next seat ID in sequence
    private int getNextSeatId() {
        open();
        Cursor cursor = db.rawQuery("SELECT MAX(CAST(SUBSTR(seat_id, 2) AS INTEGER)) FROM tbl_seat", null);
        int nextId = cursor.moveToFirst() ? cursor.getInt(0) + 1 : 1;
        cursor.close();
        return nextId;
    }


    public List<Integer> getBookedSeats(String busId, String bookingDate) {
        open();
        List<Integer> bookedSeats = new ArrayList<>();

        String query = "SELECT s.seat_number " +
                "FROM tbl_seat s " +
                "JOIN tbl_booking_seats bs ON s.seat_id = bs.seat_id " +
                "JOIN tbl_booking b ON bs.booking_id = b.booking_id " +
                "WHERE s.bus_id = ? " +
                "AND b.booking_date = ? " +
                "AND bs.seat_status = 'booked'";

        try (Cursor cursor = db.rawQuery(query, new String[]{busId, bookingDate})) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int seatNumber = cursor.getInt(cursor.getColumnIndex("seat_number"));
                bookedSeats.add(seatNumber - 1); // Adjusting to zero-based index if needed
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }


    public String getSeatId(String busId, int seatNumber) {
        String seatId = null;
        open(); // Ensure the database is open before querying

        String query = "SELECT seat_id FROM tbl_seat WHERE bus_id = ? AND seat_number = ?";
        String[] parameters = { busId, String.valueOf(seatNumber) };

        Cursor cursor = db.rawQuery(query, parameters);
        if (cursor.moveToFirst()) {
            seatId = cursor.getString(cursor.getColumnIndexOrThrow("seat_id"));
        }
        cursor.close();
        return seatId;
    }
}
