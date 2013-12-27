package com.ctdev.colloquium3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    public static final String NEW = "new";

    ListView lvTasks;
    ArrayList<Task> arrayList;
    TaskAdapter adapter;


    void fillArray() {
        arrayList.clear();
        TaskDataBaseHelper taskDataBaseHelper = new TaskDataBaseHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = taskDataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TaskDataBaseHelper.DATABASE_NAME, null, null, null, null, null, null);
        int idColumn = cursor.getColumnIndex(TaskDataBaseHelper._ID);
        int nameColumn = cursor.getColumnIndex(TaskDataBaseHelper.NAME);
        int descriptionColumn = cursor.getColumnIndex(TaskDataBaseHelper.DESCRIPTION);
        int doneColumn = cursor.getColumnIndex(TaskDataBaseHelper.DONE);
        int priorityColumn = cursor.getColumnIndex(TaskDataBaseHelper.PRIORITY);

        while (cursor.moveToNext()) {
            arrayList.add(new Task(cursor.getString(nameColumn), cursor.getString(descriptionColumn), cursor.getInt(doneColumn) == 1, cursor.getInt(priorityColumn), cursor.getInt(idColumn)));
        }
        cursor.close();
        sqLiteDatabase.close();
        taskDataBaseHelper.close();
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        arrayList = new ArrayList<Task>();
        lvTasks = (ListView) findViewById(R.id.listView);
        adapter = new TaskAdapter(getApplicationContext(), arrayList);
        lvTasks.setAdapter(adapter);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NEW, true);
                intent.setClass(getApplicationContext(), AddTaskActivity.class);
                startActivity(intent);
            }
        });

        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
                return false;
            }
        });

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList.get(position).done = true;
                TaskDataBaseHelper taskDataBaseHelper = new TaskDataBaseHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = taskDataBaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(TaskDataBaseHelper.DONE, arrayList.get(position).done);

                sqLiteDatabase.update(TaskDataBaseHelper.DATABASE_NAME, contentValues, TaskDataBaseHelper._ID + "=" + arrayList.get(position).id, null);
                sqLiteDatabase.close();
                taskDataBaseHelper.close();
                fillArray();
            }
        });
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(NEW, false);
                intent.putExtra(TaskDataBaseHelper.NAME, arrayList.get(position).name);
                intent.putExtra(TaskDataBaseHelper.DESCRIPTION, arrayList.get(position).description);
                intent.putExtra(TaskDataBaseHelper.PRIORITY, arrayList.get(position).priority);
                intent.putExtra(TaskDataBaseHelper._ID, arrayList.get(position).id);
                intent.setClass(getApplicationContext(), AddTaskActivity.class);
                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillArray();

    }
}
