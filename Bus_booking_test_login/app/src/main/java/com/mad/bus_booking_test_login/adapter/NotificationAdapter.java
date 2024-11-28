package com.mad.bus_booking_test_login.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.R;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private final Context context;
    private final Cursor cursor;

    public NotificationAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_card, parent, false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            @SuppressLint("Range") String busId = cursor.getString(cursor.getColumnIndex("bus_id"));
            @SuppressLint("Range") String busName = cursor.getString(cursor.getColumnIndex("bus_name"));
            @SuppressLint("Range") String routeName = cursor.getString(cursor.getColumnIndex("route_name"));
            @SuppressLint("Range") String driverName = cursor.getString(cursor.getColumnIndex("driver_name"));
            @SuppressLint("Range") String startingPoint = cursor.getString(cursor.getColumnIndex("starting_point"));
            @SuppressLint("Range") String endingPoint = cursor.getString(cursor.getColumnIndex("ending_point"));
            @SuppressLint("Range") String bookingDate = cursor.getString(cursor.getColumnIndex("booking_date"));
            @SuppressLint("Range") String bookingId = cursor.getString(cursor.getColumnIndex("booking_id"));
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notification_title, tv_notification_content;
        ImageButton btn_close_button;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notification_title = itemView.findViewById(R.id.tv_notification_title);
            tv_notification_content = itemView.findViewById(R.id.tv_notification_content);
            btn_close_button = itemView.findViewById(R.id.btn_close_button);
        }
    }
}
