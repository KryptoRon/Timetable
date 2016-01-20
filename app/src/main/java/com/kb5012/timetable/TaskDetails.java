package com.kb5012.timetable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;

/**
 * Created by Ronald on 7-1-2016.
 */
public class TaskDetails extends AppCompatActivity{

    TextView title;
    TextView create;
    TextView due;
    TextView description;
    Switch completed;
    ImageView avatar;
    TextView sender;

    Task task;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Intent intent = getIntent();
        String item = intent.getStringExtra("task");
        user = (User) ParseUser.getCurrentUser();

        title = (TextView) findViewById(R.id.task_titel);
        create = (TextView) findViewById(R.id.task_created_at);
        due = (TextView) findViewById(R.id.task_deadline);
        description = (TextView) findViewById(R.id.task_description_label);
        completed = (Switch) findViewById(R.id.task_switch_complete);
        avatar = (ImageView) findViewById(R.id.avatar);
        sender = (TextView) findViewById(R.id.task_sender);

        getTaskById(item);
    }

    private void getTaskById(String item) {

        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("objectId", item);
        query.getFirstInBackground(new GetCallback<Task>() {
            @Override
            public void done(Task object, ParseException e) {
                task = object;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH:mm");
                // TODO test code. replace with real code when XML file is done
                title.setText(task.getTitle());
                String createDate = sdf.format(task.getCreatedAt());
                create.setText(createDate);
                String dueDate = sdf.format(task.getDeadline());
                due.setText(dueDate);
                description.setText(task.getDescription());
                if (!task.isStatus()) {
                    completed.setChecked(false);
                } else {
                    completed.setChecked(true);
                }
                completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        updateTask(isChecked);

                    }
                });

                User user_sender = (User) task.getParseUser("sender");
                sender.setText(user_sender.getUsername());
                final ParseFile img = user_sender.getAvatar();
                if (img != null) {
                    img.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                avatar.setImageBitmap(bmp);
                            } else {
                            }
                        }
                    });


                }
            }
        });
    }

    public void onClickDelete(View view) {
        deleteTask(task.getObjectId());
        finish();
    }

    private void updateTask(final boolean status) {
        task.put("status", status);
        task.saveEventually();
    }

    private void deleteTask(String taskId) {
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.getInBackground(taskId, new GetCallback<Task>() {
            @Override
            public void done(Task object, ParseException e) {
                object.deleteEventually();
            }
        });
    }
}
