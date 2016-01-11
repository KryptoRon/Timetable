package com.kb5012.timetable;

import android.support.annotation.MainThread;
import android.util.Log;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Group_user;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
    private ArrayList<Group> groups;
    public ArrayList<Group>findAllGroupByUserId(String userId){
        //TODO hier uit db halen alle groupen van user
        groups=new ArrayList<>();
        ParseQuery<Group> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", userId);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> objects, ParseException e) {
                if (e == null) {
                    for (Group group : objects) {
                        Group newGroup = findGroupById(group.getString("group_id"));
                        groups.add(newGroup);
                    }
                }
            }
        });
        return groups;
    }
    private Group group;
    public Group findGroupById(String groupId){

        ParseQuery<Group> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("objectId", groupId);
        query.getFirstInBackground(new GetCallback<Group>() {
            @Override
            public void done(Group object, ParseException e) {
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
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", userId);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {
                    for (Task task : parseTasks) {
                        tasks.add(task);
                        Log.e("SUCCESS", task.getObjectId() + " , " + task.getDescription());
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });

        return tasks;
    }

    public static User findUserByUsername(String username) {
        //TODO hier de user uithalen
        final User[] user = new User[1];
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", username);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user[0] = (User) object;
            }
        });
        return user[0];
    }

    public ArrayList<User> findAllUsersByGroup(String objectId) {
        final ArrayList<User> users = new ArrayList<>();
        ParseQuery<User> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("group_id", objectId);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    for (User user : objects) {
                        User newUser = findUserById(user.getString("user_id"));
                        users.add(newUser);
                    }
                }
            }
        });


        return users;
    }
    public void findAllTaskByGroupId(String groupid,TaskAdapter listAdapter) {
        final TaskAdapter mAdapter = listAdapter;
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("group_id", groupid);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {
                    if(parseTasks != null){
                        mAdapter.clear();
                        for (int i = 0; i < parseTasks.size(); i++) {
                            mAdapter.add(parseTasks.get(i));
                        }
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                //Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });
    }

    public void findAllTaskByGroupIdAndUserId(String groupId, String userId, TaskAdapter mAdapter) {
        final TaskAdapter Adapter = mAdapter;
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("group_id", groupId);
        query.whereEqualTo("receiver", userId);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {
                    if(parseTasks != null){
                        Adapter.clear();
                        for (int i = 0; i < parseTasks.size(); i++) {
                            Adapter.add(parseTasks.get(i));
                        }
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                //Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });

    }

    public void findAllUserByGroupId(String groupId, UserAdapter mAdapter) {
        final UserAdapter Adapter = mAdapter;
        ParseQuery<Group_user> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("group_id", groupId);
        query.findInBackground(new FindCallback<Group_user>() {
            public void done(List<Group_user> parseUsers, ParseException e) {
                if (e == null) {
                    if(parseUsers != null){
                        Adapter.clear();
                        User user;
                        for (int i = 0; i < parseUsers.size(); i++) {
                            user= findUserById(parseUsers.get(i).getUser_id());
                            Adapter.add(user);
                        }
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                //Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });

    }
}
