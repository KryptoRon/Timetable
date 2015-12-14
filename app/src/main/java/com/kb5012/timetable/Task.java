package com.kb5012.timetable;

/**
 * Created by Ronald on 14-12-2015.
 */
public class Task {
    private int ID;
    private String name;
    private String taskMaker;
    private String taskUser;

    public Task() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskMaker() {
        return taskMaker;
    }

    public void setTaskMaker(String taskMaker) {
        this.taskMaker = taskMaker;
    }

    public String getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(String taskUser) {
        this.taskUser = taskUser;
    }
}
