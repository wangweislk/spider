package com.bbd;

import com.bbd.utils.Config;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.net.InetAddress;
import java.util.List;

/**
 * 监视器是一个守护进程
 * Created by bbd on 2016/9/21.
 */
public class SpiderWatcher implements Watcher {
    private CuratorFramework client;
    private List<String> childList;
    public SpiderWatcher() {
        String connectString = Config.zkConnect;
        // 重试间隔时间，重试次数
        RetryPolicy retry = new ExponentialBackoffRetry(3000, 3);
        int sessionTimeOutMs = 4; // 会话超时时间，4S ~ 45S之间
        int connectTimeOutMs = 1000;
        try {
            // 获取当前服务器的IP
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            client = CuratorFrameworkFactory.newClient(connectString, sessionTimeOutMs, connectTimeOutMs, retry);
            client.start();
            //监视spider下面的所有子节点的变化情况，注册监视器，注意：监视器单次有效，需要重复注册
            // 返回值为变化的子节点
            childList = client.getChildren().usingWatcher(this).forPath("/spider");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        /**
     * 当监视器发现 监控下的子节点发生 变化的时候，这个方法都会被调用
     * @param event
     */
    public void process(WatchedEvent event) {
        System.out.println("节点发生变化了");

        // 重新注册监视器
        try {
            List<String> newChildList = client.getChildren().usingWatcher(this).forPath("/spider");
            for(String node: childList){
                if(!newChildList.contains(node)){
                    System.out.println("节点消失："+node);
                    // 给管理员发消息
                    /**
                     * 发邮件：javamail
                     * 发短信：云片网
                     */
                }
            }
            for(String node:newChildList){
                if(!childList.contains(node)){
                    System.out.println("新增节点："+node);
                }
            }
            // 需要重新更新
            this.childList = newChildList;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SpiderWatcher spiderWatcher = new SpiderWatcher();
        spiderWatcher.run();
    }

    private void run() {
        while(true){
            ;
        }
    }
}
