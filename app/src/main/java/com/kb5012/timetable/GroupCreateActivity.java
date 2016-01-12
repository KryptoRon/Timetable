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
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GroupCreateActivity extends AppCompatActivity {
    private ParseFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        Bundle b = getIntent().getExtras();
        try{
            DBHelper db = new DBHelper();
            Group g = db.findGroupById(b.getString("GroupID"));
            EditText et = (EditText) findViewById(R.id.tf_name);
            et.setText(g.getName());
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(BitmapFactory.decodeByteArray(g.getImage(), 0, g.getImage().length));
            
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

                    ImageView iv = (ImageView) findViewById(R.id.imageView);
                    iv.setImageBitmap(bitmap);

                    byte[] img = stream.toByteArray();
                    file = new ParseFile("Groupimage.png", img);
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

    private boolean newGroup() {
        EditText et1 = (EditText) findViewById(R.id.tf_name);
        if(file != null && !et1.getText().toString().isEmpty()) {
            ParseObject g = ParseObject.create("Group");
            g.put("group_name", et1.getText().toString());
            g.put("group_image",file);
            g.saveInBackground();
            Toast.makeText(this.getBaseContext(), "Your group has been made!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this.getBaseContext(), "Fill all parameters!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
