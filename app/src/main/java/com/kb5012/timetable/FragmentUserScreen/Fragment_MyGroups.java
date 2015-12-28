package com.kb5012.timetable.FragmentUserScreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.Group;
import com.kb5012.timetable.R;
import com.kb5012.timetable.Task;
import com.kb5012.timetable.User;

import java.util.ArrayList;

/**
 * Created by Chie-cheung on 15-12-2015.
 */
public class Fragment_MyGroups extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tab_frag_my_group, container, false);
        setMyGroups(v);
        return v;
    }


    private void setMyGroups(View v){
        //TODO juiste manier id ophalen. de taken ophalen van de persoon.
        int userID=0;
        LinearLayout linearLayout=(LinearLayout)v.findViewById(R.id.layout_MyGroup);
        ArrayList<Group> myGroups = DBHelper.findAllGroupByUserId(userID);
        User user = DBHelper.findUserById(userID);
        //((TextView) findViewById(R.id.tv_user)).setText("welkom " + user.getFirstName() + " " + user.getLastName());
        Button button;
        for (Group group : myGroups) {
            button = new Button(getContext());
            button.setText((Html.fromHtml("taak: " + group.getName() + "<br/>" + "gemaakt door: " + group.getBeheerder().getFirstName())));
            button.setTag(group.getId());
            linearLayout.addView(button);
        }
    }

}
