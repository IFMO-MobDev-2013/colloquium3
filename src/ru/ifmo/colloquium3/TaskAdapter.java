package ru.ifmo.colloquium3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asus on 27.12.13.
 */
public class TaskAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<TaskItem> objects;

    TaskAdapter(Context context, ArrayList<TaskItem> TaskItems) {
        ctx = context;
        objects = TaskItems;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.task_item, parent, false);
        }

        TaskItem l = getTask(position);

        ((TextView) view.findViewById(R.id.taskItem)).setText(l.param[TaskItem.NAME]);

        return view;
    }

    TaskItem getTask(int position) {
        return ((TaskItem) getItem(position));
    }


}

