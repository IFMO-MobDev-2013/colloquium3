package com.example.RSSReader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;

public class RSSActivity extends Activity {

    private ListView listView;
    private String trueName;
    private String link;
    private ArrayList<RSSNode> nodes;
    //private RSSBroadcastReceiver rssBroadcastReceiver;
    //private RSSTape rssTape;

    private RSSDataBase rssDataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss);

        trueName = getIntent().getStringExtra("name");

        listView = (ListView) findViewById(R.id.listView);

        rssDataBase = new RSSDataBase(this);

        Button updateButton = (Button) findViewById(R.id.add_button);

        SimpleAdapter adapter = new SimpleAdapter(RSSActivity.this, rssDataBase.getSubTasks(trueName), R.layout.listview,
                new String[]{"title"},
                new int[] {R.id.subtask});
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        /*IntentFilter intentFilter = new IntentFilter("com.example.RSSReader.RESPONSE");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        rssBroadcastReceiver = new RSSBroadcastReceiver();
        registerReceiver(rssBroadcastReceiver, intentFilter);
        */

        final EditText nameText = (EditText) findViewById(R.id.sub_task);
        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = nameText.getText().toString();
                rssDataBase.setArticle(trueName, name);

                SimpleAdapter adapter = new SimpleAdapter(RSSActivity.this, rssDataBase.getSubTasks(trueName), R.layout.listview,
                        new String[]{"title"},
                        new int[] {R.id.subtask});
                listView.setAdapter(adapter);
            }
        });



        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long arg3) {
                final RSSDataBase rssDataBase = new RSSDataBase(RSSActivity.this);

                Intent intent = new Intent(RSSActivity.this, ArticleActivity.class);
                intent.putExtra("text", rssDataBase.getDescription(name, index));
                startActivity(intent);
            }
        });*/

        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Toast toast = Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT);
                toast.show();

                RSSDataBase rssDataBase = new RSSDataBase(RSSActivity.this);

                Map map = (Map) parent.getItemAtPosition(position);

                rssDataBase.deleteSubTask((String)map.get("name"), trueName);

                SimpleAdapter adapter = new SimpleAdapter(RSSActivity.this, rssDataBase.getSubTasks(trueName), R.layout.start_list_view,
                        new String[]{"name"},
                        new int[] {R.id.text});

                listView.setAdapter(adapter);

                return true;
            }
        });                               */

    }

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(rssBroadcastReceiver);
    }

    public class RSSBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RSSDataBase rssDataBase = new RSSDataBase(RSSActivity.this);

            Toast toast = Toast.makeText(getApplicationContext(), "Обновление завершено", Toast.LENGTH_SHORT);
            toast.show();

            SimpleAdapter adapter = new SimpleAdapter(RSSActivity.this, rssDataBase.getFeeds(name), R.layout.listview,
                    new String[]{"title", "date"},
                    new int[] {R.id.title, R.id.date});

            listView.setAdapter(adapter);
        }
    } */

}
