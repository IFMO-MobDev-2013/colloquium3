package ru.ifmo.colloquium3;

/**
 * Created by asus on 27.12.13.
 */
public class TaskItem {
    final static String[] tags = new String[]{"ID", "NAME", "PRIORITY"};
    final static int ID = 0;
    final static int NAME = 1;
    final static int PRIORITY = 2;
    String[] param = new String[tags.length];

    TaskItem makeCopy() {
        TaskItem a = new TaskItem();
        for (int i = 0; i < param.length; i++) {
            a.param[i] = this.param[i];
        }
        return a;
    }

    void clear() {
        param = new String[tags.length];
    }

}
