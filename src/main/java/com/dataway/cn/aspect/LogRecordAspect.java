package com.dataway.cn.aspect;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.annotation.SysLog;
import com.dataway.cn.component.SpringContextBeanComponent;
import com.dataway.cn.model.OperationLog;
import com.dataway.cn.service.IOperationLogService;
import com.dataway.cn.utils.BeanUtil;
import com.dataway.cn.utils.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 记录日志切面
 * @author phil
 * @date 2020/06/04 20:04
 */
@SuppressWarnings("unchecked")
public class LogRecordAspect extends AbstractAspectManager{

    private final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);
    /**
     * 用于日志时间格式化
     */
    @Getter
    @Setter
    private Date date;

    /**
     * 在构造器中初始化 aspectApi
     * @param aspectApi：切面装饰器
     */
    public LogRecordAspect(AspectApi aspectApi) {
        super(aspectApi);
    }

    /**
     * 处理切面日志
     * @param pjp：切入点
     * @param method：映射方法
     * @return Object
     * @throws Throwable：抛出可能的异常
     */
    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable{
        super.doHandlerAspect(pjp,method);
        return executing(pjp,method);
    }

    /**
     * 处理方法执行完成后的日志
     * @param returnOb：程序执行后的返回结果
     */
    public void doHandlerAspectAfter(Object returnOb){
        executeAfter(returnOb);
    }

    /**
     * 输出日志到日志文件
     */
    public void doHandlerAspectToFile(ProceedingJoinPoint pjp) {
        outLogToFile(pjp);
    }

    /**
     * 输出异常日志到日志文件
     * 本项目中的异常已经在 com.dataway.cn.exception.ControllerAdviceHandlerException 中进行集中处理
     * 无需再再在切面调用此方法
     */
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        // 记录下请求内容
        ex.printStackTrace();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(getDate());
        long dateLong = getDate().getTime();
        logger.error("################################ 异常抛出时间: " + dateStr + "################################");
        logger.error("["+dateLong+"]METHOD_NAME: " + methodName);
        logger.error("["+dateLong+"]ARGS: " + args);
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            sb.append(stackTraceElement).append(" &#10; ");
        }
        logger.error("["+dateLong+"]EXCEPTION: " + sb);

    }
    /**
     * 具体插入切面日志的操作
     * @param pjp ：切入点
     * @param method ：用于反射
     * @return CompletableFuture<Object>
     * @throws Throwable：抛出可能的异常
     */
    @Override
    @Async
    protected Object executing(ProceedingJoinPoint pjp, Method method) throws Throwable{
        SysLog sysLog = method.getAnnotation(SysLog.class);

        //异常日志信息
        String actionLog = null;
        StackTraceElement[] stackTrace = null;

        // 是否执行异常
        boolean isException = false;

        //开始时间
        long operationTime = System.currentTimeMillis();

        try {
            return pjp.proceed(pjp.getArgs());
        } catch (Throwable throwable) {
            isException = true;
            actionLog = throwable.getMessage();
            stackTrace = throwable.getStackTrace();
            throw throwable;
        }finally {
            //保存日志到数据库
            logHandle(pjp,method,sysLog,operationTime,isException,actionLog,stackTrace);
        }
    }

    /**
     * 具体插入程序执行完成后的日志操作
     * @param returnOb ：方法执行完成后的返回结果
     */
    @Async
    protected void executeAfter(Object returnOb){
        // 记录下请求内容
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(getDate());
        long dateLong = getDate().getTime();
        String returnResult = BeanUtil.isEmpty(returnOb)?"":returnOb.toString();
        logger.info("################################ 响应时间: " + dateStr + " ################################");
        logger.info("["+dateLong+"]return: " + returnResult);
    }

    /**
     * 处理切面日志
     * @param joinPoint：切入点
     * @param method：映射方法
     * @param sysLog：注解
     * @param startTime：日志发生时间
     * @param isException：是否发生异常
     * @param actionLog：异常日志信息
     * @param stackTrace：异常详细信息
     */
    private void logHandle(ProceedingJoinPoint joinPoint ,
                           Method method ,
                           SysLog sysLog ,
                           long startTime ,
                           boolean isException ,
                           String actionLog ,
                           StackTraceElement[] stackTrace){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        IOperationLogService operationLogService = SpringContextBeanComponent.getBean(IOperationLogService.class);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest request =servletRequestAttributes.getRequest();
        String authorization = request.getHeader("Authorization");
        OperationLog operationLog = new OperationLog();
        if (!BeanUtil.isEmpty(authorization)){
            String userNo = JwtUtil.getUserNo(authorization);
            operationLog.setUserNo(userNo);
        }
        operationLog.setIp(getIpAddress(request));
        operationLog.setClassName(joinPoint.getTarget().getClass().getName() );
        operationLog.setCreateTime(startTime);
        operationLog.setLogDescription(sysLog.description());
        operationLog.setModelName(sysLog.modelName());
        operationLog.setAction(sysLog.action());
        if (isException){
            StringBuilder sb = new StringBuilder();
            sb.append(actionLog).append(" &#10; ");
            for (StackTraceElement stackTraceElement : stackTrace) {
                sb.append(stackTraceElement).append(" &#10; ");
            }
            operationLog.setMessage(sb.toString());
        }
        operationLog.setMethodName(method.getName());
        operationLog.setSucceed(isException ?2:1);

        //格式化参数
        operationLog.setActionArgs(formatArgs(joinPoint.getArgs(),request));

        logger.info("执行方法信息:"+JSONObject.toJSON(operationLog));

        //保存日志到数据库
        operationLogService.save(operationLog);
    }

    /**
     * 输出切面日志到日志文件
     * @param pjp：切入点
     */
    private void outLogToFile(ProceedingJoinPoint pjp){

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(getDate());
        long dateLong = getDate().getTime();

        logger.info("################################ 请求时间: " + dateStr + " ################################");

        logger.info("["+dateLong+"]URL: " + request.getRequestURL().toString());
        logger.info("["+dateLong+"]HTTP_METHOD: " + request.getMethod());
        logger.info("["+dateLong+"]IP: " + getIpAddress(request));
        logger.info("["+dateLong+"]ARGS: " + Arrays.toString(pjp.getArgs()));
        //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
        logger.info("["+dateLong+"]CLASS_METHOD : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        //logger.info("################TARGET: " + joinPoint.getTarget());//返回的是需要加强的目标类的对象
        //logger.info("################THIS: " + joinPoint.getThis());//返回的是经过加强后的代理类的对象
        // result的值就是被拦截方法的返回值
        //return pjp.proceed();
    }

    /**
     * 获取请求的IP地址
     * @param request：请求
     * @return String
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip+":"+request.getRemotePort();
    }

    /**
     * 格式化参数
     * @param args：参数列表
     * @param request：当前请求
     * @return String
     */
    private String formatArgs(Object[] args,HttpServletRequest request){
        StringBuilder sb = new StringBuilder();
        String argStr = "";
        boolean isJoint = false;
        for (Object arg : args) {
            if (arg instanceof JSONObject) {
                JSONObject parse = (JSONObject) JSONObject.parse(arg.toString());
                argStr = parse.toString();
            } else if (arg instanceof String
                    || arg instanceof Long
                    || arg instanceof Integer
                    || arg instanceof Double
                    || arg instanceof Float
                    || arg instanceof Byte
                    || arg instanceof Short
                    || arg instanceof Character) {
                isJoint = true;
            } else if (arg instanceof String[]
                    || arg instanceof Long[]
                    || arg instanceof Integer[]
                    || arg instanceof Double[]
                    || arg instanceof Float[]
                    || arg instanceof Byte[]
                    || arg instanceof Short[]
                    || arg instanceof Character[]) {
                Object[] objects = (Object[]) arg;
                StringBuilder sbArray = new StringBuilder();
                sbArray.append("[");
                for (Object obj : objects) {
                    sbArray.append(obj.toString()).append(",");
                }
                sbArray.deleteCharAt(sbArray.length() - 1);
                sbArray.append("]");

                argStr = sbArray.toString();
            }
        }
        if(isJoint){
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            for (String key:parameterMap.keySet()) {
                String[] strings = parameterMap.get(key);
                for (String str:strings) {
                    sb.append(key).append("=").append(str).append("&");
                }
            }
            for (String key:pathVariables.keySet()) {
                String objs = pathVariables.get(key);
                sb.append(key).append("=").append(objs).append("&");
            }
            argStr = sb.deleteCharAt(sb.length()-1).toString();
        }
        return argStr;
    }

}
