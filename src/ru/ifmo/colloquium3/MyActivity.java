package ru.ifmo.colloquium3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by asus on 27.12.13.
 */
public class MyActivity extends Activity {

    ListView listView1, listView2;
    EditText addText;
    Button addButton1, addButton2;
    TaskAdapter adapter1, adapter2;

    ArrayList<TaskItem> tasks1 = new ArrayList<TaskItem>(), tasks2 = new ArrayList<TaskItem>();
    TaskDatabase db1, db2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db1 = new TaskDatabase(this, 1);
        db2 = new TaskDatabase(this, 2);
        db1.open();
        db2.open();

        listView1 = (ListView) findViewById(R.id.listView1);
        listView2 = (ListView) findViewById(R.id.listView2);
        addText = (EditText) findViewById(R.id.addText);
        addButton1 = (Button) findViewById(R.id.addButton1);
        addButton2 = (Button) findViewById(R.id.addButton2);

        tasks1 = db1.getAllTasks();
        tasks2 = db2.getAllTasks();
        adapter1 = new TaskAdapter(this, tasks1);
        adapter2 = new TaskAdapter(this, tasks2);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);


        addButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask(addText.getText().toString(), 1);
                addText.setText("");

            }
        });
        addButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask(addText.getText().toString(), 2);
                addText.setText("");

            }
        });

        listView1.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyActivity.this, EditTaskActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id", Integer.parseInt(tasks1.get(i).param[TaskItem.ID]));
                startActivityForResult(intent, 0);
            }
        });
        listView2.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyActivity.this, EditTaskActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("id", tasks2.get(i).param[TaskItem.ID]);
                startActivityForResult(intent, 0);
            }
        });

    }

    void addTask(String name, int type){
        ArrayList<TaskItem> tasks = null;
        TaskAdapter adapter = null;
        TaskDatabase db = null;
        if (type == 1){
            tasks = tasks1;
            adapter = adapter1;
            db = db1;
        } else if (type == 2) {
            tasks = tasks2;
            adapter = adapter2;
            db = db2;
        }

        TaskItem task = new TaskItem();
        task.param[TaskItem.NAME] = name;
        tasks.add(task);
        adapter.notifyDataSetChanged();
        db.addTask(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db1.close();
        db2.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            copyTo(db1.getAllTasks(), tasks1);
            copyTo(db2.getAllTasks(), tasks2);
            adapter1.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
        }
    }

    void copyTo(ArrayList<TaskItem> a, ArrayList<TaskItem> b){
        b.clear();
        for (int i = 0; i < a.size(); i++){
            b.add(a.get(i));
        }
    }
}
