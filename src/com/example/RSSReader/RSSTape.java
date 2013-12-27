package com.example.RSSReader;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/*public class RSSTape extends IntentService {

    private ArrayList<RSSNode> nodes;
    private String link;
    private String name;

    public RSSTape() {
        super("MyName");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        link = intent.getStringExtra("link");
        name = intent.getStringExtra("name");

        String task = intent.getStringExtra("task");
        RSSDataBase rssDataBase = new RSSDataBase(this);

        if (link == null) {
            return;
        }
        Log.d("mega", name);
        InputStream inputStream = null;

        URL url;
        HttpURLConnection connect;

        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            Reader reader;
            if (EntityUtils.getContentCharSet(entity) != null) {
                reader = new InputStreamReader(entity.getContent(), EntityUtils.getContentCharSet(entity));
            }
            else {
                reader = new InputStreamReader(entity.getContent(), "windows-1251");
            }
            InputSource is;
            is = new InputSource(reader);

            SAXParserFactory factory = SAXParserFactory.newInstance();


            factory.setNamespaceAware(false);
            SAXParser parser;

            parser = factory.newSAXParser();

            RSSParser rssParser = new RSSParser();

            parser.parse(is, rssParser);

            nodes = rssParser.getResult();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {}
        }

        rssDataBase.setArticle(name, nodes);

        Intent intentResponse = new Intent("com.example.RSSReader.RESPONSE");
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        if ("load".equals(task)) {
            sendBroadcast(intentResponse);
        }
    }

    private class RSSParser extends DefaultHandler {

        private ArrayList<RSSNode> result;
        private boolean isItem;
        private boolean isDate;
        private boolean isTitle;
        private boolean isDescription;
        private RSSNode rssNode;
        String ans;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            result = new ArrayList<RSSNode>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            ans = "";
            if (qName.equals("item")) {
                isItem = true;
                rssNode = new RSSNode();
            }

            if (qName.equals("title")) {
                isTitle = true;
            }
            if (qName.equals("pubDate")) {
                isDate = true;
            }
            if (qName.equals("description")) {
                isDescription = true;
            }
            super.startElement(uri, localName, qName, attributes);

        }

        @Override
        public void characters(char[] c, int start, int length) throws SAXException {
            super.characters(c, start,  length);
            String s = new String(c, start, length);

            if (isItem) {
                ans += s;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("item")) {
                isItem = false;
                result.add(rssNode);
            }

            if (isItem) {
                if (qName.equals("title")) {
                    rssNode.setTitle(ans);
                    ans = "";
                    isTitle = false;
                }
                if (qName.equals("pubDate")) {
                    rssNode.setDate(ans);
                    ans = "";
                    isDate = false;
                }
                if (qName.equals("description")) {
                    ans = ans.replaceAll("<br(.*)?>", "\n");
                    ans = ans.replaceAll("&quot;", "");
                    ans = ans.replaceAll("&#039;", "");
                    ans = ans.replaceAll("&lt;", "");
                    ans = ans.replaceAll("&gt;", "");
                    rssNode.setDescription(ans);
                    ans = "";
                    isDescription = false;
                }
            }

            super.endElement(uri,localName, qName);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        public ArrayList<RSSNode> getResult() {
            return this.result;
        }
    }
} */
