package com.bbd.repository;

import com.bbd.utils.RedisUtils;

/**
 * 共享队列
 * Created by bbd on 2016/9/21.
 */
public class RedisRepository implements Reponstory{
    RedisUtils redisUtils = new RedisUtils();

    public String poll() {
        return redisUtils.poll(RedisUtils.key);
    }

    public void add(String nextUrl) {
        redisUtils.add(RedisUtils.key,nextUrl);
    }
}
