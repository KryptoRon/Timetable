package com.kb5012.timetable;

import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronald on 14-12-2015.
 */
public class DBHelper {
    // alleen gebruikt voor demo
    private ArrayList<Group> groups = new ArrayList<>();
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

    public static User findUserById(int id) {
        //TODO hier de user uithalen
        //demo test
        User user = new User();
        user.setFirstName("test");
        user.setLastName("testen");
        return user;
    }

    public static ArrayList<Task> findAllTaskByUserId(int userId) {
        //TODO hier uit db halen alle taken van user.
        final ArrayList<Task> tasks = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseTasks, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject task : parseTasks){
                        Task newTask = new Task(task);

                        tasks.add(newTask);
                    }
                    Log.e("SUCCESS", "we have results");
                } else {
                    Log.e("ERROR", "message: " + e);
                }
            }
        });
        return tasks;
    }

    public static ArrayList<Group>findAllGroupByUserId(int userId){
        //TODO hier uit db halen alle groupen van user
        ArrayList<Group> groups=new ArrayList<>();
        Group group;
        User user=new User();
                user.setId(1);
        for (int i = 0; i <10 ; i++) {
            group=new Group(user);
            group.setName("group "+i);
            groups.add(group);
        }
        return groups;
    }

    public boolean createTask (Task task) {
        ParseObject newTask = new ParseObject("Task");
        newTask.put("title", task.getTitle());
        newTask.put("description", task.getDescription());
        newTask.put("deadline", task.getDeadline());
        newTask.put("status", task.isStatus());
        newTask.put("sender", task.getSender());
        newTask.put("receiver", task.getReceiver());
        newTask.put("group_id", task.getGroup_id());

        newTask.saveInBackground();

        return true;
    }


}
