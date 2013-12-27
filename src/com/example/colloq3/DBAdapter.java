package com.example.colloq3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    public static final String TABLE_ID = "_id";
    private static final String DATABASE_NAME = "tasks";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "task_table";
    public static final String UNTASK = "untask";
    public static final String TASK = "task";
    public static final String PRIOR = "prior";

    private static final String SQL_CREATE_ENTRIES = "create table "
            + TABLE_NAME + " ("
            + TABLE_ID + " integer primary key autoincrement, "
            + TASK + " text not null, "
            + UNTASK + " text not null, "
            + PRIOR + " integer not null ); ";


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    private final Context mcontext;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context context) {
        this.mcontext = context;
        DBHelper = new DatabaseHelper(mcontext);
        db = DBHelper.getWritableDatabase();
    }


    public void insert(String ttask, int pprior, String nntask) {
        ContentValues cv =  new ContentValues();
        cv.put(TASK, ttask);
        cv.put(PRIOR, pprior);
        cv.put(UNTASK, nntask);
        db.insert(TABLE_NAME, null, cv);
    }

    public void update(String ttask, int pprior, String nntask, String prevtask) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, ttask);
        cv.put(PRIOR, pprior);
        cv.put(UNTASK, nntask);
        db.delete(TABLE_NAME, "task = ?", new String[]{prevtask});
        db.insert(TABLE_NAME, null, cv);
    }
    public void updateun(String ttask, int pprior, String nntask, String prevtask) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, ttask);
        cv.put(PRIOR, pprior);
        cv.put(UNTASK, nntask);
        db.delete(TABLE_NAME, "untask = ?", new String[]{prevtask});
        db.insert(TABLE_NAME, null, cv);
    }
    public void deleteTask(String ttask)  {
        db.delete(TABLE_NAME, "task = ?", new String[]{ttask});
    }
    public void deleteUntask(String ttask)  {
        db.delete(TABLE_NAME, "untask = ?", new String[]{ttask});
    }

    public Cursor getAllData() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getCategoryData(String category) {
        return db.query(TABLE_NAME, null, "category = ?", new String[] {category}, null, null, "category");
    }



    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}
