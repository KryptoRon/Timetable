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
import com.kb5012.timetable.R;
import com.kb5012.timetable.Task;
import com.kb5012.timetable.User;

import java.util.ArrayList;

/**
 * Created by Chie-cheung on 15-12-2015.
 */
public class Fragment_AllTask extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tab_frag_all_task, container, false);
        setAllTask(v);
        return v;
    }


    private void setAllTask(View v){
        //TODO juiste manier id ophalen
        int userID=0;
        LinearLayout linearLayout=(LinearLayout)v.findViewById(R.id.layout_allTask);
        ArrayList<Task> tasks = DBHelper.findAllTaskByUserId(userID);
        User user = DBHelper.findUserById(userID);
        //((TextView) findViewById(R.id.tv_user)).setText("welkom " + user.getFirstName() + " " + user.getLastName());
        Button button;
        for (Task task : tasks) {
            button = new Button(getContext());
            button.setText((Html.fromHtml("taak: " + task.getID() + "<br/>" + "gemaakt door: " + task.getTaskMaker())));
            button.setTag(task.getID());
            linearLayout.addView(button);
        }
    }

}
