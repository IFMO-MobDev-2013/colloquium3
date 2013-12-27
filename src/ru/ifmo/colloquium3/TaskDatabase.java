package ru.ifmo.colloquium3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by asus on 27.12.13.
 */
public class TaskDatabase {
    public static final String KEY_TASK = TaskItem.tags[TaskItem.NAME];
    public static final String KEY_ROWID = TaskItem.tags[TaskItem.ID];
    public static final String KEY_PRIORITY = TaskItem.tags[TaskItem.PRIORITY];


    private static final String TAG = "TaskDatabase";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    private String DATABASE_NAME;
    private String DATABASE_TABLE;

    private String DATABASE_CREATE;

    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public TaskDatabase(Context ctx, int number) {
        this.mCtx = ctx;
        DATABASE_NAME = "TaskData" + number;
        DATABASE_TABLE = "TaskDatabase" + number;

        DATABASE_CREATE = "CREATE TABLE "
                + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TASK + ", " + KEY_PRIORITY + ");";

    }

    public TaskDatabase open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long addTask(TaskItem task) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TASK, task.param[TaskItem.NAME]);
        initialValues.put(KEY_PRIORITY, task.param[TaskItem.PRIORITY]);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    public boolean deleteTask(TaskItem Task) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + Task.param[TaskItem.ID], null) > 0;
    }


    public ArrayList<TaskItem> getAllTasks() {
        Cursor cursor = mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
        ArrayList<TaskItem> tasks = new ArrayList<TaskItem>();
        TaskItem curTask = new TaskItem();
        while (cursor.moveToNext()){
            curTask.clear();
            curTask.param[TaskItem.ID] = cursor.getString(cursor.getColumnIndex(KEY_ROWID));
            curTask.param[TaskItem.NAME] = cursor.getString(cursor.getColumnIndex(KEY_TASK));
            curTask.param[TaskItem.PRIORITY] = cursor.getString(cursor.getColumnIndex(KEY_PRIORITY));
            tasks.add(curTask.makeCopy());
        }
        cursor.close();
        return tasks;
    }

    public TaskItem getTask(int id) throws SQLException {

        Cursor cursor =

                mDb.query(true, DATABASE_TABLE, null, KEY_ROWID + "=" + id, null,
                        null, null, null, null);

        if (cursor == null) {
            Log.e(TAG, "Error getting Task");
            return null;
        }

        cursor.moveToFirst();
        TaskItem task = new TaskItem();
        task.param[TaskItem.ID] = cursor.getString(cursor.getColumnIndex(KEY_ROWID));
        task.param[TaskItem.NAME] = cursor.getString(cursor.getColumnIndex(KEY_TASK));
        task.param[TaskItem.PRIORITY] = cursor.getString(cursor.getColumnIndex(KEY_PRIORITY));
        cursor.close();

        return task;
    }

    public boolean updateTask(TaskItem task, int id) {
        ContentValues args = new ContentValues();
        args.put(KEY_ROWID, task.param[TaskItem.ID]);
        args.put(KEY_TASK, task.param[TaskItem.NAME]);
        args.put(KEY_PRIORITY, task.param[TaskItem.PRIORITY]);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id + "", null) > 0;
    }



}
