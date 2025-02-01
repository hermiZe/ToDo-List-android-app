package com.example.chronocrafttt;

public class Task {
    private int id;
    private String task;
    private String date;
    private int status;  // 0 = not completed, 1 = completed

    public Task(int id, String task, String date, int status) {
        this.id = id;
        this.task = task;
        this.date = date;
        this.status = status;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    // Helper method to check if the task is completed
    public boolean isCompleted() {
        return status == 1;
    }

    // Method to set the task status (completed or not)
    public void setStatus(int status) {
        this.status = status;
    }
}

