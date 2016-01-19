package com.kb5012.timetable;

import android.util.Log;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.User;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

/**
 * Created by cc on 19-1-2016.
 */
public class notification {
    public static void singleNotification(User receiver, String message){
        if(receiver!= ParseUser.getCurrentUser()) {
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("user", receiver);

            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            push.setMessage(message);
            push.sendInBackground();
        }
    }

}
