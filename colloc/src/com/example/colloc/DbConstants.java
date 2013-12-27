package com.example.colloc;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 17.11.13
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public interface DbConstants {

    public static final String DATABASE_NAME = "TaskeDB";

    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_SUB_TASKS = "stasks";


    public static final String KEY_ROWID = "_id";

    public static final String KEY_TNAME = "tname";
    public static final String KEY_SNAME = "sname";

    public static final String KEY_TPRIOR = "tprior";
    public static final String KEY_SPRIOR = "sprior";


    // stats
    public static final String KEY_TASK_ID = "taskid";


//-----------------------------------------------------------------------------------------


    public static final String CREATE_TABLE_TASKS = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " +
            KEY_ROWID + " integer primary key autoincrement, " +
            KEY_TNAME + " text not null , " +
            KEY_TPRIOR + " integer " +
            "); ";

    public static final String CREATE_TABLE_SUB_TASKS = "CREATE TABLE IF NOT EXISTS " + TABLE_SUB_TASKS + " ( " +
            KEY_ROWID + " integer primary key autoincrement, " +
            KEY_SPRIOR + " integer, " +
            KEY_TASK_ID + " integer, " +
            KEY_SNAME + " text not null " +
            ");";
}
