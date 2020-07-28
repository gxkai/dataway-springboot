package com.dataway.cn.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.result.ResponseHelper;
import com.dataway.cn.result.WebResult;
import com.dataway.cn.service.IJobAndTriggerService;
import com.dataway.cn.vo.JobAndTrigger;
import io.swagger.annotations.Api;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;


/**
 * 任务调度 控制器
 * @author Phil
 */
@RestController
@RequestMapping(value="/job")
@Api(value = "任务Controller",tags = {"任务调度"})
public class JobController {

	private final IJobAndTriggerService iJobAndTriggerService;

	private final Scheduler scheduler;


	/**
	 * 构造方法注入
	 * @param iJobAndTriggerService：
	 * @param scheduler：
	 */
	public JobController(IJobAndTriggerService iJobAndTriggerService, @Qualifier("Scheduler") Scheduler scheduler){
		this.iJobAndTriggerService = iJobAndTriggerService;
		this.scheduler = scheduler;
	}


	/**
	 * 添加任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @param cronExpression：任务的cron表达式
	 * @param description：任务描述
	 * @throws Exception：异常捕捉
	 */
	@PostMapping(value="/addJob")
	public WebResult<String> addJob(@RequestParam(value="jobClassName")String jobClassName,
			@RequestParam(value="jobGroupName")String jobGroupName, 
			@RequestParam(value="cronExpression")String cronExpression,
			@RequestParam(value="description")String description) throws Exception{
		iJobAndTriggerService.createJob(jobClassName, jobGroupName, cronExpression,description);
		return ResponseHelper.succeed("");
	}


	/**
	 * 暂停定时任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @throws Exception：异常捕捉
	 */
	@PostMapping(value="/pauseJob")
	public WebResult<String> pauseJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
		scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
		return ResponseHelper.succeed("");
	}

	/**
	 * 恢复任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @throws Exception：异常捕捉
	 */
	@PostMapping(value="/resumeJob")
	public WebResult<String> resumeJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
		scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
		return ResponseHelper.succeed("");
	}

	/**
	 * 更新任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @param cronExpression：任务的cron表达式
	 * @param description：任务描述
	 * @throws Exception：异常捕捉
	 */
	@PostMapping(value="/rescheduleJob")
	public WebResult<String> rescheduleJob(@RequestParam(value="jobClassName")String jobClassName,
			@RequestParam(value="jobGroupName")String jobGroupName,
			@RequestParam(value="cronExpression")String cronExpression,
			@RequestParam(value="description")String description
	) throws Exception {
		iJobAndTriggerService.jobReschedule(jobClassName, jobGroupName, cronExpression,description);
		return ResponseHelper.succeed("");
	}

	/**
	 * 删除定时任务
	 * @param jobClassName：任务类名称
	 * @param jobGroupName：任务分组
	 * @throws Exception：异常捕捉
	 */
	@PostMapping(value="/deleteJob")
	public WebResult<String> deleteJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
		return ResponseHelper.succeed("");
	}

	/**
	 * 分页查询定时任务
	 * @param pageNum：当前页
	 * @param pageSize：每页size
	 * @return Map<String, Object>
	 */
	@GetMapping(value="/queryJob")
	public WebResult<Page<JobAndTrigger>> queryJob(@RequestParam(value="pageNum")Integer pageNum,
												   @RequestParam(value="pageSize")Integer pageSize) {
		return ResponseHelper.succeed(iJobAndTriggerService.getJobAndTriggerDetails(pageNum, pageSize));
	}
	

}
