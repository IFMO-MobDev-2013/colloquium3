package com.example.ToDoTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBase {
    private SQLiteDatabase sqLiteDatabase;
    private Context context;
    private DatabaseHelper databaseHelper;
    private Cursor cursor;

    public static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_TASK_NAME = "my_tasks";

    public static final String KEY_TASK = "task";
    public static final String KEY_UNDER_TASK = "under_task";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_STATUS = "status";
    public static final String KEY_ID = "_id";


    public static final String DATABASE_CREATE =  "CREATE TABLE " + TABLE_TASK_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TASK + " TEXT NOT NULL, " + KEY_UNDER_TASK + " TEXT NOT NULL, " + KEY_PRIORITY + " TEXT NOT NULL, " + KEY_STATUS + " TEXT NOT NULL);";

    public DataBase(Context context) {
        this.context = context;
    }

    public DataBase open() throws SQLiteException {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public long insertTask(String name) {
        ContentValues values = new ContentValues();
        values.put(KEY_TASK, name);
        values.put(KEY_UNDER_TASK, "");
        values.put(KEY_PRIORITY, "");
        values.put(KEY_STATUS, "--");
        return sqLiteDatabase.insert(TABLE_TASK_NAME, null, values);
    }

    public boolean deleteAllTasksWithSuchName(String mainName) {
        newCursor();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            if (name.equals(mainName)) {
               return sqLiteDatabase.delete(TABLE_TASK_NAME, KEY_ID + "=" +  cursor.getString(cursor.getColumnIndex(KEY_ID)), null) > 0;
            }
        }
        return true;
    }

    public boolean deleteAllUnderTasksWithSuchName(String mainName, String name1) {
        newCursor();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            String name3 = cursor.getString(cursor.getColumnIndex(KEY_UNDER_TASK));

            if (name.equals(mainName) && name1.equals(name3)) {
                return sqLiteDatabase.delete(TABLE_TASK_NAME, KEY_ID + "=" +  cursor.getString(cursor.getColumnIndex(KEY_ID)), null) > 0;
            }
        }
        return true;
    }

    public void updateTask(String oldName, String newName) {
        newCursor();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            if (oldName.equals(name)) {
                ContentValues values = new ContentValues();
                values.put(KEY_TASK, newName);
                sqLiteDatabase.update(TABLE_TASK_NAME, values, KEY_ID + "=" + cursor.getString(cursor.getColumnIndex(KEY_ID)), null);
            }
        }
    }

    public void updateUnderTask(String oldMainName, String oldName, String newName) {
        newCursor();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            String name1 = cursor.getString(cursor.getColumnIndex(KEY_UNDER_TASK));
            if (oldMainName.equals(name) && name1.equals(oldName)) {
                ContentValues values = new ContentValues();
                values.put(KEY_UNDER_TASK, newName);
                sqLiteDatabase.update(TABLE_TASK_NAME, values, KEY_ID + "=" + cursor.getString(cursor.getColumnIndex(KEY_ID)), null);
            }
        }
    }

    public long insertUnderTask(String mainName, String underName) {
        ContentValues values = new ContentValues();
        values.put(KEY_TASK, mainName);
        values.put(KEY_UNDER_TASK, underName);
        values.put(KEY_PRIORITY, "");
        values.put(KEY_STATUS, "--");
        return sqLiteDatabase.insert(TABLE_TASK_NAME, null, values);
    }

    public ArrayList<HashMap<String, String>> getTask() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        newCursor();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            String under = cursor.getString(cursor.getColumnIndex(KEY_UNDER_TASK));
            String priority = cursor.getString(cursor.getColumnIndex(KEY_PRIORITY));
            String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
            if (under.equals("")) {
                String tmp = priority + name;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(MyActivity.LEFT, tmp);
                map.put(MyActivity.RIGHT,status);
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> getUnderTask(String mainName) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        newCursor();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            String under = cursor.getString(cursor.getColumnIndex(KEY_UNDER_TASK));
            String priority = cursor.getString(cursor.getColumnIndex(KEY_PRIORITY));
            String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
            if (mainName.equals(name) && under.equals("") == false) {
                String tmp = priority + under;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(MyActivity.LEFT, tmp);
                map.put(MyActivity.RIGHT,status);
                arrayList.add(map);
            }
        }
        return arrayList;
    }

    public void updateStatus(String name) {
        if (name.charAt(0) == '*') {
            name = name.substring(1, name.length());
        }
        newCursor();
        while (cursor.moveToNext()) {
            String name2 = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            String status = cursor.getString(cursor.getColumnIndex(KEY_STATUS));
            if (name2.equals(name)) {
                ContentValues values = new ContentValues();
                if (status.equals("OK")) {
                    status = "--";
                } else {
                    status = "OK";
                }
                values.put(KEY_STATUS, status);
                sqLiteDatabase.update(TABLE_TASK_NAME, values, KEY_ID + "=" + cursor.getString(cursor.getColumnIndex(KEY_ID)), null);
            }
        }
    }



    private void newCursor() {
        cursor = sqLiteDatabase.query(TABLE_TASK_NAME, new String[] {
                KEY_ID, KEY_TASK, KEY_UNDER_TASK, KEY_PRIORITY, KEY_STATUS},
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TABLE ARTICLE CREATE", DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_TASK_NAME);
            onCreate(db);
        }
    }
}
