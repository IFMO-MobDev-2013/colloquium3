package com.ctdev.colloquium3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Alexei on 27.12.13.
 */


public class AddTaskActivity extends Activity {
    EditText etName;
    EditText etDescription;
    boolean newTask;
    int id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        etName = (EditText) findViewById(R.id.etName);
        etDescription = (EditText) findViewById(R.id.etDescription);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton rbLow = (RadioButton) findViewById(R.id.rbLow);
        RadioButton rbMid = (RadioButton) findViewById(R.id.rbLow);
        RadioButton rbHigh = (RadioButton) findViewById(R.id.rbLow);
        Intent intent = getIntent();
        newTask = intent.getBooleanExtra(MainActivity.NEW, true);
        id = intent.getIntExtra(TaskDataBaseHelper._ID, 0);
        if (newTask) {
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            String name = "";
            name = intent.getStringExtra(TaskDataBaseHelper.NAME);
            String description = "";
            description = intent.getStringExtra(TaskDataBaseHelper.DESCRIPTION);
            int priority = intent.getIntExtra(TaskDataBaseHelper.PRIORITY, 0);
            etName.setText(name);
            etDescription.setText(description);
            if (priority == 0) {
                radioGroup.check(R.id.rbLow);
            } else if (priority == 1) {
                radioGroup.check(R.id.rbMid);
            } else if (priority == 2) {
                radioGroup.check(R.id.rbHigh);
            }
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDataBaseHelper taskDataBaseHelper = new TaskDataBaseHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = taskDataBaseHelper.getWritableDatabase();
                sqLiteDatabase.delete(TaskDataBaseHelper.DATABASE_NAME, TaskDataBaseHelper._ID + "=" + id, null);
                sqLiteDatabase.close();
                taskDataBaseHelper.close();
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(etName.getText().toString())) {
                    if (newTask) {
                        TaskDataBaseHelper taskDataBaseHelper = new TaskDataBaseHelper(getApplicationContext());
                        SQLiteDatabase sqLiteDatabase = taskDataBaseHelper.getWritableDatabase();
                        String name = etName.getText().toString();
                        String description = etDescription.getText().toString();
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioButtonID);
                        int priority = radioGroup.indexOfChild(radioButton);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TaskDataBaseHelper.NAME, name);
                        contentValues.put(TaskDataBaseHelper.DESCRIPTION, description);
                        contentValues.put(TaskDataBaseHelper.PRIORITY, priority);
                        contentValues.put(TaskDataBaseHelper.DONE, 0);
                        sqLiteDatabase.insert(TaskDataBaseHelper.DATABASE_NAME, null, contentValues);
                        sqLiteDatabase.close();
                        taskDataBaseHelper.close();
                    } else {
                        TaskDataBaseHelper taskDataBaseHelper = new TaskDataBaseHelper(getApplicationContext());
                        SQLiteDatabase sqLiteDatabase = taskDataBaseHelper.getWritableDatabase();
                        String name = etName.getText().toString();
                        String description = etDescription.getText().toString();
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioButtonID);
                        int priority = radioGroup.indexOfChild(radioButton);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(TaskDataBaseHelper.NAME, name);
                        contentValues.put(TaskDataBaseHelper.DESCRIPTION, description);
                        contentValues.put(TaskDataBaseHelper.PRIORITY, priority);
                        sqLiteDatabase.update(TaskDataBaseHelper.DATABASE_NAME, contentValues, TaskDataBaseHelper._ID + "=" + id, null);
                        sqLiteDatabase.close();
                        taskDataBaseHelper.close();
                    }
                }
                finish();
            }
        });
    }
}
