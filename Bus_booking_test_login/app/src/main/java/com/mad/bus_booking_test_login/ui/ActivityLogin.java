package com.mad.bus_booking_test_login.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class ActivityLogin extends AppCompatActivity {

    private EditText et_email, et_password, et_new_password, et_confirm_password, et_temp_password, et_email_address;
    private ImageButton btn_show_hide_password;
    private UserDAO user;
    private boolean isPasswordVisible = false;
    private Dialog forgotPasswordDialog;
    private String newPassword, confirmPassword, tempPassword, verifyPassword, emailAddress, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_show_hide_password = findViewById(R.id.btn_show_hide_password);
        user = new UserDAO(this);


        //create a pop up
        forgotPasswordDialog = new Dialog(this);
        forgotPasswordDialog.setContentView(R.layout.activity_forgot_password);
        forgotPasswordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        forgotPasswordDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_box));
    }

    //button for login
    public void onLoginClicked(View view){
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        // Validate input fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email and password are correct
        boolean isValidUser = user.validateUser(email, password);
        if (isValidUser) {
            // Retrieve both user_id and name
            String[] userDetails = user.getUserDetails(email);
            if (userDetails != null && userDetails.length == 3) {
                String userId = userDetails[0];  // user_id
                String userName = userDetails[1]; // name
                String userType = userDetails[2];

                switch (userType) {
                    case "Passenger": {
                        // Show success message
                        Toast.makeText(this, "Logged in successfully as passenger!", Toast.LENGTH_SHORT).show();

                        // Pass user_id and name to the next activity
                        Intent navigate_passenger = new Intent(this, ActivityPassengerHome.class);
                        navigate_passenger.putExtra("user_id", userId); // Pass the user_id to the next activity
                        navigate_passenger.putExtra("name", userName);  // Pass the name to the next activity

                        startActivity(navigate_passenger);
                        finish();
                        break;
                    }
                    case "Owner": {
                        // Show success message
                        Toast.makeText(this, "Logged in successfully as owner!", Toast.LENGTH_SHORT).show();

                        // Pass user_id and name to the next activity
                        Intent navigate_owner = new Intent(this, ActivityOwnerHome.class);
                        navigate_owner.putExtra("user_id", userId); // Pass the user_id to the next activity
                        navigate_owner.putExtra("name", userName);  // Pass the name to the next activity

                        startActivity(navigate_owner);
                        finish();
                        break;
                    }
                    case "Driver": {
                        // Show success message
                        Toast.makeText(this, "Logged in successfully as driver!", Toast.LENGTH_SHORT).show();

                        // Pass user_id and name to the next activity
                        Intent navigate_driver = new Intent(this, ActivityDriverHome.class);
                        navigate_driver.putExtra("user_id", userId); // Pass the user_id to the next activity
                        navigate_driver.putExtra("name", userName);  // Pass the name to the next activity

                        startActivity(navigate_driver);
                        finish();
                        break;
                    }
                }
            }
        } else {
            // Unsuccessful login
            Toast.makeText(this, "Login unsuccessful. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    //button for signup
    public void onSignUpClicked(View view){
        Intent navigate_signup_intent = new Intent(this, ActivityRegisterSelect.class);
        startActivity(navigate_signup_intent);
    }

    public void onTogglePassword(View view){
        if(isPasswordVisible){
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btn_show_hide_password.setImageResource(R.drawable.hide); // Set "hide" icon
        }else {
            // Show password
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btn_show_hide_password.setImageResource(R.drawable.view); // Set "view" icon
        }

        // Keep cursor at the end of the text
        et_password.setSelection(et_password.length());

        // Toggle visibility state
        isPasswordVisible = !isPasswordVisible;
    }

    public void onForgotPasswordClicked(View view){
        forgotPasswordDialog.show();
    }

    public void onConfirmResetPassword(View view){
        et_email_address = forgotPasswordDialog.findViewById(R.id.et_email_address);
        et_new_password = forgotPasswordDialog.findViewById(R.id.et_new_password);
        et_confirm_password = forgotPasswordDialog.findViewById(R.id.et_confirm_password);
        et_temp_password = forgotPasswordDialog.findViewById(R.id.et_temp_password);

        emailAddress = et_email_address.getText().toString();
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
            userId = user.getUserIdByEmail(emailAddress);
            boolean isUpdated = user.updatePassword(userId, newPassword);
            if (isUpdated) {
                Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                forgotPasswordDialog.dismiss();
            } else {
                Toast.makeText(this, "Error resetting password. Try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Temporary password does not match.", Toast.LENGTH_SHORT).show();
        }
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
            String userEmail = emailAddress;
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
                message.setSubject("Forgot Password");
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