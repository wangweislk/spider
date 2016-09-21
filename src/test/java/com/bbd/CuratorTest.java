package com.bbd;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by bbd on 2016/9/21.
 */
public class CuratorTest {
    /**
     * 当连接失效后，临时节点就会消失，默认40秒后消失
     */
    @Test
    public void test() throws UnknownHostException {
        String connectString = "kunlundev03:2181";
        // 重试间隔时间，重试次数
        RetryPolicy retry = new ExponentialBackoffRetry(3000, 3);
        int sessionTimeOutMs = 4; // 会话超时时间，4S ~ 45S之间
        int connectTimeOutMs = 1000;
        // 获取当前服务器的IP
        InetAddress localHost = InetAddress.getLocalHost();
        String ip = localHost.getHostAddress();
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString,sessionTimeOutMs,connectTimeOutMs,retry);
        client.start();

        try {
            client.create()
                    .creatingParentsIfNeeded()//父节点如果不存在创建
                    .withMode(CreateMode.EPHEMERAL)//指定节点类型，临时节点
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) //指定节点权限
                    .forPath("/spider/"+ip); //指定节点名称

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
