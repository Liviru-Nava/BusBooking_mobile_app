package com.mad.bus_booking_test_login.ui;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mad.bus_booking_test_login.R;
import com.mad.bus_booking_test_login.data_access_objects.UserDAO;

import java.util.Properties;
import java.util.Random;
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

public class ActivityDriverProfile extends AppCompatActivity {

    private String userId, name, email, newPassword, confirmPassword, verifyPassword, tempPassword;
    private byte[] user_image;
    Bitmap profile_image = null;
    TextView txt_username;
    ImageView userImageView;
    UserDAO user;

    Dialog resetDialog, logoutDialog;
    EditText et_new_password, et_confirm_password, et_temp_password;

    @Override
    public void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_driver_profile);

        //get values from intent and set it to variables
        userId = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        //initialise the layout components
        txt_username = findViewById(R.id.txt_user_name);
        userImageView = findViewById(R.id.image_view);
        txt_username.setText(name);
        user = new UserDAO(this);

        Cursor cursor = user.getUserById(userId);
        if(cursor.moveToFirst()){
            email = cursor.getString(5);
            user_image = cursor.getBlob(7);
            if(user_image != null){
                profile_image = BitmapFactory.decodeByteArray(user_image, 0, user_image.length);
                userImageView.setImageBitmap(profile_image);
            }
        }

        //create a pop up
        resetDialog = new Dialog(this);
        resetDialog.setContentView(R.layout.activity_reset_password);
        resetDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        resetDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_box));

        logoutDialog = new Dialog(this);
        logoutDialog.setContentView(R.layout.activity_logout);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    //method to navigate to edit profile
    public void onEditButton(View view){
        Intent navigate_edit_profile = new Intent(this, ActivityEditDriverProfile.class);
        navigate_edit_profile.putExtra("user_id", userId);
        navigate_edit_profile.putExtra("name", name);
        startActivity(navigate_edit_profile);
    }

    //method to navigate home
    public void onNavigateHome(View view){
        Intent navigate_home = new Intent(this, ActivityDriverHome.class);
        navigate_home.putExtra("name", name);
        navigate_home.putExtra("user_id", userId);
        startActivity(navigate_home);
    }

    //method to navigate to booking
    public void onNavigateBookingList(View view){
        Intent navigate_booking = new Intent(this, ActivityDriverBookingList.class);
        navigate_booking.putExtra("name", name);
        navigate_booking.putExtra("user_id", userId);
        startActivity(navigate_booking);
    }

    //method to navigate the reset password
    public void onResetPassword(View view){
        resetDialog.show();
    }

    //method to reset the password
    public void onConfirmResetPassword(View view){
        et_new_password = resetDialog.findViewById(R.id.et_new_password);
        et_confirm_password = resetDialog.findViewById(R.id.et_confirm_password);
        et_temp_password = resetDialog.findViewById(R.id.et_temp_password);

        newPassword = et_new_password.getText().toString();
        confirmPassword = et_confirm_password.getText().toString();
        verifyPassword = et_temp_password.getText().toString();

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tempPassword == null) {
            // Generate a temporary password
            tempPassword = generateTempPassword();
            sendEmailToUser(tempPassword);
            Toast.makeText(this, "A temporary password has been sent to your email.", Toast.LENGTH_SHORT).show();
        } else if (verifyPassword.equals(tempPassword)) {
            // Update the password in the database
            boolean isUpdated = user.updatePassword(userId, newPassword);
            if (isUpdated) {
                Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                resetDialog.dismiss();
            } else {
                Toast.makeText(this, "Error resetting password. Try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Temporary password does not match.", Toast.LENGTH_SHORT).show();
        }
    }
    //method to invoke the logout function
    public void onLogoutClicked(View view){
        logoutDialog.show();
    }
    public void onYesClicked(View view){
        Intent navigate_login = new Intent(this, ActivityLogin.class);
        startActivity(navigate_login);
    }
    public void onNoClicked(View view){
        logoutDialog.dismiss();
    }

    // Generate a random 5-character temporary password
    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder tempPassword = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            tempPassword.append(chars.charAt(random.nextInt(chars.length())));
        }
        return tempPassword.toString();
    }

    //method to send mail to user
    private void sendEmailToUser(String tempPassword) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            String userEmail = email; // Fetch user's email from the database
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
                message.setSubject("Temporary Password");
                message.setText("Your temporary password is: " + tempPassword);

                // Send the email
                Transport.send(message);

                // Update the UI on success
                runOnUiThread(() -> Toast.makeText(this, "Temporary password sent to email.", Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                e.printStackTrace();
                // Update the UI on failure
                runOnUiThread(() -> Toast.makeText(this, "Failed to send email.", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
