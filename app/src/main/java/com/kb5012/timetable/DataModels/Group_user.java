package com.kb5012.timetable.DataModels;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Chie-cheung on 8-1-2016.
 */
@ParseClassName("Group_user")
public class Group_user extends ParseObject {
    public Group_user() {
    }
    public ParseObject getGroup_id() {
        return (ParseObject)get("group_id");
    }

    public void setGroup_id(ParseObject group_id) {
        put("group_id" , group_id);
    }
    public String getUser_id() {
        return getString("user_id");
    }

    public void setUser_id(String user_id) {
        put("user_id" , user_id);
    }
}
