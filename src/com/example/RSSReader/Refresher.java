/*package com.example.RSSReader;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 05.11.13
 * Time: 22:07
 * To change this template use File | Settings | File Templates.
 */
/*public class Refresher extends IntentService {
    public Refresher() {
        super("Refresher");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RSSDataBase rssDataBase = new RSSDataBase(this);
        int count = rssDataBase.getTableCount();

        for (int i = 0; i < count; i++) {
            Intent intentRSS = new Intent(this, RSSTape.class);
            startService(intentRSS.putExtra("link", rssDataBase.getLink(i)).putExtra("task", "refresh").putExtra("name", rssDataBase.getTrueName(rssDataBase.getName(i))));
        }
    }
} */
