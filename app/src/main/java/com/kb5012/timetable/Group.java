package com.kb5012.timetable;

import android.media.Image;

import java.util.ArrayList;

/**
 * Created by Ronald on 14-12-2015.
 */
public class Group {
    private ArrayList<User> members;
    private ArrayList<Task> tasks;
    private User beheerder;
    private Image image;
    private String name;
    private int id;

    public Group(User beheerder) {
        this.beheerder = beheerder;
        members = new ArrayList<User>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public User getBeheerder() {
        return beheerder;
    }

    public void setBeheerder(User beheerder) {
        this.beheerder = beheerder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    public void addTask(Task task){
        // TODO toevoegen aan database
        tasks.add(task);
    }
    public void deleteTask(int taskId){
        // TODO delete van database
        for (Task task:tasks) {
            if (task.getID()==taskId){
                tasks.remove(task);
            }
        }
    }
}
