package com.example.colloquium3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.colloquium3.databases.SubtaskDatabase;
import com.example.colloquium3.databases.TaskDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 */
public class SubTasksActivity extends Activity {

    private TextView taskInfo;
    private ListView subtasksList;
    private SubtaskAdapter subtaskAdapter;
    private Context context;
    private SQLiteDatabase rdb;
    private int taskId;

    public class SubtaskAdapter extends BaseAdapter {

        private Vector<SubtaskView> subtaskViews;

        public class SubtaskView {
            public View view;
            public String name;
            public int id, priority;

            public SubtaskView(String name, int priority, int id) {
                this.name = name;
                this.id = id;
                this.priority = priority;
                TextView textView = new TextView(context);
                textView.setText(name);
                textView.setTextSize(20);
                switch (priority) {
                    case SubtaskDatabase.HIGH_PRIORITY:
                        ImageView imageView = new ImageView(context);
                        imageView.setImageResource(R.drawable.star);
                        LinearLayout layout = new LinearLayout(context);
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        layout.addView(imageView);
                        layout.addView(textView);
                        view = layout;
                        break;
                    case SubtaskDatabase.LOW_PRIORITY:
                        view = textView;
                        break;
                    case SubtaskDatabase.HIGH_PRIORITY_DONE:
                        textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        view = textView;
                        break;
                    case SubtaskDatabase.LOW_PRIORITY_DONE:
                        textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        view = textView;
                        break;
                }
            }
        }

        public SubtaskAdapter() {
            subtaskViews = new Vector<SubtaskView>();
        }

        public void addSubtask(String subtaskName, int subtaskPriority, int subtaskId) {
            subtaskViews.add(new SubtaskView(subtaskName, subtaskPriority, subtaskId));
            notifyDataSetChanged();
        }

        public void addSubtask(SubtaskInfo subtaskInfo) {
            subtaskViews.add(new SubtaskView(subtaskInfo.name, subtaskInfo.priority, subtaskInfo.id));
            notifyDataSetChanged();
        }

        public int getSubtaskId(int position) {
            return subtaskViews.get(position).id;
        }

        public int getSubtaskPriority(int position) {
            return subtaskViews.get(position).priority;
        }

        @Override
        public int getCount() {
            return subtaskViews.size();
        }

        @Override
        public Object getItem(int position) {
            return subtaskViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return subtaskViews.get(position).view;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subtasks);
        context = this;
        taskId = getIntent().getIntExtra(getString(R.string.task_id), -1);
        subtasksList = (ListView) findViewById(R.id.subtasksList);
        taskInfo = (TextView) findViewById(R.id.task_info);
        subtasksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SubtaskModifyActivity.class);
                intent.putExtra(getString(R.string.subtask_id), subtaskAdapter.getSubtaskId(position));
                intent.putExtra(getString(R.string.task_id), taskId);
                startActivity(intent);
                return true;
            }
        });
        subtasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubtaskDatabase srdb = new SubtaskDatabase(context);
                SQLiteDatabase wdb = srdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SubtaskDatabase.SUBTASK_PRIORITY, subtaskAdapter.getSubtaskPriority(position) * (-1));
                wdb.update(SubtaskDatabase.DATABASE_NAME, cv, SubtaskDatabase._ID + " = " + subtaskAdapter.getSubtaskId(position), null);
                wdb.close();
                srdb.close();
                refreshList();
            }
        });
    }

    public void onAddSubtaskClick(View view) {
        Intent intent = new Intent(this, SubtaskModifyActivity.class);
        intent.putExtra(getString(R.string.subtask_id), -1);
        intent.putExtra(getString(R.string.task_id), taskId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private class SubtaskInfo {
        String name;
        int id;
        int priority;

        SubtaskInfo(String name, int id, int priority) {
            this.name = name;
            this.id = id;
            this.priority = priority;
        }
    }

    public void refreshList() {
        TaskDatabase sdb = new TaskDatabase(this);
        rdb = sdb.getReadableDatabase();
        Cursor cursor = rdb.query(TaskDatabase.DATABASE_NAME, null,
                TaskDatabase._ID + " = " + taskId, null, null, null, null, "1");
        String taskName = "";
        while (cursor.moveToNext()) {
            taskName = cursor.getString(cursor.getColumnIndex(TaskDatabase.TASK_NAME));
        }
        cursor.close();
        rdb.close();
        sdb.close();
        taskInfo.setText(taskName);
        subtaskAdapter = new SubtaskAdapter();
        subtasksList.setAdapter(subtaskAdapter);
        SubtaskDatabase srdb = new SubtaskDatabase(this);
        rdb = srdb.getReadableDatabase();
        cursor = rdb.query(SubtaskDatabase.DATABASE_NAME, null,
                SubtaskDatabase.TASK_ID + " = " + taskId, null, null, null, null, "100");
        int subtaskNameIndex = cursor.getColumnIndex(SubtaskDatabase.SUBTASK_NAME);
        int idIndex = cursor.getColumnIndex(SubtaskDatabase._ID);
        int priorityIndex = cursor.getColumnIndex(SubtaskDatabase.SUBTASK_PRIORITY);
        String subtaskName;
        int id;
        int priority;
        List<SubtaskInfo> lowPriority = new ArrayList<SubtaskInfo>();
        List<SubtaskInfo> highPriority = new ArrayList<SubtaskInfo>();
        List<SubtaskInfo> highdonePriority = new ArrayList<SubtaskInfo>();
        List<SubtaskInfo> lowdonePriority = new ArrayList<SubtaskInfo>();
        SubtaskInfo subtaskInfo;
        while (cursor.moveToNext()) {
            subtaskName = cursor.getString(subtaskNameIndex);
            id = cursor.getInt(idIndex);
            priority = cursor.getInt(priorityIndex);
            subtaskInfo = new SubtaskInfo(subtaskName, id, priority);
            switch (priority) {
                case SubtaskDatabase.HIGH_PRIORITY:
                    highPriority.add(subtaskInfo);
                    break;
                case SubtaskDatabase.LOW_PRIORITY:
                    lowPriority.add(subtaskInfo);
                    break;
                case SubtaskDatabase.HIGH_PRIORITY_DONE:
                    highdonePriority.add(subtaskInfo);
                    break;
                case SubtaskDatabase.LOW_PRIORITY_DONE:
                    lowdonePriority.add(subtaskInfo);
                    break;
            }
        }
        cursor.close();
        rdb.close();
        srdb.close();
        for (int i = 0; i < highPriority.size(); i++) {
            subtaskAdapter.addSubtask(highPriority.get(i));
        }
        for (int i = 0; i < lowPriority.size(); i++) {
            subtaskAdapter.addSubtask(lowPriority.get(i));
        }
        for (int i = 0; i < highdonePriority.size(); i++) {
            subtaskAdapter.addSubtask(highdonePriority.get(i));
        }
        for (int i = 0; i < lowdonePriority.size(); i++) {
            subtaskAdapter.addSubtask(lowdonePriority.get(i));
        }
    }
}