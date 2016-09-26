package com.bbd.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;


import java.util.List;

/**
 * Created by bbd on 2016/9/25.
 */
public class RedisTest {

    Jedis jedis = new Jedis("kunlundev02", 6379);

    @Test
    public void test1() {
        // 获取redis的连接
//        jedis.set("baidu","www.baidu.com");
//        String baidu = jedis.get("baidu");
//        System.out.println(baidu);
        jedis.close(); // 关闭连接

    }

    @Test
    public void test2() {
        // 连接池的方式
        // 配置连接池信息
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(10);  // 表示连接池中最多允许有多少个空闲连接
        poolConfig.setMaxTotal(100); // 表示连接池中最多允许有多少个连接
        poolConfig.setMaxWaitMillis(3000); //连接池中获取一个连接的超时时间
        poolConfig.setTestOnBorrow(true);  // 表示连接池中在创建一个连接的时候，是否对这个连接进行测试，保证创建的连接可用
        String host = "kunlundev02";
        int port = 6379;
        JedisPool jedisPool = new JedisPool(host, port);
        Jedis jedis = jedisPool.getResource(); // 从连接池中取一个连接
        String baidu = jedis.get("baidu");
        System.out.println(baidu);

        jedis.close(); // 将连接归还给连接池

    }

    @Test
    public void test3() throws Exception {
        // 手动实现incr命令
        jedis.watch("a");
        String value = jedis.get("a");
        int vl = Integer.parseInt(value);
        vl++;
        Transaction multi = jedis.multi();
        jedis.set("a", vl + "");
        List<Object> exec = multi.exec();// 执行事务，执行exec之后，取消监控
        if (exec == null) {
            System.out.println("事务没有被执行");
            test3();
        } else {
            System.out.println("事务已经执行");
        }




    }


}
