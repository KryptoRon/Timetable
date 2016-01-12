package com.kb5012.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Ronald on 7-1-2016.
 */
public class TaskDetails extends AppCompatActivity{

    Task task;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Intent intent = getIntent();
        String item = intent.getStringExtra("task");
        user = (User) ParseUser.getCurrentUser();

        getTaskById(item);
    }

    private void getTaskById(String item) {

//        ArrayList<Task> tasks = user.getAllTasks();
//        for (Task task : tasks) {
//            if (task.getObjectId().equals(item)){
//
//            }
//        }
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("objectId", item);
        query.getFirstInBackground(new GetCallback<Task>() {
            @Override
            public void done(Task object, ParseException e) {
                task = object;

                // TODO test code. replace with real code when XML file is done
                TextView text9 = (TextView) findViewById(R.id.textView9);
                text9.setText(task.getTitle());
                TextView text10 = (TextView) findViewById(R.id.textView10);
                text10.setText(task.getDescription());
                TextView text11 = (TextView) findViewById(R.id.textView11);
                text11.setText("receiver: " + task.getReceiver().getObjectId());
                TextView text12 = (TextView) findViewById(R.id.textView12);
                text12.setText("sender: " + task.getSender().getObjectId());
                TextView text13 = (TextView) findViewById(R.id.textView13);
                text13.setText(task.getObjectId());
                TextView text14 = (TextView) findViewById(R.id.textView14);
                text14.setText("group id: " + task.getGroup_id());
            }
        });
    }

    public void onClickUpdate(View view) {
        updateTask(task.getObjectId());
    }

    public void onClickDelete(View view) {
        deleteTask(task.getObjectId());
    }

    private void updateTask(String taskId) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.getInBackground(taskId, new GetCallback<Task>() {
            @Override
            public void done(Task object, ParseException e) {
                object.put("status", true);
                object.saveInBackground();
            }
        });
    }

    private void deleteTask(String taskId) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.getInBackground(taskId, new GetCallback<Task>() {
            @Override
            public void done(Task object, ParseException e) {
                object.deleteInBackground();
            }
        });
    }
}
