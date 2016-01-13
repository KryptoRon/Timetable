package com.kb5012.timetable.FragmentGroupScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.R;
import com.kb5012.timetable.UserAdapter;
import com.kb5012.timetable.UserScreen;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInfo extends Fragment {
    private Group group;

    private ArrayList<User> users;
    final private DBHelper dbHelper = new DBHelper();
    private ListView mListView;
    private UserAdapter mAdapter;
    private EditText input;
    private AlertDialog d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_info, container, false);
        Bundle bundle = getArguments();
        String groupId = bundle.getString("groupId");
        group= dbHelper.findGroupById(groupId);
        setAddMemberButton(v);
        setLeaveButton(v);
        return v;
    }

    private void setAddMemberButton(View v) {
        Button button = (Button) v.findViewById(R.id.addMember);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");

                // Set up the input
                input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("geachreven", input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                d =builder.create();

                d.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something
                                   new AsyncTaskFindUser().execute(input.getText().toString());
                                //Dismiss once everything is OK.
                                //d.dismiss();
                            }
                        });
                    }
                });
                d.show();

            }
        });
    }


    private void setLeaveButton(View v) {
        Button button = (Button) v.findViewById(R.id.leaveGroup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Leaving group")
                        .setMessage("Are you sure you want to leave this group?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO db group verlaten
                                //TODO group object maken
                                Group group = new Group();
                                User user = (User) ParseUser.getCurrentUser();
                                dbHelper.removeUserFromGroup(group, user);

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
        });

    }
    private class AsyncTaskFindUser extends AsyncTask<String,User,Void>{
        User user;
        @Override
        protected Void doInBackground(String... params) {
           user= dbHelper.findUserByUsername(params[0]);
            Log.d("user", user.getObjectId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // check of iemand is gevonden
            if (user!=null){
                // check of de persoon die is gevonden al in de group zit
                if(!dbHelper.isMember(group,user)){
                    dbHelper.addMemberToGroup(group,user);
                    Toast.makeText(getContext(),user.getUsername()+" added",Toast.LENGTH_LONG).show();
                    d.dismiss();
                }
                else{
                    Toast.makeText(getContext(),user.getUsername()+" is already a member",Toast.LENGTH_LONG).show();
                }

            }
            else{
                Toast.makeText(getContext(),"Username does not exist",Toast.LENGTH_LONG).show();
            }
        }
    }

}
