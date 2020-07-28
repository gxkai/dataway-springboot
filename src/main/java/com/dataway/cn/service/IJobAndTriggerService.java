package com.dataway.cn.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.vo.JobAndTrigger;

/**
 * 任务调度 服务类
 * @author Phil
 */
public interface IJobAndTriggerService {

	/**
	 * 分页查询任务和触发器详细信息
	 * @param pageNum：当前页
	 * @param pageSize: 每页显示多少
	 * @return Page<JobAndTrigger>
	 */
	Page<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);

	/**
	 * 创建定时任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @param cronExpression：任务的cron表达式
	 * @param description：任务描述
	 * @throws Exception：异常捕捉
	 */
	void createJob(String jobClassName, String jobGroupName, String cronExpression,String description)throws Exception;


	/**
	 * 更新定时任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @param cronExpression：任务的cron表达式
	 * @param description：任务描述
	 * @throws Exception：异常捕捉
	 */
	void jobReschedule(String jobClassName, String jobGroupName, String cronExpression,String description) throws Exception;
}
