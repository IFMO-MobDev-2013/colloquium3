package com.example.ToDoTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkActivity extends Activity {
    public static final String LEFT = "my_name";
    public static final String RIGHT = "my_statys";

    private ListView listView;
    private DataBase dataBase;
    private ArrayList<HashMap<String, String>> arrayList;
    private String mainTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page);

        listView = (ListView) findViewById(R.id.listView);
        //deleteDatabase(DataBase.DATABASE_NAME);
        dataBase = new DataBase(this);
        dataBase.open();
        mainTask = getIntent().getStringExtra("TASK");
        if (mainTask.charAt(0) == '*') {
            mainTask = mainTask.substring(1, mainTask.length());
        }

        readUnderTask();
        listView = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                String name = editText.getText().toString();
                if (name.equals("")) {
                    // Toast
                    return;
                }
                editText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                dataBase.insertUnderTask(mainTask, name);
                readUnderTask();
            }
        });
        readUnderTask();
     }

    public static final int IDM_RENAME = 101;
    public static final int IDM_DELETE = 102;
    int pozition = -1;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        pozition = aMenuInfo.position;
        menu.add(Menu.NONE, IDM_RENAME, Menu.NONE, "Rename");
        menu.add(Menu.NONE, IDM_DELETE, Menu.NONE, "Delete");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        EditText editText;
        Button button;
        InputMethodManager imm;
        switch (item.getItemId())
        {
            case IDM_RENAME:
                editText = (EditText) findViewById(R.id.editText);
                editText.setHint("Enter your new name");
                editText.setText(arrayList.get(pozition).get(LEFT));
                button = (Button) findViewById(R.id.button);
                button.setText("Rename");
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
                button.setOnClickListener(new View.OnClickListener() {
                    EditText editText = (EditText) findViewById(R.id.editText);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Button button = (Button) findViewById(R.id.button);
                    @Override
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        dataBase.updateUnderTask(mainTask, arrayList.get(pozition).get(LEFT), editText.getText().toString());
                        readUnderTask();;
                        button.setText("Добавить");
                        editText.setText("");
                        editText.setHint("Введите задачу для добавления");
                    }
                });
                break;
            case IDM_DELETE:
                dataBase.deleteAllUnderTasksWithSuchName(mainTask, arrayList.get(pozition).get(LEFT));
                readUnderTask();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }


    private void readUnderTask() {
        arrayList = new ArrayList<HashMap<String, String>>();
        arrayList = dataBase.getUnderTask(mainTask);
        SimpleAdapter adapter = new SimpleAdapter(this, arrayList, R.layout.my_style, new String[] {LEFT, RIGHT}, new int[] {R.id.Colname, R.id.status});
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }
}
