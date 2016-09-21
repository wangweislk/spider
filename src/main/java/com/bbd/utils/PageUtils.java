package com.bbd.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by bbd on 2016/9/19.
 */
public class PageUtils {
    static Logger loggger = LoggerFactory.getLogger(PageUtils.class);
    public static String getContent(String url){
        String content = null;
        HttpClientBuilder builder = HttpClients.custom();
//        HttpHost proxy = new HttpHost("114.112.157.173", 8118);
        CloseableHttpClient client = builder.build();
        HttpGet request = new HttpGet(url);
        try{
            long start_time = System.currentTimeMillis();
            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity);
            loggger.info("页面下载成功，消耗时间:{},url:{}",System.currentTimeMillis()-start_time,url);

        }catch (HttpHostConnectException e){//代理IP失效异常
            // 针对抓取失败的URL，记录日志
            e.printStackTrace();
        }catch (Exception e){
            loggger.error("页面下载失败,url:{},具体的错误内容:{}",url,e.getMessage());
        }
        return content;

    }

}
