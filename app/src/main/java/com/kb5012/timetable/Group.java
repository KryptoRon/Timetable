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


    public Group(User beheerder) {
        this.beheerder = beheerder;
        members = new ArrayList<User>();
    }
}
