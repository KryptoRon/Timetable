package com.kb5012.timetable.FragmentUserScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kb5012.timetable.DBHelper;

import com.kb5012.timetable.DataModels.Group;
import com.kb5012.timetable.DataModels.User;
import com.kb5012.timetable.GroupScreen;
import com.kb5012.timetable.R;


import java.util.ArrayList;

/**
 * Created by Chie-cheung on 15-12-2015.
 */
public class MyGroupsButtons extends Fragment {
    private DBHelper dbHelper=new DBHelper();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tab_frag_my_group_button, container, false);
        setMyGroups(v);
        return v;
    }


    private void setMyGroups(View v){
       Bundle bundle=getArguments();
        String userID=bundle.getString("userId");
        LinearLayout linearLayout=(LinearLayout)v.findViewById(R.id.layout_MyGroup);
        ArrayList<Group> myGroups = dbHelper.findAllGroupByUserId(userID);
        User user = DBHelper.findUserById(userID);
        //((TextView) findViewById(R.id.tv_user)).setText("welkom " + user.getFirstName() + " " + user.getLastName());
        Button button;
        for (Group group : myGroups) {
            button = new Button(getContext());
            button.setText((Html.fromHtml("taak: " + group.getName() + "<br/>")));
            button.setTag(group.getObjectId());
            //  als de groepknop word gedrukt zal de juiste groep scherm geopend worden
            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Button b = (Button) v;
                    Log.d("clicked button: ", b.getTag() + "");
                    Intent intent = new Intent(getActivity(),GroupScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("groupId", Integer.parseInt(b.getTag()+""));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
                    linearLayout.addView(button);
        }
    }


}
