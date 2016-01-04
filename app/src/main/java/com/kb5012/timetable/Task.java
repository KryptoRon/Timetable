package com.kb5012.timetable;

import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Ronald on 14-12-2015.
 */
public class Task {
    private int ID;
    private String id;
    private String name;
    private String sender;
    private String receiver;
    private String title;
    private String description;
    private Date deadline;
    private boolean status;
    private String group_id;

    public Task() {

    }

    public Task(ParseObject task) {
        this.setId(task.getString("objectId"));
        this.title = task.getString("title");
        this.description = task.getString("description");
        this.deadline = task.getDate("deadline");
        this.status = task.getBoolean("status");
        this.sender = task.getString("sender");
        this.receiver = task.getString("receiver");
        this.group_id = task.getString("group_id");
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
