package com.example.chronocrafttt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dashboard extends AppCompatActivity {

    private TextView tabMyTodoList, tabMyNotes;
    private Button btnAddTodoList, btnAddNotes;
    private TextView tvNoTasks;
    private ExpandableListView lvItems;

    private boolean showingTodoList = true;

    private DatabaseHelper dbHelper;
    private DBHelper DbHelper; // Assuming you already have DBHelper class
    private ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize UI components
        tabMyTodoList = findViewById(R.id.tabMyTodoList);
        tabMyNotes = findViewById(R.id.tabMyNotes);
        tvNoTasks = findViewById(R.id.tvNoTasks);
        lvItems = findViewById(R.id.lvItems);
        btnAddTodoList = findViewById(R.id.btnAddTodoList);
        btnAddNotes = findViewById(R.id.btnAddNotes);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);
        DbHelper = new DBHelper(this);
        // Set default content
        updateContent();

        // Set click listeners for tabs
        tabMyTodoList.setOnClickListener(v -> {
            showingTodoList = true;
            updateContent();  // Fetch and display tasks when "tabMyTodoList" is clicked
        });

        tabMyNotes.setOnClickListener(v -> {
            showingTodoList = false;
            updateContent();  // Display notes when "tabMyNotes" is clicked
        });

        // Add Todo List button
        btnAddTodoList.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, task_manager.class);
            startActivity(intent);
        });

        // Add Notes button
        btnAddNotes.setOnClickListener(v -> {
            Intent intent = new Intent(dashboard.this, notes.class); // Navigate to notes.java
            startActivity(intent);
        });

        // Initialize the options menu button (three dots)
        ImageView optionsMenu = findViewById(R.id.optionsMenu); // Ensure `optionsMenu` is the ID in your XML.

        // Set a click listener on the options menu
        optionsMenu.setOnClickListener(view -> {
            // Create and show the PopupMenu
            PopupMenu popupMenu = new PopupMenu(dashboard.this, view, 0, 0, R.style.CustomPopupMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_options, popupMenu.getMenu()); // Ensure menu_options.xml exists in res/menu.

            // Set menu item click listener
            popupMenu.setOnMenuItemClickListener(item -> handleMenuClick(item));
            popupMenu.show();
        });
    }

    private boolean handleMenuClick(MenuItem item) {
        String menuTitle = item.getTitle().toString();

        if (menuTitle.equals(getString(R.string.set))) {
            // Navigate to Set Password page
            navigateTo(Set_Password.class);
            return true;
        } else if (menuTitle.equals(getString(R.string.chng))) {
            // Navigate to Change Password page
            navigateTo(change_password.class);
            return true;
        } else if (menuTitle.equals(getString(R.string.clear))) {
            // Clear password and navigate to the dashboard
            clearPassword();
            Intent intent = new Intent(dashboard.this, dashboard.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return false;
        }
    }

    private void clearPassword() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("password"); // Remove the saved password
        editor.apply();
        Toast.makeText(this, getString(R.string.password_cleared), Toast.LENGTH_SHORT).show();
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(dashboard.this, targetActivity);
        startActivity(intent);
    }

    private void updateContent() {
        if (showingTodoList) {
            // Fetch all Todo tasks from DB
            List<String> tasks = DbHelper.getAllTasks();  // Get all tasks using the new method

            if (tasks.isEmpty()) {
                tvNoTasks.setVisibility(View.VISIBLE);
                lvItems.setVisibility(View.GONE);
                tvNoTasks.setText(getString(R.string.no_tasks_available)); // Dynamically load string resource
            } else {
                tvNoTasks.setVisibility(View.GONE);
                lvItems.setVisibility(View.VISIBLE);

                // Example: Passing tasks as a single group with each task as a child
                HashMap<String, List<String>> tasksMap = new HashMap<>();
                tasksMap.put(getString(R.string.all_tasks), tasks);  // "All Tasks" as the group title

                // Create an adapter and populate data
                ExpandableListAdapter adapter = new ExpandableListAdapter(this, new ArrayList<>(tasksMap.keySet()), tasksMap);
                lvItems.setAdapter(adapter);
            }
        } else {
            // Display Notes
            tvNoTasks.setVisibility(View.GONE);
            lvItems.setVisibility(View.VISIBLE);

            // Fetch notes grouped by title
            HashMap<String, ArrayList<String>> groupedNotes = dbHelper.getGroupedNotes();

            if (groupedNotes.isEmpty()) {
                tvNoTasks.setVisibility(View.VISIBLE);
                lvItems.setVisibility(View.GONE);
                tvNoTasks.setText(getString(R.string.no_notes_available)); // Dynamically load string resource
            } else {
                // Prepare data for ExpandableListView
                List<String> folderTitles = new ArrayList<>(groupedNotes.keySet());
                HashMap<String, List<String>> folderContents = new HashMap<>(groupedNotes);

                ExpandableListAdapter adapter = new ExpandableListAdapter(
                        this,            // Context
                        folderTitles,    // List of folder titles (groups)
                        folderContents   // HashMap linking folder titles to notes (children)
                );

                lvItems.setAdapter(adapter); // Set the custom adapter
            }
        }
    }
}

