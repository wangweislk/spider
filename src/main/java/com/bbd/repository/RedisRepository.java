package com.bbd.repository;

import com.bbd.utils.Config;
import com.bbd.utils.RedisUtils;

/**
 * 共享队列
 * Created by bbd on 2016/9/21.
 */
public class RedisRepository implements Reponstory{
    RedisUtils redisUtils = new RedisUtils();
    String redis_key = Config.redis_key_jd;

    public RedisRepository(String key) {
        this.redis_key = key;
    }

    public String poll() {
        return redisUtils.poll(this.redis_key);
    }

    public void add(String nextUrl) {
        redisUtils.add(this.redis_key,nextUrl);
    }
}
