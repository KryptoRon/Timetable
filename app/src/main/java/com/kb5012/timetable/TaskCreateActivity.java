package com.kb5012.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskCreateActivity extends AppCompatActivity{

    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userId");
        Spinner dropdownUser = (Spinner)findViewById(R.id.spinnerUser);
        String[] itemsU = new String[]{"Ray", "Chie-Cheung", "Ronald", "Jordi,", "Bryant"}; // TODO : Make dynamic
        ArrayAdapter<String> adapterU = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsU);
        dropdownUser.setAdapter(adapterU);

        DBHelper helper = new DBHelper();

        ArrayList<ParseObject> groupList = helper.findAllGroupByUserId(userID);
        String[] groupNameList = new String[groupList.size()];
        int i = 0;
        for(ParseObject group : groupList) {
                Group gr = (Group) group;
                groupNameList[i] = gr.getName();
            System.out.println(groupNameList[i]);
            System.out.println("test");
                i++;
        }
        System.out.println("test");

        Spinner dropdownGroup = (Spinner)findViewById(R.id.spinnerGroup);
         // TODO : Make dynamic
        ArrayAdapter<String> adapterG = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groupNameList);
        dropdownGroup.setAdapter(adapterG);
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

        Spinner s = (Spinner)findViewById(R.id.spinnerUser);
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
