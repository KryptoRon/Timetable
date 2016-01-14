package com.kb5012.timetable;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kb5012.timetable.DataModels.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.list_item_task, parent, false);
        }
        // get task to get data from
        Task task = mTasks.get(position);

        // set title of the task
        TextView taskName = (TextView) itemView.findViewById(R.id.task_name);
        taskName.setText(task.getTitle());

        // set completed tag if task completed
        TextView completed = (TextView) itemView.findViewById(R.id.completed);
        if (task.isStatus()){
            completed.setVisibility(View.VISIBLE);
        }

        // Set deadline based on time left
        TextView date = (TextView) itemView.findViewById(R.id.due_date);
        Date deadline = task.getDeadline();
        String dateString;
        if (deadline == null) {
            dateString = "null";
        } else {
            MyDateFormat mdf = new MyDateFormat(deadline);
            dateString = mdf.format(deadline);

            // if task is past due
            if (deadline.before(Calendar.getInstance().getTime()) && !task.isStatus()){
                completed.setVisibility(View.VISIBLE);
                completed.setText(R.string.past_due);
                completed.setTextColor(mContext.getResources().getColor(R.color.RED));
            }
        }
        date.setText(mContext.getString(R.string.due) + dateString);


        // TODO set image of the person who assigned
        ImageView avatar = (ImageView) itemView.findViewById(R.id.avatar);


        return itemView;
    }

}