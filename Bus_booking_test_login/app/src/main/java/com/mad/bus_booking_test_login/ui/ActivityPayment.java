package com.mad.bus_booking_test_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.BookingDAO;
import com.mad.bus_booking_test_login.data_access_objects.PaymentDAO;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivityPayment extends AppCompatActivity {

    private TextView tv_bus_name, tv_from, tv_to, tv_booking_date, tv_route_name, tv_bus_fee, tv_no_seats_booked, tv_total_fee;
    String busName, startingPoint, endingPoint, bookingDate, routeName, passengerId, busId, name, bookingId;
    int busFee, seatBookedCount, totalFee;
    BookingDAO booking;
    PaymentDAO payment;
    UserDAO user;
    StringBuilder seats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        //initialize the DAOs
        booking = new BookingDAO(this);
        payment = new PaymentDAO(this);
        user = new UserDAO(this);

        //initialize components from activity_payment
        tv_bus_name = findViewById(R.id.tv_bus_name);
        tv_from = findViewById(R.id.tv_from);
        tv_to = findViewById(R.id.tv_to);
        tv_booking_date = findViewById(R.id.tv_booking_date);
        tv_route_name = findViewById(R.id.tv_route_name);
        tv_bus_fee = findViewById(R.id.tv_bus_fare);
        tv_no_seats_booked = findViewById(R.id.tv_number_seats_booked);
        tv_total_fee = findViewById(R.id.tv_total_fee);

        //get values from intents
        busName = getIntent().getStringExtra("bus_name");
        startingPoint = getIntent().getStringExtra("starting_point");
        endingPoint = getIntent().getStringExtra("ending_point");
        bookingDate = getIntent().getStringExtra("booking_date");
        routeName = getIntent().getStringExtra("route_name");

        passengerId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");
        busId = getIntent().getStringExtra("bus_id");
        busFee = getIntent().getIntExtra("bus_fee", 0);
        seatBookedCount = getIntent().getIntExtra("seats_booked_count", 0);
        totalFee = busFee * seatBookedCount;

        //set the values to the TextViews
        tv_bus_name.setText(busName);
        tv_from.setText("From: " + startingPoint);
        tv_to.setText("To: " + endingPoint);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(bookingDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
            String formatted_date = outputFormat.format(date);
            tv_booking_date.setText(formatted_date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        tv_route_name.setText("Route: " + routeName);
        tv_bus_fee.setText("Rs. " + busFee);
        tv_no_seats_booked.setText(String.valueOf(seatBookedCount));
        tv_total_fee.setText("Rs. " + totalFee);
    }

    //pay now method
    public void onPayNowClicked(View view){
        //Insert booking and retrieve booking ID
        bookingId = booking.insertBooking(passengerId, busId, bookingDate, totalFee);

        //Insert seat details for the booking
        ArrayList<String> seatIds = getIntent().getStringArrayListExtra("seat_ids");
        booking.insertBookingSeats(bookingId, seatIds, bookingDate);

        seats = new StringBuilder();
        if (seatIds != null) {
            for (String seat : seatIds) {
                seats.append(seat).append(", ");
            }
            // Remove the trailing comma and space
            if (seats.length() > 0) {
                seats.setLength(seats.length() - 2);
            }
        }

        //Insert payment details
        String paymentMethod = "Card"; // or retrieve dynamically based on user's choice
        payment.insertPayment(bookingId, passengerId, paymentMethod, totalFee);

        //Display message to mention booking successful
        Toast.makeText(this, "Booking successful!", Toast.LENGTH_LONG).show();

        //send email here
        sendEmailToUser(passengerId);

        //Navigate to the passenger's home page
        Intent navigate_passenger_home_intent = new Intent(this, ActivityPassengerHome.class);
        navigate_passenger_home_intent.putExtra("user_id", passengerId);
        navigate_passenger_home_intent.putExtra("name", name);


        startActivity(navigate_passenger_home_intent);
    }

    //method to send mail to user
    private void sendEmailToUser(String userId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            String userEmail = user.getEmailByUserId(userId);
            if (userEmail == null) {
                runOnUiThread(() -> Toast.makeText(this, "Email not found for user.", Toast.LENGTH_SHORT).show());
                return;
            }

            // Email credentials (use your email and app-specific password for Gmail)
            final String fromEmail = "hotelcinnamon123@gmail.com";
            final String fromPassword = "gkew shka uiog term";

            // Email properties
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            // Create a session
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            });

            try {
                // Create the email message
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
                message.setSubject("Booking Confirmation");
                message.setText(
                        "Dear " + name + ",\n\n" +
                                "Thank you for choosing our bus booking service! Here are your booking details:\n\n" +
                                "Booking ID: " + bookingId + "\n" +
                                "Bus Name: " + busName + "\n" +
                                "Route: " + routeName + "\n" +
                                "From: " + startingPoint + "\n" +
                                "To: " + endingPoint + "\n" +
                                "Travel Date: " + bookingDate + "\n" +
                                "Number of Seats: " + seatBookedCount + "\n" +
                                "Seats Booked: " + seats.toString() + "\n" +
                                "Total Fare: Rs. " + totalFee + "\n\n" +
                                "Please ensure to carry this email for reference during your journey.\n" +
                                "If you have any questions, feel free to contact us.\n\n" +
                                "We wish you a safe and pleasant journey!\n\n" +
                                "Warm regards,\n" +
                                "Bus Booking Team"
                );

                // Send the email
                Transport.send(message);

                // Update the UI on success
                runOnUiThread(() -> Toast.makeText(this, "Booking confirmation mail has been sent.", Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                e.printStackTrace();
                // Update the UI on failure
                runOnUiThread(() -> Toast.makeText(this, "Failed to send email.", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
