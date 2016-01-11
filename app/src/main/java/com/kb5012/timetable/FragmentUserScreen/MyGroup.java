package com.kb5012.timetable.FragmentUserScreen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroup extends ListFragment implements OnItemClickListener{

    private DBHelper dbHelper=new DBHelper();
    private User user;
    private ArrayList<Group> groups;
    private ListView mListView;
    private MyListAdapter mAdapter;

    public MyGroup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        user= (User)ParseUser.getCurrentUser();
        mAdapter = new MyListAdapter(getContext(), new ArrayList<Group>());
        mListView = (ListView)view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "klik", Toast.LENGTH_LONG).show();
            }
        });
        dbHelper.findAllGroupByUserId(user, mAdapter);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user= (User) ParseUser.getCurrentUser();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onitemclick view", view + "");
        Log.d("onitemclick parent", parent + "");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        (Group) getListAdapter().getItem(position);
        Log.d("onlistitemclick view", v + "");
        Log.d("onlistitem listview", l + "");
    }

    public class MyListAdapter extends ArrayAdapter<Group> {
        private Context mContext;
        private List<Group> mGroup;

        public MyListAdapter(Context context, List<Group> objects) {
            super(context, R.layout.list_item_task, objects);
            this.mContext = context;
            this.mGroup = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (convertView==null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                itemView=inflater.inflate(R.layout.list_item_group, parent, false);
            }
            Group group =mGroup.get(position);
            TextView taskName=(TextView)itemView.findViewById(R.id.groupName);
            taskName.setText(group.getName());
            return itemView;
        }
}}
