package com.mad.bus_booking_test_login.data_access_objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mad.bus_booking_test_login.database.DatabaseHelper;

public class NotificationDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public NotificationDAO(Context context) {
        this.db_helper = new DatabaseHelper(context);
        open();
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
    //insert notification message
    public void insertNotification(String userId, String title, String message, String sent_time) {
        open();
        SQLiteDatabase db = db_helper.getWritableDatabase();

        // Generate a new unique rating ID
        String notificationId = generateNotificationId(db);

        // Insert the new rating into the database
        db.execSQL("INSERT INTO tbl_notification (notification_id, user_id, title, message, sent_time, status) VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{notificationId, userId, title, message, sent_time, "active"});
    }

    //get all notifications for the passenger and where it's status is active
    public Cursor getNotificationById(String userId){
        open();
        SQLiteDatabase db = db_helper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tbl_notification WHERE user_id = ? AND status = ?", new String[]{userId, "active"});
    }

    public void closeNotification(String notificationId) {
        SQLiteDatabase db = db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "closed");

        db.update("tbl_notification", values, "notification_id = ?", new String[]{notificationId});
        db.close();
    }

    private String generateNotificationId(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tbl_notification", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return String.format("N%04d", count + 1);
    }

}
