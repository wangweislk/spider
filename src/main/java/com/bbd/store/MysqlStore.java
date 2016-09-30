package com.bbd.store;

import com.bbd.domain.Page;
import com.bbd.utils.MysqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bbd on 2016/9/29.
 */
public class MysqlStore implements Storeable{
    Logger logger = LoggerFactory.getLogger(HbaseStore.class);
    MysqlUtils mysqlUtils = new MysqlUtils();

    public void stroe(Page page) {
        mysqlUtils.insert(page);
    }
}
