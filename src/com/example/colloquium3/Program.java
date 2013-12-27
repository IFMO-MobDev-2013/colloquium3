package com.example.colloquium3;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Vector;

public class Program extends Activity implements IEventHadler
{
    public SubjectsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);

        Database.init(this);
        Database.gi().addEventListener(this);

        Vector<Subject> e = new Vector<Subject>();

        adapter = new SubjectsAdapter(this, e, this);
        ListView list_view = (ListView) findViewById(R.id.subjectsListView);
        list_view.setAdapter(adapter);
    }

    @Override
    public void handleEvent(Event e)
    {
        //Console.print("Message: good");
    }

    public void gotoAddSubject(View v)
    {
        Intent intent = new Intent(this, SubjectEditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        adapter.channels.clear();

        Cursor sth = Database.gi().query("select * from tasks");

        while (sth.moveToNext())
        {
            Console.print(sth.getString(0)+" "+sth.getString(1));
            Subject subject = new Subject(sth.getString(1));
            subject.id_subject = Integer.parseInt(sth.getString(0));
            subject.points = 0;
            adapter.channels.add(subject);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
