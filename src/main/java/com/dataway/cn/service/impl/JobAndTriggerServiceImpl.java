package com.dataway.cn.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.exception.CustomException;
import com.dataway.cn.mapper.JobAndTriggerMapper;
import com.dataway.cn.scheduling.job.BaseJob;
import com.dataway.cn.service.IJobAndTriggerService;
import com.dataway.cn.vo.JobAndTrigger;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;



/**
 * 任务调度 服务类实现
 * @author Phil
 */
@Service
public class JobAndTriggerServiceImpl implements IJobAndTriggerService {

	private static final Logger logger = LoggerFactory.getLogger(JobAndTriggerServiceImpl.class);

	private final JobAndTriggerMapper jobAndTriggerMapper;

	private final Scheduler scheduler;

	public JobAndTriggerServiceImpl(JobAndTriggerMapper jobAndTriggerMapper, @Qualifier("Scheduler") Scheduler scheduler){
		this.jobAndTriggerMapper = jobAndTriggerMapper;
		this.scheduler = scheduler;
	}

	@Override
	public Page<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
		Page<JobAndTrigger> p = jobAndTriggerMapper.getJobAndTriggerDetails(new Page<>(pageNum,pageSize));
		return p;

	}

	/**
	 * 创建定时任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @param cronExpression：任务的cron表达式
	 * @param description：任务描述
	 * @throws Exception：异常捕捉
	 */
	@Override
	public void createJob(String jobClassName, String jobGroupName, String cronExpression, String description)throws Exception{
		// 启动调度器  
		scheduler.start();
		//构建job信息
		JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).withDescription(description).build();
		//表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		//按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(jobClassName, jobGroupName)
				.withSchedule(scheduleBuilder)
				.build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);

		} catch (SchedulerException e) {
			logger.error("创建定时任务失败"+e);
			throw new CustomException("创建定时任务失败");
		}
	}


	/**
	 * 更新定时任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @param cronExpression：任务的cron表达式
	 * @param description：任务描述
	 * @throws Exception：异常捕捉
	 */
	@Override
	public void jobReschedule(String jobClassName, String jobGroupName, String cronExpression,String description) throws Exception {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).withDescription(description).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			logger.error("更新定时任务失败"+e);
			throw new CustomException("更新定时任务失败");
		}
	}
	private static BaseJob getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (BaseJob)class1.newInstance();
	}
}