package com.example.colloquium3;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.colloquium3.databases.SubtaskDatabase;
import com.example.colloquium3.databases.TaskDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class TaskModifyActivity extends Activity {

    private EditText taskName;
    private int taskId;
    private SQLiteDatabase rdb;
    private SQLiteDatabase wdb;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_modify);
        taskName = (EditText) findViewById(R.id.task_name_edit);
        taskId = getIntent().getIntExtra(getString(R.string.task_id), -1);
        if (taskId != -1) {
            String name = "";
            TaskDatabase db = new TaskDatabase(this);
            rdb = db.getReadableDatabase();
            Cursor cursor = rdb.query(TaskDatabase.DATABASE_NAME, null,
                    TaskDatabase._ID + " = " + taskId, null, null, null, null, "1");
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(TaskDatabase.TASK_NAME));
            }
            cursor.close();
            rdb.close();
            db.close();
            taskName.setText(name);
        } else {
            taskName.setText("");
        }
    }

    public void onProceed(View view) {
        if (taskName.getText().toString().isEmpty()) {
            onRemove(view);
        } else {
            if (taskId == -1) {
                TaskDatabase db = new TaskDatabase(this);
                wdb = db.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(TaskDatabase.TASK_NAME, taskName.getText().toString());
                wdb.insert(TaskDatabase.DATABASE_NAME, null, cv);
                wdb.close();
                db.close();
            } else {
                TaskDatabase db = new TaskDatabase(this);
                wdb = db.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(TaskDatabase.TASK_NAME, taskName.getText().toString());
                wdb.update(TaskDatabase.DATABASE_NAME, cv, TaskDatabase._ID + " = " + taskId, null);
                wdb.close();
                db.close();
            }
        }
        this.finish();
    }

    public void onRemove(View view) {
        if (taskId != -1) {
            TaskDatabase db = new TaskDatabase(this);
            wdb = db.getWritableDatabase();
            wdb.delete(TaskDatabase.DATABASE_NAME, TaskDatabase._ID + " = " + taskId, null);
            wdb.close();
            db.close();
            SubtaskDatabase srdb = new SubtaskDatabase(this);
            wdb = srdb.getWritableDatabase();
            wdb.delete(SubtaskDatabase.DATABASE_NAME, SubtaskDatabase.TASK_ID + " = " + taskId, null);
            wdb.close();
            srdb.close();
        }
        this.finish();
    }
}