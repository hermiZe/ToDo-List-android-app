package com.example.chronocrafttt;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;

public class change_password extends AppCompatActivity {

    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(view -> {
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (!isCurrentPasswordCorrect(currentPassword)) {
                Toast.makeText(this, getString(R.string.current_password_incorrect), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, getString(R.string.new_password_mismatch), Toast.LENGTH_SHORT).show();
                return;
            }

            savePassword(newPassword);
            Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private boolean isCurrentPasswordCorrect(String currentPassword) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String storedPassword = prefs.getString("password", null);
        return storedPassword != null && storedPassword.equals(encrypt(currentPassword));
    }

    private void savePassword(String password) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        editor.putString("password", new String(passwordBytes, StandardCharsets.UTF_8));
        editor.apply();
    }

    private String encrypt(String password) {
        return String.valueOf(password.hashCode());
    }
}
