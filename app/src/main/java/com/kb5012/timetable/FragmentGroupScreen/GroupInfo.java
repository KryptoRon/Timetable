package com.kb5012.timetable.FragmentGroupScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
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
import com.kb5012.timetable.GroupScreen;
import com.kb5012.timetable.R;
import com.kb5012.timetable.TaskAdapter;
import com.kb5012.timetable.UserAdapter;
import com.kb5012.timetable.UserScreen;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
        TextView groupnumber=(TextView) v.findViewById(R.id.groupName);
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
    public void leaveGroup(View view) {
        //TODO verlaat group
        new AlertDialog.Builder(view.getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Leaving group")
                .setMessage("Are you sure you want to leave this group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO db group verlaten
                        //TODO group object maken
                        Group group= new Group();
                        dbHelper.leaveGroup( group);
                        User user=(User) ParseUser.getCurrentUser();
                        Intent intent = new Intent(getActivity(), UserScreen.class);
                        Bundle b = new Bundle();
                        b.putString("userId", user.getObjectId());
                        intent.putExtras(b);
                        startActivity(intent);
                        //finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

}
