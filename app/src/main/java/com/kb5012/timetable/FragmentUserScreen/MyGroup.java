package com.kb5012.timetable.FragmentUserScreen;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.GroupScreen;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskDetails;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroup extends ListFragment {

    private DBHelper dbHelper = new DBHelper();
    private User user;
    private MyListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        user = (User) ParseUser.getCurrentUser();
        mAdapter = new MyListAdapter(getContext(), new ArrayList<Group>());
        new grouping().execute();
        setListAdapter(mAdapter);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Group item = (Group) getListAdapter().getItem(position);
        Intent intent = new Intent(getContext(), GroupScreen.class);
        Bundle b = new Bundle();
        b.putString("groupId", item.getObjectId());
        intent.putExtras(b);
        startActivity(intent);
    }
    private class grouping extends AsyncTask<Void,Void,Void>{
        ArrayList<Group>groups= new ArrayList<>();
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<ParseObject> objects=(ArrayList<ParseObject>)dbHelper.findAllGroupByUser(user);
            Group group;
            if (objects!=null) {
                for (ParseObject object : objects) {
                    group = (Group) object.getParseObject("group_id");
                    try {
                        group.fetch();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    groups.add(group);
                }
                ;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            mAdapter.clear();
            for (Group group:groups) {
                mAdapter.add(group);
            }
        }
    }


    public class MyListAdapter extends ArrayAdapter<Group> {
        private List<Group> mGroup;

        public MyListAdapter(Context context, List<Group> objects) {
            super(context, R.layout.list_item_group, objects);
            this.mGroup = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                itemView = inflater.inflate(R.layout.list_item_group, parent, false);
            }
            Group group = mGroup.get(position);
            TextView taskName = (TextView) itemView.findViewById(R.id.groupName);
            taskName.setText(group.getName());
            return itemView;
        }
    }
}
