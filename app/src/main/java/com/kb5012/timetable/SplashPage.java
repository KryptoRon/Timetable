package com.kb5012.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Ronald on 9-1-2016.
 */
public class SplashPage extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (LinearLayout) findViewById(R.id.loadingLayout);

        fetchAllDataForUser(ParseUser.getCurrentUser());
    }

    private void fetchAllDataForUser(ParseUser user) {
        // TODO get all tasks from user
        // TODO get all groups of which user is a member
        // TODO get all tasks from other group members
        // TODO pin all results to local datastore

        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", user);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {

                }
                else {
                    Log.e("ERROR", "message: " + e);
                }
                Log.e("SUCCESS", "we have " + parseTasks.size() + " results");
            }
        });
    }
}
