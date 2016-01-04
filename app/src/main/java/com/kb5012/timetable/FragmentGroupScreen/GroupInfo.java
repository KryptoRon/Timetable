package com.kb5012.timetable.FragmentGroupScreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kb5012.timetable.DBHelper;
import com.kb5012.timetable.Group;
import com.kb5012.timetable.GroupScreen;
import com.kb5012.timetable.R;
import com.kb5012.timetable.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupInfo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tab_frag_my_group, container, false);
        setMyGroups(v);
        return v;
    }


    private void setMyGroups(View v){
        //TODO juiste manier id ophalen. de taken ophalen van de persoon.
        //Bundle
        TextView groupNumber=(TextView)v.findViewById(R.id.groupNumber);

        }


}
