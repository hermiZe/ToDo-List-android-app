package com.example.chronocrafttt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class password extends AppCompatActivity {

    private EditText etPassword;
    private Button btnSubmit;
    private TextView forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // Bind UI components
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        // Set UI text dynamically
        etPassword.setHint(getString(R.string.enter_password));
        btnSubmit.setText(getString(R.string.submit));
        forgotPasswordLink.setText(getString(R.string.forgot_password));

        // Forgot Password Link click listener
        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(password.this, reset_password.class);
            startActivity(intent);
        });

        // Submit Button click listener
        btnSubmit.setOnClickListener(v -> {
            String enteredPassword = etPassword.getText().toString();
            if (isPasswordValid(enteredPassword)) {
                // Navigate to the dashboard if password is valid
                Intent intent = new Intent(password.this, dashboard.class);
                startActivity(intent);
                finish();
            } else {
                // Show error message if password is invalid
                Toast.makeText(this, getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates the entered password against the stored password.
     *
     * @param enteredPassword The password entered by the user.
     * @return True if the password is valid, false otherwise.
     */
    private boolean isPasswordValid(String enteredPassword) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String storedPassword = prefs.getString("password", null);
        return enteredPassword.equals(storedPassword); // Compare passwords
    }

    /**
     * Encrypts the password (dummy encryption for demonstration purposes).
     *
     * @param password The password to encrypt.
     * @return The encrypted password.
     */
    private String encrypt(String password) {
        return String.valueOf(password.hashCode()); // Simple hash for demonstration
    }
}
