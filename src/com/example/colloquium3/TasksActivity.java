package com.example.colloquium3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.colloquium3.databases.SubtaskDatabase;
import com.example.colloquium3.databases.TaskDatabase;

import java.util.Vector;

public class TasksActivity extends Activity {
    private ListView tasksList;
    private TasksAdapter tasksAdapter;
    private Context context;
    private SQLiteDatabase rdb;

    public class TasksAdapter extends BaseAdapter {

        private Vector<TaskView> taskViews;

        public class TaskView {
            public View view;
            public String name;
            public int id;

            public TaskView(String name, int id) {
                TextView textView = new TextView(context);
                textView.setText(name);
                textView.setTextSize(20);
                view = textView;
                this.name = name;
                this.id = id;
            }
        }

        public TasksAdapter() {
            taskViews = new Vector<TaskView>();
        }

        public void addTask(String taskName, int taskId) {
            taskViews.add(new TaskView(taskName, taskId));
            notifyDataSetChanged();
        }

        public int getTaskID(int position) {
            return taskViews.get(position).id;
        }

        @Override
        public int getCount() {
            return taskViews.size();
        }

        @Override
        public Object getItem(int position) {
            return taskViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return taskViews.get(position).view;
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks);
        context = this;
        tasksList = (ListView) findViewById(R.id.tasksList);
        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SubTasksActivity.class);
                intent.putExtra(getString(R.string.task_id), tasksAdapter.getTaskID(position));
                startActivity(intent);
            }
        });
        tasksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, TaskModifyActivity.class);
                intent.putExtra(getString(R.string.task_id), tasksAdapter.getTaskID(position));
                startActivity(intent);
                return true;
            }
        });
        refreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList() {
        context = this;
        SQLiteOpenHelper db = new TaskDatabase(this);
        rdb = db.getReadableDatabase();
        tasksAdapter = new TasksAdapter();
        tasksList.setAdapter(tasksAdapter);
        Cursor cursor = rdb.query(TaskDatabase.DATABASE_NAME,
                null, null, null, null, null, null, "100");
        int name_column = cursor.getColumnIndex(TaskDatabase.TASK_NAME);
        int id_column = cursor.getColumnIndex(TaskDatabase._ID);
        String name;
        int id;
        while (cursor.moveToNext()) {
            name = cursor.getString(name_column);
            id = cursor.getInt(id_column);
            tasksAdapter.addTask(name, id);
        }
        cursor.close();
        rdb.close();
        db.close();
    }

    public void onAddTaskClick(View view) {
        Intent intent = new Intent(this, TaskModifyActivity.class);
        intent.putExtra(getString(R.string.task_id), -1);
        startActivity(intent);
    }
}
