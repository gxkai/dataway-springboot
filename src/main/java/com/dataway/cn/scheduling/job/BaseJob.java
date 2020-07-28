package com.dataway.cn.scheduling.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *@author phil
 */
public interface BaseJob extends Job {


	/**
	 * 需要执行的定时任务的方法
	 * @param context:定时任务上下文，可以获取到定时任务相关信息
	 * @throws JobExecutionException:捕捉可能的异常
	 */
	@Override
	void execute(JobExecutionContext context) throws JobExecutionException;
}

