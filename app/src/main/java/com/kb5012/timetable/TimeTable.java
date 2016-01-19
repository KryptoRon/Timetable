package com.kb5012.timetable;

import android.app.Application;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.Group_user;
import com.kb5012.timetable.DataModels.Task;
import com.kb5012.timetable.DataModels.User;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by cc on 19-1-2016.
 */
public class TimeTable extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Task.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Group_user.class);
        ParseObject.registerSubclass(Group.class);

        Parse.initialize(this, "QHIC2tKjRl2qG6DnqODp0zfdCqIdvIVacKB2anbI", "J78bulEGe6tAHuYdNobTRlSZb3NDrcvFuOrho5Bc");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
