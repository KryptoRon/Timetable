package com.kb5012.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ronald on 14-12-2015.
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onclick(View v) {
        String username = "" + ((EditText) findViewById(R.id.tf_username)).getText();
        String password = "" + ((EditText) findViewById(R.id.tf_password)).getText();

        User user = DBHelper.userInlog(username, password);

        if (user == null) {
            Toast.makeText(getApplicationContext(), "Login failled, try again", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), UserScreen.class);
            Bundle b = new Bundle();
            b.putInt("userID", user.getId());
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }


}
