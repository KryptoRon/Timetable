package com.kb5012.timetable;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.User;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ronald on 12-1-2016.
 */
public class SignUpActivity extends AppCompatActivity {
    private ParseFile file;
    private EditText username;
    private String usernametxt;
    private EditText password;
    private String passwordtxt;
    private EditText email;
    private String emailtxt;
    private EditText phonenumber;
    private String phonetxt;
    private EditText passwordR;
    private ImageView avatar;
    private boolean newUser = true;
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.username_signup);
        password = (EditText) findViewById(R.id.password_signup);
        email = (EditText) findViewById(R.id.email_signup);
        phonenumber = (EditText) findViewById(R.id.phone_signup);
        passwordR = (EditText) findViewById(R.id.password_signupR);
        avatar = (ImageView) findViewById(R.id.signUpAvatar);

        try {
            ParseUser current_user = ParseUser.getCurrentUser();

            file = (ParseFile) current_user.get("avatar");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    ParseUser current_user = ParseUser.getCurrentUser();
                    username.setText(current_user.getString("username"));
                    password.setText(current_user.getString("password"));
                    passwordR.setText(current_user.getString("password"));
                    email.setText(current_user.getString("email"));
                    phonenumber.setText(current_user.getString("phonenumber"));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    avatar.setImageBitmap(bitmap);
                    newUser = false;
                }
            });
        }catch (NullPointerException e){
        }
    }

    public void onClickMakeUser(View v){
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(null == e){
                    signUp();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void signUp() {
        usernametxt = username.getText().toString();
        passwordtxt = password.getText().toString();
        String password1 = passwordtxt;
        String password2 = passwordR.getText().toString();
        emailtxt = email.getText().toString();
        phonetxt = phonenumber.getText().toString();
        // Force user to fill up the form
       if (usernametxt.equals("") || passwordtxt.equals("") || password2.equals("") || emailtxt.equals("") || phonetxt.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();
       }else if (!password1.equals(password2)){
            Toast.makeText(getApplicationContext(),
                    "Your passwords doesn't match",
                    Toast.LENGTH_LONG).show();
       }else {
           // Save new user data into Parse.com Data Storage
           if(newUser) {
               User user = new User();
               user.put("username",usernametxt);
               user.put("password",passwordtxt);
               user.put("email",emailtxt);
               user.put("phonenumber",phonetxt);
               user.put("avatar", file);
               user.signUpInBackground(new SignUpCallback() {
                   public void done(ParseException e) {
                       if (e == null) {
                           // Show a simple Toast message upon successful registration
                           Toast.makeText(getApplicationContext(),
                                   "Successfully Signed up, please log in.",
                                   Toast.LENGTH_LONG).show();
                           finish();
                       } else {
                           Toast.makeText(getApplicationContext(),
                                   "Sign up error", Toast.LENGTH_LONG)
                                   .show();
                           e.printStackTrace();
                       }
                   }
               });
           } else {
               ParseUser user = ParseUser.getCurrentUser();
               user.put("username",usernametxt);
               user.put("password",passwordtxt);
               user.put("email",emailtxt);
               user.put("phonenumber",phonetxt);
               user.put("avatar", file);
               user.saveInBackground(new SaveCallback() {
                   @Override
                   public void done(ParseException e) {
                       if (e == null) {
                           // Show a simple Toast message upon successful change
                           Toast.makeText(getApplicationContext(),
                                   "Successfully updated your account. Please log in.",
                                   Toast.LENGTH_LONG).show();
                           finish();
                       } else {
                           Toast.makeText(getApplicationContext(),
                                   "Sign up error", Toast.LENGTH_LONG)
                                   .show();
                           e.printStackTrace();
                       }

                   }
               });
           }
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

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);

                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    avatar.setImageBitmap(bitmap);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] img = stream.toByteArray();
                    file = new ParseFile("avatar.png", img);
                    cursor.close();
                }
                break;
            default:
                break;
        }
    }
}
