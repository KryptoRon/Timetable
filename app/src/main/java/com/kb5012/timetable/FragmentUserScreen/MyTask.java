package com.kb5012.timetable.FragmentUserScreen;


import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.R;
import com.kb5012.timetable.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTask extends ListFragment implements AdapterView.OnItemClickListener {


    public MyTask() {
        // Required empty public constructor
    }

    private int userId;
    private ArrayList<Task> tasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        userId = bundle.getInt("userId");
        setList();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_group, container, false);
    }

    private void setList() {
        tasks = DBHelper.findAllTaskByUserId(userId);
        ArrayAdapter<Task> adapter = new MyListAdapter();
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public class MyListAdapter extends ArrayAdapter<Task> {
        public MyListAdapter() {
            super(getActivity(), R.layout.list_item_task, tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (convertView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.list_item_task,parent,false);
            }
            Task task =tasks.get(position);
            TextView taskName=(TextView)itemView.findViewById(R.id.taskName);
            taskName.setText(task.getName());
            return itemView;
        }
    }
}
