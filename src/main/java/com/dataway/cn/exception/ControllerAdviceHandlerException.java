package com.dataway.cn.exception;

import com.alibaba.fastjson.JSONException;
import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.result.ResultStatusCode;
import com.dataway.cn.result.WebResult;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;

/**
 * @author phil
 * @date 2020/05/22 14:20
 */

@RestControllerAdvice
public class ControllerAdviceHandlerException {
    private final static Logger logger = LoggerFactory.getLogger(ControllerAdviceHandlerException.class);

    /**
     * 自定义异常拦截
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public WebResult<String> handleCustomException(CustomException e) {
        String message = e.getMessage();
        if(message.indexOf(BaseConstant.CUSTOM_EXCEPTION_SEPARATOR) > 0){
            String[] split = message.split(BaseConstant.CUSTOM_EXCEPTION_SEPARATOR);
            return new WebResult<>(false,Integer.parseInt(split[0]),split[1],"");
        }
        //输出日志到日志文件
        outPutLogToFile(e,message);
        return new WebResult<>(false,ResultStatusCode.CUSTOM_EXCEPTION.getCode(),message,"");
    }

    /**
     * 参数异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ParamJsonException.class)
    @ResponseBody
    public WebResult<String> handleParamJsonException(ParamJsonException e) {

        //输出日志到日志文件
        outPutLogToFile(e,e.getMessage());
        return new WebResult<>(false,ResultStatusCode.PARAM_JSON_EXCEPTION.getCode(),e.getMessage(),"");
    }

    /**
     * 权限不足
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public WebResult<String> handleUnauthorizedException(UnauthorizedException e) {

        //输出日志到日志文件
        outPutLogToFile(e,e.getMessage()+"  "+ResultStatusCode.PERMISSION_ERROR.getMsg()+"  ");
        return new WebResult<>(false,ResultStatusCode.PERMISSION_ERROR,"");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public WebResult<String> handleParamJsonException(HttpMessageNotReadableException e) {
        //输出日志到日志文件
        outPutLogToFile(e,ResultStatusCode.PARAM_ERROR.getMsg());
        return new WebResult<>(false,ResultStatusCode.PARAM_ERROR.getCode(),ResultStatusCode.PARAM_ERROR.getMsg(),"");

    }
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class})
    @ResponseBody
    public WebResult<String> handleIllegalArgumentException(Exception e) {
        //输出日志到日志文件
        outPutLogToFile(e,ResultStatusCode.PARAM_ERROR.getMsg());
        return new WebResult<>(false,ResultStatusCode.PARAM_ERROR.getCode(),ResultStatusCode.PARAM_ERROR.getMsg(),"");

    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({JSONException.class})
    @ResponseBody
    public WebResult<String> handleJsonException(Exception e) {
        //输出日志到日志文件
        outPutLogToFile(e,ResultStatusCode.JSON_STRING_ERROR.getMsg());
        return new WebResult<>(false,ResultStatusCode.JSON_STRING_ERROR.getCode(),ResultStatusCode.JSON_STRING_ERROR.getMsg(),"");

    }
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MissingServletRequestParameterException.class, BindException.class,
            ServletRequestBindingException.class, MethodArgumentNotValidException.class,ArithmeticException.class})
    public WebResult<String> handleHttpMessageNotReadableException(Exception e) {

        Date date = Date.from(Clock.systemDefaultZone().instant());
        // 记录下请求内容
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        long dateLong = date.getTime();
        if (e instanceof BindException){
            logger.error("################################ 异常抛出时间: " + dateStr + "################################");
            logger.error("["+dateLong+"]MESSAGE: " + ResultStatusCode.BAD_REQUEST.getMsg());
            logger.error("["+dateLong+"]EXCEPTION: " + e);
            return new WebResult<>(false,ResultStatusCode.BAD_REQUEST.getCode(),ResultStatusCode.BAD_REQUEST.getMsg(), ((BindException)e).getAllErrors().get(0).getDefaultMessage());
        }
        logger.error("################################ 异常抛出时间: " + dateStr + "################################");
        logger.error("["+dateLong+"]MESSAGE: " + ResultStatusCode.BAD_REQUEST.getMsg());
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            sb.append(stackTraceElement).append(" &#10; ");
        }
        logger.error("["+dateLong+"]EXCEPTION: " + sb);
        return new WebResult<>(false,ResultStatusCode.BAD_REQUEST.getCode(),ResultStatusCode.BAD_REQUEST.getMsg(), e.getMessage());
    }

    /**
     * 405 - Method Not Allowed
     * 带有@ResponseStatus注解的异常类会被ResponseStatusExceptionResolver 解析
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public WebResult<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        //输出日志到日志文件
        outPutLogToFile(e,ResultStatusCode.METHOD_NOT_ALLOWED.getMsg());
        return new WebResult<>(false,ResultStatusCode.METHOD_NOT_ALLOWED.getCode(),ResultStatusCode.METHOD_NOT_ALLOWED.getMsg(), null);
    }

    /**
     * 其他全局异常在此捕获
     * @param e:
     * @return Result
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public WebResult<String> handleException(Exception e) {
        //输出日志到日志文件
        outPutLogToFile(e,ResultStatusCode.SYSTEM_ERR.getMsg());
        return new WebResult<>(false,ResultStatusCode.SYSTEM_ERR.getCode(),ResultStatusCode.SYSTEM_ERR.getMsg(),null);
    }

    private void outPutLogToFile(Exception e,String message){
        Date date = Date.from(Clock.systemDefaultZone().instant());
        // 记录下请求内容
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        long dateLong = date.getTime();

        logger.error("=========================================================");
        logger.error("################################ 异常抛出时间: " + dateStr + " ################################");
        logger.error("["+dateLong+"]MESSAGE: " + message);
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t").append(e.getMessage()).append(";\n");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            sb.append("\t\t").append(stackTraceElement).append(";\n");
        }
        logger.error("["+dateLong+"]EXCEPTION: \n" + sb);
        logger.error("=========================================================");
    }
}

