package com.example.colloquium3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

class SubTasksAdapter extends ArrayAdapter<SubTask>
{
    private final Context context;
    public Vector<SubTask> subtasks;
    private SubTasksActivity program;

    public SubTasksAdapter(Context context, Vector<SubTask> entries, SubTasksActivity program)
    {
        super(context, R.layout.subtask, entries);
        this.context = context;
        this.subtasks = entries;
        this.program = program;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View markView = inflater.inflate(R.layout.subtask, parent, false);

        TextView markTitleView = (TextView) markView.findViewById(R.id.subtask_title);
        ImageView subtasksStarView = (ImageView)markView.findViewById(R.id.imageView);

        final SubTask subtask = subtasks.get(index);

        markTitleView.setText(subtask.title);
        if (subtask.type == 0)
        {
            subtasksStarView.setVisibility(View.VISIBLE);
        }
        else
        {
            subtasksStarView.setVisibility(View.INVISIBLE);
            markTitleView.setTextColor(Color.WHITE);
        }
        if (subtask.type == 2)
        {
            markTitleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            markTitleView.setTextColor(Color.WHITE);
        }

        markView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(program, SubTasksEditActivity.class);
                intent.putExtra("ID_MARK", subtask.id_subtask);
                program.startActivity(intent);
            }
        });

        return markView;
    }

}
