package com.kb5012.timetable.DataModels;

import com.kb5012.timetable.DataModels.Task;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Ronald on 14-12-2015.
 */
@ParseClassName("_User")
public class User extends ParseUser{
    ArrayList<Task> tasks;

    public ParseFile getAvatar() {
        return getParseFile("avatar");
    }

    public void setAvatar(ParseFile avatar) {
        put("avatar", avatar);
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

    public void setPhoneNumber(String phoneNumber) {
        put("phonenumber", phoneNumber);
    }

    public String getPhoneNumber() {
        return getString("phonenumber");
    }


}
