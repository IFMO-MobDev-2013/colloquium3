package com.example.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String _ID = "_id";
    public static final String DATABASE_NAME = "my_todo_db";
    public static final String TASK = "task";
    public static final String SUB_TASK = "sub_task";
    public static final String PRIORITY = "priority";
    public static final String STATUS = "status";


    public static final String CREATE_DATABASE = "CREATE TABLE " + DATABASE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + SUB_TASK + " TEXT, " + PRIORITY +
            " TEXT, " + STATUS + " TEXT" + ");";

    public static final String DROP_DATABASE = "DROP TABLE IF EXISTS " + DATABASE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        if (old_v != new_v) {
            db.execSQL(DROP_DATABASE);
            onCreate(db);
        }
    }
}


