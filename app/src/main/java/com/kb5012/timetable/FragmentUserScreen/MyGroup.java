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

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroup extends ListFragment implements OnItemClickListener{

    private DBHelper dbHelper=new DBHelper();
    private String userId;
    private ArrayList<Group> groups;

    public MyGroup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        Bundle bundle = getArguments();
        userId = bundle.getString("userId");
        groups = new ArrayList<>();

        MyListAdapter adapter = new MyListAdapter(getActivity());
        ListView myList = (ListView) view.findViewById(android.R.id.list);
        myList.setAdapter(adapter);

        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        userId = bundle.getString("userId");


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setList();
//        getListView().setOnItemClickListener(this);
//        getListView().setAdapter(getListAdapter());
//       ListView listView = (ListView) getActivity().findViewById(android.R.id.list);
//        getListView().setOnItemClickListener(new OnItemClickListener() {
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
//        super.onListItemClick(l, v, position, id);
//        (Group) getListAdapter().getItem(position);
        Log.d("onlistitemclick view", v + "");
        Log.d("onlistitem listview", l + "");
    }

    public class MyListAdapter extends ArrayAdapter<Group> {
        public MyListAdapter(Context myGroup) {
            super(myGroup, R.layout.list_item_group,groups);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (convertView == null){
                itemView=getActivity().getLayoutInflater().inflate(R.layout.list_item_group,parent,false);
            }
            Group group = groups.get(position);
            TextView groupName = (TextView) itemView.findViewById(R.id.groupName);
            groupName.setText(group.getName());
            return itemView;
        }
    }
}
