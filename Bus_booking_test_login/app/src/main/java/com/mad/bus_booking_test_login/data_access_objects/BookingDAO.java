package com.mad.bus_booking_test_login.data_access_objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mad.bus_booking_test_login.database.DatabaseHelper;

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

    public Cursor getBookingsFromUserId(String userId) {
        open();
        String query = "SELECT \n" +
                "    b.bus_id, \n" +
                "    b.bus_name, \n" +
                "    r.route_name, \n" +
                "    d.user_id AS driver_id, \n" +
                "    d.name AS driver_name,\n" +
                "    r.starting_point, \n" +
                "    r.ending_point, \n" +
                "    bk.booking_date, \n" +
                "    bk.booking_id\n" +
                "FROM \n" +
                "    tbl_booking bk\n" +
                "JOIN \n" +
                "    tbl_bus b ON bk.bus_id = b.bus_id\n" +
                "JOIN \n" +
                "    tbl_route r ON b.route_id = r.route_id\n" +
                "JOIN \n" +
                "    tbl_user d ON b.driver_id = d.user_id\n" +
                "JOIN \n" +
                "    tbl_user p ON bk.user_id = p.user_id\n" +
                "WHERE \n" +
                "    p.user_id = ? AND \n" +
                "    bk.booking_date >= DATE('now', '-3 days')";
        return db.rawQuery(query, new String[]{userId});
    }

    public Cursor getUpcomingBookings(){
        open();
        String query = "SELECT \n" +
                "    bk.booking_date, \n" +
                "    r.route_name,\n" +
                "    r.starting_point, \n" +
                "    r.ending_point, \n" +
                "    b.departure_time, \n" +
                "    COUNT(DISTINCT bs.seat_id) AS total_no_of_passengers\n" +
                "FROM \n" +
                "    tbl_booking bk\n" +
                "JOIN \n" +
                "    tbl_bus b ON bk.bus_id = b.bus_id\n" +
                "JOIN \n" +
                "    tbl_route r ON b.route_id = r.route_id\n" +
                "JOIN \n" +
                "    tbl_booking_seats bs ON bk.booking_id = bs.booking_id\n" +
                "WHERE \n" +
                "    bk.booking_date >= DATE('now') -- Filters bookings on or after today\n" +
                "GROUP BY \n" +
                "    bk.booking_date, r.route_name, r.starting_point, r.ending_point, b.departure_time";
        return db.rawQuery(query, new String[]{});
    }
}
