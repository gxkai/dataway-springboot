package com.dataway.cn.scheduling.job;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
  
/**
 * @author Phil
 */
public class HelloJob implements BaseJob {
  
    private static final Logger log = LoggerFactory.getLogger(HelloJob.class);

    public HelloJob() {}
     
    @Override
    public void execute(JobExecutionContext context) {
        log.info("Hello Job执行时间: " + new Date());
    }  
}
