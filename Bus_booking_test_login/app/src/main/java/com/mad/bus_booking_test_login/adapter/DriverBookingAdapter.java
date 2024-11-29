package com.mad.bus_booking_test_login.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mad.bus_booking_test_login.R;

public class DriverBookingAdapter extends RecyclerView.Adapter<DriverBookingAdapter.DriverBookingViewHolder>{
    private final Context context;
    private final Cursor cursor;

    public DriverBookingAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public DriverBookingAdapter.DriverBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_driver_book_card, parent, false);
        return new DriverBookingAdapter.DriverBookingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverBookingAdapter.DriverBookingViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            @SuppressLint("Range") String bookingDate = cursor.getString(cursor.getColumnIndex("booking_date"));
            @SuppressLint("Range") String routeName = cursor.getString(cursor.getColumnIndex("route_name"));
            @SuppressLint("Range") String startingPoint = cursor.getString(cursor.getColumnIndex("starting_point"));
            @SuppressLint("Range") String endingPoint = cursor.getString(cursor.getColumnIndex("ending_point"));
            @SuppressLint("Range") String departureTime = cursor.getString(cursor.getColumnIndex("departure_time"));
            @SuppressLint("Range") String totalPassengers = cursor.getString(cursor.getColumnIndex("total_no_of_passengers"));


            holder.tv_booking_date.setText(bookingDate);
            holder.tv_route_name.setText("Route name: " + routeName);
            holder.tv_starting_point.setText(startingPoint);
            holder.tv_ending_point.setText(endingPoint);
            holder.tv_departure_time.setText("Departure time: " + departureTime);
            holder.tv_no_of_passengers.setText("Total Passengers: " + totalPassengers);
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    static class DriverBookingViewHolder extends RecyclerView.ViewHolder {
        TextView tv_booking_date, tv_route_name, tv_starting_point, tv_ending_point, tv_departure_time, tv_no_of_passengers;

        public DriverBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_booking_date = itemView.findViewById(R.id.tv_booking_date);
            tv_route_name = itemView.findViewById(R.id.tv_route_name);
            tv_starting_point = itemView.findViewById(R.id.tv_starting_point);
            tv_ending_point = itemView.findViewById(R.id.tv_ending_point);
            tv_departure_time = itemView.findViewById(R.id.tv_departure_time);
            tv_no_of_passengers = itemView.findViewById(R.id.tv_no_of_passengers);
        }
    }
}
