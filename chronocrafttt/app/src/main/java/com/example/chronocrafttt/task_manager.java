package com.example.chronocrafttt;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class task_manager extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText inputTodo;
    private Button addTodoButton;
    private RecyclerView recyclerTodo;
    private List<String> todoList;
    private TodoAdapter todoAdapter;
    private TextView dateTaskLabel;
    private String selectedDate = "";  // Format: "dd/MM/yyyy"
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        // Initialize the views
        calendarView = findViewById(R.id.calendarView);
        inputTodo = findViewById(R.id.inputTodo);
        addTodoButton = findViewById(R.id.btnAddTask);
        recyclerTodo = findViewById(R.id.recyclerTodo);
        dateTaskLabel = findViewById(R.id.dateTaskLabel);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize the task list
        todoList = new ArrayList<>();

        // Set up RecyclerView
        recyclerTodo.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(todoList);
        recyclerTodo.setAdapter(todoAdapter);

        // Handle Add Task button click
        addTodoButton.setOnClickListener(v -> {
            String task = inputTodo.getText().toString().trim();
            if (!task.isEmpty()) {
                if (selectedDate.isEmpty()) {
                    Toast.makeText(task_manager.this, getString(R.string.select_date_message), Toast.LENGTH_SHORT).show();
                } else {
                    // Insert task into SQLite
                    dbHelper.addTask(task, selectedDate);
                    fetchTasksForSelectedDate(selectedDate);  // Fetch tasks for the selected date
                    inputTodo.setText("");  // Clear the input field
                }
            } else {
                Toast.makeText(task_manager.this, getString(R.string.enter_task_message), Toast.LENGTH_SHORT).show();
            }
        });

        // CalendarView date selection listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
            dateTaskLabel.setText(getString(R.string.tasks_for_date, selectedDate));
            fetchTasksForSelectedDate(selectedDate);  // Fetch tasks for the selected date
        });
    }

    // Fetch tasks for the selected date
    private void fetchTasksForSelectedDate(String date) {
        todoList.clear();  // Clear previous tasks
        todoList.addAll(dbHelper.getTasksByDate(date));  // Fetch tasks from the database
        todoAdapter.notifyDataSetChanged();  // Notify the adapter to update the task list
    }
}
