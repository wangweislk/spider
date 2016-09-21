package com.bbd.repository;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bbd on 2016/9/21.
 */
public class QueueRepository implements Reponstory{
    /** 入口URL队列*/
    ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    public String poll() {
        return this.queue.poll();
    }

    public void add(String nextUrl) {
        this.queue.add(nextUrl);
    }
}
