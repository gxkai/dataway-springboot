package com.dataway.cn.vo;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Phil
 */
public class JobAndTrigger implements Serializable {
	private static final long serialVersionUID = -451325598334003590L;
	private String jobName;
	private String jobGroup;
	private String jobClassName;
	private String triggerName;
	private String triggerGroup;
	private BigInteger repeatInterval;
	private BigInteger timesTriggered;
	private String cronExpression;
	private String timeZoneId;
	private String triggerState;

	public JobAndTrigger() {
	}

	public JobAndTrigger(String jobName, String jobGroup, String jobClassName, String triggerName, String triggerGroup, BigInteger repeatInterval, BigInteger timesTriggered, String cronExpression, String timeZoneId) {
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobClassName = jobClassName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.repeatInterval = repeatInterval;
		this.timesTriggered = timesTriggered;
		this.cronExpression = cronExpression;
		this.timeZoneId = timeZoneId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public BigInteger getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(BigInteger repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public BigInteger getTimesTriggered() {
		return timesTriggered;
	}

	public void setTimesTriggered(BigInteger timesTriggered) {
		this.timesTriggered = timesTriggered;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public String getTriggerState() {
		return triggerState;
	}

	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}

	@Override
	public String toString() {
		return "JobAndTrigger{" +
				"jobName='" + jobName + '\'' +
				", jobGroup='" + jobGroup + '\'' +
				", jobClassName='" + jobClassName + '\'' +
				", triggerName='" + triggerName + '\'' +
				", triggerGroup='" + triggerGroup + '\'' +
				", repeatInterval=" + repeatInterval +
				", timesTriggered=" + timesTriggered +
				", cronExpression='" + cronExpression + '\'' +
				", timeZoneId='" + timeZoneId + '\'' +
				", triggerState='" + triggerState + '\'' +
				'}';
	}
}
