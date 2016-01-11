package com.kb5012.timetable;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskCreateActivity extends AppCompatActivity{

    private User user;
    private final Calendar currentDate = Calendar.getInstance();

    private String year;
    private String month;
    private String day;
    private static final int TIME_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID = 1;
    private int yearI, dayI, monthI, hourI, minuteI;
    private TextView date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        user=(User)ParseUser.getCurrentUser();


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

        DBHelper helper = new DBHelper();

        String[] groupNameList = new String[]{"test", "test2", "test3"};// TODO : Make dynamic

        Spinner dropdownGroup = (Spinner)findViewById(R.id.spinnerGroup);
         // TODO : Make dynamic
        ArrayAdapter<String> adapterG = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groupNameList);
        dropdownGroup.setAdapter(adapterG);
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

        //TODO : Make Dynamic
        User receiverT = new User();
        receiverT.setObjectId("4lCSDPPBSX");

        t.put("title", "" + ((EditText) findViewById(R.id.tf_name)).getText());
        t.put("description", "" + ((EditText) findViewById(R.id.tf_description)).getText());
        t.put("receiver", receiverT);
        //TODO : Make Dynamic
        t.put("group_id", "qSAL3jKMhY");
        t.put("status", false);
        t.put("sender", ParseUser.getCurrentUser());
        t.put("deadline", convertedDate);

        t.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                Log.d("CHAT OBJECT", " SAVED");
        }});

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
