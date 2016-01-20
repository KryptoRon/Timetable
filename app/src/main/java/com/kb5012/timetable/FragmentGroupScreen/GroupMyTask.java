package com.kb5012.timetable.FragmentGroupScreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.List;

public class GroupMyTask extends ListFragment {
    private Group group;
    private User user;
    private ArrayList<Task> tasks;

    final private DBHelper dbHelper=new DBHelper();

    private ListView mListView;
    private TaskAdapter mAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_my_task, container, false);
        Bundle bundle = getArguments();
        String groupId = bundle.getString("groupId");
        group= dbHelper.findGroupById(groupId);
        user= (User)ParseUser.getCurrentUser();
        new AsyncGetMyTasks().execute(groupId);
        return view;

    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task item = (Task) getListAdapter().getItem(position);
        Intent intent = new Intent(getContext(), TaskDetails.class);
        intent.putExtra("task", item.getObjectId());
        startActivity(intent);
    }
    private void filterTask(String filter){
        ArrayList<Task> tempTask=new ArrayList<>();
        Calendar c = Calendar.getInstance();
        for (Task task :tasks  ) {
            // look if the filter is completer or to.do with the same task
            if (filter == "Completed" &&task.isStatus()){
                tempTask.add(task);
            }
            //task that has need to be done but isn`t pas the deadline yet
            if(filter=="TODO"&&!task.isStatus()&&task.getDeadline().compareTo(c.getTime())>0){
                tempTask.add(task);
                break;
            }
            // filter need to be on past due and the deadline has expired and haven`t been completed
            if(filter=="Past Due"&&task.getDeadline().compareTo(c.getTime())<0&&!task.isStatus()){
                tempTask.add(task);
            }
        }

        mAdapter=new TaskAdapter(getContext(),tempTask);
        setListAdapter(mAdapter);
    }
    private void setSpinner(){
        Spinner spinner=(Spinner)view.findViewById(R.id.spinner);
        List<String> list= new ArrayList<>();
        list.add("TODO");
        list.add("Completed");
        list.add("Past Due");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getItemAtPosition(position).toString();
                filterTask(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private class AsyncGetMyTasks extends AsyncTask<String,Void,Void> {
        ArrayList<Task> tempTask= new ArrayList<>();
        @Override
        protected Void doInBackground(String... groupId) {
            Group group=dbHelper.findGroupById(groupId[0]);
            tempTask = dbHelper.findAllTaskByGroupIdAndUserId(group, user);
            return null ;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tasks=tempTask;

            setSpinner();
        }
    }
}
