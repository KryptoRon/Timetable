package com.kb5012.timetable;

import android.content.Context;
import android.support.annotation.MainThread;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Group_user;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.FragmentUserScreen.MyGroup;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronald on 14-12-2015.
 */
public class DBHelper {

    public DBHelper() {
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
    public User findUserByUsername(String username){
        ParseQuery<User> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username", username);
        try {
            return query.getFirst();
        } catch (ParseException e) {
            return null;
        }

    }

    public Group findGroupById(String groupId) {

        ParseQuery<Group> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("objectId", groupId);
        try {
            return query.getFirst();
        } catch (ParseException e) {
            return null;
        }

    }

    private static Task task;

    public static Task findTaskById(String taskId) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("objectId", taskId);
        query.getFirstInBackground(new GetCallback<Task>() {
            @Override
            public void done(Task object, ParseException e) {
                task = object;
            }
        });
        return task;
    }


    /*
     *  Find all tasks based on User ID
     *  @param user : ParseUser to be used.
     *  @param adapter : adapter to be updated when query is succes
     */
    public void findAllTaskByUserId(ParseUser user, final TaskAdapter adapter) {

        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", user);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null && parseTasks != null) {
                    adapter.clear();
                    for (Task task : parseTasks) {
                        if (!task.isStatus()) {
                            adapter.add(task);
                        }
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                //Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });
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

    public void findAllTaskByGroupId(Group group, TaskAdapter listAdapter) {
        final TaskAdapter mAdapter = listAdapter;
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("group", group);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {
                    if (parseTasks != null) {
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


    public void findAllTaskByGroupIdAndUserId(Group group, User user, TaskAdapter mAdapter) {
        final TaskAdapter myAdapter = mAdapter;
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", user);
        query.whereEqualTo("group", group);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {
                    if (parseTasks != null) {
                        myAdapter.clear();
                        for (int i = 0; i < parseTasks.size(); i++) {
                            myAdapter.add(parseTasks.get(i));
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
                    if (parseUsers != null) {
                        Adapter.clear();
                        User user;
                        for (int i = 0; i < parseUsers.size(); i++) {
                            user = findUserById(parseUsers.get(i).getUser_id());
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

    public void addMemberToGroup(Group group, User member) {
        ParseObject group_user = ParseObject.create("Group_user");
        Log.d("voegdtoe", group.getObjectId() + " " + member.getObjectId());
        group_user.put("group_id", group);
        group_user.put("user_id", member);
        group_user.saveEventually();
    }

    public void removeUserFromGroup(Group group, User user) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("group_id", group);
        query.whereEqualTo("user_id", user);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {


                    for (ParseObject delete : parseObjects) {
                        delete.deleteEventually();
                    }
                } else {
                    Log.e("ERROR", "message: " + e);
                }
            }
        });
    }
    public Boolean isMember(Group group,User user){
        List<ParseObject> objects=null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", user);
        query.whereEqualTo("group_id", group);
        try {
             objects=query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(objects.size()>0){
            return true;
        }
        return false;
    }

    public List<ParseObject> findAllGroupByUser(User user) {
        ArrayList<Group> groups=new ArrayList<>();
        ArrayList<ParseObject> parseObjects= new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", user);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
