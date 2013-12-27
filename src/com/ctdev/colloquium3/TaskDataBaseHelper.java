package com.ctdev.colloquium3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexei on 27.12.13.
 */
public class TaskDataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String _ID = "_id";
    public static final String DATABASE_NAME = "tasks";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DONE = "done";
    public static final String PRIORITY = "priority";


    public TaskDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDatabase());
    }

    String createDatabase() {
        String temp = "CREATE TABLE " + DATABASE_NAME
                + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT," + DESCRIPTION + " TEXT," + DONE + " INTEGER," + PRIORITY + " INTEGER);";
        return temp;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(dropDataBase());
            onCreate(db);
        }
    }

    String dropDataBase() {
        return "DROP TABLE IF EXISTS " + DATABASE_NAME;
    }
}
