package com.example.Coll_3;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	public   SQLiteDatabase main_db;
	public   SQLiteDatabase opt_db;
	public   DataBaseMainTargetHelper dbMainHelper;
	public   DataBaseOptionalTargetHelper dbOptHeler;
	public   ListView listTarget;
	public   Cursor cursor;
	public   ArrayAdapter<String> adapter;
	public   int TABLE_SHOW = 0;

	private void install_db() {
		dbMainHelper = new DataBaseMainTargetHelper(this);
		dbOptHeler = new DataBaseOptionalTargetHelper(this);
		main_db = dbMainHelper.getWritableDatabase();
		opt_db =  dbOptHeler.getWritableDatabase();

	}
	private boolean update_listTarget(int num_db) {


		List<String> from = new ArrayList<String>();
		if (num_db == 0) {
			cursor = main_db.query(dbMainHelper.NAME_DATABASE,null,null,null,null,null,null);

		} else {
			cursor = opt_db.query(dbMainHelper.NAME_DATABASE,null,null,null,null,null,null);

		}
		while (cursor.moveToNext()) {
			String text = cursor.getString(cursor.getColumnIndex("target"));
			from.add(text);
		}

		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,from);
		cursor.close();
		listTarget.setAdapter(adapter);
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listTarget = (ListView) findViewById(R.id.listView);
		install_db();
		update_listTarget(0);




	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.main_target:
				TABLE_SHOW = 0;
				return update_listTarget(0);
			case R.id.optional_target:
				TABLE_SHOW = 1;
				return update_listTarget(1);
		}
		return true;
	}

	public void onClickButtonAdd(View v) {
		EditText text = (EditText) findViewById(R.id.editText);
		String s = text.getText().toString();
		ContentValues cv = new ContentValues();
		if (TABLE_SHOW == 0) {
			cv.put(DataBaseMainTargetHelper.TARGET,s);
			main_db.insert(DataBaseMainTargetHelper.NAME_TABLE,null,cv);
		} else  {
			cv.put(DataBaseOptionalTargetHelper.TARGET,s);
			main_db.insert(DataBaseOptionalTargetHelper.NAME_TABLE,null,cv);
		}


	}
}
