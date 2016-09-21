package com.bbd;


import com.bbd.domain.Page;
import com.bbd.download.HttpClientDownloadImpl;
import com.bbd.process.JDProcessImpl;
import com.bbd.store.ConsoleStoreImpl;
import org.junit.Test;

/**
 * Created by bbd on 2016/9/19.
 */
public class SpiderTest {

    @Test
    public void test(){
        Spider spider = new Spider();
        spider.setDownloadable(new HttpClientDownloadImpl());
        spider.setProcess(new JDProcessImpl());
        spider.setStore(new ConsoleStoreImpl());
        // 商品详细页面
        String url = "http://item.jd.com/10165149572.html";
        Page page = spider.download(url);
        spider.process(page);
        System.out.println(page.getValues().get("price"));




    }


}
