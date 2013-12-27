package com.example.colloquium3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class SubTasksActivity extends Activity implements IEventHadler
{
    public SubTasksAdapter adapter;

    private int id_subject = 0;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subtasks);
        Vector<SubTask> e = new Vector<SubTask>();
        adapter = new SubTasksAdapter(this, e, this);
        ListView list_view = (ListView) findViewById(R.id.marksListView);
        list_view.setAdapter(adapter);

        id_subject = getIntent().getIntExtra("ID_SUBJECT", 0);
        Console.print("ID SUBJECT = "+id_subject);
        Cursor sth = Database.gi().query("select * from tasks where id_task = "+id_subject);
        if (sth.moveToNext())
        {
            TextView tv = (TextView)findViewById(R.id.marksHeader);
            tv.setText(sth.getString(1));
        }
    }

    public void gotoAddMark(View v)
    {
        Intent intent = new Intent(this, SubTasksEditActivity.class);
        intent.putExtra("ID_SUBJECT", id_subject);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        adapter.subtasks.clear();
        int sum = 0;
        Cursor sth = Database.gi().query("select * from subtasks where id_task = "+id_subject+" order by type asc");
        while (sth.moveToNext())
        {
            SubTask mark = new SubTask(sth.getInt(0));
            mark.id_subject = sth.getInt(1);
            mark.title = sth.getString(2);
            mark.type = sth.getInt(3);
            sum += mark.type;
            adapter.subtasks.add(mark);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void handleEvent(Event e)
    {

    }
}
