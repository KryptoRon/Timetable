package com.kb5012.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kb5012.timetable.DataModels.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Ronald on 12-1-2016.
 */
public class SignUpActivity extends AppCompatActivity {

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
        emailtxt = email.getText().toString();
        phonetxt = phonenumber.getText().toString();
        // Force user to fill up the form
        if (usernametxt.equals("") || passwordtxt.equals("") || emailtxt.equals("") || phonetxt.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete the sign up form",
                    Toast.LENGTH_LONG).show();

        } else {
            // Save new user data into Parse.com Data Storage
            User user = new User();
            user.setUsername(usernametxt);
            user.setPassword(passwordtxt);
            user.setEmail(emailtxt);
            user.setPhoneNumber(phonetxt);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Show a simple Toast message upon successful registration
                        Toast.makeText(getApplicationContext(),
                                "Successfully Signed up, please log in.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error: " + e, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
    }
}
