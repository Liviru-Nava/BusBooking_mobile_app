package com.mad.bus_booking_test_login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RouteDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public RouteDAO(Context context) {
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

    // Insert new Route
    public String insertRoute(String route_name, String starting_point, String ending_point) {
        open();
        String route_id = getNextRouteId();
        ContentValues values = new ContentValues();
        values.put("route_id", route_id);
        values.put("route_name", route_name);
        values.put("starting_point", starting_point);
        values.put("ending_point", ending_point);

        long result = db.insert("tbl_route", null, values);
        return result != -1 ? route_id : null;
    }

    @SuppressLint("DefaultLocale")
    private String getNextRouteId() {
        open();
        String next_id = "R0001";
        Cursor cursor = db.rawQuery("SELECT route_id FROM tbl_route WHERE route_id LIKE 'R%' ORDER BY route_id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String last_id = cursor.getString(0);
            int id = Integer.parseInt(last_id.substring(1)) + 1;
            next_id = String.format("R%04d", id);
        }
        cursor.close();
        return next_id;
    }

    //display all routes
    public Cursor getAllRoutes() {
        open();
        return db.rawQuery("SELECT * FROM tbl_route", null);
    }
}
