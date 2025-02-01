package com.example.chronocrafttt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Set_Password extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnSavePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSavePassword = findViewById(R.id.btnSavePassword);

        btnSavePassword.setOnClickListener(v -> {
            String password = etNewPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (validatePasswords(password, confirmPassword)) {
                // Encrypt the password before saving it
                String encryptedPassword = encrypt(password);
                if (encryptedPassword != null) {
                    savePassword(encryptedPassword);
                    Toast.makeText(this, getString(R.string.password_set_success), Toast.LENGTH_SHORT).show();
                    finish(); // Return to the launcher or dashboard
                }
            }
        });
    }

    /**
     * Validates the entered passwords.
     *
     * @param newPassword      The new password entered by the user.
     * @param confirmPassword  The confirmation password entered by the user.
     * @return true if validation passes, false otherwise.
     */
    private boolean validatePasswords(String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, getString(R.string.fill_in_all_fields), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, getString(R.string.password_length_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Saves the encrypted password securely using SharedPreferences.
     *
     * @param password The encrypted password to save.
     */
    private void savePassword(String password) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            // Ensure password is stored securely, using SHA-256 hash
            editor.putString("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    /**
     * Encrypts the password using SHA-256.
     *
     * @param password The password to encrypt.
     * @return The encrypted password (SHA-256 hash).
     */
    private String encrypt(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));  // Ensure UTF-8 encoding
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString(); // Return the hashed password
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Navigates the user back to the Dashboard.
     */
    private void navigateToDashboard() {
        Intent intent = new Intent(Set_Password.this, dashboard.class);
        startActivity(intent);
        finish();
    }
}
