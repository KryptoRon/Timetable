package com.kb5012.timetable;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronald on 9-1-2016.
 */
public class SplashPage extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashpage);
        layout = (LinearLayout) findViewById(R.id.loadingLayout);
        layout.setVisibility(View.VISIBLE);


        fetchAllDataForUser(ParseUser.getCurrentUser());

    }

    private void fetchAllDataForUser(final ParseUser user) {

        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", user);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {
                    // Pin all tasks of "user" in the Local Datastore with label MyTasks
                    ParseObject.pinAllInBackground("MyTasks", parseTasks);
                    Log.d("Tasks", parseTasks.size() + " total tasks found.");

                    //Start gathering all groups that "user" is a part of.
                    ParseQuery<Group> groupParseQuery = ParseQuery.getQuery("Group_user");
                    groupParseQuery.whereEqualTo("user_id", user);
                    groupParseQuery.findInBackground(new FindCallback<Group>() {
                        @Override
                        // groupUserRelations = list of objects from Group_user join table
                        public void done(List<Group> groupUserRelations, ParseException e) {

                            //Pin all group relations of "User" in the Local Datastore with label MyGroupRelations
                            ParseObject.pinAllInBackground("MyGroupRelations", groupUserRelations);
                            Log.d("GROUP RELATIONS", groupUserRelations.size() + " total relations found.");

                            //Create list of Groups to use next.
                            ArrayList<Group> groups = new ArrayList<>();
                            for (ParseObject group: groupUserRelations) {
                                Log.d("GROUP FOUND", group.getParseObject("group_id").getObjectId());
                                groups.add((Group) group.getParseObject("group_id"));
                            }

                            // TODO: VERIFY CODE STARTING HERE NEXT STEP, GETIING ALL USERS FOR EACH GROUP
                            //Get all users from above queried groups
                            for (final ParseObject group: groupUserRelations){
                                ParseQuery<User> userParseQuery = ParseQuery.getQuery("Group_user");
                                userParseQuery.whereEqualTo("group_id", group);
                                userParseQuery.findInBackground(new FindCallback<User>() {
                                    @Override
                                    public void done(List<User> objects, ParseException e) {
                                        ParseObject.pinAllInBackground(group.getObjectId(), objects);
                                        Log.d("Group Users", group.getObjectId() + "has " + objects.size() + " members");
                                    }
                                });
                            }

                            // Start new Activity
                            Intent intent = new Intent(getApplicationContext(), UserScreen.class);


                            startActivity(intent);

                        }
                    });

                }
                else {
                    Log.e("ERROR", "message: " + e);
                }
                Log.e("SUCCESS", "we have " + parseTasks.size() + " results");
            }
        });
    }
}
