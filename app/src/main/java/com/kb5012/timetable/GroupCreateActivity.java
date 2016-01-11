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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Group_user;
import com.kb5012.timetable.DataModels.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GroupCreateActivity extends AppCompatActivity {
    private ArrayList<User> users;
    private ArrayList<String> ids;
    private byte[] img;
    private ListView lv;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        users = new ArrayList<User>();
        ids = new ArrayList<String>();

        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new MyListAdaper(this, R.layout.list_group_members_managment, ids));
        Bundle b = getIntent().getExtras();
        userID = b.getString("UserID");

        try{
            DBHelper db = new DBHelper();
            Group g = db.findGroupById(b.getString("GroupID"));
            EditText et = (EditText) findViewById(R.id.tf_name);
            et.setText(g.getName());
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(BitmapFactory.decodeByteArray(g.getImage(), 0, g.getImage().length));

            users = g.getGroupUsers();
            for (int i = 0; i < users.size(); i++) {
                ids.add(users.get(i).getUsername());
            }
            lv.setAdapter(new MyListAdaper(this, R.layout.list_group_members_managment, ids));
        } catch (NullPointerException j){

        }
    }

    public void onClickGallery(View v){
        Intent galleryInten = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryInten, 1);
    }
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
    public void onClick(View v){
        if(newGroup()){
            finish();
        }
    }

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
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_name);
                viewHolder.button = (ImageView) convertView.findViewById(R.id.list_item_btn);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView title = (TextView) findViewById(R.id.list_item_name);
                        DBHelper helper = new DBHelper();
                        User userList = helper.findUserByUsername(title.getText().toString());
                        downgradeList(userList);
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
        ImageView button;
    }

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
    private void downgradeList(User u){
        users.remove(u);
        ids.remove("iets om te testen");// TODO een username ophalen en plaatsen
        lv.setAdapter(new MyListAdaper(this, R.layout.list_group_members_managment, ids));
    }
    private void updateList(User u) {
        users.add(u);
        ids.add("iets om te testen"); // TODO een username ophalen en plaatsen
        lv.setAdapter(new MyListAdaper(this, R.layout.list_group_members_managment, ids));
    }

    private boolean newGroup() {
        EditText et1 = (EditText) findViewById(R.id.tf_name);
        EditText et2 = (EditText) findViewById(R.id.tf_addUser);
        if(img != null && !et1.getText().toString().isEmpty() && !et2.getText().toString().isEmpty()) {
            Group g = new Group();
            g.setName(et1.getText().toString());
            g.setImage(img);
            g.saveEventually();
            for (int i = 0; i < users.size(); i++) {
                Group_user gu = new Group_user();
                gu.setGroup_id(g.getId());
                gu.setUser_id(users.get(i).getId());
                gu.saveEventually();
            }
            Group_user gu = new Group_user();
            gu.setGroup_id(g.getId());
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
