package com.kb5012.timetable.FragmentGroupScreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskAdapter;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class GroupMyTask extends ListFragment {
    private Group group;
    private User user;
    private ArrayList<Task> tasks;

    final private DBHelper dbHelper=new DBHelper();

    private ListView mListView;
    private TaskAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_my_task, container, false);
        Bundle bundle = getArguments();
        String groupId = bundle.getString("groupId");
        group= dbHelper.findGroupById(groupId);
        user= (User)ParseUser.getCurrentUser();


        mAdapter = new TaskAdapter(getContext(), new ArrayList<Task>());

        mListView = (ListView)v.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "klik", Toast.LENGTH_LONG).show();
            }
        });
        dbHelper.findAllTaskByGroupIdAndUserId(group,user, mAdapter);
        return v;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();


    }
}
