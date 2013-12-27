package com.ctdev.colloquium3;

/**
 * Created by Alexei on 27.12.13.
 */
public class Task {
    String name;
    String description;
    boolean done;
    int priority;//-1 - done 0-low 1-average 2-high
    int id;

    Task(String name, String description, int priority, int id) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.done = false;
        this.id = id;
    }

    Task(String name, String description, boolean done, int priority, int id) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.done = done;
        this.id = id;
    }

    Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.priority = 1;
        this.done = false;
        this.id = id;
    }
}
