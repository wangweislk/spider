package com.bbd;

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

        List<String> urls = redisUtils.lrange(RedisUtils.start_url, 0, -1);
        // 循环吧所有URL添加到Redise仓库中
        for(String url : urls){
            redisUtils.add(RedisUtils.key,url);
        }
    }
}
