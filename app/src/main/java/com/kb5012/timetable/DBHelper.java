package com.kb5012.timetable;

import android.util.Log;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronald on 14-12-2015.
 */
public class DBHelper {

    //ToDo database connection maken..
    public DBHelper() {
    }

    public static User userInlog(String username, String password) {
        // demo
        if (username.equals("username") && password.equals("password")) {
            User user = new User();
            user.setFirstName("test");
            user.setLastName("testen");
            return user;
        }
        // TODO hier user ophalen met wachtwoord en username van db als er geen is word er niks teruggegeven.
        return null;
    }

    public static User findUserById(int id) {
        //TODO hier de user uithalen
        //demo test
        User user = new User();
        user.setFirstName("test");
        user.setLastName("testen");
        return user;
    }
    private ArrayList<ParseObject> groups;
    public ArrayList<ParseObject>findAllGroupByUserId(String userId){
        //TODO hier uit db halen alle groupen van user
        groups=new ArrayList<>();
        ParseQuery<Group> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", userId);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject group : objects) {
                        ParseObject newGroup = findGroupById(group.getString("group_id"));
                        groups.add(newGroup);
                    }
                }
            }
        });
        return groups;
    }
    private ParseObject group;
    public ParseObject findGroupById(String groupId){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("objectId", groupId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                group = object;
            }
        });
        return group;
    }
    /*
     *  Find all tasks based on User ID
     *  @param userId : String which is the PK from ParseObject
     *  @return : returns a list of all Tasks found by using "userId" as parameter
     */
    public ArrayList<Task> findAllTaskByUserId(String userId) {

        final ArrayList<Task> tasks = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseTasks, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject task : parseTasks) {
                        Task newTask = (Task) task;

                        tasks.add(newTask);
                        Log.e("SUCCESS", newTask.getObjectId() + " , " + newTask.getDescription());
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });

        return tasks;
    }



}
