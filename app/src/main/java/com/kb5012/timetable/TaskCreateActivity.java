package com.kb5012.timetable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Group_user;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskCreateActivity extends AppCompatActivity{

    private String userID;
    private final Calendar currentDate = Calendar.getInstance();

    private String year;
    private String month;
    private String day;
    private static final int TIME_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID = 1;
    private int yearI, dayI, monthI, hourI, minuteI;
    private TextView date, time;
    private String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        //Bundle bundle = getIntent().getExtras();
       // userID = bundle.getString("userId");


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
        date.setText(dayI +"-"+  monthI +"-"+ yearI);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        Spinner dropdownUser = (Spinner)findViewById(R.id.spinnerUser);
        String[] itemsU = new String[]{"Ray", "Chie-Cheung", "Ronald", "Jordi,", "Bryant"}; // TODO : Make dynamic
        ArrayAdapter<String> adapterU = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsU);
        dropdownUser.setAdapter(adapterU);

        findAllGroupByUserId(ParseUser.getCurrentUser(), this);


    }

    public void onClick(View v){
        findGroupById(groupID);
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

        //TODO: Make Dynamic
        User receiver = (User) ParseUser.getCurrentUser();

        t.setTitle("" + ((EditText) findViewById(R.id.tf_name)).getText());
        t.setDescription("" + ((EditText) findViewById(R.id.tf_description)).getText());


        t.put("title", "" + ((EditText) findViewById(R.id.tf_name)).getText());
        t.put("description", "" + ((EditText) findViewById(R.id.tf_description)).getText());
        t.put("receiver",receiver);
        //TODO : Make Dynamic
        t.put("group", group);
        t.put("status", false);
        t.put("sender", ParseUser.getCurrentUser());
        t.put("deadline", convertedDate);

        t.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                Log.d("CHAT OBJECT", " SAVED");
        }});

    }

    public void findAllGroupByUserId(ParseUser userId, final Context context){
        //TODO hier uit db halen alle groupen van user
        final ArrayList<ParseObject> groups=new ArrayList<>();
        ParseQuery<Group_user> query = ParseQuery.getQuery("Group_user");
        query.whereEqualTo("user_id", userId);
        query.findInBackground(new FindCallback<Group_user>() {
            @Override
            public void done(List<Group_user> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (Group_user group : objects) {
                        groups.add(group.getGroup_id());
                    }
                    String[] groupList = new String[groups.size()];
                    int i = 0;
                    for (ParseObject g : groups) {
                        groupList[i] = g.getObjectId();
                        i++;
                    }
                    populateSpinner(groupList, context, R.id.spinnerGroup);
                }
            }
        });
    }
    private Group group;
    public void findGroupById(String groupId){
        group = new Group();
        ParseQuery<Group> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("objectId", groupId);
        query.getFirstInBackground(new GetCallback<Group>() {
            @Override
            public void done(Group object, com.parse.ParseException e) {
                group = object;
                newTask();
            }
        });
    }

    private Spinner dropdown;
    public void populateSpinner(String[] list, Context context, int iD) {
        Spinner dropdown = (Spinner)findViewById(iD);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        dropdown.setAdapter(adapter);
        //System.out.println(groupID = dropdown.getSelectedItem().toString());
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

}
