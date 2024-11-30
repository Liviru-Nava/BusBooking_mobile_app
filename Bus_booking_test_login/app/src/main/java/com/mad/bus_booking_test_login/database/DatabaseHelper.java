package com.mad.bus_booking_test_login.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BusBooking.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE tbl_user USER TABLE
        String CREATE_USER_TABLE = "CREATE TABLE tbl_user (" +
                "user_id TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "user_type TEXT, " +
                "tel_no TEXT, " +
                "password TEXT, " +
                "email TEXT, " +
                "dob TEXT, " +
                "profile_picture BLOB);";
        db.execSQL(CREATE_USER_TABLE);

        //CREATE tbl_route ROUTE TABLE
        String CREATE_ROUTE_TABLE = "CREATE TABLE tbl_route (" +
                "route_id TEXT PRIMARY KEY, " +
                "route_name TEXT, " +
                "starting_point TEXT, " +
                "ending_point TEXT);";
        db.execSQL(CREATE_ROUTE_TABLE);

        //CREATE tbl_bus BUS TABLE
        String CREATE_BUS_TABLE = "CREATE TABLE tbl_bus (" +
                "bus_id TEXT PRIMARY KEY, " +
                "route_id TEXT, " +
                "owner_id TEXT, " +
                "driver_id TEXT, " +
                "bus_name TEXT, " +
                "bus_license TEXT, " +
                "no_of_seats INTEGER, " +
                "bus_fee INTEGER, " +   
                "departure_time TEXT, " +
                "FOREIGN KEY(route_id) REFERENCES tbl_route(route_id), " +
                "FOREIGN KEY(owner_id) REFERENCES tbl_route(user_id), " +
                "FOREIGN KEY(driver_id) REFERENCES tbl_user(user_id));";
        db.execSQL(CREATE_BUS_TABLE);

        //CREATE tbl_seat SEAT TABLE
        String CREATE_SEAT_TABLE = "CREATE TABLE tbl_seat (" +
                "seat_id TEXT PRIMARY KEY, " +
                "bus_id TEXT, " +
                "seat_number INTEGER, " +
                "FOREIGN KEY(bus_id) REFERENCES tbl_bus(bus_id));";
        db.execSQL(CREATE_SEAT_TABLE);

        //CREATE tbl_booking BOOKING TABLE
        String CREATE_BOOKING_TABLE = "CREATE TABLE tbl_booking (" +
                "booking_id TEXT PRIMARY KEY, " +
                "user_id TEXT, " +
                "bus_id TEXT, " +
                "booking_date TEXT, " +
                "total_fee INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES tbl_user(user_id), " +
                "FOREIGN KEY(bus_id) REFERENCES tbl_bus(bus_id));";
        db.execSQL(CREATE_BOOKING_TABLE);

        //CREATE tbl_payment PAYMENT TABLE
        String CREATE_PAYMENT_TABLE = "CREATE TABLE tbl_payment (" +
                "payment_id TEXT PRIMARY KEY, " +
                "booking_id TEXT, " +
                "user_id TEXT, " +
                "payment_method TEXT, " +
                "amount INTEGER, " +
                "payment_date TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES tbl_user(user_id), " +
                "FOREIGN KEY(booking_id) REFERENCES tbl_booking(booking_id));";
        db.execSQL(CREATE_PAYMENT_TABLE);

        //CREATE tbl_booking_seats BOOKING SEATS TABLE
        String CREATE_BOOKING_SEATS_TABLE = "CREATE TABLE tbl_booking_seats (" +
                "booking_id TEXT, " +
                "seat_id TEXT, " +
                "booking_date TEXT, " +
                "seat_status TEXT, " +
                "FOREIGN KEY(booking_id) REFERENCES tbl_booking(booking_id), " +
                "FOREIGN KEY(seat_id) REFERENCES tbl_seat(seat_id), " +
                "PRIMARY KEY(booking_id, seat_id));";
        db.execSQL(CREATE_BOOKING_SEATS_TABLE);

        //CREATE tbl_booking_seats BOOKING SEATS TABLE
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE tbl_notification (" +
                "notification_id TEXT PRIMARY KEY, " +
                "user_id TEXT, " +
                "title TEXT, " +
                "message TEXT, " +
                "sent_time TEXT, " +
                "status TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES tbl_user(user_id));";
        db.execSQL(CREATE_NOTIFICATION_TABLE);

        //CREATE tbl_booking_seats BOOKING SEATS TABLE
        String CREATE_RATINGS_TABLE = "CREATE TABLE tbl_rating (" +
                "rating_id TEXT PRIMARY KEY, " +
                "booking_id TEXT, " +
                "rating REAL, " +
                "FOREIGN KEY(booking_id) REFERENCES tbl_booking(booking_id));";
        db.execSQL(CREATE_RATINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_user");
        db.execSQL("DROP TABLE IF EXISTS tbl_route");
        db.execSQL("DROP TABLE IF EXISTS tbl_bus");
        db.execSQL("DROP TABLE IF EXISTS tbl_seat");
        db.execSQL("DROP TABLE IF EXISTS tbl_booking");
        db.execSQL("DROP TABLE IF EXISTS tbl_payment");
        db.execSQL("DROP TABLE IF EXISTS tbl_booking_seats");
        onCreate(db);
    }

    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE tbl_bus");
        db.execSQL("DROP TABLE tbl_route");
        db.execSQL("DROP TABLE tbl_user");
        // Repeat for any other tables
        db.close();
    }
}
