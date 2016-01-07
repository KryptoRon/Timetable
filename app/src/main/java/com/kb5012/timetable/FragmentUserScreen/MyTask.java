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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTask extends ListFragment implements AdapterView.OnItemClickListener {

    private String userId;
    private ArrayList<Task> tasks;
    final private DBHelper dbHelper = new DBHelper();
    private ArrayAdapter<Task> adapter;
    private ProgressDialog pDialog;

    public MyTask() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        pDialog = ProgressDialog.show(getActivity(), "Loading...", "Fetching Data...", true, false);
        Bundle bundle = getArguments();
        userId = bundle.getString("userId");
        tasks = new ArrayList<>();

        findAllTaskByUserId(userId);
        ListView list = (ListView) view.findViewById(android.R.id.list);

        adapter = new MyListAdapter();
        setListAdapter(adapter);
        list.setOnItemClickListener(this);



        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("ONITEMCLICK: ", "Clicked " + position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // abstract stub. not needed
    }

    public class MyListAdapter extends ArrayAdapter<Task> {
        public MyListAdapter() {
            super(getActivity(), R.layout.list_item_task, tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (convertView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.list_item_task, parent, false);
            }
            Task task = tasks.get(position);
            TextView taskName = (TextView) itemView.findViewById(R.id.group_name);
            taskName.setText(task.getTitle());
            return itemView;
        }
    }

    public void findAllTaskByUserId(String userId) {

        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("receiver", userId);
        query.findInBackground(new FindCallback<Task>() {
            public void done(List<Task> parseTasks, ParseException e) {
                if (e == null) {
                    for (Task task : parseTasks) {
                        tasks.add(task);
                        setListAdapter(adapter);
                        pDialog.dismiss();
                        Log.e("SUCCESS", task.getObjectId() + " , " + task.getDescription());
                    }

                } else {
                    Log.e("ERROR", "message: " + e);
                }
                Log.e("SUCCESS", "we have " + tasks.size() + " results");
            }
        });
    }
}
