package com.bbd.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bbd on 2016/9/19.
 */
public class Page {
    private String content;
    private String url;
    private String goodsId;

    /**
     * 保存商品的基本信息
     */
    private Map<String,String> values = new HashMap<String, String>();
    //存储临时URL
    private List<String> urls = new ArrayList<String>();


    public String getUrl() {
        return url;

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public void addField(String key,String value){
        this.values.put(key,value);
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void addUrl(String url){
        this.urls.add(url);
    }
}
