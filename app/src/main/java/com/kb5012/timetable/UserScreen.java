package com.kb5012.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ronald on 14-12-2015.
 */
public class UserScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        Bundle b = getIntent().getExtras();
        int userID = b.getInt("userID");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout);
        ArrayList<Task> tasks = DBHelper.findAllTaskByUserId(userID);
        User user = DBHelper.findUserById(userID);
        ((TextView) findViewById(R.id.tv_user)).setText("welkom " + user.getFirstName() + " " + user.getLastName());
        Button button;
        for (Task task : tasks) {
            button = new Button(getApplicationContext());
            button.setText((Html.fromHtml("taak: " + task.getID() + "<br/>" + "gemaakt door: " + task.getTaskMaker())));
            button.setTag(task.getID());
            linearLayout.addView(button);
        }
    }
}
