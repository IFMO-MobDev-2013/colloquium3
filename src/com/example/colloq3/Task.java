package com.example.colloq3;

/**
 * Created with IntelliJ IDEA.
 * User: Валера тыкал
 * Date: 27.12.13
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class Task {
    String task, untask;
    int prior;
    public Task(String task, int prior, String nntask) {
        this.task = task;
        this.prior = prior;
        this.untask = nntask;
    }
}
