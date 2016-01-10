package com.kb5012.timetable.DataModels;

import com.kb5012.timetable.DataModels.Task;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("_User")
public class User extends ParseUser{
    private String id;
    private String firstName;
    private String lastName;
    private ArrayList<Task> tasks;
    public String getUsername() {
        return getString("username");
    }
    public void setUsername(String username) {
        put("username", username);
    }

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

    public Task getTask(String taskID) {
        for (Task task : tasks) {
            if (task.getObjectId().equals(taskID)) {
                return task;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
