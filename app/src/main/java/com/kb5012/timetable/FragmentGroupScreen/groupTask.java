package com.kb5012.timetable.FragmentGroupScreen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.FragmentUserScreen.MyGroup;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskAdapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class groupTask extends ListFragment {
    private String groupId;
    private ArrayList<Task> tasks;

    final private DBHelper dbHelper=new DBHelper();

    private ListView mListView;
    private TaskAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_task, container, false);
        Bundle bundle = getArguments();
        groupId = bundle.getString("groupId");

        ParseObject.registerSubclass(Task.class);
        // Inflate the layout for this fragment
        mAdapter = new TaskAdapter(getContext(), new ArrayList<Task>());

        mListView = (ListView)v.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "klik", Toast.LENGTH_LONG).show();
            }
        });
        dbHelper.findAllTaskByGroupId(groupId,mAdapter);
        return v;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        groupId = bundle.getString("groupId");


    }

}