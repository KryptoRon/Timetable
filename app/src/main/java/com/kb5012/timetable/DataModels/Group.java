package com.kb5012.timetable.DataModels;

import android.media.Image;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Ronald on 14-12-2015.
 */
public class Group extends ParseObject {
    private ArrayList<User> members;
    private ArrayList<Task> tasks;
    private User beheerder;
    private Image image;
    private String name;

    public Group() {
        
    }

    public String getName() {
        return getString("group_name");
    }

    public void setName(String group_name) {
        put("group_name", group_name);
    }

    public ArrayList<User> getGroupUsers() {
        ArrayList<User> users = DBHelper.findAllUsersByGroup(this.getObjectId());
        return users;
    }

    public void addGroupMember(User u){
        members.add(u);
    }

    public void setBeheerder(User u){
        beheerder = u;
    }

}
