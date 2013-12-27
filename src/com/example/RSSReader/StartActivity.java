package com.example.RSSReader;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StartActivity extends Activity {

    private RSSDataBase rssDataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        rssDataBase = new RSSDataBase(this);

        /*String[] channelLink = {"http://lenta.ru/rss",
                                "http://bash.im/rss",
                                "http://4pda.ru/feed"};

        String[] channelName = {"lenta.ru", "bash.im", "4pda"};*/

        /*for (int i = 0; i < channelLink.length; i++) {
            rssDataBase.addTable(channelLink[i], channelName[i]);
            Intent intentRSS = new Intent(StartActivity.this, RSSTape.class);
            startService(intentRSS.putExtra("link", channelLink[i]).putExtra("task", "refresh").putExtra("name", rssDataBase.getTrueName(channelName[i])));
        } */

        Button addButton = (Button) findViewById(R.id.add_button);

        //final EditText siteText = (EditText) findViewById(R.id.site_text);
        final EditText nameText = (EditText) findViewById(R.id.name_text);

        final ListView listView = (ListView) findViewById(R.id.start_list_view);

        SimpleAdapter adapter = new SimpleAdapter(StartActivity.this, rssDataBase.getRSSItems(), R.layout.start_list_view,
                new String[]{"name"},
                new int[] {R.id.text});
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //tring link = siteText.getText().toString();
                String name = nameText.getText().toString();
                rssDataBase.addTable(name);

                SimpleAdapter adapter = new SimpleAdapter(StartActivity.this, rssDataBase.getRSSItems(), R.layout.start_list_view,
                        new String[]{"name"},
                        new int[] {R.id.text});
                //Intent intentRSS = new Intent(StartActivity.this, RSSTape.class);
                //startService(intentRSS.putExtra("name", rssDataBase.getTrueName(name)));
                listView.setAdapter(adapter);
            }
        });

        /*Intent intent = new Intent(this, Refresher.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30 * 60 * 1000, 30 * 60 * 1000, pendingIntent); */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                RSSDataBase rssDataBase = new RSSDataBase(StartActivity.this);

                Map map = (Map) parent.getItemAtPosition(position);

                Intent intent = new Intent(StartActivity.this, RSSActivity.class);
                intent.putExtra("name", (String)map.get("name"));
                startActivity(intent);
            }
        });

        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Toast toast = Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT);
                toast.show();

                RSSDataBase rssDataBase = new RSSDataBase(StartActivity.this);

                Map map = (Map) parent.getItemAtPosition(position);

                rssDataBase.delete(rssDataBase.getId((String)map.get("name")), rssDataBase.getTrueName((String)map.get("name")));

                SimpleAdapter adapter = new SimpleAdapter(StartActivity.this, rssDataBase.getRSSItems(), R.layout.start_list_view,
                        new String[]{"name"},
                        new int[] {R.id.text});

                listView.setAdapter(adapter);

                return true;
            }
        }); */
    }

}
