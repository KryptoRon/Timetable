package com.kb5012.timetable;

import android.util.Log;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronald on 14-12-2015.
 */
public class DBHelper {
    // alleen gebruikt voor demo
    private ArrayList<Group> groups = new ArrayList<Group>();
    private ArrayList<User> users = new ArrayList<>();

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

    public static User findUserById(String id) {
        //TODO hier de user uithalen
        final User[] user = new User[1];
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("objectId", id);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user[0] = (User) object;
            }
        });
        return user[0];
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
            public void done(List<ParseObject> parseTasks, ParseException e) {
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
