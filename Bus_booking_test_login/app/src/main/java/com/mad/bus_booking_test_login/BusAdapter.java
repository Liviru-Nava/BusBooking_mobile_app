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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder>{
    private final Context context;
    private final Cursor cursor;
    private String booking_date;
    private String passenger_id;

    public BusAdapter(Context context, Cursor cursor, String bookingDate, String userId) {
        this.context = context;
        this.cursor = cursor;
        this.booking_date= bookingDate;
        this.passenger_id = userId;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus_card, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            @SuppressLint("Range") String busId = cursor.getString(cursor.getColumnIndex("bus_id"));
            @SuppressLint("Range") String busName = cursor.getString(cursor.getColumnIndex("bus_name"));
            @SuppressLint("Range") String licenseNumber = cursor.getString(cursor.getColumnIndex("bus_license"));
            @SuppressLint("Range") int seatsAvailable = cursor.getInt(cursor.getColumnIndex("no_of_seats_available"));
            @SuppressLint("Range") int busFee = cursor.getInt(cursor.getColumnIndex("bus_fee"));
            @SuppressLint("Range") String departureTime = cursor.getString(cursor.getColumnIndex("departure_time"));
            @SuppressLint("Range") String routeName = cursor.getString(cursor.getColumnIndex("route_name"));
            @SuppressLint("Range") String startingPoint = cursor.getString(cursor.getColumnIndex("starting_point"));
            @SuppressLint("Range") String endingPoint = cursor.getString(cursor.getColumnIndex("ending_point"));
            @SuppressLint("Range") String userId = cursor.getString(cursor.getColumnIndex("user_id"));
            @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex("name"));

            holder.textBusName.setText(busName);
            holder.textLicenseNumber.setText("License: " + licenseNumber);
            holder.textSeatsAvailable.setText("Seats: " + seatsAvailable);
            holder.textBusFee.setText("Fee: Rs." + busFee);
            holder.textDepartureTime.setText("Departs: " + departureTime);
            holder.textRouteName.setText("Route: " + routeName);
            holder.textStartingPoint.setText(startingPoint);
            holder.textEndingPoint.setText(endingPoint);

            // Set an OnClickListener for the "Book Now" button
            holder.bookNowButton.setOnClickListener(v -> {
                // Create an Intent to open ActivityBookSeat and pass data
                Intent intent = new Intent(context, ActivityBookSeat.class);
                intent.putExtra("bus_id", busId);
                intent.putExtra("bus_name", busName);
                intent.putExtra("bus_license", licenseNumber);
                intent.putExtra("no_of_seats_available", seatsAvailable);
                intent.putExtra("bus_fee", busFee);
                intent.putExtra("departure_time", departureTime);
                intent.putExtra("route_name", routeName);
                intent.putExtra("starting_point", startingPoint);
                intent.putExtra("ending_point", endingPoint);
                intent.putExtra("user_id", userId);
                intent.putExtra("name", userName);
                intent.putExtra("booking_date", booking_date);
                intent.putExtra("passenger_id", passenger_id);

                // Start ActivityBookSeat with the data
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView textBusName, textLicenseNumber, textSeatsAvailable, textBusFee, textDepartureTime, textRouteName, textStartingPoint, textEndingPoint;
        Button bookNowButton;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            textBusName = itemView.findViewById(R.id.tv_bus_name);
            textLicenseNumber = itemView.findViewById(R.id.tv_bus_license);
            textSeatsAvailable = itemView.findViewById(R.id.tv_seats_available);
            textBusFee = itemView.findViewById(R.id.tv_bus_fee);
            textDepartureTime = itemView.findViewById(R.id.tv_departure_time);
            textRouteName = itemView.findViewById(R.id.tv_route_name);
            textStartingPoint = itemView.findViewById(R.id.tv_starting_point);
            textEndingPoint = itemView.findViewById(R.id.tv_ending_point);
            bookNowButton = itemView.findViewById(R.id.btn_book_now);
        }
    }
}
