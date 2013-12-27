package com.example.colloq3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class MyActivity extends Activity {
    EditText input;
    Button add;
    ListView listView;
    Task[] tasks;
    DBAdapter ddata;
    int sizeList, sizeTasks;
    ArrayList<HashMap<String, Object>> data;
    SimpleAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        input = (EditText) findViewById(R.id.editText);
        add = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
        sizeList = 0;
        ddata = new DBAdapter(this);
        getTasks();
        makeList();
    }



    private void makeList() {
        data = new ArrayList<HashMap<String, Object>>(sizeList);
        HashMap<String, Object> map;
        for (int i = 0; i <= sizeTasks; ++i) {
            map = new HashMap<String, Object>();
            map.put("task", tasks[i].task);
            data.add(map);
        }
        String[] from = {"task"};
        int[] to = {R.id.task};

        adapter = new SimpleAdapter(this, data, R.layout.tasklist, from, to);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View view,
                                    int position, long id) {
                Intent intent = new Intent(MyActivity.this, MiniTasks.class);
                intent.putExtra("task", tasks[position].task);
                startActivity(intent);
            }
        });

    }

    public void add(View v) {
        String text = input.getText().toString();
        if (text.equals("")) return;
        ddata.insert(text, -1, "");
        sizeTasks++;
        sizeList++;
        tasks[sizeTasks] = new Task(text,-1, "");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("task", text);
        data.add(map);
        adapter.notifyDataSetChanged();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить задание");
    }
    public  boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            AdapterContextMenuInfo aaa =(AdapterContextMenuInfo) item.getMenuInfo();
            data.remove(aaa.position);
            ddata.deleteTask(tasks[aaa.position].task);
            for (int i = aaa.position + 1; i <= sizeTasks; ++i) {
                tasks[i - 1] = tasks[i];
            }
            sizeTasks--;
            sizeList--;
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }
    private void getTasks() {
        Cursor cursor = ddata.getAllData();
        tasks = new Task[1000];
        sizeTasks = -1;
        String task; int prior;
        while (cursor.moveToNext()) {
            task = cursor.getString(cursor.getColumnIndex("task"));
            prior = cursor.getInt(cursor.getColumnIndex("prior"));
            if (prior != -1) continue;
            sizeTasks++;
            tasks[sizeTasks] = new Task(task, prior, "");
        }
    }

}
