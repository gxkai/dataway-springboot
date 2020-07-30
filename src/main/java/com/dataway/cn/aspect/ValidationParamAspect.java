package com.dataway.cn.aspect;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.annotation.ValidationParam;
import com.dataway.cn.exception.ParamJsonException;
import com.dataway.cn.utils.BeanUtil;
import com.dataway.cn.utils.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 验证参数切面
 * 验证是否包含了必填的参数,
 * 没有抛出ParamJsonException异常
 * @author phil
 * @date 2020/06/08 10:24
 */
public class ValidationParamAspect extends AbstractAspectManager{

    public ValidationParamAspect(AspectApi aspectApi){
        super(aspectApi);
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable{
        super.doHandlerAspect(pjp,method);
        executing(pjp,method);
        return null;
    }
    @Override
    protected Object executing(ProceedingJoinPoint pjp, Method method) {
        //获取注解的value值返回
        String validationParamValue = StringUtil.getMethodAnnotationOne(method, ValidationParam.class.getSimpleName());
        //验证参数
        Object[] obj = pjp.getArgs();
        if (!BeanUtil.isEmpty(validationParamValue)) {
            for (Object o : obj) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = JSONObject.parseObject(o.toString());
                    //是否有所有必须参数
                    hasAllRequired(jsonObject, validationParamValue);
                }else if (o instanceof Map){
                    JSONObject jsonObject = new JSONObject();
                    try {

                        BeanUtils.populate(jsonObject,(Map<String,?>) o);
                        //是否有所有必须参数
                        hasAllRequired(jsonObject, validationParamValue);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        throw new ParamJsonException("参数转换异常");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 验证是否包含了必填的参数,
     * 没有抛出ParamJsonException异常
     * @param jsonObject：
     * @param requiredColumns：必要的字段
     */
    private void hasAllRequired(final JSONObject jsonObject, String requiredColumns) {
        if (!BeanUtil.isEmpty(requiredColumns)) {
            //验证字段非空
            String[] columns = requiredColumns.split(",");
            StringBuilder missCol = new StringBuilder();
            for (String column : columns) {
                Object val = jsonObject.get(column.trim());
                if (BeanUtil.isEmpty(val)) {
                    missCol.append(column).append("  ");
                }
            }
            if (!BeanUtil.isEmpty(missCol.toString())) {
                jsonObject.clear();
                throw new ParamJsonException("缺少必填参数:"+ missCol.toString().trim());
            }
        }
    }
}
