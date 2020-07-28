package com.dataway.cn.aspect;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.exception.ParamJsonException;
import com.dataway.cn.utils.DateTimeUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 防止xss攻击切面
 * @author phil
 * @date 2020/06/09 18:10
 */
public class ParamXssPassAspect extends  AbstractAspectManager{
    private Logger logger = LoggerFactory.getLogger(ParamXssPassAspect.class);
    public ParamXssPassAspect(AspectApi aspectApi){
        super(aspectApi);
    }


    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        super.doHandlerAspect(pjp,method);
        executing(pjp,method);
        return null;
    }

    @Override
    protected Object executing(ProceedingJoinPoint pjp, Method method)throws ParamJsonException{
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if(args[i] instanceof JSONObject){
                args[i]=JSONObject.parseObject(xssEncode(args[i].toString()));
            }else if(args[i] instanceof String){
                args[i]=xssEncode(args[i].toString());
            }
        }
        return args;
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     * 这里只是替换，当然也可以以异常的形式抛出，
     * 并进行统一处理
     */
    private  String xssEncode(String s) throws ParamJsonException {
        if (s == null || "".equals(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    //全角大于号
                    sb.append("《");
                    logger.info("################################ 当前时间: " + DateTimeUtil.formatDateTimeToString(new Date()) + " ################################");
                    logger.info("################################ 已经防止了来自于 '>' 的Xss攻击 ################################");
                    throw new ParamJsonException("参数包含 '>' 可能产生Xss攻击");
                    //break;
                case '<':
                    //全角小于号
                    sb.append("》");
                    logger.info("################################ 当前时间: " + DateTimeUtil.formatDateTimeToString(new Date()) + " ################################");
                    logger.info("################################ 已经防止了来自于 '<' 的Xss攻击 ################################");
                    throw new ParamJsonException("参数包含 '<' 可能产生Xss攻击");
                case '\'':
                    //全角单引号
                    sb.append('‘');

                    logger.info("################################ 当前时间: " + DateTimeUtil.formatDateTimeToString(new Date()) + " ################################");
                    logger.info("################################ 已经防止了来自于 " + "'" +" 的Xss攻击 ################################");
                    throw new ParamJsonException("参数包含 ' 可能产生Xss攻击");
                case '\\':
                    //全角斜线
                    sb.append('＼');
                    logger.info("################################ 当前时间: " + DateTimeUtil.formatDateTimeToString(new Date()) + " ################################");
                    logger.info("################################ 已经防止了来自于 '\\' 的Xss攻击 ################################");
                    throw new ParamJsonException("参数包含 \\ 可能产生Xss攻击");
                case '#':
                    //全角井号
                    sb.append('＃');
                    logger.info("################################ 当前时间: " + DateTimeUtil.formatDateTimeToString(new Date()) + " ################################");
                    logger.info("################################ 已经防止了来自于 '＃' 的Xss攻击 ################################");
                    throw new ParamJsonException("参数包含 # 可能产生Xss攻击");
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
