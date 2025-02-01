package com.example.chronocrafttt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if a password is set
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedPassword = prefs.getString("password", null);

        if (savedPassword != null) {
            // If password is set, navigate to PasswordActivity
            Intent passwordIntent = new Intent(MainActivity.this, password.class);
            startActivity(passwordIntent);
        } else {
            // If no password is set, navigate to DashboardActivity
            Intent dashboardIntent = new Intent(MainActivity.this, dashboard.class);
            startActivity(dashboardIntent);
        }

        // Finish MainActivity to prevent going back to this screen
        finish();
    }


}
