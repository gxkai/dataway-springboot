package com.dataway.cn.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataway.cn.mapper.sys.InfoToUserMapper;
import com.dataway.cn.model.sys.InfoToUser;
import com.dataway.cn.service.sys.IInfoToUserService;
import org.springframework.stereotype.Service;

/**
 * 用户电话关系表 服务实现类
 * @author phil
 * @date 2020/06/08 11:03
 */
@Service
public class InfoToUserServiceImpl extends ServiceImpl<InfoToUserMapper, InfoToUser> implements IInfoToUserService {

}
