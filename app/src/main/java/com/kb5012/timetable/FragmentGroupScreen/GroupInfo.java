package com.kb5012.timetable.FragmentGroupScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskAdapter;
import com.kb5012.timetable.UserAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInfo extends ListFragment {
    private String groupId;
    private ArrayList<User> users;
    final private DBHelper dbHelper=new DBHelper();
    private ListView mListView;
    private UserAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_info, container, false);
        setMyGroups(v);

        return v;
    }


    private void setMyGroups(View v) {
        //Bundle
        Bundle bundle = getArguments();
        String groupId="";
        if (bundle != null) {
            groupId = bundle.getString("groupId");
        }
        TextView groupnumber=(TextView) v.findViewById(R.id.groupNumber);
        groupnumber.setText("Group id: " + groupId);
        mAdapter = new UserAdapter(getContext(), new ArrayList<User>());

        mListView = (ListView)v.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "klik", Toast.LENGTH_LONG).show();
            }
        });
        //TODO members uit de db halen
        //dbHelper.findAllUserByGroupId(groupId, mAdapter);


    }

}
