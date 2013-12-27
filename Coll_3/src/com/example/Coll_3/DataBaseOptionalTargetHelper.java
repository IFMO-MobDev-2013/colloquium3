package com.example.Coll_3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alex on 27.12.13.
 */

public class DataBaseOptionalTargetHelper extends SQLiteOpenHelper {
	public static final String NAME_DATABASE = "db_opt_target";
	public static final int VERSION_DATABASE = 1;
	public static final String NAME_TABLE = "table_opt_target";
	public static final String TARGET = "target";
	public static final String PRIORY = "priory";


	public DataBaseOptionalTargetHelper(Context context) {
		super(context, NAME_DATABASE, null, VERSION_DATABASE);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	  	db.execSQL("CREATE TABLE " + NAME_TABLE
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ TARGET + " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + NAME_TABLE);
		onCreate(db);
	}


}
