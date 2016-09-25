package com.bbd;


import com.bbd.domain.Page;
import com.bbd.download.HttpClientDownloadImpl;
import com.bbd.process.JDProcessImpl;
import com.bbd.process.MeizuProcessImpl;
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
        spider.setProcess(new MeizuProcessImpl());
        spider.setStore(new ConsoleStoreImpl());
        // 商品详细页面
        String url = "http://detail.meizu.com/item/meilan_note3.html";
//        String url = "http://lists.meizu.com/page/list";
        Page page = spider.download(url);
        spider.process(page);
//        System.out.println(page.getValues().get("price"));




    }


}
