package com.example.chronocrafttt;



import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Notes Table Columns
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_NOTE_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL)";
        String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES + " ("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_CONTENT + " TEXT NOT NULL);";
        db.execSQL(createTableQuery);
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Method to add a new user
    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Return true if insertion was successful
        return result != -1;
    }


    // Method to verify user login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return userExists;
    }
 //fetch and display notes
    public HashMap<String, ArrayList<String>> getGroupedNotes() {
        HashMap<String, ArrayList<String>> groupedNotes = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch all notes
        Cursor cursor = db.rawQuery("SELECT title, content FROM notes", null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String content = cursor.getString(1);

                // Group notes by title
                if (!groupedNotes.containsKey(title)) {
                    groupedNotes.put(title, new ArrayList<>());
                }
                groupedNotes.get(title).add(content);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return groupedNotes;
    }
    public boolean updateNote(String groupTitle, String oldNote, String newNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", newNote); // Assuming the column is named 'note_content'

        int rowsAffected = db.update("notes", values, "title = ? AND content = ?",
                new String[]{groupTitle, oldNote});
        return rowsAffected > 0;
    }
    // Update the folder title
    public boolean updateGroupTitle(String oldTitle, String newTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", newTitle); // Assuming the column is 'folder_title'

        int rowsAffected = db.update("notes", values, "title = ?", new String[]{oldTitle});
        return rowsAffected > 0;
    }

    public boolean deleteNote(String groupTitle, String noteContent) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete("notes", "title = ? AND content = ?",
                new String[]{groupTitle, noteContent});
        return rowsDeleted > 0;
    }
    public List<String> searchNotesByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> notes = new ArrayList<>();

        String query = "SELECT title FROM notes WHERE title LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + title + "%"});

        if (cursor.moveToFirst()) {
            do {
                notes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }
    public List<String> searchTasksByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> tasks = new ArrayList<>();

        String query = "SELECT title FROM tasks WHERE title LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + title + "%"});

        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasks;
    }
    // Method to set password
    public void setPassword(String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        db.insert("User", null, values); // Adjust table/column names as needed
        db.close();
    }

    // Method to verify and change password
    public boolean changePassword(String currentPassword, String newPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE password = ?", new String[]{currentPassword});
        if (cursor.getCount() > 0) {
            cursor.close();
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("password", newPassword);
            db.update("User", values, "password = ?", new String[]{currentPassword});
            db.close();
            return true;
        }
        cursor.close();
        return false;
    }

}
