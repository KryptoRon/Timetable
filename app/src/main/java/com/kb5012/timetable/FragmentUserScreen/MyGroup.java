package com.kb5012.timetable.FragmentUserScreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.Group;
import com.kb5012.timetable.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroup extends ListFragment implements OnItemClickListener{


    public MyGroup() {
        // Required empty public constructor
    }

    private int userId;
    private ArrayList<Group> groups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_my_group, container, false);
        Bundle bundle = getArguments();
        userId = bundle.getInt("userId");
        setList();
        // Inflate the layout for this fragment
        return v;
    }

    private void setList() {
        groups = DBHelper.findAllGroupByUserId(userId);
        ArrayAdapter<Group> adapter = new MyListAdapter();
        setListAdapter(adapter);


    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(this);
        getListView().setAdapter(getListAdapter());
//       ListView listView = (ListView) getActivity().findViewById(android.R.id.list);
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("view", view + "");
//                Log.d("parent", parent + "");
//            }
//        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onitemclick view", view + "");
        Log.d("onitemclick parent", parent + "");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("onlistitemclick view", v + "");
        Log.d("onlistitem listview", l + "");
    }

    public class MyListAdapter extends ArrayAdapter<Group> {
        public MyListAdapter() {
            super(getActivity(), R.layout.list_view_group,groups);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (convertView==null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.list_view_group,parent,false);
            }
            Group group = groups.get(position);
            TextView groupName=(TextView)itemView.findViewById(R.id.groupName);
            groupName.setText(group.getName());
            return itemView;
        }
    }
}
