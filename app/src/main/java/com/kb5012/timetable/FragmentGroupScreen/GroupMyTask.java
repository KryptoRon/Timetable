package com.kb5012.timetable.FragmentGroupScreen;

import android.content.Intent;
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
import com.kb5012.timetable.TaskDetails;
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
        setListAdapter(mAdapter);
        dbHelper.findAllTaskByGroupIdAndUserId(group,user, mAdapter);
        return v;

    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task item = (Task) getListAdapter().getItem(position);
        Intent intent = new Intent(getContext(), TaskDetails.class);
        intent.putExtra("task", item.getObjectId());
        startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();


    }
}
