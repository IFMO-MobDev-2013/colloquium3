package com.example.todo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    ArrayList<String> Task = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button_add1 = (Button) findViewById(R.id.button_add1);
        final EditText editText1 = (EditText) findViewById(R.id.editText);
        final DBHelper dbHelper = new DBHelper(this);
        ListView listView = (ListView) findViewById(R.id.listView1);
        button_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.TASK, editText1.getText().toString());
                cv.put(DBHelper.SUB_TASK, "FULL");
                SQLiteDatabase wdb = dbHelper.getWritableDatabase();
                wdb.insert(DBHelper.DATABASE_NAME, null, cv);
                //Log.d("qqq","w");
                show();
            }
        });
        show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("task", Task.get(i));
                intent.setClass(getApplicationContext(), SubTask.class);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("task", Task.get(i));
                intent.setClass(getApplicationContext(), Change.class);

                startActivity(intent);
                show();
                return true;
            }
        });
    }

    public void show() {
        Task.clear();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        Cursor cursor = rdb.query(DBHelper.DATABASE_NAME, null, null, null, null, null, null);

        int task_column = cursor.getColumnIndex(DBHelper.TASK);
        int sub_task_column = cursor.getColumnIndex(DBHelper.SUB_TASK);
        while (cursor.moveToNext()) {
            if (cursor.getString(task_column) != null && cursor.getString(sub_task_column)!=null && cursor.getString(sub_task_column).equals("FULL")) {
                Task.add(cursor.getString(task_column));
            }
        }
        cursor.close();
        Log.d("q2","");
        ListView listView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, Task);
        listView.setAdapter(arrayAdapter);

    }
    @Override
        public void onResume() {
                super.onResume();
        show();
    }
}
