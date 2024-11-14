package com.mad.bus_booking_test_login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>{
    private final Context context;
    private final Cursor cursor;

    public BookingAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
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
            @SuppressLint("Range") String busName = cursor.getString(cursor.getColumnIndex("bus_name"));
            @SuppressLint("Range") String routeName = cursor.getString(cursor.getColumnIndex("route_name"));
            @SuppressLint("Range") String driverName = cursor.getString(cursor.getColumnIndex("driver_name"));
            @SuppressLint("Range") String startingPoint = cursor.getString(cursor.getColumnIndex("starting_point"));
            @SuppressLint("Range") String endingPoint = cursor.getString(cursor.getColumnIndex("ending_point"));
            @SuppressLint("Range") String bookingDate = cursor.getString(cursor.getColumnIndex("booking_date"));


            holder.textBusName.setText(busName);
            holder.textDriverName.setText("Driver Name: " + driverName);
            holder.textRouteName.setText("Route: " + routeName);
            holder.textStartingPoint.setText(startingPoint);
            holder.textEndingPoint.setText(endingPoint);
            holder.textBookingDate.setText("Date: " + bookingDate);
//            // Set an OnClickListener for the "Book Now" button
//            holder.bookNowButton.setOnClickListener(v -> {
//                // Create an Intent to open ActivityBookSeat and pass data
//                Intent intent = new Intent(context, ActivityBookSeat.class);
//                intent.putExtra("bus_id", busId);
//                intent.putExtra("bus_name", busName);
//                intent.putExtra("bus_license", licenseNumber);
//                intent.putExtra("no_of_seats_available", seatsAvailable);
//                intent.putExtra("bus_fee", busFee);
//                intent.putExtra("departure_time", departureTime);
//                intent.putExtra("route_name", routeName);
//                intent.putExtra("starting_point", startingPoint);
//                intent.putExtra("ending_point", endingPoint);
//                intent.putExtra("user_id", userId);
//                intent.putExtra("name", userName);
//                intent.putExtra("booking_date", booking_date);
//                intent.putExtra("passenger_id", passenger_id);
//
//                // Start ActivityBookSeat with the data
//                context.startActivity(intent);
//            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textBusName, textRouteName, textDriverName, textStartingPoint, textEndingPoint, textBookingDate;
        Button rateButton, changeButton, cancelButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textBusName = itemView.findViewById(R.id.tv_bus_name);
            textRouteName = itemView.findViewById(R.id.tv_route_name);
            textDriverName = itemView.findViewById(R.id.tv_driver_name);
            textStartingPoint = itemView.findViewById(R.id.tv_starting_point);
            textEndingPoint = itemView.findViewById(R.id.tv_ending_point);
            textBookingDate = itemView.findViewById(R.id.tv_booking_date);
            rateButton = itemView.findViewById(R.id.btn_rate);
            changeButton = itemView.findViewById(R.id.btn_change);
            cancelButton = itemView.findViewById(R.id.btn_cancel);
        }
    }
}
