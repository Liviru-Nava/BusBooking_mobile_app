package com.mad.bus_booking_test_login.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.BookingDAO;
import com.mad.bus_booking_test_login.data_access_objects.RatingDAO;
import com.mad.bus_booking_test_login.ui.ActivityChangeSeat;
import com.mad.bus_booking_test_login.ui.ActivityLogin;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>{
    private final Context context;
    private final Cursor cursor;
    private final String userId;
    RatingDAO ratingDAO;
    Dialog logoutDialog;

    public BookingAdapter(Context context, Cursor cursor, String userId) {
        this.context = context;
        this.cursor = cursor;
        this.userId = userId;
        ratingDAO= new RatingDAO(context);
    }

    @NonNull
    @Override
    public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_card, parent, false);
        return new BookingAdapter.BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.BookingViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            @SuppressLint("Range") String busId = cursor.getString(cursor.getColumnIndex("bus_id"));
            @SuppressLint("Range") String busName = cursor.getString(cursor.getColumnIndex("bus_name"));
            @SuppressLint("Range") String routeName = cursor.getString(cursor.getColumnIndex("route_name"));
            @SuppressLint("Range") String driverName = cursor.getString(cursor.getColumnIndex("driver_name"));
            @SuppressLint("Range") String startingPoint = cursor.getString(cursor.getColumnIndex("starting_point"));
            @SuppressLint("Range") String endingPoint = cursor.getString(cursor.getColumnIndex("ending_point"));
            @SuppressLint("Range") String bookingDate = cursor.getString(cursor.getColumnIndex("booking_date"));
            @SuppressLint("Range") String bookingId = cursor.getString(cursor.getColumnIndex("booking_id"));


            holder.textBusName.setText(busName);
            holder.textDriverName.setText("Driver Name: " + driverName);
            holder.textRouteName.setText("Route: " + routeName);
            holder.textStartingPoint.setText(startingPoint);
            holder.textEndingPoint.setText(endingPoint);
            holder.textBookingDate.setText("Date: " + bookingDate);

            // Set an OnClickListener for the "Book Now" button
            holder.changeButton.setOnClickListener(v -> {
                // Create an Intent to open ActivityBookSeat and pass data
                Intent intent = new Intent(context, ActivityChangeSeat.class);
                intent.putExtra("bus_id", busId);
                intent.putExtra("user_id", userId);
                intent.putExtra("booking_id", bookingId);
                intent.putExtra("bus_name", busName);
                intent.putExtra("route_name", routeName);
                intent.putExtra("driver_name", driverName);
                intent.putExtra("starting_point", startingPoint);
                intent.putExtra("ending_point", endingPoint);
                intent.putExtra("booking_date", bookingDate);

                // Start ActivityBookSeat with the data
                context.startActivity(intent);
            });

            //rate now button
            holder.rateButton.setOnClickListener(v -> {
                showRatingDialog(bookingId);
            });

            holder.cancelButton.setOnClickListener(v -> {
                cancelBooking(bookingId);
            });
        }
    }

    private void cancelBooking(String bookingId) {
        // Create and configure the dialog
        logoutDialog = new Dialog(context);
        logoutDialog.setContentView(R.layout.activity_cancel_booking);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Initialize "Yes" and "No" buttons
        Button yesButton = logoutDialog.findViewById(R.id.btn_yes);
        Button noButton = logoutDialog.findViewById(R.id.btn_no);

        // Set "Yes" button behavior
        yesButton.setOnClickListener(v -> {
            // Call a method to delete the booking from the database
            deleteBooking(bookingId);

            // Show a success message
            Toast.makeText(context, "Booking cancelled successfully!", Toast.LENGTH_SHORT).show();

            // Dismiss the dialog
            logoutDialog.dismiss();

            // Refresh the RecyclerView
            notifyDataSetChanged();
        });

        // Set "No" button behavior
        noButton.setOnClickListener(v -> logoutDialog.dismiss());

        // Show the dialog
        logoutDialog.show();
    }

    // Method to delete a booking from the database
    private void deleteBooking(String bookingId) {
        try {
            // Assuming you have a DAO or helper to handle the database operations
            BookingDAO booking = new BookingDAO(context);

            // Call the delete method
            boolean isDeleted = booking.deleteBooking(bookingId);

            if (!isDeleted) {
                Toast.makeText(context, "Failed to cancel the booking!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "Booking has been cancelled successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "An error occurred while cancelling the booking!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textBusName, textRouteName, textDriverName, textStartingPoint, textEndingPoint, textBookingDate;
        Button rateButton, changeButton, cancelButton;
        Dialog logoutDialog;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textBusName = itemView.findViewById(R.id.tv_bus_name);
            textRouteName = itemView.findViewById(R.id.tv_route_name);
            textDriverName = itemView.findViewById(R.id.tv_driver_name);
            textStartingPoint = itemView.findViewById(R.id.tv_starting_point);
            textEndingPoint = itemView.findViewById(R.id.tv_ending_point);
            textBookingDate = itemView.findViewById(R.id.tv_booking_date);
            rateButton = itemView.findViewById(R.id.btn_rate_now);
            changeButton = itemView.findViewById(R.id.btn_change);
            cancelButton = itemView.findViewById(R.id.btn_cancel);
        }
    }

    private void showRatingDialog(String bookingId) {
        // Inflate dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_dialog_rate_now, null);

        // Initialize dialog components
        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        TextView ratingValue = dialogView.findViewById(R.id.tv_rating_value);
        Button submitButton = dialogView.findViewById(R.id.btn_submit_rating);

        // Update rating text on change
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) ->
                ratingValue.setText("Rating: " + (int) rating + "/5"));

        // Show dialog
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        submitButton.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            if (rating > 0) {
                ratingDAO.storeRating(bookingId, rating);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please select a rating!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
