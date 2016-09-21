package com.bbd.store;

import com.bbd.domain.Page;
import com.bbd.utils.HbaseUtiles;

import java.io.IOException;
import java.util.Map;

/**
 * 实现Hbase存储，分为两个簇
 * 标题、价格、图片地址
 * 规格参数
 * create 'spider','goodsinfo','spec'
 * alter 'spider',{NAME=>'gooodsinfo',VERSIONS=>30}  //修改保存版本
 * rowkey:goodsId_网站标识
 *
 * Created by bbd on 2016/9/20.
 */
public class HbaseStore implements Storeable{

    HbaseUtiles hbaseUtiles = new HbaseUtiles();
    public void stroe(Page page) {
        String goodsId = page.getGoodsId();
        Map<String, String> values = page.getValues();
        try {
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_DATA_URL,page.getUrl());
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_PIC_URL,values.get("pictureurl"));
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_PRICE,values.get("price"));
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_TITLE,values.get("title"));
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_2,HbaseUtiles.COLUMNFAMILY_2_PARAM,values.get("spec"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
