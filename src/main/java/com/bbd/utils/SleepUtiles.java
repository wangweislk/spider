package com.bbd.utils;

/**
 * Created by bbd on 2016/9/21.
 */
public class SleepUtiles {
    public static void sleep(long millions){
        try {
            Thread.sleep(millions);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
