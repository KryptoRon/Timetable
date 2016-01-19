package com.kb5012.timetable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskCreateActivity extends AppCompatActivity{

    private String userID;
    private final Calendar currentDate = Calendar.getInstance();

    private String year;
    private String month;
    private User receiver;
    private String day;
    private static final int TIME_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID = 1;
    private int yearI, dayI, monthI, hourI, minuteI;
    private TextView date, time;
    private UserAdapter adapterU;
    private String groupID;
    private Group group;
    private DBHelper dbHelper= new DBHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        yearI = currentDate.get(Calendar.YEAR);
        monthI = currentDate.get(Calendar.MONTH) + 1;
        dayI = currentDate.get(Calendar.DAY_OF_MONTH);
        hourI = currentDate.get(Calendar.HOUR_OF_DAY);
        minuteI = currentDate.get(Calendar.MINUTE);

        time = (TextView) findViewById(R.id.time_view);
        time.setText(hourI + ":" + minuteI);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        date = (TextView) findViewById(R.id.date_view);
        date.setText(dayI + "-" + monthI + "-" + yearI);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        Spinner dropdownUser = (Spinner)findViewById(R.id.spinnerUser);

        Spinner dropdownGroup = (Spinner)findViewById(R.id.spinnerGroup);
        adapterU=new UserAdapter(this,new ArrayList<User>());
        dropdownUser.setAdapter(adapterU);
        dropdownUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receiver = dbHelper.findUserByUsername(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dropdownGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group = dbHelper.findgroupByGroupName(parent.getSelectedItem().toString());
                new AsyncFindMember().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new AsyncGroup().execute();


    }


    public void onClick(View v){
        newTask();
        finish();
    }

    //Saving a new task in the database
    private void newTask() {
        Task t = new Task();

        String datetime = yearI + "-" + monthI + "-" + dayI + "T" + hourI + ":" + minuteI + ":00.000Z";

        Date convertedDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        try{
            convertedDate = format.parse(datetime);
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        t.setTitle("" + ((EditText) findViewById(R.id.tf_name)).getText());
        t.setDescription("" + ((EditText) findViewById(R.id.tf_description)).getText());


        t.put("title", "" + ((EditText) findViewById(R.id.tf_name)).getText());
        t.put("description", "" + ((EditText) findViewById(R.id.tf_description)).getText());
        t.put("receiver",receiver);
        t.put("group", group);
        t.put("status", false);
        t.put("sender", ParseUser.getCurrentUser());
        t.put("deadline", convertedDate);
        t.saveEventually();
        useNotification();

    }
    private void useNotification(){
        String text= ParseUser.getCurrentUser().getUsername()+" has given you a task in "+group.getName()+" group";
        notification.singleNotification(receiver, text);
    }




    public void populateSpinner(String[] list, Context context, int iD) {
        Spinner dropdown = (Spinner)findViewById(iD);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        dropdown.setAdapter(adapter);
    }


    //Making a Dialog for the date & time pickers
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(
                        this, mTimeSetListener, hourI, minuteI, true);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, yearI, monthI, dayI);
        }
        return null;

    }

    //Making a listener for the timepicker
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener()
            {
                public void onTimeSet(
                        TimePicker view, int hourOfDay, int minuteOfHour)
                {
                    hourI = hourOfDay;
                    minuteI = minuteOfHour;

                    time.setText(hourI + ":" + minuteI);

                }
            };

    //Making a listener for the datepicker
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yearI = year;
            monthI = monthOfYear + 1;
            dayI = dayOfMonth;

            date.setText(dayI + "-" + monthI + "-"+ yearI);
        }

    };
    private class AsyncFindMember extends AsyncTask<Void, Void, Void> {
        ArrayList<User> users = new ArrayList<>();
        String[] memberNames;
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<ParseObject> objects = (ArrayList<ParseObject>) dbHelper.findAllUsersByGroup(group);

            User user;
            if (objects != null) {
                memberNames= new String[objects.size()];
                int i=0;
                for (ParseObject object : objects) {
                    user = (User) object.getParseObject("user_id");
                    try {
                        user.fetch();
                        memberNames[i]=user.getUsername();
                        i++;
                    } catch (com.parse.ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            populateSpinner(memberNames, TaskCreateActivity.this, R.id.spinnerUser);

        }
    }
    private class AsyncGroup extends AsyncTask<Void,Void,Void>{
        ArrayList<Group> groups = new ArrayList<>();
        String[] groupNames;
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<ParseObject> objects = (ArrayList<ParseObject>) dbHelper.findAllGroupByUser((User)ParseUser.getCurrentUser());

            Group tempGroup;
            if (objects != null) {
                groupNames= new String[objects.size()];
                int i=0;
                for (ParseObject object : objects) {
                    tempGroup = (Group) object.getParseObject("group_id");
                    try {
                        tempGroup.fetch();
                        groupNames[i]=tempGroup.getName();
                        i++;
                    } catch (com.parse.ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            populateSpinner(groupNames, TaskCreateActivity.this, R.id.spinnerGroup);
        }
    }

}
