package com.dataway.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataway.cn.mapper.OperationLogMapper;
import com.dataway.cn.model.OperationLog;
import com.dataway.cn.service.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务实现类
 * @author phil
 * @date 2020/06/05 10:41
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {
}
