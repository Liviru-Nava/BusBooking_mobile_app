package com.mad.bus_booking_test_login.data_access_objects;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mad.bus_booking_test_login.database.DatabaseHelper;

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

    //method to insert seat
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


    //method to retrieve the seat ID
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


    // Helper method to get the next seat ID in sequence
    private int getNextSeatId() {
        open();
        Cursor cursor = db.rawQuery("SELECT MAX(CAST(SUBSTR(seat_id, 2) AS INTEGER)) FROM tbl_seat", null);
        int nextId = cursor.moveToFirst() ? cursor.getInt(0) + 1 : 1;
        cursor.close();
        return nextId;
    }

    //method to retreive seats that are booked on the particular bus on a particular booking date
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


    // Method to check if a seat is booked by the current user
    public boolean isSeatBookedByUser(int seatNumber, String bookingId, String busId) {
        open();
        boolean isBookedByUser = false;

        String query = "SELECT s.seat_id " +
                "FROM tbl_seat s " +
                "JOIN tbl_booking_seats bs ON s.seat_id = bs.seat_id " +
                "WHERE s.seat_number = ? " +
                "AND s.bus_id = ? " +
                "AND bs.booking_id = ? " +
                "AND bs.seat_status = 'booked'";

        try (Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(seatNumber), busId, bookingId})) {
            if (cursor.moveToFirst()) {
                isBookedByUser = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isBookedByUser;
    }



    // Method to retrieve seats booked by the current booking ID
    public List<Integer> getBookedSeatsForBooking(String bookingId) {
        open();
        List<Integer> seatNumbers = new ArrayList<>();

        String query = "SELECT s.seat_number " +
                "FROM tbl_seat s " +
                "JOIN tbl_booking_seats bs ON s.seat_id = bs.seat_id " +
                "WHERE bs.booking_id = ? " +
                "AND bs.seat_status = 'booked'";

        try (Cursor cursor = db.rawQuery(query, new String[]{bookingId})) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int seatNumber = cursor.getInt(cursor.getColumnIndex("seat_number"));
                seatNumbers.add(seatNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seatNumbers;
    }


    // Method to send a seat swap request and log the details
    public void sendSeatSwapRequest(String userId, String bookingId, List<Integer> requestedSeats, String busId, String bookingDate) {
        open();
        // Retrieve the user ID of the person who has booked the requested seats
        String recipientUserId = getUserIdForBookedSeats(busId, bookingDate, requestedSeats);

        // Format the requested and current booked seat numbers
        String requestedSeatNumbers = requestedSeats.toString().replaceAll("[\\[\\]]", ""); // Format seat numbers as comma-separated list
        String currentBookingSeats = getBookedSeatsForBooking(bookingId).toString().replaceAll("[\\[\\]]", "");

        // Log the notification message to Logcat
        String logMessage = "Seat swap requested by User ID: " + userId +
                " for Booking ID: " + bookingId +
                ". Current Seats: " + currentBookingSeats +
                " Requested Seats: " + requestedSeatNumbers +
                ". Request sent to User ID: " + (recipientUserId != null ? recipientUserId : "No matching user found");
        android.util.Log.d("SeatSwapRequest", logMessage);
    }
    // Helper method to find the user ID of the person who booked the requested seats
    private String getUserIdForBookedSeats(String busId, String bookingDate, List<Integer> requestedSeats) {
        String userId = null;
        String seatNumbers = requestedSeats.toString().replaceAll("[\\[\\]]", ""); // Format seat numbers
        String query = "SELECT b.user_id " +
                "FROM tbl_booking b " +
                "JOIN tbl_booking_seats bs ON b.booking_id = bs.booking_id " +
                "JOIN tbl_seat s ON bs.seat_id = s.seat_id " +
                "WHERE b.booking_date = ? AND s.bus_id = ? AND s.seat_number IN (" + makePlaceholders(requestedSeats.size()) + ") " +
                "GROUP BY b.user_id";

        try (Cursor cursor = db.rawQuery(query, getQueryParams(bookingDate, busId, requestedSeats))) {
            if (cursor.moveToFirst()) {
                userId = cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }
    private String makePlaceholders(int count) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < count; i++) {
            placeholders.append("?");
            if (i < count - 1) {
                placeholders.append(", ");
            }
        }
        return placeholders.toString();
    }
    private String[] getQueryParams(String bookingDate, String busId, List<Integer> requestedSeats) {
        List<String> params = new ArrayList<>();
        params.add(bookingDate);
        params.add(busId);
        for (int seat : requestedSeats) {
            params.add(String.valueOf(seat));
        }
        return params.toArray(new String[0]);
    }



    // Method to swap seats
    public boolean swapSeats(String bookingId, List<Integer> newSeats, String busId, String bookingDate) {
        open();
        List<Integer> currentSeats = getBookedSeatsForBooking(bookingId);
        if (currentSeats.size() != newSeats.size()) {
            return false; // Ensure the number of seats matches
        }

        try {
            for (int i = 0; i < currentSeats.size(); i++) {
                int currentSeatNumber = currentSeats.get(i);
                int newSeatNumber = newSeats.get(i);

                // Get seat IDs for current and new seats
                String currentSeatId = getSeatIdBySeatNumber(currentSeatNumber, busId);
                String newSeatId = getSeatIdBySeatNumber(newSeatNumber, busId);

                if (currentSeatId == null || newSeatId == null) {
                    Log.e("SeatSwap", "Invalid seat IDs for swapping.");
                    return false; // Invalid seat IDs
                }

                // Check if the new seat is already booked by someone else
                if (isSeatBooked(newSeatId, bookingDate)) {
                    Log.e("SeatSwap", "New seat " + newSeatId + " is already booked.");
                    return false; // Cannot swap to an already booked seat
                }

                // Update the seat booking to the new seat ID
                ContentValues updateValues = new ContentValues();
                updateValues.put("seat_id", newSeatId);
                int rowsAffected = db.update("tbl_booking_seats", updateValues,
                        "booking_id = ? AND seat_id = ?", new String[]{bookingId, currentSeatId});

                if (rowsAffected == 0) {
                    Log.e("SeatSwap", "Failed to update seat booking for " + currentSeatId);
                    return false; // Update failed
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Swap failed
        }

        return true; // Swap successful
    }

    // Helper method to get the seat ID by seat number and bus ID
    private String getSeatIdBySeatNumber(int seatNumber, String busId) {
        open();
        String seatId = null;

        String query = "SELECT seat_id FROM tbl_seat WHERE seat_number = ? AND bus_id = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seatNumber), busId})) {
            if (cursor.moveToFirst()) {
                seatId = cursor.getString(cursor.getColumnIndexOrThrow("seat_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seatId;
    }

    // Method to check if a seat is already booked
    public boolean isSeatBooked(String seatId, String bookingDate) {
        boolean isBooked = false;

        String query = "SELECT seat_status FROM tbl_booking_seats WHERE seat_id = ? AND seat_status = 'booked' and booking_date = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{seatId, bookingDate})) {
            if (cursor.moveToFirst()) {
                isBooked = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isBooked;
    }
}
