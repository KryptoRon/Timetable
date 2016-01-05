package com.kb5012.timetable.DataModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Ronald on 14-12-2015.
 */
@ParseClassName("Task")
public class Task extends ParseObject {

    public Task() {}

    public String getSender() {
        return getString("sender");
    }

    public void setSender(String sender) {
        put("sender", sender);
    }

    public String getReceiver() {
        return getString("receiver");
    }

    public void setReceiver(String receiver) {
        put("receiver", receiver);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public Date getDeadline() {
        return getDate("deadline");
    }

    public void setDeadline(Date deadline) {
        put("deadline" , deadline);
    }

    public boolean isStatus() {
        return getBoolean("status");
    }

    public void setStatus(boolean status) {
        put("status" , status);
    }

    public String getGroup_id() {
        return getString("group_id");
    }

    public void setGroup_id(String group_id) {
        put("group_id" , group_id);
    }
}
