package com.bbd;

import com.bbd.utils.Config;
import com.bbd.utils.RedisUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by bbd on 2016/9/21.
 */
public class UrlJob implements Job{

    RedisUtils redisUtils = new RedisUtils();

    /**
     * 满足条件这个方法被调用
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("被调用了");

        List<String> urls = redisUtils.lrange(Config.redis_start_key_ajk, 0, -1);
        // 循环吧所有URL添加到Redis仓库中
        for(String url : urls){
            redisUtils.add(Config.redis_key_ajk,url);
        }
//        redisUtils.add(Config.redis_key_ajk,redisUtils.get(Config.redis_start_key_ajk));
    }
}
