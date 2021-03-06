package com.kb5012.timetable.FragmentUserScreen;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskAdapter;
import com.kb5012.timetable.TaskDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTask extends ListFragment {

    private ArrayList<Task> tasks;
    final private DBHelper dbHelper = new DBHelper();
    private TaskAdapter mAdapter;
    private View view;
    //LinearLayout loading;
    private User user;
    private SwipeRefreshLayout refreshSwipeTask;
    public MyTask() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_task, container, false);
        user = (User) getCurrentUser();
        tasks = new ArrayList<>();

        mAdapter = new TaskAdapter(getContext(), new ArrayList<Task>());
        new AsyncGetMyTasks().execute();
        user.setAllTasks(tasks);
        refreshSwipeTask = (SwipeRefreshLayout) view.findViewById(R.id.swipe_my_task);
        refreshSwipeTask.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return view;
    }
    private void setSpinner(){
        Spinner spinner=(Spinner)view.findViewById(R.id.spinner);
        List<String> list= new ArrayList<>();
        list.add("TODO");
        list.add("Completed");
        list.add("Past Due");
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,list);
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
    @Override
    public void onResume() {
        super.onResume();
        tasks=dbHelper.findAllTaskByUserId(user);
        user.setAllTasks(tasks);
    }

    public void refresh() {
        mAdapter = new TaskAdapter(getContext(), new ArrayList<Task>());
        new AsyncGetMyTasks().execute();
        user.setAllTasks(tasks);
        refreshSwipeTask.setRefreshing(false);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Task item = (Task) getListAdapter().getItem(position);
        Intent intent = new Intent(getContext(), TaskDetails.class);
        intent.putExtra("task" , item.getObjectId());
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
            else if(filter=="TODO"&&!task.isStatus()&&task.getDeadline().compareTo(c.getTime())>0){
                tempTask.add(task);
            }
            // filter need to be on past due and the deadline has expired and haven`t been completed
            else if(filter=="Past Due"&&task.getDeadline().compareTo(c.getTime())<0&&!task.isStatus()){
                tempTask.add(task);
            }
        }

        mAdapter=new TaskAdapter(getContext(),tempTask);
        setListAdapter(mAdapter);
    }
    private class AsyncGetMyTasks extends AsyncTask<Void,Void,Void>{
        ArrayList<Task> tempTask= new ArrayList<>();
        @Override
        protected Void doInBackground(Void... params) {
            tempTask=dbHelper.findAllTaskByUserId(user);
            return null ;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tasks=tempTask;

            setSpinner();
        }
    }
}
