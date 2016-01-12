package com.kb5012.timetable;

import android.content.Context;
import android.support.annotation.MainThread;
import android.text.Layout;
import android.util.Log;
import android.widget.Toast;
import android.view.View;
import android.widget.LinearLayout;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.FragmentUserScreen.MyGroup;
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
    public void findAllGroupByUser(User user,final MyGroup.MyListAdapter adapter) {
        final MyGroup.MyListAdapter mAdapter = adapter;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseGroup, ParseException e) {
                if (e == null) {
                    if (parseGroup != null) {
                        mAdapter.clear();
                        for (int i = 0; i < parseGroup.size(); i++) {
                            //findGroupById(parseGroup.get(i).get("group_id")+"",adapter);
                            //TODO get groups
                            Group group = (Group) parseGroup.get(i).getParseObject("group_id");
                            try {
                                group.fetch();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            mAdapter.add(group);
                        }
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                //Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });
    }
    public Group findGroupById(String groupId) {
        ParseQuery<Group> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("objectId", groupId);
        Group group= null ;
        try {
            group =query.getFirst();
            Log.d("group",group.getName());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return group ;
    }
    /*
     *  Find all tasks based on User ID
     *  @param userId : String which is the PK from ParseObject
     *  @return : returns a list of all Tasks found by using "userId" as parameter
     */
    public void findAllTaskByUserId(User user, TaskAdapter listAdapter) {
        final TaskAdapter mAdapter = listAdapter;
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", user);
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
        query.whereEqualTo("group",group);
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
    public void addMemberToGroup(Group group, User member){
        ParseObject group_user=ParseObject.create("Group_user");
        Log.d("voegdtoe", group.getObjectId()+" "+member.getObjectId());
        group_user.put("group_id", group);
        group_user.put("user_id", member);
        group_user.saveEventually();
    }
    private ArrayList<Group> groups;
    public ArrayList<Group> findAllGroupByUserId(String userID) {
        //TODO hier uit db halen alle groupen van user
        groups=new ArrayList<>();
        ParseQuery<Group> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", userID);
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
}
