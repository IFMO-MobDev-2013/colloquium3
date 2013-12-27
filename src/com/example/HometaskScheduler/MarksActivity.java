package com.example.HometaskScheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 08.11.13
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class MarksActivity extends Activity {
    private ArrayList<Mark> values = new ArrayList<>();
    private ScheduleDAO dao = new ScheduleDAO(this);
    private Integer subjectID;
    LayoutInflater inflater;
    public static HashMap<String, Integer> priorities = new HashMap<>();

    private Context getContext() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks);
        inflater = getLayoutInflater();
        priorities.put(getResources().getString(R.string.common), 10);
        priorities.put(getResources().getString(R.string.high), 20);
        subjectID = getIntent().getIntExtra("subjectID", 0);
        final ListView marks = (ListView) findViewById(R.id.marks);
        values = dao.getAllMarks(subjectID);
        final MarksArrayAdapter adapter = new MarksArrayAdapter(this, android.R.layout.simple_list_item_1, values);
        Button addNew = new Button(this);
        addNew.setText(R.id.add_subtask);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View layout = inflater.inflate(R.layout.new_mark,
                        (ViewGroup) findViewById(R.id.addNewMark));
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("New mark");
                alert.setView(layout);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        final Spinner priority = (Spinner) layout.findViewById(R.id.prioritySelect);
                        String markDescription = ((EditText) layout.findViewById(R.id.markDescription)).getText().toString();
                        int points = priorities.get(priority.getSelectedItem().toString());
                        Mark mark = new Mark(subjectID, markDescription, points);
                        dao.addMark(mark);

                        values.add(points == 10 ? values.size() : 0, mark);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton(R.string.cancel, null);
                alert.show();
            }
        });
        marks.addHeaderView(addNew);
        marks.setAdapter(adapter);

        marks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                final Mark mark = values.get(position - 1);
                final int currentPrior = mark.points;
                final LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.new_mark,
                        (ViewGroup) findViewById(R.id.addNewMark));
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle(R.string.edit);
                ((EditText) layout.findViewById(R.id.markDescription)).setText(mark.description);
                final Spinner priority = (Spinner) layout.findViewById(R.id.prioritySelect);

                priority.setSelection(mark.points == 10 ? 0 : 1);
                alert.setView(layout);
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String markDescription = ((EditText) layout.findViewById(R.id.markDescription)).getText().toString();
                        mark.description = markDescription;
                        int prior = priorities.get(priority.getSelectedItem().toString());
                        mark.points = prior;
                        dao.editMark(mark);
                        if (prior != currentPrior) {
                            values.remove(position - 1);
                            if (prior > currentPrior) {
                                values.add(0, mark);
                            } else {
                                values.add(mark);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.setNeutralButton(R.string.remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dao.deleteMark(values.get(position - 1).dbID);
                        values.remove(position - 1);
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton(R.string.cancel, null);
                alert.show();
                return true;
            }
        });
    }

    public class MarksArrayAdapter extends ArrayAdapter<Mark> {

        public MarksArrayAdapter(Context context, int resource, List<Mark> objects) {
            super(context, resource, objects);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int pos, View convertView, ViewGroup parent) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.subtask, null);
            final Mark mark = this.getItem(pos);
            TextView descr = (TextView) layout.getChildAt(0);
            final ImageView imageView = (ImageView) layout.getChildAt(1);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int prior = mark.points;
                    mark.points = prior==10?20:10;
                    dao.editMark(mark);
                    values.remove(pos);
                    if (prior == 10) {
                        values.add(0, mark);
                    } else {
                        values.add(mark);
                    }
                    notifyDataSetChanged();
                }
            });
            imageView.setImageResource(mark.points == 10 ? R.drawable.star_empty_32 : R.drawable.star_gold_256);
            descr.setText(mark.description);
            descr.setTextColor(Color.WHITE);
            descr.setTextSize(40);
            return layout;
        }
    }


}
