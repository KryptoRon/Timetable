package com.kb5012.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kb5012.timetable.DataModels.*;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import com.kb5012.timetable.DataModels.User;

/**
 * Created by Ronald on 14-12-2015.
 */
public class LoginActivity extends AppCompatActivity {

    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.tf_username);
        password = (EditText) findViewById(R.id.tf_password);

        ParseObject.registerSubclass(Task.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Group_user.class);
        ParseObject.registerSubclass(Group.class);

        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        User currentUser =(User) ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), UserScreen.class);
            Bundle b = new Bundle();
            b.putString("UserID", currentUser.getObjectId());
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public void signIn(View v) {
        usernametxt = username.getText().toString();
        passwordtxt = password.getText().toString();

        ParseUser.logInInBackground(usernametxt, passwordtxt,
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // If user exist and authenticated, send user to Welcome.class
                            Intent intent = new Intent(getApplicationContext(), UserScreen.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Successfully Logged in",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No such user exist, please signup",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void startSignUp(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }



}
