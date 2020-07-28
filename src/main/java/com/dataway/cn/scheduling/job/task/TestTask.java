package com.dataway.cn.scheduling.job.task;

import com.dataway.cn.scheduling.job.BaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author phil
 * @date 2020/06/23 14:15
 */
public class TestTask implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(TestTask.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("TestTask Job执行时间："+new Date());
    }
}
