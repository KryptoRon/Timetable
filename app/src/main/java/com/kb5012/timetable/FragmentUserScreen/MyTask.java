package com.kb5012.timetable.FragmentUserScreen;


import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskAdapter;
import com.kb5012.timetable.TaskDetails;

import java.util.ArrayList;

import static com.parse.ParseUser.getCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTask extends ListFragment {

    private ArrayList<Task> tasks;
    final private DBHelper dbHelper = new DBHelper();
    private TaskAdapter mAdapter;
    //LinearLayout loading;

    public MyTask() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_task, container, false);
        User user = (User) getCurrentUser();
        tasks = new ArrayList<>();

        mAdapter = new TaskAdapter(getContext(), new ArrayList<Task>());
        dbHelper.findAllTaskByUserId(user, mAdapter);

        user.setAllTasks(tasks);

        setListAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = (User) getCurrentUser();
        dbHelper.findAllTaskByUserId(user, mAdapter);
        user.setAllTasks(tasks);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task item = (Task) getListAdapter().getItem(position);
        Intent intent = new Intent(getContext(), TaskDetails.class);
        intent.putExtra("task" , item.getObjectId());
        startActivity(intent);
    }
}
