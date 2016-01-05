package com.kb5012.timetable.DataModels;

import android.media.Image;

import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;

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


    public Group(User beheerder) {
        this.beheerder = beheerder;
        members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
