package com.example.chronocrafttt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUpRedirect;
    private CheckBox checkbox;

    private static final String SHARED_PREFS = "UserPrefs"; // Shared Preferences File Name
    private static final String USERNAME_KEY = "username"; // Key for Username
    private static final String PASSWORD_KEY = "password"; // Key for Password

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.Username);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUpRedirect = findViewById(R.id.tvSignUpRedirect);
        checkbox = findViewById(R.id.Check);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Load username and password from SharedPreferences (if saved)
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(USERNAME_KEY, "");
        String savedPassword = sharedPreferences.getString(PASSWORD_KEY, "");

        // Populate the EditText fields if data exists
        etUsername.setText(savedUsername);
        etPassword.setText(savedPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(login.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isUserValid = dbHelper.checkUser(username, password);
                    if (isUserValid) {
                        Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // Save credentials to SharedPreferences if checkbox is checked
                        if (checkbox.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(USERNAME_KEY, username);
                            editor.putString(PASSWORD_KEY, password);
                            editor.apply();
                        }

                        // Redirect to Main Activity or Dashboard
                        startActivity(new Intent(login.this, dashboard.class));
                        finish();
                    } else {
                        Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Redirect to Sign Up Page
        tvSignUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, sign_up.class));
                finish();
            }
        });
    }
}
