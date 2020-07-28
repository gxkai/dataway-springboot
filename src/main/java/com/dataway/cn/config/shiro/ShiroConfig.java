package com.dataway.cn.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.annotation.Pass;
import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.utils.BeanUtil;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Filter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Shiro 相关配置
 * @author phil
 * @date 2020/06/06 16:00
 */
@Configuration
public class ShiroConfig{

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * 注入环境
     */
    private final Environment env ;

    public ShiroConfig(Environment env) { //2
        this.env = env;
    }

    /**
     * 管理生命周期
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 管理生命周期
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);

        return defaultAdvisorAutoProxyCreator;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(){

        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
        manager.setRealm(new MyRealm());
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>(16);
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 访问401和404页面不通过我们的Filter
        //通过http://127.0.0.1:9527/druid/index.html 访问 root/root
        filterRuleMap.put("/druid/**", "anon");
        //放行webSocket
        filterRuleMap.put("/websocket/*", "anon");
        //放行swagger
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/swagger-resources/**", "anon");
        filterRuleMap.put("/swagger/**","anon");
        //这个一定要放开，否则会导致无法访问
        filterRuleMap.put("/v2/api-docs","anon");
        filterRuleMap.put("/webjars/**","anon");
        filterRuleMap.put("/favicon.ico","anon");
        filterRuleMap.put("/captcha.jpg","anon");
        filterRuleMap.put("/csrf","anon");

        //放行静态页面
        filterRuleMap.put("/schedul/**", "anon");

        //测试 放行任务调度
        filterRuleMap.put("/job/**", "anon");

        // 加 @Pass注解的请求直接跳过 shiro过滤器
        // 没有加 @Pass注解的 用JWT Filter对请求进行处理
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.putAll(getUrlAndMethodSet());
        filterRuleMap.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }


    public Map<String, String> getUrlAndMethodSet(){
        String scanPackage = env.getProperty("scanPackage");
        String contextPath = env.getProperty("server.servlet.context-path");
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(Controller.class);
        classesList.addAll(reflections.getTypesAnnotatedWith(RestController.class));
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        for (Class<?> clazz :classesList) {
            String baseUrl;
            String[] classUrl ={};
            if(!BeanUtil.isEmpty(clazz.getAnnotation(RequestMapping.class))){
                classUrl=clazz.getAnnotation(RequestMapping.class).value();
            }
            Method[] methods = clazz.getMethods();
            for (Method method:methods) {
                if(method.isAnnotationPresent(Pass.class)){
                    String [] methodUrl;
                    StringBuilder sb  =new StringBuilder();
                    if(!BeanUtil.isEmpty(method.getAnnotation(PostMapping.class))){
                        methodUrl=method.getAnnotation(PostMapping.class).value();
                        if(BeanUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(PostMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else if(!BeanUtil.isEmpty(method.getAnnotation(GetMapping.class))){
                        methodUrl=method.getAnnotation(GetMapping.class).value();
                        if(BeanUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(GetMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else if(!BeanUtil.isEmpty(method.getAnnotation(DeleteMapping.class))){
                        methodUrl=method.getAnnotation(DeleteMapping.class).value();
                        if(BeanUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(DeleteMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else if(!BeanUtil.isEmpty(method.getAnnotation(PutMapping.class))){
                        methodUrl=method.getAnnotation(PutMapping.class).value();
                        if(BeanUtil.isEmpty(methodUrl)){
                            methodUrl=method.getAnnotation(PutMapping.class).path();
                        }
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }else {
                        methodUrl=method.getAnnotation(RequestMapping.class).value();
                        baseUrl=getRequestUrl(classUrl, methodUrl, sb,contextPath);
                    }
                    if(!BeanUtil.isEmpty(baseUrl)){
                        filterRuleMap.put(baseUrl,"anon");
                    }
                }
            }
        }
        BaseConstant.METHOD_URL_SET.addAll(filterRuleMap.keySet());
        logger.info("@Pass:"+ JSONObject.toJSONString(filterRuleMap.keySet()));
        return filterRuleMap;
    }


    private String  getRequestUrl(String[] classUrl, String[] methodUrl, StringBuilder sb,String contextPath) {
        if(!BeanUtil.isEmpty(contextPath)){
            sb.append(contextPath);
        }
        if(!BeanUtil.isEmpty(classUrl)){
            for (String url:classUrl) {
                sb.append(url).append("/");
            }
        }
        for (String url:methodUrl) {
            sb.append(url);
        }
        if(sb.toString().endsWith("/")){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString().replaceAll("/+", "/");
    }

    /**
     * 用于开启注解  @RequiresAuthentication （"权限编码"）
     * @param securityManager：
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
