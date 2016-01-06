package com.kb5012.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kb5012.timetable.DataModels.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskCreateActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"Ray", "Chie-Cheung", "Ronald", "Jordi,", "Bryant"}; // TODO : Make dynamic
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }

    public void onClick(View v){
        newTask();
        finish();
    }

    private void newTask() {
        Task t = new Task();

        String datetime = "" + ((EditText) findViewById(R.id.tf_date)).getText() + " "
                          + ((EditText) findViewById(R.id.tf_time)).getText();

        Date convertedDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy hh:mm");
        try{
            convertedDate = format.parse(datetime);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        Spinner s = (Spinner)findViewById(R.id.spinner);
        String receiver = s.getSelectedItem().toString();
        Date convert = new Date();

        t.setTitle("" + ((EditText) findViewById(R.id.tf_name)).getText());
        t.setDescription("" + ((EditText) findViewById(R.id.tf_description)).getText());
        t.setReceiver(receiver);

        Calendar c = Calendar.getInstance();

        t.setDeadline(c.getTime());

        t.saveInBackground();

    }

}
