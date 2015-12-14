package com.kb5012.timetable;

import java.util.ArrayList;

/**
 * Created by Ronald on 14-12-2015.
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private ArrayList<Task> tasks;

    public User() {
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public void setAllTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task getTask(int taskID) {
        for (Task task : tasks) {
            if (task.getID() == taskID) {
                return task;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
