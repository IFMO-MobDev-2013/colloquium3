package com.example.colloquium3.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Genyaz
 * Date: 08.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class SubtaskDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String _ID = "_id";
    public static final String DATABASE_NAME = "subtasksdb";
    public static final String TASK_ID = "task_id";
    public static final String SUBTASK_NAME = "name";
    public static final String SUBTASK_PRIORITY = "priority";
    public static final int LOW_PRIORITY_DONE = -1;
    public static final int HIGH_PRIORITY_DONE = -2;
    public static final int LOW_PRIORITY = 1;
    public static final int HIGH_PRIORITY = 2;

    public static final String CREATE_DATABASE = "CREATE TABLE " + DATABASE_NAME
            + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK_ID + " INTEGER, "
            + SUBTASK_NAME + " TEXT, " + SUBTASK_PRIORITY + " INTEGER);";

    public static final String DROP_DATABASE = "DROP TABLE IF EXISTS " + DATABASE_NAME;

    public SubtaskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL(DROP_DATABASE);
            onCreate(db);
        }
    }
}
