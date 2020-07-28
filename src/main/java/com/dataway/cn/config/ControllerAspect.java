package com.dataway.cn.config;

import com.dataway.cn.annotation.AccessLimit;
import com.dataway.cn.annotation.ParamXssPass;
import com.dataway.cn.annotation.SysLog;
import com.dataway.cn.annotation.ValidationParam;
import com.dataway.cn.aspect.*;
import com.dataway.cn.utils.BeanUtil;
import com.dataway.cn.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.time.Clock;
import java.util.Date;

/**
 * 切面:防止xss攻击 记录log  参数验证
 * @author phil
 * @date 2020/06/05 13:16
 */
@Aspect
@Configuration
public class ControllerAspect {

    /**
     * 确保一次请求用一个LogRecordAspect
     */
    private LogRecordAspect logRecordAspect;

    @Pointcut(value = "execution(public * com.dataway.cn.controller..*.*(..)))")
    public void aspect() {}

    /**
     * 在切入点的方法run之前要干的
     * 注解括号里面写的是一个切入点，这里看见切入点表达式可以用逻辑符号&&,||,!来描述。  括号里面也可以内置切点表达式
     */
    @Before("aspect()")
    public void logBeforeController(){}

    /**
     * 环绕切面
     * @param pjp：切入点
     * @return Object
     * @throws Throwable：捕捉异常
     */
    @Around(value = "aspect()")
    public Object validationPoint(ProceedingJoinPoint pjp)throws Throwable{
        Method method = currentMethod(pjp,pjp.getSignature().getName());

        //创建被装饰者
        AspectApiImpl aspectApi = new AspectApiImpl();

        //设置请求时间
        Date requestDate = Date.from(Clock.systemDefaultZone().instant());
        LogRecordAspect logRecordAspect = new LogRecordAspect(aspectApi);
        logRecordAspect.setDate(requestDate);

        this.logRecordAspect =logRecordAspect;

        //是否需要验证参数
        //验证一些必填参数
        //验证是否包含了必填的参数,
        //没有抛出ParamJsonException异常,异常会在 com.dataway.cn.exception.ControllerAdviceHandlerException 中集中处理
        if (!BeanUtil.isEmpty(StringUtil.getMethodAnnotationOne(method, ValidationParam.class.getSimpleName()))) {
            new ValidationParamAspect(aspectApi).doHandlerAspect(pjp,method);
        }
        //是否需要限流
        //需要限流是，添加注解 @AccessLimit
        if (method.isAnnotationPresent(AccessLimit.class)) {
            new AccessLimitAspect(aspectApi).doHandlerAspect(pjp,method);
        }

        // 是否需要拦截xss攻击,默认都需要防止 Xss。
        // 不需要防止时，添加注解@ParamXssPass即可
        if(!method.isAnnotationPresent( ParamXssPass.class )){
            new ParamXssPassAspect(aspectApi).doHandlerAspect(pjp,method);
        }

        //是否需要记录日志到数据库
        if(method.isAnnotationPresent(SysLog.class)){
            return logRecordAspect.doHandlerAspect(pjp,method);
        }
        //输入日志到日志文件
        logRecordAspect.doHandlerAspectToFile(pjp);
        return  pjp.proceed(pjp.getArgs());
    }
    /**
     * 在切入点的方法run之后要干的
     * 一般都是将接口执行后的响应结果输出到日志
     */
    @AfterReturning(returning = "returnOb", pointcut = "aspect()")
    public void doAfterReturning(Object returnOb){
        logRecordAspect.doHandlerAspectAfter(returnOb);
    }

    /**
     * 在切入点的方法抛出异常时
     */
    @AfterThrowing(pointcut = "aspect()")
    public void doAfterThrowing() {
        //ex.printStackTrace();
        //创建被装饰者
        //logRecordAspect.doAfterThrowing(joinPoint,ex);
        //本项目中的异常已经在 com.dataway.cn.exception.ControllerAdviceHandlerException 中进行集中处理
        //无需再在切面进行处理
        //这里只做打印，方便调试

    }

    /**
     * 获取目标类的所有方法，找到当前要执行的方法
     */
    private Method currentMethod ( ProceedingJoinPoint joinPoint , String methodName ) {
        Method[] methods      = joinPoint.getTarget().getClass().getMethods();
        Method   resultMethod = null;
        for ( Method method : methods ) {
            if ( method.getName().equals( methodName ) ) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }
}
