package com.kb5012.timetable;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GroupCreateActivity extends AppCompatActivity {
    private ArrayList<User> users;
    private ArrayList<String> username;
    private byte[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        users = new ArrayList<User>();
        username = new ArrayList<String>();
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
        newGroup();
        finish();
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

    private void updateList(User u){
        users.add(u);
        username.add(u.getUsername());
        ListView lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, username);

        lv.setAdapter(arrayAdapter);
    }

    private void newGroup() {
        EditText et1 = (EditText) findViewById(R.id.tf_name);
        EditText et2 = (EditText) findViewById(R.id.tf_addUser);
        if(img != null && !et1.getText().toString().isEmpty() && !et2.getText().toString().isEmpty()) {
            Group g = new Group();
            g.setName(et1.getText().toString());
            g.setImage(img);
            g.saveInBackground();
            Toast.makeText(this.getBaseContext(), "Your group has been made!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getBaseContext(), "Fill all parameters!", Toast.LENGTH_SHORT).show();
        }
    }
}
