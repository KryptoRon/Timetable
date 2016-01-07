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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTask extends ListFragment implements AdapterView.OnItemClickListener {

    private Thread thread;
    private Handler handler;
    private ProgressDialog progress;
    private String userId;
    private ArrayList<Task> tasks;
    final private DBHelper dbHelper=new DBHelper();

    public MyTask() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressBar);
        Bundle bundle = getArguments();
        userId = bundle.getString("userId");
//        thread = new Thread(new MyThread());
//        thread.start();
//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                tasks = (ArrayList) msg.obj;
//            }
//        };
        tasks = dbHelper.findAllTaskByUserId(userId);
        Log.e("Next Step:", "moving on to newListAdapter");
        ArrayAdapter<Task> adapter = new MyListAdapter();
        //ListView list = (ListView) view.findViewById(android.R.id.list);
        setListAdapter(adapter);

        return view;
    }

    private void setList() {
//        ProgressDialog prog= ProgressDialog.show(getActivity(), "Please Wait...", "Fetching Data...",true, false);//Assuming that you are using fragments.
//        prog.dismiss();


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
            taskName.setText(task.getTitle());
            return itemView;
        }
    }

    class MyThread implements Runnable {

        ArrayList<Task> tasks = new ArrayList<>();
        @Override
        public void run() {
            Message message = Message.obtain();
            tasks = dbHelper.findAllTaskByUserId(userId);
            message.obj = tasks;
            handler.sendMessage(message);

        }
    }
}
