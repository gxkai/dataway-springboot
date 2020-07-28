package com.dataway.cn.config;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Quartz定时任务配置
 * @author Phil
 */
@Configuration
public class SchedulerConfig {

    private final JobFactory jobFactory;

    public SchedulerConfig(JobFactory jobFactory){
        this.jobFactory = jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        //延长启动
        schedulerFactoryBean.setStartupDelay(1);
        //设置加载的配置文件
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("/config/quartz.properties"));
        return schedulerFactoryBean;
    }
    @Bean(name="Scheduler")
    @QuartzDataSource
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

}
