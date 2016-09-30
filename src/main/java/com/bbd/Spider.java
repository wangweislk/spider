package com.bbd;

import com.bbd.domain.Page;
import com.bbd.download.Downloadable;
import com.bbd.download.HttpClientDownloadImpl;
import com.bbd.process.AnjukeProcessImpl;
import com.bbd.process.JDProcessImpl;
import com.bbd.process.Processalbe;
import com.bbd.repository.QueueRepository;
import com.bbd.repository.RedisRepository;
import com.bbd.repository.Reponstory;
import com.bbd.store.ConsoleStoreImpl;
import com.bbd.store.HbaseStore;
import com.bbd.store.MysqlStore;
import com.bbd.store.Storeable;
import com.bbd.utils.Config;
import com.bbd.utils.SleepUtiles;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 爬虫入口
 * Created by bbd on 2016/9/19.
 */
public class Spider {
    Logger logger = LoggerFactory.getLogger(Spider.class);

    /** 默认选项 */
    private Downloadable downloadable = new HttpClientDownloadImpl();
    private Processalbe processable;
    private Storeable storeable = new ConsoleStoreImpl();
    private Reponstory reponstory = new QueueRepository();

    public Spider() {
//        // 爬虫向zookeeper注册临时节点
//        String connectString = Config.zkConnect;
//        // 重试间隔时间，重试次数
//        RetryPolicy retry = new ExponentialBackoffRetry(3000, 3);
//        int sessionTimeOutMs = 4; // 会话超时时间，4S ~ 45S之间
//        int connectTimeOutMs = 1000;
//        try {
//            // 获取当前服务器的IP
//            InetAddress localHost = InetAddress.getLocalHost();
//            String ip = localHost.getHostAddress();
//            CuratorFramework client = CuratorFrameworkFactory.newClient(connectString,sessionTimeOutMs,connectTimeOutMs,retry);
//            client.start();
//            client.create()
//                    .creatingParentsIfNeeded()//父节点如果不存在创建
//                    .withMode(CreateMode.EPHEMERAL)//指定节点类型，临时节点
//                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) //指定节点权限
//                    .forPath("/spider/"+ip); //指定节点名称
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /** 入口URL队列*/
    //ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    // 线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(Config.nThread);

    public void start() {
        /** 校验基础环境*/
        check();
        logger.info("爬虫开始运行");
        while(true){
            final String url = reponstory.poll();
            if(url == null){
                logger.info("没有URL，等待...");
                SleepUtiles.sleep(Config.millons_5);
            }else{
                threadPool.execute(new Runnable() {
                    public void run() {
                        Page page = Spider.this.download(url);
                        Spider.this.process(page);
                        List<String> urls = page.getUrls();
                        for(String nextUrl: urls){
                            Spider.this.reponstory.add(nextUrl);
                        }
                        // 表示为商品详情页面
                        if(urls.isEmpty()){
                            Spider.this.store(page);
                        }
                    }
                });

            }
            SleepUtiles.sleep(Config.millons_1);
        }

    }

    private void check() {
        logger.info("开始检查爬虫运行环境...");
        if(this.processable == null){
            String message = "没有设置默认的解析类...";
            logger.error(message);
            throw new RuntimeException(message);
        }
        logger.info("=================================================");
        logger.info("downloadable的实现类是：{}", downloadable.getClass().getName());
        logger.info("processable的实现类是：{}",processable.getClass().getName());
        logger.info("storeable的实现类是：{}",storeable.getClass().getName());
        logger.info("repository的实现类是：{}",reponstory.getClass().getName());
        logger.info("=================================================");
    }

    /**
     * 页面下载
     * @param url
     * @return
     */
    public Page download(String url) {

        return this.downloadable.download(url);
    }

    /**
     * 解析
     *
     * @param page
     */
    public void process(Page page) {

        this.processable.process(page);
    }

    /**
     * 存储
     * @param page
     */
    public void store(Page page){
        this.storeable.stroe(page);
    }

    /**
     * 设置入口地址
     * @param url
     */
    public void setSeedUrl(String url){
        this.reponstory.add(url);
    }

    /**
     * getter setter方法
     * @return
     */
    public Downloadable getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Downloadable downloadable) {
        this.downloadable = downloadable;
    }

    public Processalbe getProcess() {
        return processable;
    }

    public void setProcess(Processalbe process) {
        this.processable = process;
    }

    public Storeable getStore() {
        return storeable;
    }

    public void setStore(Storeable store) {
        this.storeable = store;
    }

    public Reponstory getReponstory() {
        return reponstory;
    }

    public void setReponstory(Reponstory reponstory) {
        this.reponstory = reponstory;
    }

    public static void main(String args[]) {
        Spider spider = new Spider();
        spider.setProcess(new AnjukeProcessImpl());
//        spider.setStore(new HbaseStore());
//        spider.setReponstory(new RedisRepository());
//        spider.setDownload(new HttpClientDownloadImpl());
//        spider.setStore(new ConsoleStoreImpl());
//        spider.setReponstory(new QueueRepository());
        spider.setStore(new MysqlStore());
        spider.setReponstory(new RedisRepository(Config.redis_key_ajk));
        // 分页页面入口URL
        String url = "http://cd.fang.anjuke.com/loupan/414067.html";
//        String url = "http://cd.fang.anjuke.com/loupan/all/p1/";
//        spider.setSeedUrl(url);

        spider.start();

    }
}
