package com.example.colloq3;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class MiniTasks extends Activity {
    EditText input;
    Button add;
    ListView listView;
    Task[] tasks;
    String MYTASK;
    DBAdapter ddata;
    int sizeList, sizeTasks;
    ArrayList<HashMap<String, Object>> data;
    SimpleAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minitasks);
        MYTASK = getIntent().getStringExtra("task");
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
        Task temp;
        for (int i = 0; i <= sizeTasks; ++i) {
            for (int j = i + 1; j <= sizeTasks; ++j) {
                if (tasks[i].prior < tasks[j].prior) {
                    temp = tasks[i];
                    tasks[i] = tasks[j];
                    tasks[j] = temp;
                }
            }
        }
        for (int i = 0; i <= sizeTasks; ++i) {
            map = new HashMap<String, Object>();
            map.put("task", tasks[i].untask);
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


            }
        });

    }

    public void add(View v) {
        String text = input.getText().toString();
        if (text.equals("")) return;
        ddata.insert(MYTASK, 0, text);
        sizeTasks++;
        sizeList++;
        tasks[sizeTasks] = new Task(MYTASK, 0, text);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("task", text);
        data.add(map);
        adapter.notifyDataSetChanged();
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить задание");
        menu.add(0, 2, 0, "Высокий приоритет");
        menu.add(0, 3, 0, "Обычный приоритет");
    }
    public  boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            AdapterContextMenuInfo aaa =(AdapterContextMenuInfo) item.getMenuInfo();
            data.remove(aaa.position);
            ddata.deleteUntask(tasks[aaa.position].untask);
            for (int i = aaa.position + 1; i <= sizeTasks; ++i) {
                tasks[i - 1] = tasks[i];
            }
            sizeTasks--;
            sizeList--;
            adapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == 2) {
            AdapterContextMenuInfo aaa =(AdapterContextMenuInfo) item.getMenuInfo();
            tasks[aaa.position].prior = 1;
            String temp = tasks[aaa.position].untask;
            tasks[aaa.position].untask = tasks[aaa.position].untask + "*";
            makeList();
            ddata.updateun(tasks[aaa.position].task, tasks[aaa.position].prior, tasks[aaa.position].untask, temp);
            return true;
        }
        if (item.getItemId() == 3) {
            AdapterContextMenuInfo aaa =(AdapterContextMenuInfo) item.getMenuInfo();
            tasks[aaa.position].prior = 0;
            String temp = tasks[aaa.position].untask;
            if (tasks[aaa.position].untask.charAt(tasks[aaa.position].untask.length() - 1) == '*') {
                tasks[aaa.position].untask = tasks[aaa.position].untask.substring(0, tasks[aaa.position].untask.length() - 1);
            }
            makeList();
            ddata.update(tasks[aaa.position].task, tasks[aaa.position].prior, tasks[aaa.position].untask, temp);
            return true;
        }
        return super.onContextItemSelected(item);
    }
    private void getTasks() {
        Cursor cursor = ddata.getAllData();
        tasks = new Task[1000];
        sizeTasks = -1;
        String task, untask; int prior;
        while (cursor.moveToNext()) {
            task = cursor.getString(cursor.getColumnIndex("task"));
            prior = cursor.getInt(cursor.getColumnIndex("prior"));
            untask = cursor.getString(cursor.getColumnIndex("untask"));
            if (prior == -1) continue;
            sizeTasks++;
            tasks[sizeTasks] = new Task(task, prior, untask);
        }
    }

}
