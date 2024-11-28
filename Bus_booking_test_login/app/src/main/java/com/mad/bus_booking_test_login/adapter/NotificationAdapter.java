package com.mad.bus_booking_test_login.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.NotificationDAO;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private final Context context;
    private Cursor cursor;
    private final String userId;
    private NotificationDAO notification;

    public NotificationAdapter(Context context, Cursor cursor, String user_id) {
        this.context = context;
        this.cursor = cursor;
        this.userId = user_id;
        notification = new NotificationDAO(context);
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
            @SuppressLint("Range") String notificationId = cursor.getString(cursor.getColumnIndex("notification_id"));
            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("user_id"));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String message = cursor.getString(cursor.getColumnIndex("message"));
            @SuppressLint("Range") String sentTime = cursor.getString(cursor.getColumnIndex("sent_time"));
            @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("status"));

            holder.tv_notification_title.setText(title);
            holder.tv_notification_content.setText(message);

            // Close button click listener
            holder.btn_close_button.setOnClickListener(view -> {
                // Update status in the database
                notification.closeNotification(notificationId);

                // Remove notification from the list and update UI
                removeItem(position);
            });

        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    // Remove notification from the RecyclerView
    private void removeItem(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        swapCursor(notification.getNotificationById(userId)); // Refresh data
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        notifyDataSetChanged();
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
