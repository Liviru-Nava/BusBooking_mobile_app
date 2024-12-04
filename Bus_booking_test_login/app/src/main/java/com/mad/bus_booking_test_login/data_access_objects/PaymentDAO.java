package com.mad.bus_booking_test_login.data_access_objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mad.bus_booking_test_login.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentDAO {
    private final DatabaseHelper db_helper;
    private SQLiteDatabase db;

    public PaymentDAO(Context context) {
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

    // Insert into tbl_payment
    public void insertPayment(String bookingId, String userId, String paymentMethod, int amount) {
        String paymentId = generatePaymentId();
        ContentValues values = new ContentValues();
        values.put("payment_id", paymentId);
        values.put("booking_id", bookingId);
        values.put("user_id", userId);
        values.put("payment_method", paymentMethod);
        values.put("amount", amount);
        values.put("payment_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        db.insert("tbl_payment", null, values);
    }

    // Method to generate a unique payment ID in the format PAY1, PAY2, etc.
    private String generatePaymentId() {
        open();
        Cursor cursor = db.rawQuery("SELECT payment_id FROM tbl_payment ORDER BY ROWID DESC LIMIT 1", null);
        String newId;
        if (cursor.moveToFirst()) {
            // Extract the last booking ID
            String lastId = cursor.getString(0);
            // Extract the numeric part from the ID and increment it
            int numericPart = Integer.parseInt(lastId.replace("PAY", ""));
            newId = "PAY" + (numericPart + 1);
        } else {
            // If no records exist, start with "BOOK1"
            newId = "PAY1";
        }
        cursor.close();
        return newId;
    }
}
