package com.dataway.cn.aspect;

import com.dataway.cn.annotation.AccessLimit;
import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.exception.CustomException;
import com.dataway.cn.utils.DateTimeUtil;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author phil
 * @date 2020/06/10 12:34
 */
@SuppressWarnings("UnstableApiUsage")
public class AccessLimitAspect extends AbstractAspectManager{
    private final static Logger logger = LoggerFactory.getLogger(AccessLimitAspect.class);

    /**
     * 在构造器中初始化 aspectApi
     */
    public AccessLimitAspect(AspectApi aspectApi) {
        super(aspectApi);
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method)throws Throwable {
        super.doHandlerAspect(pjp,method);
        executing(pjp,method);
        return null;
    }

    @Override
    protected Object executing(ProceedingJoinPoint pjp, Method method) throws Throwable {
        AccessLimit lxRateLimit = method.getAnnotation(AccessLimit.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 或者url(存在map集合的key)
        String url = request.getRequestURI();

        //添加速率.保证是单例的
        RateLimiter rateLimiter;
        if (!BaseConstant.LIMIT_MAP.containsKey(url)) {
            // 创建令牌桶
            rateLimiter = RateLimiter.create(lxRateLimit.perSecond());
            BaseConstant.LIMIT_MAP.put(url, rateLimiter);
            logger.info("<<=================  当前时间!{}",DateTimeUtil.formatDateTimeToString(new Date()));
            logger.info("<<=================  请求{},创建令牌桶,容量{} 成功!!!",url,lxRateLimit.perSecond());
        }
        rateLimiter = BaseConstant.LIMIT_MAP.get(url);
        //获取令牌
        if (!rateLimiter.tryAcquire(lxRateLimit.timeOut(), lxRateLimit.timeOutUnit())) {
            logger.info("<<=================  当前时间!{}",DateTimeUtil.formatDateTimeToString(new Date()));
            logger.info("<<=================  请求{},获取令牌失败 失败!!!",url);
            throw new CustomException("服务器繁忙，请稍后再试!");
        }
        return null;
    }
}
