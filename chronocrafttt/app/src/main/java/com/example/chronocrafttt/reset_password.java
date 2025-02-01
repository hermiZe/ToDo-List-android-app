package com.example.chronocrafttt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class reset_password extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Bind UI components
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Set dynamic hints and button text for localization
        etNewPassword.setHint(getString(R.string.new_password_hint));
        etConfirmPassword.setHint(getString(R.string.confirm_password_hint));
        btnResetPassword.setText(getString(R.string.reset_password_button));

        // Set click listener for the reset button
        btnResetPassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (!newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                if (newPassword.equals(confirmPassword)) {
                    // Save the new password to SharedPreferences
                    savePassword(newPassword);

                    // Notify the user and finish the activity
                    Toast.makeText(this, getString(R.string.password_reset_success), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Show an error message if passwords do not match
                    Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                }
            } else {
                // Show an error message if fields are empty
                Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Saves the password securely to SharedPreferences.
     *
     * @param password The new password to save.
     */
    private void savePassword(String password) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("password", password);
        editor.apply();
    }
}
