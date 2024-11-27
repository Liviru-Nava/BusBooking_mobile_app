package com.mad.bus_booking_test_login.data_access_objects;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mad.bus_booking_test_login.database.DatabaseHelper;

public class RatingDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public RatingDAO(Context context) {
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

    public void storeRating(String bookingId, int newRating) {
        open();
        SQLiteDatabase db = db_helper.getWritableDatabase();

        // Generate a new unique rating ID
        String ratingId = generateRatingId(db);

        // Insert the new rating into the database
        db.execSQL("INSERT INTO tbl_rating (rating_id, booking_id, rating) VALUES (?, ?, ?)",
                new Object[]{ratingId, bookingId, newRating});

    }

    public String generateRatingId(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tbl_rating", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return String.format("R%04d", count + 1);
    }

}
