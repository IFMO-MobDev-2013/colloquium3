package com.example.colloquium3;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class SubTasksEditActivity extends Activity implements AdapterView.OnItemSelectedListener
{
    private SubTask current = null;
    private Subject subject = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.markedit);

        current = new SubTask(getIntent().getIntExtra("ID_MARK", 0));
        subject = new Subject(getIntent().getIntExtra("ID_SUBJECT", 0));

        if (current.id_subtask != 0)
        {
            Cursor sth = Database.gi().query("select * from subtasks where id_subtask = "+ current.id_subtask);
            sth.moveToNext();
            current.id_subject = Integer.parseInt(sth.getString(1));
            current.title = sth.getString(2);
            current.type = Integer.parseInt(sth.getString(3));

            EditText name = (EditText)findViewById(R.id.editMarkTitle);
            name.setText(current.title);
            ((Button)findViewById(R.id.deleteButton)).setEnabled(true);
        }
        else
        {
            current.id_subject = subject.id_subject;
            ((Button)findViewById(R.id.deleteButton)).setEnabled(false);
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(current.type);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        Console.print("You have chosen ID = " + id);
        current.type = pos;
    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void onSaveMark(View v)
    {
        EditText name = (EditText)findViewById(R.id.editMarkTitle);

        current.title = name.getText().toString();

        if (current.id_subtask == 0)
        {
            Console.print("Current id_subject = "+current.id_subject);
            Database.gi().exec("insert into subtasks values(null, "+current.id_subject+", '" + current.title + "', " + current.type + ")");
            Toast t = Toast.makeText(this, "Subtask is added", 3000);
            t.show();
            finish();
        }
        else
        {
            Database.gi().exec("update subtasks set title = '" + current.title+ "', type = " + current.type + " where id_subtask = "+ current.id_subtask);
            Toast t = Toast.makeText(this, "Subtask is updated", 3000);
            t.show();
        }
    }

    public void onDeleteMark(View v)
    {
        Database.gi().exec("delete from subtasks where id_subtask = " + current.id_subtask);
        Toast t = Toast.makeText(this, "Subtask is deleted", 3000);
        t.show();
        finish();
    }
}
