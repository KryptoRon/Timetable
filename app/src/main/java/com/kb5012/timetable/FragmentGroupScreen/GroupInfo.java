package com.kb5012.timetable.FragmentGroupScreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kb5012.timetable.R;

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
        View v = inflater.inflate(R.layout.fragment_group_info, container, false);
        setMyGroups(v);
        return v;
    }


    private void setMyGroups(View v) {
        //Bundle
        Bundle bundle = getArguments();
        String groupId="";
        if (bundle != null) {
            groupId = bundle.getString("groupId");
        }
        TextView groupnumber=(TextView) v.findViewById(R.id.groupNumber);
        groupnumber.setText("Group id: " + groupId);

    }


}
