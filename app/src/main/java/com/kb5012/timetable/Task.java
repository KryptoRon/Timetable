package com.kb5012.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Task extends AppCompatActivity {
    private int ID;
    private String name;
    private String taskMaker;
    private String taskUser;

    public Task() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"Ray", "Chie-Cheung", "Ronald", "Jordi,", "Bryant"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }

    public void onclickT(View v) {
        saveTask();
    }

    public void saveTask() {
        String name = "" + ((EditText) findViewById(R.id.tf_name)).getText();
        String desc = "" + ((EditText) findViewById(R.id.tf_description)).getText();
        //String taskUser = "" + ((Spinner) findViewById(R.id.spinner)).getContext();
        String date = "" + ((EditText) findViewById(R.id.tf_date));
        String time = "" + ((EditText) findViewById(R.id.tf_time));

        System.out.println(name);
        System.out.println(desc);
        //System.out.println(taskUser);
        System.out.println(date);
        System.out.println(time);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskMaker() {
        return taskMaker;
    }

    public void setTaskMaker(String taskMaker) {
        this.taskMaker = taskMaker;
    }

    public String getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(String taskUser) {
        this.taskUser = taskUser;
    }
}
