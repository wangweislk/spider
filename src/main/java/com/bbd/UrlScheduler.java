package com.bbd;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

/**
 * Created by bbd on 2016/9/21.
 * URL调度器
 */
public class UrlScheduler {
    public static void main(String[] args){
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            // 开启调度器
            scheduler.start();
            JobDetail jobDetail = new JobDetail(UrlJob.class.getSimpleName(), Scheduler.DEFAULT_GROUP, UrlJob.class);
            CronTrigger trigger = new CronTrigger(UrlJob.class.getSimpleName(),Scheduler.DEFAULT_GROUP,"0 10 01 ? * *");

            scheduler.scheduleJob(jobDetail,trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
