package com.kb5012.timetable.FragmentGroupScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.R;
import com.kb5012.timetable.UserAdapter;
import com.kb5012.timetable.UserScreen;
import com.kb5012.timetable.notification;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo extends ListFragment {
    private Group group;

    final private DBHelper dbHelper = new DBHelper();
    private UserAdapter mAdapter;
    private EditText input;
    private AlertDialog d;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_info, container, false);
        Bundle bundle = getArguments();
        String groupId = bundle.getString("groupId");
        group = dbHelper.findGroupById(groupId);
        mAdapter = new UserAdapter(getContext(), new ArrayList<User>());
        new AsyncFindMember().execute();
        setListAdapter(mAdapter);
        setDeleteButton(v);
        setAddMemberButton(v);
        setLeaveButton(v);
        try {
            mListView = (ListView) v.findViewById(android.R.id.list);
            TextView mTitle = (TextView) v.findViewById(R.id.groupName);
            mTitle.setText(group.getName());
            ImageView iv = (ImageView) v.findViewById(R.id.groupAvatar);
            ParseFile file = group.getImage();
            if (file != null) {
                byte[] data = file.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv.setImageBitmap(bitmap);
            }
            return v;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setDeleteButton(View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.removeGroup);
        if (group.getOwner().equals(ParseUser.getCurrentUser())) {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("deleting group")
                            .setMessage("Are you sure you want to delete this group?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new AsyncDeleteGroup().execute();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

        } else {
            button.setVisibility(View.GONE);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final User member = (User) getListAdapter().getItem(position);
        final User user = (User) ParseUser.getCurrentUser();
        if (group.getOwner().equals(user) && !member.equals(user)) {

            new AlertDialog.Builder(v.getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Kick Member")
                    .setMessage("Are you sure you want to kick " + member.getUsername() + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.removeUserFromGroup(group, member);
                            ArrayList<Task> tasks = dbHelper.findAllTaskByGroupIdAndUserId(group, member);
                            for (Task task : tasks) {
                                task.deleteEventually();
                            }
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            Toast.makeText(getContext(), member.getUsername() + " is kicked.", Toast.LENGTH_LONG);
        }
    }

    private void setAddMemberButton(View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.addMember);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Username");

                // Set up the input
                input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                d = builder.create();

                d.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something
                                new AsyncTaskAddMember().execute(input.getText().toString());
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

    private void useNotification(User receiver) {
        String text = "You have been added to " + group.getName();
        notification.singleNotification(receiver, text);
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
                                User user = (User) ParseUser.getCurrentUser();
                                dbHelper.removeUserFromGroup(group, user);
                                ArrayList<Task> tasks = dbHelper.findAllTaskByGroupIdAndUserId(group, user);
                                for (Task task : tasks) {
                                    task.deleteEventually();
                                }
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

    private class AsyncDeleteGroup extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // delete all tasks
            ArrayList<Task> tasks = dbHelper.findAllTaskByGroupId(group);
            for (Task task : tasks) {
                task.deleteEventually();
            }
            // erase connection from group to user
            List<ParseObject> parseObjects = dbHelper.findAllUsersByGroup(group);
            for (ParseObject object : parseObjects) {
                object.deleteEventually();
            }
            group.deleteEventually();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getContext(), "Group deleted", Toast.LENGTH_LONG);
            Intent intent = new Intent(getActivity(), UserScreen.class);
            Bundle b = new Bundle();
            b.putString("userId", ParseUser.getCurrentUser().getObjectId());
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    private class AsyncFindMember extends AsyncTask<Void, Void, Void> {
        ArrayList<User> users = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<ParseObject> objects = (ArrayList<ParseObject>) dbHelper.findAllUsersByGroup(group);
            User user;
            if (objects != null) {
                for (ParseObject object : objects) {
                    user = (User) object.getParseObject("user_id");
                    try {
                        user.fetch();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    users.add(user);
                }
                ;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            mAdapter.clear();
            for (User user : users) {
                mAdapter.add(user);
            }
        }
    }

    private class AsyncTaskAddMember extends AsyncTask<String, User, Void> {
        User user;

        @Override
        protected Void doInBackground(String... params) {
            user = dbHelper.findUserByUsername(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // check of iemand is gevonden
            if (user != null) {
                // check of de persoon die is gevonden al in de group zit
                if (!dbHelper.isMember(group, user)) {
                    dbHelper.addMemberToGroup(group, user);
                    Toast.makeText(getContext(), user.getUsername() + " added", Toast.LENGTH_LONG).show();
                    useNotification(user);
                    d.dismiss();
                } else {
                    Toast.makeText(getContext(), user.getUsername() + " is already a member", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getContext(), "Username does not exist", Toast.LENGTH_LONG).show();
            }
        }
    }
}
