package com.kb5012.timetable.FragmentUserScreen;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.R;
import com.kb5012.timetable.Task;
import com.kb5012.timetable.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

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
        //TODO set listAdapter for listView




    }

}
