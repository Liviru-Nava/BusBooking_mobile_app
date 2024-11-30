package com.mad.bus_booking_test_login.data_access_objects;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mad.bus_booking_test_login.database.DatabaseHelper;

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
    public String insertBus(String route_id, String ownerId, String driverId, String bus_name, String bus_license, int no_of_seats, int bus_fee, String departure_time) {
        open();
        ContentValues values = new ContentValues();
        String bus_id = getNextBusId();
        values.put("bus_id", bus_id);
        values.put("route_id", route_id);
        values.put("owner_id", ownerId);
        values.put("driver_id", driverId);
        values.put("bus_name", bus_name);
        values.put("bus_license", bus_license);
        values.put("no_of_seats", no_of_seats);
        values.put("bus_fee", bus_fee);
        values.put("departure_time", departure_time);

        long result = db.insert("tbl_bus", null, values);
        return result != -1 ? bus_id : null;
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

    //getAllBusDetailsForGivenRoute
    public Cursor getBusesByRouteAndBookingDate(String startingPoint, String endingPoint, String booking_date) {
        open();
        String query = "SELECT \n" +
                "    b.bus_id, \n" +
                "    b.bus_name, \n" +
                "    b.bus_license, \n" +
                "    (b.no_of_seats - COALESCE(SUM(CASE WHEN bs.seat_status = 'booked' AND bs.booking_date = ? THEN 1 ELSE 0 END), 0)) AS no_of_seats_available,\n" +
                "    b.bus_fee, \n" +
                "    b.departure_time, \n" +
                "    r.route_name, \n" +
                "    r.starting_point, \n" +
                "    r.ending_point,\n" +
                "    u.user_id,\n" +
                "    u.name\n" +
                "FROM \n" +
                "    tbl_bus b\n" +
                "JOIN \n" +
                "    tbl_route r ON b.route_id = r.route_id\n" +
                "JOIN \n" +
                "    tbl_user u ON b.driver_id = u.user_id  -- Assuming `tbl_bus` has `user_id` as a foreign key\n" +
                "LEFT JOIN \n" +
                "    tbl_booking_seats bs ON bs.seat_id IN (\n" +
                "        SELECT seat_id FROM tbl_seat WHERE tbl_seat.bus_id = b.bus_id\n" +
                "    )\n" +
                "WHERE \n" +
                "    r.starting_point = ? \n" +
                "    AND r.ending_point = ? \n" +
                "GROUP BY \n" +
                "    b.bus_id, b.bus_name, b.bus_license, b.no_of_seats, b.bus_fee, b.departure_time, \n" +
                "    r.route_name, r.starting_point, r.ending_point, u.user_id, u.name\n" +
                "HAVING \n" +
                "    no_of_seats_available > 0";
        return db.rawQuery(query, new String[]{booking_date, startingPoint, endingPoint});
    }

    public Cursor getBusDetailsForOwner(String ownerId){
        open();
        String query = "SELECT \n" +
                "    b.bus_name, \n" +
                "    b.bus_license, \n" +
                "    r.starting_point, \n" +
                "    r.ending_point, \n" +
                "    r.route_name, \n" +
                "    d.name AS driver_name, \n" +
                "    b.bus_fee, \n" +
                "    b.departure_time, \n" +
                "    b.no_of_seats, \n" +
                "    IFNULL(SUM(bk.total_fee), 0) AS total_earnings_from_bus, \n" +
                "    IFNULL(SUM(bk.total_fee * 0.5), 0) AS owner_earnings, -- Assuming owner gets 80% of total earnings\n" +
                "    IFNULL(AVG(rt.rating), 0) AS average_rating, \n" +
                "    COUNT(rt.rating) AS no_of_people_rated\n" +
                "FROM \n" +
                "    tbl_bus b\n" +
                "JOIN \n" +
                "    tbl_route r ON b.route_id = r.route_id\n" +
                "JOIN \n" +
                "    tbl_user d ON b.driver_id = d.user_id\n" +
                "LEFT JOIN \n" +
                "    tbl_booking bk ON b.bus_id = bk.bus_id\n" +
                "LEFT JOIN \n" +
                "    tbl_rating rt ON bk.booking_id = rt.booking_id\n" +
                "WHERE \n" +
                "    b.owner_id = ?\n" +
                "GROUP BY \n" +
                "    b.bus_id, b.bus_name, b.bus_license, r.starting_point, r.ending_point, \n" +
                "    r.route_name, d.name, b.bus_fee, b.departure_time, b.no_of_seats;\n";

        return db.rawQuery(query, new String[]{ownerId});
    }
}
