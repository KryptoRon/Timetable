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

import com.kb5012.timetable.DataModels.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
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
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.username_signup);
        password = (EditText) findViewById(R.id.password_signup);
        email = (EditText) findViewById(R.id.email_signup);
        phonenumber = (EditText) findViewById(R.id.phone_signup);


    }

    public void signUp(View v) {
        usernametxt = username.getText().toString();
        passwordtxt = password.getText().toString();
        EditText passwordR = (EditText) findViewById(R.id.password_signupR);
        String password1 = passwordtxt;
        String password2 = passwordR.getText().toString();
        emailtxt = email.getText().toString();
        phonetxt = phonenumber.getText().toString();
        // Force user to fill up the form
       if (usernametxt.equals("") || passwordtxt.equals("") || emailtxt.equals("") || phonetxt.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();
        }else if (!password1.equals(password2)){
            Toast.makeText(getApplicationContext(),
                    "Your passwords doesn't match. KUT!" + password1 +" : "+ password2,
                    Toast.LENGTH_LONG).show();
        } else {
            // Save new user data into Parse.com Data Storage
            User user = new User();
            user.setUsername(usernametxt);
            user.setPassword(passwordtxt);
            user.setEmail(emailtxt);
            user.setPhoneNumber(phonetxt);
            user.setAvatar(file);
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
                                "Sign up Error: " + e, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
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
                    byte[] img = stream.toByteArray();
                    file = new ParseFile("avatar.png", img);

                    ImageView iv = (ImageView) findViewById(R.id.signUpAvatar);
                    iv.setImageBitmap(bitmap);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    cursor.close();
                }
                break;
            default:
                break;
        }
    }
}
