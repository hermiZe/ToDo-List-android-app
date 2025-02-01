package com.example.chronocrafttt;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class notes extends AppCompatActivity {

    private EditText etTitle, etNoteContent;
    private Button btnSave, btnClear;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Initialize UI elements
        etTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);
        btnSave = findViewById(R.id.btnSaveNote);
        btnClear = findViewById(R.id.btnClearNote);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Back button in the taskbar
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notes.this, dashboard.class));
                finish();
            }
        });

        // Save button functionality
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String content = etNoteContent.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(notes.this, getString(R.string.both_fields_required), Toast.LENGTH_SHORT).show();
                } else {
                    saveNoteToDatabase(title, content);
                }
            }
        });

        // Clear button functionality
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTitle.setText("");
                etNoteContent.setText("");
            }
        });
    }

    private void saveNoteToDatabase(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);

        long result = db.insert("notes", null, values);
        if (result != -1) {
            Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etNoteContent.setText("");
        } else {
            Toast.makeText(this, getString(R.string.note_failed) , Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
