package com.ctdev.colloquium3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alexei on 27.12.13.
 */
class TaskAdapter extends ArrayAdapter<Task> {
    Context context;
    ArrayList<Task> arrayList;

    public TaskAdapter(Context context, ArrayList<Task> objects) {
        super(context, R.layout.main, objects);
        this.context = context;
        this.arrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_item, parent, false);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvName.setText(arrayList.get(position).name);
        if (arrayList.get(position).priority == 0) {
            tvName.setTextColor(Color.YELLOW);
        } else if (arrayList.get(position).priority == 1) {
            tvName.setTextColor(Color.GREEN);
        } else tvName.setTextColor(Color.RED);
        if (arrayList.get(position).done) tvName.setTextColor(Color.GRAY);
        tvDescription.setText(arrayList.get(position).description);
        return view;
    }
}
