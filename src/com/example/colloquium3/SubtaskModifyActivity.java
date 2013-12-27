package com.example.colloquium3;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import com.example.colloquium3.databases.SubtaskDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class SubtaskModifyActivity extends Activity {

    private EditText subtaskName;
    private Switch subtaskPriority;
    private int subtaskId;
    private int taskId;
    private boolean done;
    private SQLiteDatabase rdb, wdb;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subtask_modify);
        subtaskName = (EditText) findViewById(R.id.subtask_name_edit);
        subtaskPriority = (Switch) findViewById(R.id.PrioritySwitch);
        subtaskId = getIntent().getIntExtra(getString(R.string.subtask_id), -1);
        taskId = getIntent().getIntExtra(getString(R.string.task_id), -1);
        SubtaskDatabase srdb = new SubtaskDatabase(this);
        rdb = srdb.getReadableDatabase();
        Cursor cursor = rdb.query(SubtaskDatabase.DATABASE_NAME, null,
                SubtaskDatabase._ID + " = " + subtaskId, null, null, null, null, "1");
        while (cursor.moveToNext()) {
            subtaskName.setText(cursor.getString(cursor.getColumnIndex(SubtaskDatabase.SUBTASK_NAME)));
            subtaskPriority.setChecked(Math.abs(cursor.getInt(cursor.getColumnIndex(SubtaskDatabase.SUBTASK_PRIORITY))) == 2);
            done = cursor.getInt(cursor.getColumnIndex(SubtaskDatabase.SUBTASK_PRIORITY)) < 0;
        }
        cursor.close();
        rdb.close();
        srdb.close();
    }

    public void onSubtaskProceed(View view) {
        if (subtaskName.getText().toString().isEmpty()) {
            onSubtaskRemove(view);
        } else {
            int priority = 1;
            if (subtaskPriority.isChecked()) priority ++;
            if (done) priority *= -1;
            if (subtaskId == -1) {
                SubtaskDatabase srdb = new SubtaskDatabase(this);
                wdb = srdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SubtaskDatabase.SUBTASK_NAME, subtaskName.getText().toString());
                cv.put(SubtaskDatabase.SUBTASK_PRIORITY, priority);
                cv.put(SubtaskDatabase.TASK_ID, taskId + "");
                wdb.insert(SubtaskDatabase.DATABASE_NAME, null, cv);
                wdb.close();
                srdb.close();
            } else {
                SubtaskDatabase srdb = new SubtaskDatabase(this);
                wdb = srdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SubtaskDatabase.SUBTASK_NAME, subtaskName.getText().toString());
                cv.put(SubtaskDatabase.SUBTASK_PRIORITY, priority);
                cv.put(SubtaskDatabase.TASK_ID, taskId + "");
                wdb.update(SubtaskDatabase.DATABASE_NAME, cv, SubtaskDatabase._ID + " = " + subtaskId, null);
                wdb.close();
                srdb.close();
            }
        }
        this.finish();
    }

    public void onSubtaskRemove(View view) {
        if (subtaskId != -1) {
            SubtaskDatabase srdb = new SubtaskDatabase(this);
            wdb = srdb.getWritableDatabase();
            wdb.delete(SubtaskDatabase.DATABASE_NAME, SubtaskDatabase._ID + " = " + subtaskId, null);
            wdb.close();
            srdb.close();
        }
        this.finish();
    }
}