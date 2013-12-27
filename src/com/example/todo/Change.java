package com.example.todo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Change extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);
        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(getIntent().getStringExtra("task"));
        Button button_change = (Button) findViewById(R.id.button_change);
        Button button_delete = (Button) findViewById(R.id.button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase wdb = dbHelper.getWritableDatabase();
                Cursor cursor = wdb.query(DBHelper.DATABASE_NAME, null, null, null, null, null, null);
                int task_column = cursor.getColumnIndex(DBHelper.TASK);
                int id_column = cursor.getColumnIndex(DBHelper._ID);
                while (cursor.moveToNext()) {
                    if (cursor.getString(task_column) != null && cursor.getString(task_column).equals(getIntent().getStringExtra("task"))) {
                        wdb.delete(DBHelper.DATABASE_NAME, DBHelper._ID + "=" + cursor.getString(id_column), null);
                    }
                }
                cursor.close();
            finish();
            }

        });

        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SQLiteDatabase wdb = dbHelper.getWritableDatabase();
                Cursor cursor = wdb.query(DBHelper.DATABASE_NAME, null, null, null, null, null, null);
                int task_column = cursor.getColumnIndex(DBHelper.TASK);
                int sub_task_column = cursor.getColumnIndex(DBHelper.SUB_TASK);
                int priority_column = cursor.getColumnIndex(DBHelper.PRIORITY);
                int status_column = cursor.getColumnIndex(DBHelper.STATUS);
                int id_column = cursor.getColumnIndex(DBHelper._ID);
                while (cursor.moveToNext()) {
                    if (cursor.getString(task_column) != null && cursor.getString(task_column).equals(getIntent().getStringExtra("task"))) {
                        ContentValues cv = new ContentValues();
                        cv.put(DBHelper.TASK, textView.getText().toString());
                        cv.put(DBHelper.SUB_TASK, cursor.getString(sub_task_column));
                        cv.put(DBHelper.PRIORITY, cursor.getString(priority_column));
                        cv.put(DBHelper.STATUS, cursor.getString(status_column));

                        wdb.delete(DBHelper.DATABASE_NAME, DBHelper._ID + "=" + cursor.getString(id_column), null);
                        wdb.insert(DBHelper.DATABASE_NAME, null, cv);
                    }
                }
                cursor.close();
                finish();
            }
        });
    }
}
