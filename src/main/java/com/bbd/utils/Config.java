package com.bbd.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * 加载资源配置文件
 * Created by bbd on 2016/9/21.
 */
public class Config {
    static Properties properties;
    static {
        properties = new Properties();
        try {
            //加载资源文件中的内容
            properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int nThread = Integer.parseInt(properties.getProperty("nThread"));
    public static long millons_1 = Long.parseLong(properties.getProperty("millons_1"));
    public static long millons_5 = Long.parseLong(properties.getProperty("millons_5"));
    public static String zkConnect = properties.getProperty("zkConnect");
    public static String hdfs = properties.getProperty("hdfs");
    public static String redisConnect = properties.getProperty("redisConnect");
    public static int redisPort = Integer.parseInt(properties.getProperty("redisPort"));

    // XPATH页需要提取出来，可以保存到文件或者数据库中





}
