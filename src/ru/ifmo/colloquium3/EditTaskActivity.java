package ru.ifmo.colloquium3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by asus on 27.12.13.
 */
public class EditTaskActivity extends Activity {

    Button editButton, cancelButton, deleteButton;
    EditText text;
    int id, type;
    TaskDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        editButton = (Button) findViewById(R.id.readyButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        text = (EditText) findViewById(R.id.editTaskText);

        type = getIntent().getExtras().getInt("type");
        id = getIntent().getExtras().getInt("id");

        db = new TaskDatabase(this, type);
        db.open();
        final TaskItem task = db.getTask(id);

        text.setText(task.param[TaskItem.NAME]);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.param[TaskItem.NAME] = text.getText().toString();
                db.updateTask(task, id);
                setResult(RESULT_OK);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteTask(task);
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}