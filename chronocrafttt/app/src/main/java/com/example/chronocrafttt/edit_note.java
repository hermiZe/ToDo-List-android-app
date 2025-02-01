package com.example.chronocrafttt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class edit_note extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private Button btnSaveChanges;
    private DatabaseHelper dbHelper;
    private String originalTitle, originalContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        dbHelper = new DatabaseHelper(this);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Retrieve data from the Intent
        Intent intent = getIntent();
        originalTitle = intent.getStringExtra("title");
        originalContent = intent.getStringExtra("content");

        // Pre-fill the EditTexts with the current data
        editTextTitle.setText(originalTitle);
        editTextContent.setText(originalContent);

        // Handle Save Changes button click
        btnSaveChanges.setOnClickListener(v -> {
            String updatedTitle = editTextTitle.getText().toString().trim();
            String updatedContent = editTextContent.getText().toString().trim();

            if (!updatedTitle.isEmpty() && !updatedContent.isEmpty()) {
                // Update the database
                boolean titleUpdated = dbHelper.updateGroupTitle(originalTitle, updatedTitle); // Implement this method in the DatabaseHelper
                boolean contentUpdated = dbHelper.updateNote(updatedTitle, originalContent, updatedContent);

                if (titleUpdated && contentUpdated) {
                    Toast.makeText(this, getString(R.string.note_updated_successfully), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Indicate success to the calling activity
                    finish(); // Close the activity
                } else {
                    Toast.makeText(this, getString(R.string.failed_to_update_note), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.fields_cannot_be_empty), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
