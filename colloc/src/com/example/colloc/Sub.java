package com.example.colloc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 27.12.13
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class Sub extends Activity {

    SimpleCursorAdapter sca = null;
    final DbAdapter mDb = new DbAdapter(this);

    private static final int DELETE_ID = Menu.FIRST;

    ListView lv;

    ImageButton imb;
    EditText edt;

    String curTask;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        String s = intent.getStringExtra(DbAdapter.KEY_TNAME);
        curTask = s;
        mDb.open();
        Cursor cursor = mDb.fetchSTASKS(s);

        String[] from = new String[]{DbConstants.KEY_TNAME};
        int[] to = new int[]{R.id.textViewName};

        sca = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
        lv.setAdapter(sca);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor) sca.getItem(i);
                String s = c.getString(c.getColumnIndex(DbAdapter.KEY_SNAME));
            }
        });

        mDb.close();

        imb = (ImageButton) findViewById(R.id.imageButton);
        edt = (EditText) findViewById(R.id.editText);


        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = edt.getText().toString();
                addTask(s);
            }
        });
    }

    void addTask(String s) {
        ContentValues cv = new ContentValues();
        try {
            mDb.open();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (s.charAt(s.length() - 1) == '*')
            cv.put(DbAdapter.KEY_SPRIOR, 1);
        else
            cv.put(DbAdapter.KEY_SPRIOR, 0);
        cv.put(DbAdapter.KEY_SNAME, s);



        mDb.addStask(cv, curTask);
        String[] from = new String[]{DbConstants.KEY_SNAME};
        int[] to = new int[]{R.id.textViewName};
        Cursor c = mDb.fetchSTASKS(curTask);
        try {
            sca = new SimpleCursorAdapter(this, R.layout.list_item, c, from, to);
            lv.setAdapter(sca);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDb.close();
        edt.setText("");

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean flag = false;
        switch (item.getItemId()) {
            case DELETE_ID:
                if (lv.getCount() == 1) {
                    flag = true;
                    // onBackPressed

                    return true;
                }
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


                Cursor c = (Cursor) sca.getItem((int) info.id);
                String name = c.getString(c.getColumnIndex(DbAdapter.KEY_SNAME));

                mDb.open();
                mDb.deleteStask(name);

                if (flag) {
                    onBackPressed();
                }


                return true;
        }
        return super.onContextItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 0)
//            return;
//        else {
//            String c = data.getStringExtra(InsertActivity.KEY_NAME);
//            ContentValues cv = new ContentValues();
//
//
//            if(c.charAt(c.length()-1) == '*')
//                cv.put(DbAdapter.KEY_SPRIOR, 1);
//            else
//                cv.put(DbAdapter.KEY_SPRIOR, 0);
//            cv.put(DbAdapter.KEY_SNAME, c);
//
//            mDb.addTask(cv);
//            mDb.open();
//
//
//            return;
//        }
//    }
}
