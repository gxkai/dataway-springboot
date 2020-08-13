package com.dataway.cn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.vo.JobAndTrigger;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @author Phil
 */
@Repository
public interface JobAndTriggerMapper extends BaseMapper<JobAndTrigger> {

	/**
	 * 分页查询任务和触发器详细信息
	 * @param page：分页信息
	 * @return Page<JobAndTrigger>
	 */
	Page<JobAndTrigger> getJobAndTriggerDetails(@Param("page") Page<JobAndTrigger> page);
}
