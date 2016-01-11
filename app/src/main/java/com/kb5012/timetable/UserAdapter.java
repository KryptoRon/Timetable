package com.kb5012.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kb5012.timetable.DataModels.User;

import java.util.List;

/**
 * Created by Chie-cheung on 8-1-2016.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context context, List<User> objects) {
        super(context, R.layout.list_item_user, objects);
        this.mContext = context;
        this.mUsers = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.list_item_user, parent, false);
        }
        //User user = mUsers.get(position);
        TextView userName = (TextView) itemView.findViewById(R.id.userName);
        //userName.setText(user.getUsername());
        userName.setText(mUsers.get(position).getUsername());
        return itemView;
    }

}
