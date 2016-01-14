package com.kb5012.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.User;

import java.util.List;

/**
 * Created by Chie-cheung on 8-1-2016.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private List<User> mUser;

    public UserAdapter(Context context, List<User> objects) {
        super(context, R.layout.list_item_user, objects);
        this.mUser = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.list_item_user, parent, false);
        }
        User user = mUser.get(position);
        TextView taskName = (TextView) itemView.findViewById(R.id.userName);
        taskName.setText(user.getUsername());
        return itemView;
    }
}


