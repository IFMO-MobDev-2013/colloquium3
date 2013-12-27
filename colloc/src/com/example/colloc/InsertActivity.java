package com.example.colloc;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 27.12.13
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public class InsertActivity extends Activity {

    public static final String KEY_NAME = "name";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        final EditText e = (EditText) findViewById(R.id.editTask);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = e.getText().toString();
                Intent intent = getIntent();
                intent.putExtra(KEY_NAME, s);
                setResult(1, intent);
                finish();
            }
        });
    }
}