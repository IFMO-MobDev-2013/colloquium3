package com.example.todo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class SubTask extends Activity {


    ArrayList<String> Task = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_task);

        Button button_add1 = (Button) findViewById(R.id.button_add2);
        final EditText editText1 = (EditText) findViewById(R.id.editText_sub);
        final DBHelper dbHelper = new DBHelper(this);
        ListView listView = (ListView) findViewById(R.id.listView2);
        button_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.TASK, getIntent().getStringExtra("task"));
                cv.put(DBHelper.SUB_TASK, editText1.getText().toString());
                cv.put(DBHelper.PRIORITY,"0");
                cv.put(DBHelper.STATUS,"0");
                SQLiteDatabase wdb = dbHelper.getWritableDatabase();
                wdb.insert(DBHelper.DATABASE_NAME, null, cv);
                show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("task", getIntent().getStringExtra("task"));
                intent.putExtra("sub_task", Task.get(i));
                intent.setClass(getApplicationContext(), Change2.class);

                startActivity(intent);
                show();
                return true;
            }
        });
    }

    public void show() {
        Task.clear();
        ArrayList<Record> task=new ArrayList<Record>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        Cursor cursor = rdb.query(DBHelper.DATABASE_NAME, null, null, null, null, null, null);

        int task_column = cursor.getColumnIndex(DBHelper.TASK);
        int sub_task_column = cursor.getColumnIndex(DBHelper.SUB_TASK);
        int priority_column = cursor.getColumnIndex(DBHelper.PRIORITY);
        int status_column = cursor.getColumnIndex(DBHelper.STATUS);
        while (cursor.moveToNext()) {
            if (cursor.getString(task_column) != null && cursor.getString(task_column).equals(getIntent().getStringExtra("task")) && !cursor.getString(sub_task_column).equals("FULL")) {
                task.add(new Record(cursor.getString(sub_task_column),cursor.getString(status_column),cursor.getString(priority_column)));
            }
        }
        cursor.close();
        for(int i=0;i<task.size();i++)
        {
            if (task.get(i).priority.equals("1") && task.get(i).status.equals("0"))
            {
                Task.add("*"+task.get(i).task);
            }
        }

        for(int i=0;i<task.size();i++)
        {
            if (task.get(i).priority.equals("0") && task.get(i).status.equals("0"))
            {
                Task.add(task.get(i).task);
            }
        }
        for(int i=0;i<task.size();i++)
        {
            if (task.get(i).status.equals("1"))
            {
                Task.add("-"+task.get(i).task);
            }
        }
        ListView listView = (ListView) findViewById(R.id.listView2);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, Task);
        listView.setAdapter(arrayAdapter);

    }
    @Override
    public void onResume() {
        super.onResume();
        show();
    }
}
