package com.bbd.repository;

/**
 * 共享队列接口
 * Created by bbd on 2016/9/21.
 */
public interface Reponstory {
    public String poll();

    public void add(String nextUrl);

}
