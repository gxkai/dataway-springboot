package com.dataway.cn.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SpringBoot自带定时任务，不利于动态控制
 * //@Async启用自定义的线程池
 * @author phil
 * @date 2020/05/29 17:13
 */
@Component
@Async("asyncThreadPool")
public class ScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Scheduled(cron = "${scheduleTask.cron1}")
    public void execute() {
        logger.info("=========================="+ Thread.currentThread().getName() + " start==============================");
        logger.info("This is schedule1111 test.");
        logger.info(sdf.format(new Date()));
        logger.info("=========================="+ Thread.currentThread().getName() + " end==============================");

    }

    @Scheduled(cron = "${scheduleTask.cron2}")
    public void execute2() {
        logger.info("=========================="+ Thread.currentThread().getName() + " start==============================");
        logger.info("This is schedule222 test.");
        logger.info(sdf.format(new Date()));
        logger.info("=========================="+ Thread.currentThread().getName() + " end==============================");

    }
}
