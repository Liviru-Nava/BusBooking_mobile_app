package com.mad.bus_booking_test_login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tbl_payment", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return "PAY" + (count + 1);
    }
}
