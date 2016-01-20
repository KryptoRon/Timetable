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

    public User findUserByUsername(String username) {
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
    public ArrayList<Task> findAllTaskByUserId(User user) {

        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", user);
        query.orderByAscending("deadline");
        try {
            return (ArrayList<Task>)query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ParseObject> findAllUsersByGroup(Group group) {
        List<ParseObject> objects = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("group_id", group);
        try {
            objects = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public List<ParseObject> findAllGroupByUser(User user) {
        List<ParseObject> object = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", user);
        try {
            object = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return object;
    }

    public ArrayList<Task> findAllTaskByGroupId(Group group) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("group", group);
        try {
            return (ArrayList<Task>)query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteGroup(Group group) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("group_id", group);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : parseObjects) {
                        object.deleteEventually();
                    }
                }
            }
        });
        group.deleteEventually();
    }


    public ArrayList<Task> findAllTaskByGroupIdAndUserId(Group group, User user) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", user);
        query.whereEqualTo("group", group);
        try {
            return (ArrayList<Task>)query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    return null;
    }

    public void addMemberToGroup(Group group, User member) {
        ParseObject group_user = ParseObject.create("Group_user");
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
                    Log.e("ERROR", "message removeUserFromGroup: " + e);
                }
            }
        });
    }

    public Boolean isMember(Group group, User user) {
        List<ParseObject> objects = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", user);
        query.whereEqualTo("group_id", group);
        try {
            objects = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (objects.size() > 0) {
            return true;
        }
        return false;
    }


    public Group findgroupByGroupName(String groupName) {
        ParseQuery<Group> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("group_name", groupName);
        try {
            return query.getFirst();
        } catch (ParseException e) {
            return null;
        }
    }
}
