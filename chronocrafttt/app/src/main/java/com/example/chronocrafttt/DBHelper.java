package com.example.chronocrafttt;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_TODO = "todo";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the task table
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK + " TEXT, " +
                COLUMN_DATE + " TEXT);";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if it exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    // Add a task to the database
    public void addTask(String task, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task);
        values.put(COLUMN_DATE, date);

        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    // Get tasks for a specific date
    @SuppressLint("Range")
    public List<String> getTasksByDate(String date) {
        List<String> tasks = new ArrayList<>();

        // SQL query to fetch tasks for the specific date
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO, new String[]{COLUMN_TASK}, COLUMN_DATE + " = ?", new String[]{date}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String task = cursor.getString(cursor.getColumnIndex(COLUMN_TASK));
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return tasks;
    }
    public List<String> getAllTasks() {
        List<String> tasks = new ArrayList<>();

        // SQL query to fetch all tasks
        SQLiteDatabase db = this.getReadableDatabase();

        // Change the query as needed to order by date or any other column
        Cursor cursor = db.query(TABLE_TODO, new String[]{COLUMN_TASK}, null, null, null, null, COLUMN_DATE + " ASC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String task = cursor.getString(cursor.getColumnIndex(COLUMN_TASK));
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return tasks;
    }


}