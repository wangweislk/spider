package com.bbd.store;

import com.bbd.domain.Page;
import com.bbd.utils.HbaseUtiles;
import com.bbd.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Logger logger = LoggerFactory.getLogger(HbaseStore.class);

    HbaseUtiles hbaseUtiles = new HbaseUtiles();
    // 向redis中写入rowkey,使用es或者solr建立索引
    RedisUtils redisUtils = new RedisUtils();

    public void stroe(Page page) {
        String goodsId = page.getGoodsId();
        Map<String, String> values = page.getValues();
        try {
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_DATA_URL,page.getUrl());
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_PIC_URL,values.get("pictureurl"));
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_PRICE,values.get("price"));
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_1,HbaseUtiles.COLUMNFAMILY_1_TITLE,values.get("title"));
            hbaseUtiles.put(HbaseUtiles.TABLE_NAME,goodsId,HbaseUtiles.COLUMNFAMILY_2,HbaseUtiles.COLUMNFAMILY_2_PARAM,values.get("spec"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据爬取异常，存在NULL的数据:{} {} {} {} {}",
                    page.getUrl(),values.get("pictureurl"),values.get("price"),values.get("title"),values.get("spec"));
        }
        hbaseUtiles.clearConnect();
    }
}
