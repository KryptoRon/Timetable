package com.kb5012.timetable;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kb5012.timetable.DataModels.Task;

import java.util.List;

/**
 * Created by cc on 7-1-2016.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
        private Context mContext;
        private List<Task> mTasks;

        public TaskAdapter(Context context, List<Task> objects) {
            super(context, R.layout.list_item_task, objects);
            this.mContext = context;
            this.mTasks = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (convertView==null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                itemView=inflater.inflate(R.layout.list_item_task, parent, false);
            }
            Task task =mTasks.get(position);
            TextView taskName=(TextView)itemView.findViewById(R.id.taskName);
            taskName.setText(task.getTitle());
            return itemView;
        }

}