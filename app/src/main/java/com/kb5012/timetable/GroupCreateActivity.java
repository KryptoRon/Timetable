package com.kb5012.timetable;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Group_user;
import com.kb5012.timetable.DataModels.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GroupCreateActivity extends AppCompatActivity {
    private ArrayList<User> users;
    private ArrayList<String> username;
    private byte[] img;
    private ListView lv;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        users = new ArrayList<User>();
        username = new ArrayList<String>();

        lv = (ListView) findViewById(R.id.listView);

        Bundle b = getIntent().getExtras();
        userID = b.getString("UserID");
        try {
            Group group =(Group) b.get("Group");
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            Bitmap bmap = BitmapFactory.decodeByteArray(group.getImage(), 0, group.getImage().length);
            iv.setImageBitmap(bmap);
            EditText et = (EditText) findViewById(R.id.tf_name);
            et.setText(group.getName());
            users = group.getMembers();
            for (int i = 0; i < users.size(); i++) {
                username.add(users.get(i).getUsername());
            }
        } catch (NullPointerException e){

        }
    }

    //  This onClick starts the Gallery intent to select a image
    public void onClickGallery(View v){
        Intent galleryInten = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryInten, 1);
    }
    //  The onActivityResult takes the image from your gallery.
    //  Than it convert it to a byte[] to eventually store it in the db.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode==RESULT_OK){
                    Uri uri=data.getData();
                    String[] projection={MediaStore.Images.Media.DATA};

                    Cursor cursor=getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex=cursor.getColumnIndex(projection[0]);
                    String filePath=cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    img = stream.toByteArray();
                    ImageView iv = (ImageView) findViewById(R.id.imageView);
                    iv.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }

    // This onClick triggers when the "Save" button is clicked.
    // It checks if a new group is made with the users that belongs
    // in the groep. if the group is made and saved the whole intent
    // shut down and you are returned to the group overwiev.
    public void onClick(View v){
        if(newGroup()) {
            finish();
        }
    }

    // This onClick triggers when the user pushed the "Add" button.
    // It first watch if the named user exist before going on.
    public void onClickAdd(View v){
        DBHelper helper = new DBHelper();
        EditText etName = (EditText) findViewById(R.id.tf_addUser);
        String username = etName.getText().toString();
        try {
            User userList = helper.findUserByUsername(username);
            updateList(userList);
        } catch (NullPointerException j){
            Toast.makeText(this.getBaseContext(), "User doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }

    //  This class is made so the listView can be filled with
    //  users and a button to delete them from the group.
    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.imageButton);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView title = (TextView) findViewById(R.id.tv_name);
                        downdateList(title.getText().toString());
                    }
                });
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }
    public class ViewHolder {
        TextView title;
        ImageButton button;
    }

    // Adds a new user to the listView
    private void updateList(User u){
        users.add(u);
        username.add(u.getUsername());
        lv.setAdapter(new MyListAdaper(this, R.layout.list_group_manage_member, username));
    }
    // Removes a user from the listView
    private void downdateList(String user){
        users.remove(user);
        username.remove(user);
        lv.setAdapter(new MyListAdaper(this, R.layout.list_group_manage_member, username));
    }

    private boolean newGroup() {
        EditText et1 = (EditText) findViewById(R.id.tf_name);
        if(img != null && !et1.getText().toString().isEmpty()) {
            ParseObject  g = new ParseObject ("Group");
            g.put("name", et1.getText().toString());
            g.put("image", img);
            g.saveEventually();
            for (int i = 0; i < users.size(); i++) {
                Group_user gu = new Group_user();
                gu.setGroup_id(g.getObjectId());
                gu.setUser_id(users.get(i).getId());
                gu.saveEventually();
            }
            Group_user gu = new Group_user();
            gu.setGroup_id(g.getObjectId());
            gu.setUser_id(userID);
            gu.saveEventually();
            Toast.makeText(this.getBaseContext(), "Your group has been made!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this.getBaseContext(), "Fill all parameters!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
