package com.kb5012.timetable.FragmentUserScreen;


import android.app.ProgressDialog;
import android.content.Intent;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTask extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayList<Task> tasks;
    final private DBHelper dbHelper = new DBHelper();
    private TaskAdapter mAdapter;
    LinearLayout loading;

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
        ListView list = (ListView) view.findViewById(android.R.id.list);
        loading = (LinearLayout) view.findViewById(R.id.loadingLayout);

        setListAdapter(mAdapter);
        list.setOnItemClickListener(this);
        loading.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task item = (Task) getListAdapter().getItem(position);
        Intent intent = new Intent(getContext(), TaskDetails.class);
        intent.putExtra("task" , item.getObjectId());
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // abstract stub. not needed
    }
}
