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

    public boolean isSeatBookedByUser(int seatNumber, String bookingId) {
        String query = "SELECT bs.seat_id " +
                "FROM tbl_booking_seats bs " +
                "JOIN tbl_seat s ON bs.seat_id = s.seat_id " +
                "WHERE bs.booking_id = ? AND s.seat_number = ?";
        Cursor cursor = db.rawQuery(query, new String[]{bookingId, String.valueOf(seatNumber)});

        boolean isBooked = cursor.moveToFirst(); // If there's a result, the seat belongs to the user
        cursor.close();
        return isBooked;
    }

    public void sendSeatSwapRequest(String userId, String bookingId, ArrayList<Integer> requestedSeats) {
        // Get the user who booked the requested seats
        String seatNumbers = requestedSeats.toString().replaceAll("[\\[\\]]", ""); // Format seat numbers
        String notificationQuery = "SELECT u.user_id, u.name, bs.booking_id " +
                "FROM tbl_booking_seats bs " +
                "JOIN tbl_seat s ON bs.seat_id = s.seat_id " +
                "JOIN tbl_booking b ON bs.booking_id = b.booking_id " +
                "JOIN tbl_user u ON b.user_id = u.user_id " +
                "WHERE s.seat_number IN (" + seatNumbers + ") AND bs.booking_id != ?";

        Cursor cursor = db.rawQuery(notificationQuery, new String[]{bookingId});

        while (cursor.moveToNext()) {
            String recipientUserId = cursor.getString(0);
            String recipientName = cursor.getString(1);

            // Simulate notification (e.g., via a notification table or third-party service)
            ContentValues values = new ContentValues();
            values.put("recipient_id", recipientUserId);
            values.put("sender_id", userId);
            values.put("message", "User " + userId + " has requested a swap for seats: " + seatNumbers);

            // Log message (replace with actual notification system if available)
            System.out.println("Notification sent to: " + recipientName + " (User ID: " + recipientUserId + ")");
        }
        cursor.close();
    }

    public boolean swapSeats(String bookingId, ArrayList<String> newSeats) {
        // Fetch seat IDs for the new seats
        String seatNumbers = newSeats.toString().replaceAll("[\\[\\]]", ""); // Format seat numbers for SQL query
        String seatIdQuery = "SELECT seat_id FROM tbl_seat WHERE seat_number IN (" + seatNumbers + ")";
        Cursor cursor = db.rawQuery(seatIdQuery, null);

        List<Integer> newSeatIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            newSeatIds.add(cursor.getString(cursor.getColumnIndex("seat_id")));
        }
        cursor.close();

        // Ensure the correct number of seats were selected
        if (newSeatIds.size() != newSeats.size()) {
            return false; // Not all selected seats exist
        }

        // Fetch the current seat assignments for the booking
        String currentSeatQuery = "SELECT seat_id FROM tbl_booking_seats WHERE booking_id = ?";
        Cursor currentCursor = db.rawQuery(currentSeatQuery, new String[]{bookingId});

        List<Integer> currentSeatIds = new ArrayList<>();
        while (currentCursor.moveToNext()) {
            currentSeatIds.add(currentCursor.getInt(0));
        }
        currentCursor.close();

        // Check if the number of current seats matches the new seats
        if (currentSeatIds.size() != newSeatIds.size()) {
            return false; // Number of seats mismatch
        }

        // Update the seat assignments
        boolean allUpdated = true;
        for (int i = 0; i < currentSeatIds.size(); i++) {
            int currentSeatId = currentSeatIds.get(i);
            int newSeatId = newSeatIds.get(i);

            ContentValues values = new ContentValues();
            values.put("seat_id", newSeatId); // Update to new seat ID

            int rowsAffected = db.update(
                    "tbl_booking_seats",
                    values,
                    "booking_id = ? AND seat_id = ?",
                    new String[]{bookingId, String.valueOf(currentSeatId)}
            );

            if (rowsAffected == 0) {
                allUpdated = false; // Update failed for a seat
            }
        }

        return allUpdated;
    }
}
