package com.mad.bus_booking_test_login.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BusBooking.db";
    private static final int DATABASE_VERSION = 1;

//    // DEFINE THE TABLES
//    public static final String TABLE_USER = "tbl_user";
//    public static final String TABLE_BUS = "tbl_bus";
//    public static final String TABLE_ROUTE = "tbl_route";
//    public static final String TABLE_SEAT = "tbl_seat";
//    public static final String TABLE_BOOKING  = "tbl_booking";
//    public static final String TABLE_PAYMENT = "tbl_payment";
//    public static final String TABLE_BOOKED_SEAT = "tbl_booking_seat";
//
//    //CREATE TABLE USER
//    public static final String COLUMN_USER_ID = "user_id";
//    public static final String COLUMN_NAME = "name";
//    public static final String COLUMN_USER_TYPE = "user_type";
//    public static final String COLUMN_TEL_NO = "tel_no";
//    public static final String COLUMN_PASSWORD = "password";
//    public static final String COLUMN_EMAIL = "email";
//    public static final String COLUMN_DOB = "dob";
//    public static final String COLUMN_PROFILE_PICTURE = "profile_picture";
//
//    private static final String USER_CREATE =
//            "CREATE TABLE " + TABLE_USER + " (" +
//                    COLUMN_USER_ID + " TEXT PRIMARY KEY, " +
//                    COLUMN_NAME + " TEXT, " +
//                    COLUMN_USER_TYPE + " TEXT, " +
//                    COLUMN_TEL_NO + " TEXT, " +
//                    COLUMN_PASSWORD + " TEXT, " +
//                    COLUMN_EMAIL + " TEXT UNIQUE, " +
//                    COLUMN_DOB + " TEXT, " +
//                    COLUMN_PROFILE_PICTURE + " BLOB);";
//
//    //CREATE TABLE BUS
//    public static final String COLUMN_BUS_ID = "bus_id";
//    public static final String COLUMN_USER_ID_FOR_BUS = "user_id";
//    public static final String COLUMN_ROUTE_ID_FOR_BUS = "route_id";
//    public static final String COLUMN_BUS_NAME = "bus_name";
//    public static final String COLUMN_BUS_LICENSE = "bus_license";
//    public static final String COLUMN_NO_SEATS = "no_of_seats";
//    public static final String COLUMN_FEE = "bus_fee";
//    public static final String COLUMN_DEPARTURE_TIME = "departure_time";
//
//    private static final String BUS_CREATE =
//            "CREATE TABLE " + TABLE_BUS + " (" +
//                    COLUMN_BUS_ID + " TEXT PRIMARY KEY, " +
//                    COLUMN_USER_ID_FOR_BUS + " TEXT, " +
//                    COLUMN_ROUTE_ID_FOR_BUS + " TEXT, " +
//                    COLUMN_BUS_NAME + " TEXT, " +
//                    COLUMN_BUS_LICENSE + " TEXT, " +
//                    COLUMN_NO_SEATS + " INTEGER, " +
//                    COLUMN_FEE + " INTEGER, " +
//                    COLUMN_DEPARTURE_TIME + " TEXT, " +
//                    "FOREIGN KEY(user_id) REFERENCES " + TABLE_USER + "(user_id), " +
//                    "FOREIGN KEY(route_id) REFERENCES " + TABLE_ROUTE + "(route_id)); ";
//
//    //CREATE TABLE ROUTE
//    public static final String COLUMN_ROUTE_ID = "route_id";
//    public static final String COLUMN_ROUTE_NAME = "route_name";
//    public static final String COLUMN_ROUTE_START = "starting_point";
//    public static final String COLUMN_ROUTE_END = "ending_point";
//
//    private static final String ROUTE_CREATE =
//            "CREATE TABLE " + TABLE_ROUTE + " (" +
//                    COLUMN_ROUTE_ID + " TEXT PRIMARY KEY, " +
//                    COLUMN_ROUTE_NAME + " TEXT, " +
//                    COLUMN_ROUTE_START + " TEXT, " +
//                    COLUMN_ROUTE_END + " TEXT);";
//
//    //CREATE TABLE PAYMENT
//    public static final String COLUMN_PAYMENT_ID = "payment_id";
//    public static final String COLUMN_BOOKING_ID_FOR_PAYMENT = "booking_id";
//    public static final String COLUMN_PAYMENT_METHOD = "payment_method";
//    public static final String COLUMN_AMOUNT = "amount";
//    public static final String COLUMN_PAYMENT_DATE = "payment_date";
//
//    private static final String PAYMENT_CREATE =
//            "CREATE TABLE " + TABLE_PAYMENT + " (" +
//                    COLUMN_PAYMENT_ID + " TEXT PRIMARY KEY, " +
//                    COLUMN_BOOKING_ID_FOR_PAYMENT + " TEXT, " +
//                    COLUMN_PAYMENT_METHOD + " TEXT, " +
//                    COLUMN_AMOUNT + " INTEGER, " +
//                    COLUMN_PAYMENT_DATE + "TEXT, " +
//                    "FOREIGN KEY(booking_id) REFERENCES " + TABLE_BOOKING + "(booking_id));";
//
//    //CREATE TABLE BOOKING
//    public static final String COLUMN_BOOKING_ID = "booking_id";
//    public static final String COLUMN_BOOKING_DATE = "booking_date";
//    public static final String COLUMN_TOTAL_FEE = "total_fee";
//    public static final String COLUMN_BUS_ID_FOR_BOOKING = "bus_id";
//    public static final String COLUMN_USER_ID_FOR_BOOKING = "user_id";
//
//    private static final String BOOKING_CREATE =
//            "CREATE TABLE " + TABLE_BOOKING + " (" +
//                    COLUMN_BOOKING_ID + " TEXT PRIMARY KEY, " +
//                    COLUMN_BOOKING_DATE + " TEXT, " +
//                    COLUMN_TOTAL_FEE + " INTEGER, " +
//                    COLUMN_BUS_ID_FOR_BOOKING + " TEXT, " +
//                    COLUMN_USER_ID_FOR_BOOKING + " TEXT, " +
//                    "FOREIGN KEY(bus_id) REFERENCES " + TABLE_BUS + "(bus_id), " +
//                    "FOREIGN KEY(user_id) REFERENCES " + TABLE_USER + "(user_id));";
//
//
//    //CREATE TABLE SEAT
//    public static final String COLUMN_SEAT_ID = "seat_id";
//    public static final String COLUMN_SEAT_NUMBER = "seat_status";
//    public static final String COLUMN_BUS_ID_FOR_SEAT = "no_of_seats";
//
//    private static final String SEAT_CREATE =
//            "CREATE TABLE " + TABLE_SEAT + " (" +
//                    COLUMN_SEAT_ID + " TEXT PRIMARY KEY, " +
//                    COLUMN_BUS_ID_FOR_SEAT + " TEXT, " +
//                    COLUMN_SEAT_NUMBER + " INTEGER, " +
//                    "FOREIGN KEY(bus_id) REFERENCES " + TABLE_BUS + "(bus_id));";
//
//    //CREATE TABLE BOOKING SEATS
//    public static final String COLUMN_BOOKING_ID_FOR_BOOKED_SEAT = "booking_id";
//    public static final String COLUMN_SEAT_ID_FOR_BOOKED_SEAT = "seat_id";
//    public static final String COLUMN_BOOKING_DATE_FOR_BOOKED_SEAT = "booking_date";
//    public static final String COLUMN_SEAT_STATUS = "seat_status";
//
//    private static final String BOOKED_SEAT_CREATE =
//            "CREATE TABLE " + TABLE_BOOKED_SEAT + " (" +
//                    COLUMN_BOOKING_ID_FOR_BOOKED_SEAT + " TEXT, " +
//                    COLUMN_SEAT_ID_FOR_BOOKED_SEAT + " TEXT, " +
//                    COLUMN_BOOKING_DATE_FOR_BOOKED_SEAT + " TEXT, " +
//                    COLUMN_SEAT_STATUS + " TEXT, " +
//                    "FOREIGN KEY(booking_id) REFERENCES " + TABLE_BOOKING + "(booking_id), " +
//                    "FOREIGN KEY(seat_id) REFERENCES " + TABLE_SEAT + "(seat_id), " +
//                    "PRIMARY KEY(booking_id, seat_id));";

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
                "profile_picture TEXT);";
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
                "user_id TEXT, " +
                "bus_name TEXT, " +
                "bus_license TEXT, " +
                "no_of_seats INTEGER, " +
                "bus_fee INTEGER, " +   
                "departure_time TEXT, " +
                "FOREIGN KEY(route_id) REFERENCES tbl_route(route_id), " +
                "FOREIGN KEY(user_id) REFERENCES tbl_user(user_id));";
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
